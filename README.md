# caller-identity service authentication

java library that utilizes aws credentials to verify/authenticate clients that call a service.

### how it works (under the covers)

all requests to aws must be signed using aws credentials as described [here](https://docs.aws.amazon.com/general/latest/gr/signature-version-4.html "AWS v4 Signature").  when using any aws sdk, this signing is automatically done for you.

if a resource running as a role in AWScall the sts:get-caller-identity method directly, `CLI: aws sts get-caller-identity` the request would be signed, sent to aws and return the following information:


```json
{
    "Account": "123114898530",
    "UserId": "AROAIFKRSDN7VGB6RU7NC:i-0023a0dd680d02199",
    "Arn": "arn:aws:sts::123114898530:assumed-role/some-jenkins/i-0023a0dd680d02199"
}
```

this method can also be used to verify the identity of the client.  basically the client signs a sts:get-caller-identity request but does not send it to aws. instead the sts:get-caller-identity request headers are forwarded to the service as part of the service request.  when the service receives the request from the client, the service forwards the sts:get-caller-identity request to aws.  the response from aws will contain information about the role that originally signed the request, in this case the client.

**here are the steps more detail:**

1.  client uses the aws v4 signing algorithm to sign a sts:get-caller-identity request.  the signing algorithm uses the aws role credentials to sign the request
2.  client sends the data/headers created in step 1, (x-amz-date, x-amz-security-token, authorization) to the target service for verification
3.  target service reconstructs the sts:get-caller-identity request using the data/headers sent by the client
4.  target service sends request to aws where the "caller identity", i.e. the identity of the caller that created the request, (in this case the client) is returned
5.  target service now knows the identity of the client.

### authorization header
this library produces and verifies an authorization header that is derived from the sts:get-caller-identity request formatted in json as follows:\
```json
{
   "date":"20180702T202321Z",
   "token":"FQoDYXdzEE0aDFwUCInZ/zRxC5RwwCL6Ar5sj6ULcjlqHvYGLqabuIAmdRRC7YEu25vhpBfJ65esyPeFzJtU1IjFP7ul4OEQBlrgGiNvcnNCZNu9vAa0Vr1M5GMH30RMPFBqIW0/4c5ImjMxcQ4BxS6iBhMp96O1MIfQUMqkGCcLAo4y7Vmh3gvmqr2XWAo/iNOGhM1shYizu7+GSmHqzkT5am76dZmYZoi9r5f8ZWmMmG7axH0aNj6wZmZzH7g2eT1MyXJlnjCKqvxnoubIP2ilNhxPOz3KMIUKBbLtXSYjTLvFMq85AGyNRTVCXJJl7WlM5bMseZeIpfrwWfbasfKlHqyam81xMew/BnqQWydHmmRdT+jJDSh5wVxOuxiXvTQXhX5FNg/am6eAhDm7ZEaTOV0+75O2kuaFKFhzYJ+/3FV8+59LgboLwJOUJ+VpYErIC7BVCQBFFXnVI7I3B1xaDBBYEooxgwhXfOxeMAaoyXWRki3LVfIPWYyvDy2EMubVtk82cElSQCPFSI/oMzQbaijzkerZBQ==",
   "auth":"AWS4-HMAC-SHA256 Credential=ASIAIBSSDFF2YUS4ELIQ/20180702/us-east-1/sts/aws4_request, SignedHeaders=host;x-amz-date;x-amz-security-token, Signature=3aa8704de660aa23d295eace137d3bd25f2c6f2934da95aefeed228aa99c8f16",
   "headers":{}
}
```

This json is then base64 encoded and placed in the authoration header:

```Authorization: caller-identity {Base64 Encoded String of JSON above}```

### key advantages

the key advantages to using this authentication method are:

1.  you definitively know the aws role/identity/stackid of the client making the request
2.  you know the account where the client is running.  this account number can be extrapolated to Dev, Stage, QA, Prod, etc.

### how the library works

the caller-identity java library simplifies the process of signing and verifying the request.  it makes use of the aws sts java sdk.

#### client code
the client creates a `CallerIdentityRequest` object using the following method:

```java
final CallerIdentityRequest signedGetCallerIdentityRequest = CallerIdentityRequestBuilder.builder().build();
```

##### note:  when running this code on an EC2 or ECS task in aws, the `DefaultAWSCredentialsProviderChain` is automatically used. if you are executing this code locally, you may need to provide an aws credentials provider to sign the request as follows:

 ```java
final CallerIdentityRequest callerIdentityProducer = CallerIdentityRequestBuilder.builder()
      .AWSCredentialsProvider(new ProfileCredentialsProvider("some-stackid-profile"))
      .build();
 ```

finally send the results to the target service using the `.toBase64String()` method of the CallerIdentityRequest object.

the contents of the base64 string that will be verified by the service using the `CalleridentityVerifier` class contains information from the GetCallerIdentity signed request.  Here is an example:

```$json
{
  "date" : "20180425T204742Z",
  "token" : "FQoDYXdzEH0aDLdHUKDr0dLCXtkCVyLFAgxIxv0VlH2mO8x7J+ViwOHuJHYdGSn4MI5peDQoCfKNB93GT04IJQG7SLkFbYzwlHZjv4HDR5Ku1FBkRHtH/E0eX5LtuON90leBmFhIA4kOkcPWM9TgSIPXE1n2IvySC/dcL70kkrI8fNNBjvsYi1u+ou6jNaiPic8o7DQ/dZbLoldfUmC9vxPPBP21Qom1q8b/r7eI+SoutEJo2BJbT+ck8znQqEWtJhe5tmmhe+1QtDPzgNGJZobj42a6Jz5+OBVF7UAI6uT3ysJ6LhepcH2x3A7DQlS8fwAdwNiUf03TbgxVZpAsCjpo62HAxKo2/b1e+YMmC0VduJ5vQpe4GPd5WUVP/NyE8mDEuJHn4GNd92CCgI/nM695GGm23/uVUUQMGt02t9hjP7F0mxShfybgCG1GnneFnxvHbyM5MwmdTLHN+MsozL2D1wU=",
  "auth" : "AWS4-HMAC-SHA256 Credential=ASIAJVKRTABKTZLN7VIQ/20180425/us-east-1/sts/aws4_request, SignedHeaders=host;x-amz-date;x-amz-security-token, Signature=d6691f9035263f3f35de2ed9a21da26c5a5681037211a50f75802e601bc93948"
}
```

#### service code

 the target service will use the `CallerIdentityVerifier` class to verify the client identity using the based64 data string sent from the client as follows:

```java
    final CallerIdentityVerifier awsV4SignatureValidator = new CallerIdentityVerifier();
    final CallerIdentity callerIdentity = awsV4SignatureValidator.verify(**Base64String**);
```

##### note:  because the CallerIdentityVerifier relies on an http client to make the request to aws, it is suggested to make `CallerIdentityVerifier` a singleton so the http client does not need to be recreated for every verification request.  

when the `verify` method is called, an aws get-caller-identity request is built to exactly match the request built by the client.  however the request is NOT signed again, but the authorization header that contains the signature created by the client is sent to aws for execution/verification.  

if successful, the `verify` method will return a `CallerIdentity` object that contains the role name, role arn and account number of the client that signed the request. this data will allow you to make a trust decision knowing the client's identity.

if there are any errors with the request, the `verify` method will throw a `RuntimeException`

**example `CallerIdentity` object in json format**

```json
{
  "identityType" : "ASSUMED_ROLE",
  "accountId" : "123114898530",
  "arn" : "arn:aws:sts::123114898530:assumed-role/some-jenkins/i-0023a0dd680d02199",
  "name" : "some-jenkins",
  "path" : "some-jenkins/i-0023a0dd680d02199"
}
```

#### maven dependency

library can be referenced from artifactory using the following:

```xml
<dependency>
    <groupId>com.fullbay.util</groupId>
    <artifactId>idp-calleridentity-lib</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```
