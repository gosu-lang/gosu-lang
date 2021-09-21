#!/usr/bin/env bash
echo "Running Release Preparation job"
echo "Release version passed: $1"
echo "git operation started"
mkdir ~/.ssh/ && echo -e "Host github.com\n\tStrictHostKeyChecking no\n" > ~/.ssh/config
git config user.email "reach.sadheesh@gmail.com"
git config user.name "circleCi-bot"

if [[ $1 != *"SNAPSHOT"* ]] ; then
    releaseVersion=$1
    mvn release:prepare -B -DreleaseVersion=${releaseVersion}
    else
       echo " Version number should not contain SNAPSHOT"
       exit 1
fi
# Running final release target
mvn release:perform -B -s settings.xml
