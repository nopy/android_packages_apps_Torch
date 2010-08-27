#define CONFIG_SENSOR_M4MO

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

  dev = open("/dev/msm_camera/msm_camera0", O_RDWR);
  if (dev < 0) {
    return (*env)->NewStringUTF(env, "Failed");
  }
  return (*env)->NewStringUTF(env, "OK");
}

JNIEXPORT jstring JNICALL Java_net_cactii_flash2_FlashDevice_setFlashOff(JNIEnv *env)
{
    ioctl_msg_info ctrl_info;
    ctrl_info.codeA = FLASH_CMD ;
    ctrl_info.codeB = FLASH_MOVIE ; 
    ctrl_info.codeC = FLASH_CMD_OFF ;
  if ((ioctlRetVal = ioctl( dev, MSM_CAM_IOCTL_PGH_MSG, &ctrl_info)) < 0) {
    return (*env)->NewStringUTF(env, "Failed");
  }
  return (*env)->NewStringUTF(env, "OK");
}

JNIEXPORT jstring JNICALL Java_net_cactii_flash2_FlashDevice_setFlashOn(JNIEnv *env)
{

    ioctl_msg_info ctrl_info;
    ctrl_info.codeA = FLASH_CMD ;
    ctrl_info.codeB = FLASH_MOVIE ; 
    ctrl_info.codeC = FLASH_CMD_ON ;
  if ((ioctlRetVal = ioctl( dev, MSM_CAM_IOCTL_PGH_MSG, &ctrl_info)) < 0) {
    return (*env)->NewStringUTF(env, "Failed");
  }
  return (*env)->NewStringUTF(env, "OK");
}

JNIEXPORT jstring JNICALL Java_net_cactii_flash2_FlashDevice_setFlash2On(JNIEnv *env)
{
    ioctl_msg_info ctrl_info;
    ctrl_info.codeA = FLASH_CMD ;
    ctrl_info.codeB = FLASH_CAMERA ; 
    ctrl_info.codeC = FLASH_CMD_ON ;
  if ((ioctlRetVal = ioctl( dev, MSM_CAM_IOCTL_PGH_MSG, &ctrl_info)) < 0) {
    return (*env)->NewStringUTF(env, "Failed");
  }
  return (*env)->NewStringUTF(env, "OK");
}

JNIEXPORT jstring JNICALL Java_net_cactii_flash2_FlashDevice_setFlash2Off(JNIEnv *env)
{
    ioctl_msg_info ctrl_info;
    ctrl_info.codeA = FLASH_CMD ;
    ctrl_info.codeB = FLASH_CAMERA ; 
    ctrl_info.codeC = FLASH_CMD_OFF ;
  if ((ioctlRetVal = ioctl( dev, MSM_CAM_IOCTL_PGH_MSG, &ctrl_info)) < 0) {
    return (*env)->NewStringUTF(env, "Failed");
  }
  return (*env)->NewStringUTF(env, "OK");
}

JNIEXPORT jstring JNICALL Java_net_cactii_flash2_FlashDevice_closeFlash(JNIEnv *env)
{
  if (dev > 0) {
    close(dev);
    return (*env)->NewStringUTF(env, "OK");
  }
  return (*env)->NewStringUTF(env, "Failed");
}
