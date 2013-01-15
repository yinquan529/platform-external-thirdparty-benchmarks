#!/bin/bash

#need to be defined for different benchmark apks
activity="com.aurorasoftworks.quadrant.ui.standard/.QuadrantStandardLauncherActivity"
apk_file_name="com.aurorasoftworks.quadrant.ui.standard-1.apk"
test_method="testQuadrant"
apk_package="com.aurorasoftworks.quadrant.ui.standard"

#following should no need to modify
parent_dir=`dirname ${0}`
source "${parent_dir}/../common/common.sh"
main "$@"
