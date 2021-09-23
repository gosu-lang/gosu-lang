#!/usr/bin/env bash
echo "Update version job"
echo "snaphsot version passed: $1"
if [[ $1 == *-SNAPSHOT ]] ; then
    snapshotVersion=$1
    mvn -B release:update-versions -DdevelopmentVersion=${snapshotVersion}
    else
       echo " Version number should end with -SNAPSHOT"
       exit 1
fi
echo "git operation started"
mkdir ~/.ssh/ && echo -e "Host github.com\n\tStrictHostKeyChecking no\n" > ~/.ssh/config
git config user.email "gosu.lang.team@gmail.com"
git config user.name "circleCi-bot"
git add .
git commit -m "Change version $1 for testing - [Skip ci]"
git push --set-upstream origin ${CIRCLE_BRANCH}