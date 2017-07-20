#!/bin/bash


let nt=${CIRCLE_NODE_TOTAL}
let ni=${CIRCLE_NODE_INDEX}


if [ $ni -eq 0 ] ; then
    echo "Running on first node "
   mvn surefire:test -Dtest=*.*Test  -pl gosu-ant-tools -pl gosu-doc -pl gosu-lab -pl gosu-maven-compiler -pl gosu-core-api -B

else
    echo "Running the below test of rest of the nodes"
    mvn surefire:test -Dtest=*.*Test -pl gosu-test -B

fi

