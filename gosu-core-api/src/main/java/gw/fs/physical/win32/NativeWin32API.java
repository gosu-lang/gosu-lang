/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.fs.physical.win32;

import gw.lang.UnstableAPI;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@UnstableAPI
public class NativeWin32API {

  // ------------------------ Start test code
  public static int _numNativeFiles = 0;
  public static int _numJavaFiles = 0;

  public static void main(String[] args) {
    long start = System.currentTimeMillis();
    recursiveListDir("C:\\eng\\diamond\\pl2\\active\\core");
    System.out.println("Native found " + _numNativeFiles + " in " + (System.currentTimeMillis() - start));

//    start = System.currentTimeMillis();
//    recursiveListDirInJava(new File("C:\\eng\\diamond\\pl2\\active\\core"));
//    System.out.println("Java found " + _numJavaFiles + " in " + (System.currentTimeMillis() - start));

    testFileMetadata("C:\\eng\\diamond\\pl2\\active\\core\\platform\\gosu-core\\src\\gw\\internal\\gosu\\module\\fs\\IFileFactory.java",
        "C:\\eng\\diamond\\pl2\\active\\core\\platform\\gosu-core\\src\\gw\\internal\\gosu\\module\\fs\\ResourcePath.java");

    testFileListing("C:\\eng\\diamond\\pl2\\active\\core",
        "C:\\eng\\diamond\\pl2\\active\\core\\platform\\gosu-core\\src\\gw\\internal\\gosu\\module\\fs");
 }

  public static void recursiveListDir(String path) {
    List<Win32FindData> metadataList = listDir(path);
    for (Win32FindData fileMd : metadataList) {
      _numNativeFiles++;
//      System.out.println("[" + path + "\\" + fileMd.getName() + "]");
      if ((Win32Util.FILE_ATTRIBUTE_DIRECTORY & fileMd.getAttributes()) != 0) {
        recursiveListDir(path + "\\" + fileMd.getName());
      }
    }
  }

  public static void recursiveListDirInJava(File f) {
    File[] files = f.listFiles();
    for (File child : files) {
      _numJavaFiles++;
      if (child.isDirectory()) {
        recursiveListDirInJava(child);
      }
    }
  }

  public static void testFileMetadata(String path1, String path2) {
    int numTests = 10000;
    long start = System.currentTimeMillis();
    for (int i = 0; i < numTests; i++) {
//      fileMetadata(path1);
      fileMetadata(path2);
    }
    long nativeTime = System.currentTimeMillis() - start;
    System.out.println("Native attribute time was " + nativeTime + "ms");

    start = System.currentTimeMillis();
    for (int i = 0; i < numTests; i++) {
//      new File(path1).lastModified();
      new File(path2).lastModified();
    }
    long javaTime = System.currentTimeMillis() - start;
    System.out.println("Java attribute time was " + javaTime + "ms");
  }

  public static void testFileListing(String path1, String path2) {
    int numTests = 1000;
    long start = System.currentTimeMillis();
    for (int i = 0; i < numTests; i++) {
      listDir(path1);
      listDir(path2);
    }
    long nativeTime = System.currentTimeMillis() - start;
    System.out.println("Native listing time was " + nativeTime + "ms");

    start = System.currentTimeMillis();
    for (int i = 0; i < numTests; i++) {
      new File(path1).listFiles();
      new File(path2).listFiles();
    }
    long javaTime = System.currentTimeMillis() - start;
    System.out.println("Java listing time was " + javaTime + "ms");
  }
  // --------------- END TEST CODE

  public static List<Win32FindData> listDir(String path) {
    final List<Win32FindData> results = new ArrayList<Win32FindData>();
    listDirectory(path, new INativeWin32APICallback() {
      @Override
      public void handleFile(String name, int attributes, long createTime, long lastAccessTime, long lastModifiedTime, long length) {
        if (!(name.equals(".") || name.equals(".."))) {
          results.add(new Win32FindData(attributes, createTime, lastAccessTime, lastModifiedTime, length, name));
        }
      }
    });
    return results;
  }

  public static Win32FindData fileMetadata(String path) {
    final Win32FindData[] result = new Win32FindData[1];
    fileMetadata(path, new INativeWin32APICallback() {
      @Override
      public void handleFile(String name, int attributes, long createTime, long lastAccessTime, long lastModifiedTime, long length) {
        result[0] = new Win32FindData(attributes, createTime, lastAccessTime, lastModifiedTime, length, name);
      }
    });

    return result[0];
  }

  private static boolean _enabled = false;

  static {
    try {
      URL loc = Class.forName( "gw.NativeLibraryLocationMarker" ).getProtectionDomain().getCodeSource().getLocation();
      System.load( new File( loc.toURI() ).getParent() + File.separator + "newdirscanner.dll" );
      _enabled = true;
    } catch(Throwable t) {
      _enabled = false;
      t.printStackTrace();
    }
  }

  public static native void listDirectory(String path, INativeWin32APICallback callback);

  public static native void fileMetadata(String path, INativeWin32APICallback callback);
}
