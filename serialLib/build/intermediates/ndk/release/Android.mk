LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := serial_port
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_SRC_FILES := \
	C:\Users\Enrico\Desktop\CertoBalance\serialLib\src\main\jni\Android.mk \
	C:\Users\Enrico\Desktop\CertoBalance\serialLib\src\main\jni\Application.mk \
	C:\Users\Enrico\Desktop\CertoBalance\serialLib\src\main\jni\gen_SerialPort_h.sh \
	C:\Users\Enrico\Desktop\CertoBalance\serialLib\src\main\jni\SerialPort.c \

LOCAL_C_INCLUDES += C:\Users\Enrico\Desktop\CertoBalance\serialLib\src\main\jni
LOCAL_C_INCLUDES += C:\Users\Enrico\Desktop\CertoBalance\serialLib\src\release\jni

include $(BUILD_SHARED_LIBRARY)
