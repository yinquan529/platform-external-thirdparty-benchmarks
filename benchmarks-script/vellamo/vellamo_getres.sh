#!/bin/bash

path_dir=`dirname $0`
old_pwd=`pwd`
cd ${path_dir}
if [ -n "${1}" ]; then
    adb -s "${1}" pull /data/data/com.quicinc.vellamo/files files
else
    adb pull /data/data/com.quicinc.vellamo/files files
fi
if [ $? -ne 0 ]; then
    echo "Failed to get the result of vellamo test"
    exit 1
fi

sed 's/<[^>]*>//g' files/latest_result.html |grep -v '^[ ]*$'|sed 's/&nbsp;/:/g'|sed 's/"/ "/g'|sed 's/:/=/g'|grep '=' >results.txt
rm -fr files
cd "${old_pwd}"
