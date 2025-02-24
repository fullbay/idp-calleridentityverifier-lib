package com.fullbay.util.exceptions;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

@Getter
public class AwsV4SignatureValidationException extends RuntimeException
{

  private final String message;
  private final Throwable cause;
  private final JsonNode awsError;

  public AwsV4SignatureValidationException(final String message, final JsonNode awsError,
                                           final Throwable cause)
  {
    super(message, cause);
    this.message = message;
    this.cause = cause;
    this.awsError = awsError;
  }





}
