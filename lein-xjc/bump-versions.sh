#!/bin/bash

set -e

VERSION=$1

if [ -z "$VERSION" ]; then
    echo "Usage: $0 <VERSION>"
    exit 1
fi

if [ -n "$(git status --porcelain)" ]; then
    echo "You have unstaged commits!"
    exit 1
fi

echo "Bump version numbers to $VERSION"
find .  -iname 'project.clj' -print0 | xargs -0 sed -i "s;clj-jaxb/lein-xjc \".\+\";clj-jaxb/lein-xjc \"$VERSION\";g"

echo "Add files to version control"
find . -iname 'project.clj' -print0 | xargs -0 git add

echo "Commit"
git commit -m "Bump version to $VERSION."
