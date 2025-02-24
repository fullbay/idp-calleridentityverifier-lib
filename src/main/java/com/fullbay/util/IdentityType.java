package com.fullbay.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Getter
@Accessors(fluent = true)
public enum IdentityType
{
  ROLE("role","role"),
  ASSUMED_ROLE("assumed-role","role"),
  FEDERATED_USER("federated-user","federated-user"),
  USER("user","user");

  private final String identityName;
  private final String qualifiedName;

  public static IdentityType parse(final String text)
  {
    for (final IdentityType b : IdentityType.values())
    {
      if (b.identityName.equalsIgnoreCase(text))
      {
        return b;
      }
    }
    throw new RuntimeException("Identity Type Enumeration value of " + text + " not found");
  }
}
