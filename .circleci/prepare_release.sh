#!/usr/bin/env bash
echo "Running Release Preparation job"
echo "Release version passed: $1"
if [[ $1 != *"SNAPSHOT"* ]] ; then
    releaseVersion=$1
    mvn release:prepare -B -DreleaseVersion=${releaseVersion}
    else
       echo " Version number should not contain SNAPSHOT"
       exit 1
fi
