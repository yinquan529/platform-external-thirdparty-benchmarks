#!/bin/bash

if [ -z "$ANDROID_HOME" ]; then
    echo "Please export the ANDORID_HOME environment variable as following:"
    echo "export ANDORID_HOME=<sdk-path>"
    exit 1
fi

if ! `which ant >&/dev/null`; then
    echo "Please install the ant command which will be used for building the jar file"
    exit 1
fi

parent=$(cd $(dirname $0);pwd)
proj_name=`basename $parent`

${ANDROID_HOME}/tools/android create uitest-project -n $proj_name -t 1 -p $parent
if [ $? -ne 0 ]; then
    echo "Failed to create the uitest-project with the source under this directory"
    exit 1
fi
ant build
if [ $? -ne 0 ]; then
    echo "Failed to build the directory"
    exit 1
fi

