variable "vpc_id" {
  default = "vpc-03ff1e65"
}

data "aws_subnet" "server-1a" {
  vpc_id = var.vpc_id

  filter {
    name = "tag:Name"
    values = [
      "Server-1a"]
  }
}

data "aws_subnet" "server-1b" {
  vpc_id = var.vpc_id

  filter {
    name = "tag:Name"
    values = [
      "Server-1b"]
  }
}

data "aws_security_group" "server-security-group" {
  vpc_id = var.vpc_id
  name = "Jenkins"
}

resource "aws_iam_role" "codebuild_role" {
  name = "codebuild-caller-identity"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": "codebuild.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
EOF
}

resource "aws_iam_role_policy" "codebuild_vpc_policy" {
  name = "codebuild-vpc-policy"
  role = aws_iam_role.codebuild_role.id
  policy = <<POLICY
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "ec2:CreateNetworkInterface",
                "ec2:DescribeDhcpOptions",
                "ec2:DescribeNetworkInterfaces",
                "ec2:DeleteNetworkInterface",
                "ec2:DescribeSubnets",
                "ec2:DescribeSecurityGroups",
                "ec2:DescribeVpcs"
            ],
            "Resource": "*"
        },
        {
            "Effect": "Allow",
            "Action": [
                "ec2:CreateNetworkInterfacePermission"
            ],
            "Resource": "arn:aws:ec2:us-east-1:310865762107:network-interface/*",
            "Condition": {
                "StringEquals": {
                    "ec2:Subnet": [
                        "arn:aws:ec2:us-east-1:310865762107:subnet/${data.aws_subnet.server-1a.id}",
                        "arn:aws:ec2:us-east-1:310865762107:subnet/${data.aws_subnet.server-1b.id}"
                    ],
                    "ec2:AuthorizedService": "codebuild.amazonaws.com"
                }
            }
        }
    ]
}
POLICY
}

resource "aws_iam_role_policy" "codebuild_policy" {
  name = "codebuild-policy"
  role = aws_iam_role.codebuild_role.id
  policy = <<POLICY
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Resource": [
        "arn:aws:logs:us-east-1:310865762107:log-group:/aws/codebuild/*"
      ],
      "Action": [
        "logs:CreateLogGroup",
        "logs:CreateLogStream",
        "logs:PutLogEvents"
      ]
    }
  ]
}
POLICY
}


#Use the AWS Provider in the account where the terraform service is running.  AKA "aws.home"
data "aws_kms_secret" "github_token" {
  provider = "aws.home"
  secret {
    name = "token"
    payload = "AQICAHgRnTjTgWE+Ov/8WVejuoASDFFxAoN023dMd3K7e63XJwFuda+CqKDFaxfPLffGWQt8AAAAhzCBhAYJKoZIhvcNAQcGoHcwdQIBADBwBgkqhkiG9w0BBwEwHgYJYIZIAWUDBAEuMBEEDApclEP2hrUPmK/T5AIBEIBDo3DwI0PLL5EYR3QuiNfE9vgmxUglkKOGDgjyHFEuyFwWt8WjWf6qtfCTxciSRaQ6lR3CBbwfOaQi0VnyGbGuXBp/1g=="
  }
}

resource "aws_codebuild_project" "codebuild-project" {
  name = "caller-identity"
  description = "StackId: caller-identity"
  build_timeout = "5"
  service_role = aws_iam_role.codebuild_role.arn

  environment {
    compute_type = "BUILD_GENERAL1_SMALL"
    image = "aws/codebuild/java:openjdk-8"
    type = "LINUX_CONTAINER"
    privileged_mode = true
  }

  artifacts {
    type = "S3"
    location = "acom-codebuild-cache-310865762107-us-east-1"
    name = "caller-identity"
    path = "artifacts"
  }

  cache {
    type     = "S3"
    location = "acom-codebuild-cache-310865762107-us-east-1/cache"
  }

  source {
    type = "GITHUB_ENTERPRISE"
    location = "https://github.coxautoinc.com/coxautoinc/caller-identity"
    auth {
      type = "OAUTH"
      resource = data.aws_kms_secret.github_token.token
    }
  }

  vpc_config {
    vpc_id = var.vpc_id

    subnets = [
      data.aws_subnet.server-1a.id,
      data.aws_subnet.server-1b.id
    ]

    security_group_ids = [
      data.aws_security_group.server-security-group.id
    ]
  }

  tags {
    "StackId" = "caller-identity"
  }
}
