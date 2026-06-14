#!/usr/bin/env bash
#
# Bumps the patch component of the project version (X.Y.Z -> X.Y.(Z+1))
# in gradle.properties. Safe to run by hand or from a git hook.
#
set -euo pipefail

cd "$(git rev-parse --show-toplevel)"
FILE="gradle.properties"

# Read the current "version = X.Y.Z" value (whitespace tolerant).
current="$(awk -F'=' '/^version[[:space:]]*=/ {gsub(/[[:space:]]/, "", $2); print $2; exit}' "$FILE")"

if [[ ! "$current" =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
    echo "bump-version: could not parse a semver from 'version = ${current:-<missing>}'" >&2
    exit 1
fi

IFS='.' read -r major minor patch <<< "$current"
new="${major}.${minor}.$((patch + 1))"

# Rewrite only the first version line; leave everything else byte-for-byte.
tmp="$(mktemp)"
awk -v new="$new" '
    /^version[[:space:]]*=/ && !done { print "version = " new; done = 1; next }
    { print }
' "$FILE" > "$tmp"
mv "$tmp" "$FILE"

echo "bump-version: ${current} -> ${new}"
