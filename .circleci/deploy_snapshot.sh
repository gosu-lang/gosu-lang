#!/usr/bin/env bash
echo "Running Deploy job"
if [ -z ${CI_PULL_REQUEST} ]; then
    mvn deploy -Dmaven.main.skip=true -Dmaven.resources.skip=true -Dmaven.test.skip=true -s settings.xml -B
fi