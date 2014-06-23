#!/bin/sh

file=$0
_G_ROOT_DIR=`dirname "$file"`
file=`basename "$file"`
while [ x"$file" \!= x ];
do
  ch=`echo "$file" | cut -c1`
  if [ $ch = / ]; then
    _G_ROOT_DIR=`dirname "$file"`
    file=`readlink "$file"`
  else
    file="$_G_ROOT_DIR/$file"
    _G_ROOT_DIR=`dirname "$file"`
    file=`readlink "$file"`
  fi
done

_G_CLASSPATH=
for i in `ls $_G_ROOT_DIR/../jars 2>/dev/null`
do
  _G_CLASSPATH="$_G_CLASSPATH:$_G_ROOT_DIR/../jars/${i}"
done

_G_CLASSPATH="$_G_CLASSPATH:$CLASSPATH"

if [ "$1" = "debug" ]; then
  _DEBUG="-Xdebug -Xrunjdwp:transport=dt_socket,address=5005,server=y,suspend=y"
  shift
fi

# setupArgs
# while [ $# -ge 0 ]; do
#
# done

# checkJava
_JAVACMD=$JAVACMD
if [ "$_JAVACMD" = "" ]; then
  if [ "$JAVA_HOME" = "" -o ! -f "$JAVA_HOME/bin/java" ]; then
    _JAVACMD=java
  else
    _JAVACMD="$JAVA_HOME/bin/java"
  fi
fi

_G_OSNAME=`uname -s`

if [ "$_G_OSNAME" = "Darwin" ]; then
  _G_EXTRA_OPTS=-Xdock:name=Gosu
fi

#echo "Command: $_JAVACMD $DEBUG $GOSU_OPTS -classpath $_G_CLASSPATH gw.lang.shell.Gosu $_G_ROOT_DIR $CMD_LINE_ARGS"
$_JAVACMD $_DEBUG $GOSU_OPTS $_G_EXTRA_OPTS -classpath $_G_CLASSPATH gw.lang.shell.Gosu "$@"

