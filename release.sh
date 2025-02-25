#!/bin/bash

set -e  # Exit immediately if a command fails

# Ensure GitHub CLI is installed
if ! command -v gh &> /dev/null; then
    echo "🚨 GitHub CLI (gh) is not installed! Please install it before proceeding."
    exit 1
fi

# Ensure we are in a Git repository
if ! git rev-parse --is-inside-work-tree &> /dev/null; then
    echo "🚨 This is not a Git repository! Please run the script inside a valid Git repository."
    exit 1
fi

# Ensure required Gradle files exist
if [ ! -f "build.gradle" ] && [ ! -f "build.gradle.kts" ]; then
    echo "🚨 No build.gradle or build.gradle.kts file found! Ensure this is a Gradle project."
    exit 1
fi

if [ ! -f "gradle.properties" ]; then
    echo "🚨 No gradle.properties file found! Ensure this file exists before proceeding."
    exit 1
fi

# Determine the default branch (main or master)
DEFAULT_BRANCH=$(git symbolic-ref refs/remotes/origin/HEAD | sed 's@^refs/remotes/origin/@@')

# Ensure we are on the default branch
CURRENT_BRANCH=$(git rev-parse --abbrev-ref HEAD)
if [ "$CURRENT_BRANCH" != "$DEFAULT_BRANCH" ]; then
    echo "🚨 You are on branch '$CURRENT_BRANCH'."
    echo "❗ Please switch to '$DEFAULT_BRANCH' and ensure it's up to date before proceeding."
    echo "👉 Run: git checkout $DEFAULT_BRANCH && git pull origin $DEFAULT_BRANCH"
    exit 1
fi

# Fetch latest remote changes
git fetch origin

# Check if local branch is behind remote
LOCAL_COMMIT=$(git rev-parse @)
REMOTE_COMMIT=$(git rev-parse @{u})
BASE_COMMIT=$(git merge-base @ @{u})

if [ "$LOCAL_COMMIT" = "$REMOTE_COMMIT" ]; then
    echo "✅ Local '$DEFAULT_BRANCH' is up to date."
elif [ "$LOCAL_COMMIT" = "$BASE_COMMIT" ]; then
    echo "🚨 Your local '$DEFAULT_BRANCH' branch is BEHIND the remote."
    echo "❗ Please pull the latest changes before proceeding."
    echo "👉 Run: git pull origin $DEFAULT_BRANCH"
    exit 1
elif [ "$REMOTE_COMMIT" = "$BASE_COMMIT" ]; then
    echo "⚠️ Your local '$DEFAULT_BRANCH' branch is AHEAD of remote."
    echo "You may want to push your changes before continuing."
else
    echo "🚨 Local and remote '$DEFAULT_BRANCH' branches have diverged!"
    echo "❗ Please manually sync them before proceeding."
    exit 1
fi

# Ensure Gradle Wrapper is available
if [ ! -f "./gradlew" ]; then
    echo "🚨 Gradle wrapper not found! Please run 'gradle wrapper' first."
    exit 1
fi

# Ensure gradle.properties contains a version and it's a SNAPSHOT
PROJECT_VERSION=$(grep "^version=" gradle.properties | cut -d'=' -f2)
if [[ -z "$PROJECT_VERSION" ]]; then
    echo "🚨 No 'version' property found in gradle.properties!"
    exit 1
fi

if [[ ! "$PROJECT_VERSION" =~ -SNAPSHOT$ ]]; then
    echo "🚨 The current version ($PROJECT_VERSION) is not a SNAPSHOT version!"
    echo "❗ Ensure the version ends with '-SNAPSHOT' before proceeding."
    exit 1
fi

echo "✅ Version check passed: $PROJECT_VERSION"

# Extract version parts and prepare release
IFS='.' read -r MAJOR MINOR PATCH <<< "${PROJECT_VERSION%-SNAPSHOT}"
NEW_VERSION="$MAJOR.$MINOR.$PATCH"
RELEASE_BRANCH="release-$NEW_VERSION"

# Prompt user for release confirmation
echo "🚀 Ready to release version \"$NEW_VERSION\" and prepare \"$MAJOR.$MINOR.$((PATCH+1))-SNAPSHOT\""
read -p "Proceed? (y/n): " CONFIRM
if [[ "$CONFIRM" != "y" ]]; then
    echo "❌ Release cancelled."
    exit 1
fi

# Create release branch
git checkout -b "$RELEASE_BRANCH"

# Update gradle.properties to remove -SNAPSHOT
sed -i "" "s/version=.*/version=$NEW_VERSION/" gradle.properties

git add gradle.properties
git commit -m "Release version $NEW_VERSION"
git push --set-upstream origin "$RELEASE_BRANCH"

git tag "v$NEW_VERSION"
git push origin "v$NEW_VERSION"

echo "✅ Release version $NEW_VERSION completed."

# Prepare next snapshot version
NEXT_PATCH=$((PATCH + 1))
NEXT_VERSION="$MAJOR.$MINOR.$NEXT_PATCH-SNAPSHOT"
NEXT_BRANCH="next-version-$NEXT_VERSION"

git checkout -b "$NEXT_BRANCH"
sed -i "" "s/version=.*/version=$NEXT_VERSION/" gradle.properties
git add gradle.properties
git commit -m "Prepare for next release $NEXT_VERSION"
git push --set-upstream origin "$NEXT_BRANCH"

echo "✅ Prepared next snapshot version: $NEXT_VERSION"

# Checkout default branch
git checkout "$DEFAULT_BRANCH"
echo "✅ Switched back to default branch: $DEFAULT_BRANCH"

# Explicit PR merge instructions
echo "📢 IMPORTANT:"
echo "👉 Merge the PR for the release branch ('$RELEASE_BRANCH') first."
echo "👉 After the release PR is merged, merge the next version PR ('$NEXT_BRANCH')."
echo "✅ Once both PRs are merged, the release process is complete!"
