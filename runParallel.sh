#!/bin/bash


#CIRCLE_NODE_TOTAL=4
#CIRCLE_NODE_INDEX=1

let nt=$(($CIRCLE_NODE_TOTAL-1))
let ni=$(($CIRCLE_NODE_INDEX-1))

echo $nt
echo $ni
echo "--------------------------"

if [ $CIRCLE_NODE_INDEX -eq 0 ] ; then
    echo "Running on first node "
   mvn install surefire:test -Dtest=*.*Test  -pl gosu-ant-tools -pl gosu-doc -pl gosu-lab -pl gosu-maven-compiler -pl gosu-core-api -B

else
    echo "Running the below test of rest of the nodes"
    #testlist=$(find ./gosu-test -name "*Test.gs" -not -path "*/target/*" -o -name "*Test.java" -not -path "*/target/*" |rev |cut -d"/" -f1|rev|cut -d"." -f1|sort|awk "NR %${nt}==${ni}"|tr '\n' ',')
    echo "--------------"
    mvn install surefire:test -Dtest=*.*Test -pl gosu-test -B

fi
#test1
#test2
