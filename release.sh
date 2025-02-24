#!/bin/bash

set -e

# Ensure Gradle Wrapper is available
if [ ! -f "./gradlew" ]; then
    echo "Gradle wrapper not found! Please run 'gradle wrapper' first."
    exit 1
fi

# Get current version from `gradle.properties`
PROJECT_VERSION=$(grep "version=" gradle.properties | cut -d'=' -f2)
IFS='.' read -r MAJOR MINOR PATCH <<< "$PROJECT_VERSION"

# Set new release version
NEW_VERSION="$MAJOR.$MINOR.$PATCH"
echo "Releasing version $NEW_VERSION"
./gradlew clean build -Pversion=$NEW_VERSION

# Commit, tag, and push release
git commit -am "Release Version $NEW_VERSION"
git tag v$NEW_VERSION
git push
git push --tags

# Publish the release
./gradlew publish

# Increment patch version for next snapshot
NEXT_PATCH=$((PATCH + 1))
NEXT_VERSION="$MAJOR.$MINOR.$NEXT_PATCH-SNAPSHOT"
echo "Preparing next version: $NEXT_VERSION"

# Update `gradle.properties`
sed -i "" "s/version=.*/version=$NEXT_VERSION/" gradle.properties

# Commit next snapshot
git commit -am "Prepare for Next Release"
git push

# Publish snapshot
./gradlew publish