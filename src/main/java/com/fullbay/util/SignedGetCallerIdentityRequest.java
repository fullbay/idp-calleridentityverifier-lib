package com.fullbay.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Value
@Getter(onMethod = @__(@JsonProperty))
@Builder
@Jacksonized
@Slf4j
@Accessors(fluent = true)
public class SignedGetCallerIdentityRequest {
  Map<String, List<String>> headers;

  public static SignedGetCallerIdentityRequestBuilder builder() {
    return new SignedGetCallerIdentityRequestBuilder();
  }

  public String toBase64String() throws JsonProcessingException {
    return new String(Base64.getEncoder().encode(toJson().getBytes()));
  }

  public String toJson() throws JsonProcessingException {
    final ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(this);
  }


}
