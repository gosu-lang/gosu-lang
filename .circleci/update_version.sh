#!/usr/bin/env bash
echo "Updating version job"
echo "snaphsot version passed: $1"
if [[ $1 == *-SNAPSHOT ]] ; then
    echo "snaphsot version passed: $1"
    snapshotversion=$1
    mvn -B release:update-versions -DdevelopmentVersion=${snapshotversion}
    else
       echo " Version number should end with -SNAPSHOT"
       exit 1
fi
git add -u
git commit -m "Change version $1 for testing"
git push