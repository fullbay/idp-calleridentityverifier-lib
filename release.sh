#!/bin/bash

set -e
mvn build-helper:parse-version versions:set -DnewVersion=\${parsedVersion.majorVersion}.\${parsedVersion.minorVersion}.\${parsedVersion.incrementalVersion}
mvn clean compile
export PROJECT_VERSION=$(mvn -q -N org.codehaus.mojo:exec-maven-plugin:1.3.1:exec -Dexec.executable='echo' -Dexec.args='${project.version}')
git commit -am "Release Version $PROJECT_VERSION"
git tag v$PROJECT_VERSION
git push
git push --tags
mvn clean deploy
mvn build-helper:parse-version versions:set -DnewVersion=\${parsedVersion.majorVersion}.\${parsedVersion.minorVersion}.\${parsedVersion.nextIncrementalVersion}-SNAPSHOT
git commit -am "Prepare for Next Release"
git push
mvn clean deploy

