package com.fullbay.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
@Builder
@Accessors(fluent = true)
@ToString
@EqualsAndHashCode
@With
@Value
public class AwsPrincipal {
  IdentityType identityType;
  String accountId;
  String arn;
  String name;
  String path;

  public static void main(String[] args) {
    AwsPrincipal parse =
        AwsPrincipal.parse(
            "arn:aws:sts::533267388672:assumed-role/idp-auth-zone-svc/botocore-session-1729226419");
    System.out.println(parse.path);
  }

  public String abbreviatedArn() {
    return String.format("%s:%s:%s", accountId, identityType.qualifiedName(), name);
  }

  public static boolean isArn(final String arn) {
    final Matcher matcher =
        Pattern.compile("arn:aws:(iam|sts)::(?<acctNum>\\d{12}):(?<type>.*?)/(?<path>.*)")
            .matcher(arn);
    return matcher.matches();
  }

  public static AwsPrincipal parse(final String arn) {
    // Parse the ARN with Regex
    final Matcher matcher =
        Pattern.compile("arn:aws:(iam|sts)::(?<acctNum>\\d{12}):(?<type>.*?)/(?<path>.*)")
            .matcher(arn);

    if (matcher.matches()) {
      final String accountNum = matcher.group("acctNum");
      final String[] splitPath = matcher.group("path").split("/");

      return AwsPrincipal.builder()
          .accountId(accountNum)
          .path(matcher.group("path"))
          .name(splitPath[0])
          .identityType(IdentityType.parse(matcher.group("type")))
          .arn(arn)
          .build();
    }
    throw new RuntimeException(
        "The ARN of the caller-identity could not be matched to the pattern");
  }
}
