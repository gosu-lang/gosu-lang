package gw.lang.enhancements
uses java.lang.System
uses java.lang.Throwable

/*
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreSystemEnhancement : java.lang.System
{
  public static property get CurrentUser() : String {
    return System.getProperty("user.name")
  }

  public static property get Platform() : gw.util.OSType
  {
    var g_iPlatform : gw.util.OSType
    try {
        var strOSName = System.getProperty("os.name")
        if (strOSName != null) {
            strOSName = strOSName.toLowerCase()
            if (strOSName.startsWith("windows")) {
                g_iPlatform = Windows
            } else if (strOSName.startsWith("mac")) {
                g_iPlatform = Macintosh
            } else if (strOSName.startsWith("solaris")) {
                g_iPlatform = Solaris
            } else if (strOSName.startsWith("linux")) {
                g_iPlatform = Linux
            } else if (strOSName.startsWith("os/2")) {
                g_iPlatform = OS2
            } else if (strOSName.startsWith("aix")) {
                g_iPlatform = AIX
            } else if (strOSName.startsWith("hp")) {
                g_iPlatform = HPUX
            } else if (strOSName.startsWith("netware")) {
                g_iPlatform = Netware
            } else {
                g_iPlatform = Undefined
            }
        }
    } catch (t : Throwable) {
        t.printStackTrace()
    }
    return g_iPlatform
  }
}
