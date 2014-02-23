#!/bin/bash

# Fail if any of the commands fails
set -e

RELEASE_VERSION=$1
NEXT_VERSION=$2


if [ -z "$RELEASE_VERSION" ] || [ -z "$NEXT_VERSION" ]; then
    echo "Usage:"
    echo "    $0 <RELEASE_VERSION> <NEXT_VERSION>"
    exit 1
fi

if [ -n "$(git status --porcelain)" ]; then
    echo "You have unstaged commits!"
    exit 1
fi

./bump-versions.sh $RELEASE_VERSION

echo "Run lein install"
lein install

echo "Run lein midje"
lein midje

echo "Create git tag"
git tag -s "v$RELEASE_VERSION" -m "Tag version v$RELEASE_VERSION"

./bump-versions.sh $NEXT_VERSION

cat <<EOF
Almost done!

Don't forget to:

    - push your release to github: git push github $(git rev-parse --abbrev-ref HEAD) --tags
    - push to clojars: lein deploy clojars
EOF
