#!/usr/bin/env bash
echo "Running Release Preparation job"
echo "Release version passed: $1"
echo "git operation started"
mkdir ~/.ssh/ && echo -e "Host github.com\n\tStrictHostKeyChecking no\n" > ~/.ssh/config
git config user.email "gosu.lang.team@gmail.com"
git config user.name "circleCi-bot"
gpg --list-keys

if [[ $1 != *"SNAPSHOT"* ]] ; then
    releaseVersion=$1
    mvn release:prepare -B -DreleaseVersion=${releaseVersion}
    #mvn release:perform -B -s settings.xml
    else
       echo " Version number should not contain SNAPSHOT"
       exit 1
fi


