#!/bin/bash

set -e  # Exit immediately if a command fails

# Ensure we are on the main branch
CURRENT_BRANCH=$(git rev-parse --abbrev-ref HEAD)
if [ "$CURRENT_BRANCH" != "main" ]; then
    echo "🚨 You are on branch '$CURRENT_BRANCH'."
    echo "❗ Please switch to 'main' and ensure it's up to date before proceeding."
    echo "👉 Run: git checkout main && git pull origin main"
    exit 1
fi

# Fetch latest remote changes
git fetch origin

# Check if local main is behind remote main
LOCAL_MAIN=$(git rev-parse @)
REMOTE_MAIN=$(git rev-parse @{u})
BASE_MAIN=$(git merge-base @ @{u})

if [ "$LOCAL_MAIN" = "$REMOTE_MAIN" ]; then
    echo "✅ Local 'main' is up to date."
elif [ "$LOCAL_MAIN" = "$BASE_MAIN" ]; then
    echo "🚨 Your local 'main' branch is BEHIND the remote."
    echo "❗ Please pull the latest changes before proceeding."
    echo "👉 Run: git pull origin main"
    exit 1
elif [ "$REMOTE_MAIN" = "$BASE_MAIN" ]; then
    echo "⚠️ Your local 'main' branch is AHEAD of remote."
    echo "You may want to push your changes before continuing."
else
    echo "🚨 Local and remote 'main' branches have diverged!"
    echo "❗ Please manually sync them before proceeding."
    exit 1
fi

# Ensure Gradle Wrapper is available
if [ ! -f "./gradlew" ]; then
    echo "🚨 Gradle wrapper not found! Please run 'gradle wrapper' first."
    exit 1
fi

# Check for SNAPSHOT dependencies
SNAPSHOT_DEPENDENCIES=$(./gradlew dependencies --configuration runtimeClasspath | grep -E "SNAPSHOT" || true)

if [[ ! -z "$SNAPSHOT_DEPENDENCIES" ]]; then
    echo "🚨 Release blocked! SNAPSHOT dependencies found:"
    echo "$SNAPSHOT_DEPENDENCIES"
    echo "❗ Please remove all SNAPSHOT dependencies before proceeding."
    exit 1
fi

echo "✅ No SNAPSHOT dependencies found. Proceeding with release."

# Get current version from `gradle.properties`
PROJECT_VERSION=$(grep "version=" gradle.properties | cut -d'=' -f2)
IFS='.' read -r MAJOR MINOR PATCH <<< "${PROJECT_VERSION%-SNAPSHOT}"

# Set new release version
NEW_VERSION="$MAJOR.$MINOR.$PATCH"
RELEASE_BRANCH="release-$NEW_VERSION"

echo "🚀 Creating release branch: $RELEASE_BRANCH"
git checkout -b $RELEASE_BRANCH

# Update `gradle.properties` to remove `-SNAPSHOT`
echo "Updating version in gradle.properties to $NEW_VERSION"
sed -i "" "s/version=.*-SNAPSHOT/version=$NEW_VERSION/" gradle.properties

# Verify the change
if grep -q "version=$NEW_VERSION" gradle.properties; then
    echo "✅ gradle.properties updated to $NEW_VERSION"
else
    echo "🚨 Failed to update gradle.properties!"
    exit 1
fi

# Commit and push release branch
git add gradle.properties
git commit -m "Release Version $NEW_VERSION"
git push --set-upstream origin $RELEASE_BRANCH

# Create and push a Git tag for the release
git tag v$NEW_VERSION
git push origin v$NEW_VERSION

echo "✅ Release branch and tag pushed: $RELEASE_BRANCH (tag: v$NEW_VERSION)"

# Create next snapshot version
NEXT_PATCH=$((PATCH + 1))
NEXT_VERSION="$MAJOR.$MINOR.$NEXT_PATCH-SNAPSHOT"
NEXT_BRANCH="next-version-$NEXT_VERSION"

echo "🚀 Creating next version branch: $NEXT_BRANCH"
git checkout -b $NEXT_BRANCH

# Update `gradle.properties` to the next snapshot version
echo "Updating version in gradle.properties to $NEXT_VERSION"
sed -i "" "s/version=.*/version=$NEXT_VERSION/" gradle.properties

# Verify the change
if grep -q "version=$NEXT_VERSION" gradle.properties; then
    echo "✅ gradle.properties updated to $NEXT_VERSION"
else
    echo "🚨 Failed to update gradle.properties!"
    exit 1
fi

# Commit and push next version branch
git add gradle.properties
git commit -m "Prepare for Next Release"
git push --set-upstream origin $NEXT_BRANCH

echo "✅ Next version branch pushed: $NEXT_BRANCH"
echo "🎉 Release process complete! Merge these branches via PRs when ready."
