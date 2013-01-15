# Author: Linaro Android Team <linaro-dev@lists.linaro.org>
#
# These files are Copyright (C) 2012 Linaro Limited and they
# are licensed under the Apache License, Version 2.0.
# You may obtain a copy of this license at
# http://www.apache.org/licenses/LICENSE-2.0

local_target_dir := $(TARGET_OUT_DATA)/local/tmp
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_MODULE_TAGS := optional

LOCAL_MODULE := linaro.android

LOCAL_JAVA_LIBRARIES := uiautomator.core

LOCAL_MODULE_PATH := $(local_target_dir)

include $(BUILD_JAVA_LIBRARY)
