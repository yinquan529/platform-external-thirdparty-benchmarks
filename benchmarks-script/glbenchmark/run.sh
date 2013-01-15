#!/bin/bash

#need to be defined for different benchmark apks
activity="com.glbenchmark.glbenchmark25/com.glbenchmark.activities.GLBenchmarkDownloaderActivity"
#apk_file_name="GLBenchmark_v2.5.apk"
apk_file_name="GLBenchmark_2.5.1.apk"
test_method="testGLBenchmark"
apk_package="com.glbenchmark.glbenchmark25"

function func_post_install(){
    #get the obb file and push it into android
    mkdir -p "${parent_dir}/Android/obb/com.glbenchmark.glbenchmark25"
    obb_file_name="main.1.com.glbenchmark.glbenchmark25.obb"
    obb_file_path="${parent_dir}/Android/obb/com.glbenchmark.glbenchmark25/${obb_file_name}"
    if [ ! -f "${obb_file_path}" ]; then
        get_file_with_base_url "${obb_file_name}"
        cp -uvf "${APKS_DIR}/${obb_file_name}" "${parent_dir}/Android/obb/com.glbenchmark.glbenchmark25/${obb_file_name}"
    fi
    adb push ${parent_dir}/Android /storage/sdcard0/Android

    user=`adb shell ls -l /data/data/|grep com.glbenchmark.glbenchmark25|cut -d \  -f 2`
    user=`echo ${user}|sed 's/\r//'`
    dir_prefs="/data/data/com.glbenchmark.glbenchmark25/shared_prefs"
    adb push ${parent_dir}/shared_prefs "${dir_prefs}"
    adb shell chown ${user}:${user} "${dir_prefs}"
    adb shell chmod 771 "${dir_prefs}"
    adb shell chown ${user}:${user} "${dir_prefs}/com.glbenchmark.glbenchmark25_preferences.xml"
    adb shell chmod 660 "${dir_prefs}/com.glbenchmark.glbenchmark25_preferences.xml"
}

#following should no need to modify
parent_dir=`dirname ${0}`
source "${parent_dir}/../common/common.sh"
post_install="func_post_install"
pre_uninstall="adb pull /data/data/com.glbenchmark.glbenchmark25/cache/last_results_2.5.1.xml $parent_dir/last_results_2.5.1.xml"
main "$@"
