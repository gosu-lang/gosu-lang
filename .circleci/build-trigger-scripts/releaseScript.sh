#!/usr/bin/env bash

if [ "$1" == "-h" ]; then
  echo "Usage: `basename $0` releaseVersion branchName"
  echo "	For ex: `basename $0` 1.14.55 rel/1.14.55"
  exit 0
fi

releaseVersion=$(echo $1 | xargs)
branchName=$(echo $2 | xargs)

echo ""
echo "snapshot version passed: $releaseVersion"
echo "branchName passed: $branchName"

echo ""
echo ".....Triggering release workflow in circleCI"
echo ""

curl  --request POST \
	--url https://circleci.com/api/v2/project/gh/gosu-lang/gosu-lang/pipeline \
	--header "Circle-Token: ${CIRCLECI_API_TOKEN}" \
	--header 'content-type: application/json' \
	--data @/dev/stdin<<EOF
		{"parameters":{"run_workflow_release":true, "release-version":"$releaseVersion"}, "branch" : "$branchName"}
		EOF