#!/usr/bin/env bash

echo "Release Gosu job"

mvn release:perform -B -s settings.xml

