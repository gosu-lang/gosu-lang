#!/usr/bin/env bash
echo "Updating version job"
echo "snaphsot version passed: $1"
if [[ $1 == *-SNAPSHOT ]] ; then
    echo "snaphsot version passed: $1"
    snapshotversion=$1
    echo ${snapshotversion}
    mvn -B release:update-versions -DdevelopmentVersion=${snapshotversion}
    else
       echo " Version number should end with -SNAPSHOT"
       exit 1
fi
echo "git operation started"
mkdir ~/.ssh/ && echo -e "Host github.com\n\tStrictHostKeyChecking no\n" > ~/.ssh/config
git config user.email "reach.sadheesh@gmail.com"
git config user.name "circleCi-bot"
git add .
git commit -m "Change version $1 for testing - [Skip ci]"
git push --set-upstream origin ${CIRCLE_BRANCH}