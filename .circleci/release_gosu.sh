#!/usr/bin/env bash
echo "Release Gosu job"
if [ -z ${CI_PULL_REQUEST} ]; then # returns true if CI_PULL_REQUEST value is empty
    echo "Running Deploy job"
   # mvn --batch-mode clean release:prepare release:perform
    mvn release:perform -B -s settings.xml
fi
