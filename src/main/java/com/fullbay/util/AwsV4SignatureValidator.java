package com.fullbay.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import com.fullbay.util.exceptions.AwsV4SignatureValidationException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

@AllArgsConstructor
@Builder
@Slf4j
public class AwsV4SignatureValidator {
  final JsonMapper jsonMapper =
      JsonMapper.builder()
          .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
          .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
          .build();
  final Pattern pattern = Pattern.compile(".*/(.*)/sts/aws4_request.*");
  private final LoadingCache<String, AwsPrincipal> cache =
      CacheBuilder.newBuilder()
          .maximumSize(1000)
          .expireAfterWrite(30, TimeUnit.MINUTES)
          .build(
              new CacheLoader<String, AwsPrincipal>() {
                @Override
                public AwsPrincipal load(@NotNull final String base64CallerIdentity)
                    throws Exception {
                  final SignedGetCallerIdentityRequest signedGetCallerIdentityRequest =
                      jsonMapper.readValue(
                          Base64.getDecoder().decode(base64CallerIdentity),
                          SignedGetCallerIdentityRequest.class);
                  return verifierFunction.apply(signedGetCallerIdentityRequest);
                }
              });

  @SneakyThrows
  public AwsPrincipal verify(final String base64Signature) {
    return verify(base64Signature, true);
  }

  @SneakyThrows
  public AwsPrincipal verify(final String base64Signature, final boolean useCache) {
    if (useCache) {
      return cache.get(base64Signature);
    }

    // Convert the Base64 String to a SignedGetCallerIdentityRequest
    final SignedGetCallerIdentityRequest signedGetCallerIdentityRequest =
        jsonMapper.readValue(
            Base64.getDecoder().decode(base64Signature), SignedGetCallerIdentityRequest.class);

    // Verify thesignature
    return verifierFunction.apply(signedGetCallerIdentityRequest);
  }

  // send the HTTP call to aws;
  private AwsPrincipal verifySignature(
      final SignedGetCallerIdentityRequest signedGetCallerIdentityRequest)
      throws AwsV4SignatureValidationException {
    final OkHttpClient client = new OkHttpClient();

    final RequestBody formBody =
        new FormBody.Builder()
            .add("Action", "GetCallerIdentity")
            .add("Version", "2011-06-15")
            .build();

    final Request.Builder requestBuilder =
        new Request.Builder()
            .url("https://sts.amazonaws.com")
            .addHeader("Accept", "application/json")
            .post(formBody);

    // Load all the headers from the signed request
    signedGetCallerIdentityRequest
        .headers()
        .keySet()
        .forEach(
            (key) ->
                requestBuilder.addHeader(
                    key, signedGetCallerIdentityRequest.headers().get(key).get(0)));

    final Request request = requestBuilder.build();

    try (Response response = client.newCall(request).execute()) {
      if (response.code() != 200) {
        throw new AwsV4SignatureValidationException(
            "Signature is not valid",
            jsonMapper.readTree(
                IOUtils.toString(
                    Objects.requireNonNull(response.body()).byteStream(), StandardCharsets.UTF_8)),
            null);
      }
      val json =
          IOUtils.toString(
              Objects.requireNonNull(response.body()).byteStream(), StandardCharsets.UTF_8);
      final JsonNode jsonNode = jsonMapper.readValue(json, JsonNode.class);
      response.close();

      return AwsPrincipal.parse(
          jsonNode
              .get("GetCallerIdentityResponse")
              .get("GetCallerIdentityResult")
              .get("Arn")
              .textValue());

    } catch (IOException e) {
      throw new AwsV4SignatureValidationException(
          "There was an error sending the request to AWS", null, e);
    }
  }

  private final Function<SignedGetCallerIdentityRequest, AwsPrincipal> verifierFunction =
      signedGetCallerIdentityRequest -> {
        //        final Matcher matcher =
        //
        // Pattern.compile(".*/(.*)/sts/aws4_request.*").matcher(signedGetCallerIdentityRequest.auth());
        //        matcher.matches();
        try {
          return verifySignature(signedGetCallerIdentityRequest);
        } catch (AwsV4SignatureValidationException ex) {
          log.error("Failed Signature Verification");
          log.error(ex.getAwsError().get("Error").toString());
          throw ex;
        }
      };
}
