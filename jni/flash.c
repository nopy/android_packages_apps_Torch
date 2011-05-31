#define CONFIG_SENSOR_M4MO

#define LOG_TAG "Torch-JNI"
#include <utils/Log.h>

#include "msm_camera.h"
#include "sec_m4mo.h"
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/syscall.h>
#include <sys/ioctl.h>
#include <jni.h>

int dev;
int led_mode;
int ioctlRetVal = 1;

JNIEXPORT jstring JNICALL Java_net_cactii_flash2_FlashDevice_openFlash(JNIEnv* env)
{
  LOGD("opening /dev/msm_camera/control0") ;
  dev = open("/dev/msm_camera/control0", O_RDWR);
  if (dev < 0) {
    LOGE("failed opening /dev/msm_camera/control0") ;
    return (*env)->NewStringUTF(env, "Failed");
  }
  LOGD("open OK") ;
  return (*env)->NewStringUTF(env, "OK");
}

JNIEXPORT jstring JNICALL Java_net_cactii_flash2_FlashDevice_setFlashOff(JNIEnv *env)
{
  LOGD("setFlashOff") ;
    ioctl_msg_info ctrl_info;
    ctrl_info.codeA = FLASH_CMD ;
    ctrl_info.codeB = FLASH_MOVIE ; 
    ctrl_info.codeC = FLASH_CMD_OFF ;
  if ((ioctlRetVal = ioctl( dev, MSM_CAM_IOCTL_PGH_MSG, &ctrl_info)) < 0) {
    LOGD("FAILED") ;
    return (*env)->NewStringUTF(env, "Failed");
  }
  LOGD("SUCCESS") ;
  return (*env)->NewStringUTF(env, "OK");
}

JNIEXPORT jstring JNICALL Java_net_cactii_flash2_FlashDevice_setFlashOn(JNIEnv *env)
{
  LOGD("setFlashOn") ;

    ioctl_msg_info ctrl_info;
    ctrl_info.codeA = FLASH_CMD ;
    ctrl_info.codeB = FLASH_MOVIE ; 
    ctrl_info.codeC = FLASH_CMD_ON ;
  if ((ioctlRetVal = ioctl( dev, MSM_CAM_IOCTL_PGH_MSG, &ctrl_info)) < 0) {
    LOGD("FAILED") ;    
    return (*env)->NewStringUTF(env, "Failed");
  }
    LOGD("SUCCESS") ;  
  return (*env)->NewStringUTF(env, "OK");
}

JNIEXPORT jstring JNICALL Java_net_cactii_flash2_FlashDevice_setFlash2On(JNIEnv *env)
{
    LOGD("setFlash2On") ;
    ioctl_msg_info ctrl_info;
    ctrl_info.codeA = FLASH_CMD ;
    ctrl_info.codeB = FLASH_MOVIE ; 
    ctrl_info.codeC = FLASH_CMD_ON ;
  if ((ioctlRetVal = ioctl( dev, MSM_CAM_IOCTL_PGH_MSG, &ctrl_info)) < 0) {
    LOGD("FAILED") ;    
    return (*env)->NewStringUTF(env, "Failed");
  }
  return (*env)->NewStringUTF(env, "OK");
}

JNIEXPORT jstring JNICALL Java_net_cactii_flash2_FlashDevice_setFlash2Off(JNIEnv *env)
{
  LOGD("setFlash2Off") ;
    ioctl_msg_info ctrl_info;
    ctrl_info.codeA = FLASH_CMD ;
    ctrl_info.codeB = FLASH_MOVIE ; 
    ctrl_info.codeC = FLASH_CMD_OFF ;
  if ((ioctlRetVal = ioctl( dev, MSM_CAM_IOCTL_PGH_MSG, &ctrl_info)) < 0) {
    LOGD("FAILED") ;    
    return (*env)->NewStringUTF(env, "Failed");
  }
    LOGD("SUCCESS") ;    
  return (*env)->NewStringUTF(env, "OK");
}

JNIEXPORT jstring JNICALL Java_net_cactii_flash2_FlashDevice_closeFlash(JNIEnv *env)
{
  LOGD("closing device") ;
  if (dev > 0) {
    close(dev);
    LOGD("SUCCESS") ;
    return (*env)->NewStringUTF(env, "OK");
  }
  LOGD("FAILED") ;
  return (*env)->NewStringUTF(env, "Failed");
}
