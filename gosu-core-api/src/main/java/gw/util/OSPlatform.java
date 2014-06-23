/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util;

import static gw.util.OSPlatform.Platform.*;

/**
 */
public class OSPlatform {
  //Platform enum
  public static enum Platform {
    PLAT_UNINIT,
    PLAT_UNDEFINED,
    PLAT_WINDOWS,
    PLAT_MAC,
    PLAT_SOLARIS,
    PLAT_LINUX,
    PLAT_OS2,
    PLAT_HPUX,
    PLAT_AIX,
    PLAT_NETWARE,
  }

  private static Platform PLATFORM = PLAT_UNINIT;

  public static Platform getPlatform() {
    if (PLATFORM == PLAT_UNINIT) {
      String strOSName = System.getProperty("os.name");
      if (strOSName != null) {
        strOSName = strOSName.toLowerCase();

        if (strOSName.startsWith("windows")) {
          PLATFORM = PLAT_WINDOWS;
        } else if (strOSName.startsWith("mac") || strOSName.startsWith("darwin")) {
          PLATFORM = PLAT_MAC;
        } else if (strOSName.startsWith("solaris")) {
          PLATFORM = PLAT_SOLARIS;
        } else if (strOSName.startsWith("linux")) {
          PLATFORM = PLAT_LINUX;
        } else if (strOSName.startsWith("os/2")) {
          PLATFORM = PLAT_OS2;
        } else if (strOSName.startsWith("aix")) {
          PLATFORM = PLAT_AIX;
        } else if (strOSName.startsWith("hp")) {
          PLATFORM = PLAT_HPUX;
        } else if (strOSName.startsWith("netware")) {
          PLATFORM = PLAT_NETWARE;
        } else {
          PLATFORM = PLAT_UNDEFINED;
        }
      }
    }

    return PLATFORM;
  }

  public static boolean isWindows() {
    return getPlatform() == PLAT_WINDOWS;
  }

  public static boolean isMac() {
    return getPlatform() == PLAT_MAC;
  }

  public static boolean isSolaris() {
    return getPlatform() == PLAT_SOLARIS;
  }

  public static boolean isLinux() {
    return getPlatform() == PLAT_LINUX;
  }

}
