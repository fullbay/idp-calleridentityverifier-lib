#!/bin/bash

set -e

# Ensure Gradle Wrapper is available
if [ ! -f "./gradlew" ]; then
    echo "Gradle wrapper not found! Please run 'gradle wrapper' first."
    exit 1
fi

# Fetch latest changes
git fetch origin

# Get current version from `gradle.properties`
PROJECT_VERSION=$(grep "version=" gradle.properties | cut -d'=' -f2)
IFS='.' read -r MAJOR MINOR PATCH <<< "$PROJECT_VERSION"

# Set new release version
NEW_VERSION="$MAJOR.$MINOR.$PATCH"
RELEASE_BRANCH="release-$NEW_VERSION"

echo "Creating release branch: $RELEASE_BRANCH"
git checkout -b $RELEASE_BRANCH

# Update `gradle.properties` with release version
sed -i "" "s/version=.*/version=$NEW_VERSION/" gradle.properties
git commit -am "Release Version $NEW_VERSION"

# Push release branch for PR
git push --set-upstream origin $RELEASE_BRANCH

echo "--------------------------------------------"
echo "✅ Release branch created: $RELEASE_BRANCH"
echo "📢 Please create a PR and merge it into main."
echo "⏳ Waiting for PR merge..."
echo "After merge, press ENTER to continue."
echo "--------------------------------------------"

# Checkout main and pull latest changes
git checkout main
git pull origin main

# Create tag and push it
git tag v$NEW_VERSION
git push origin v$NEW_VERSION

# Publish the release
./gradlew publish

# Create next snapshot version
NEXT_PATCH=$((PATCH + 1))
NEXT_VERSION="$MAJOR.$MINOR.$NEXT_PATCH-SNAPSHOT"
NEXT_BRANCH="next-version-$NEXT_VERSION"

echo "Creating next version branch: $NEXT_BRANCH"
git checkout -b $NEXT_BRANCH

# Update `gradle.properties` with next snapshot version
sed -i "" "s/version=.*/version=$NEXT_VERSION/" gradle.properties
git commit -am "Prepare for Next Release"

# Push next version branch for PR
git push --set-upstream origin $NEXT_BRANCH

echo "--------------------------------------------"
echo "✅ Next version branch created: $NEXT_BRANCH"
echo "📢 Please create a PR and merge it into main."
echo "⏳ Waiting for PR merge..."
echo "After merge, press ENTER to continue."
echo "--------------------------------------------"

# Checkout main and pull latest changes
git checkout main
git pull origin main

# Final deployment
./gradlew publish

echo "--------------------------------------------"
echo "🎉 Release $NEW_VERSION complete!"
echo "🚀 Next snapshot version is $NEXT_VERSION."
echo "--------------------------------------------"