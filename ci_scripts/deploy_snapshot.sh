#!/usr/bin/env bash
if [ -z ${CI_PULL_REQUEST} ]; then
    mvn deploy -Dmaven.test.skip=true -s settings.xml -B
fi