/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.gosuc;

import com.sun.source.tree.Tree;
import gw.config.CommonServices;
import gw.internal.ext.com.beust.jcommander.JCommander;
import gw.internal.ext.org.objectweb.asm.ClassWriter;
import gw.lang.Gosu;
import manifold.api.fs.IDirectory;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import manifold.api.host.IManifoldHost;

/**
 */
public class GosucUtil {
  public static String indent( String in ) {
    StringBuilder sb = new StringBuilder( in );
    sb.insert( 0, "  " );
    for( int i = 0; i < sb.length(); i++ ) {
      char c = sb.charAt( i );
      if( c == '\n' ) {
        sb.insert( i + 1, "  " );
      }
    }
    return sb.toString();
  }

  public static IDirectory getDirectoryForPath( String path ) {
    return CommonServices.getFileSystem().getIDirectory( new File( path ) );
  }

  public static List<String> makeStringPaths( List<IDirectory> sourcePaths ) {
    List<String> paths = new ArrayList<String>();
    for( IDirectory dir: sourcePaths ) {
      paths.add( dir.getPath().getPathString() );
    }
    return paths;
  }

  public static URL toURL( File file ) {
    try {
      return file.toURI().toURL();
    }
    catch( MalformedURLException e ) {
      System.err.println( "Could not get URL for " + file );
      return null;
    }
  }

  public static List<IDirectory> toDirectories(List<String> paths) {
    List<IDirectory> dirs = new ArrayList<IDirectory>();
    for (String path : paths) {
      dirs.add(CommonServices.getFileSystem().getIDirectory(new File(path)));
    }
    return dirs;
  }

  /**
   * Get all JARs from the lib directory of the System's java.home property
   * @return List of absolute paths to all JRE libraries
   */
  public static List<String> getJreJars() {
    String javaHome = System.getProperty("java.home");
    Path libsDir = FileSystems.getDefault().getPath(javaHome, "/lib");
    List<String> retval = GosucUtil.getIbmClasspath();
    try {
      retval.addAll(Files.walk(libsDir)
              .filter( path -> path.toFile().isFile())
              .filter( path -> path.toString().endsWith(".jar"))
              .map( Path::toString )
              .collect(Collectors.toList()));
    } catch (SecurityException | IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    return retval;
  }

  /**
   * Special handling for the unusual structure of the IBM JDK.
   * @return A list containing the special 'vm.jar' absolute path if we are using an IBM JDK; otherwise an empty list is returned.
   */
  protected static List<String> getIbmClasspath() {
    List<String> retval = new ArrayList<>();
    if(System.getProperty("java.vendor").equals("IBM Corporation")) {
      String fileSeparator = System.getProperty("file.separator");
      String classpathSeparator = System.getProperty("path.separator");
      String[] bootClasspath = System.getProperty("sun.boot.class.path").split(classpathSeparator);
      for(String entry : bootClasspath) {
        if(entry.endsWith(fileSeparator + "vm.jar")) {
          retval.add(entry);
          break;
        }
      }
    }
    return retval;
  }

  public static List<String> getGosuBootstrapJars() throws ClassNotFoundException {
    return Arrays.asList( getClassLocation(Class.forName("gw.internal.gosu.parser.MetaType")), //get gosu-core
                          getClassLocation(Gosu.class), //get gosu-core-api
                          getClassLocation(IManifoldHost.class), //get manifold
                          getClassLocation(ClassWriter.class), //get asm
                          getClassLocation(JCommander.class), //get jcommander
                          getClassLocation(Tree.class) //get tools.jar
    );
  }

  private static String getClassLocation(Class clazz) throws ClassNotFoundException {
    ProtectionDomain pDomain = clazz.getProtectionDomain();
    CodeSource cSource = pDomain.getCodeSource();

    if (cSource != null) {
      URL loc = cSource.getLocation();
      File file;
      try {
        file = new File(URLDecoder.decode(loc.getPath(), StandardCharsets.UTF_8.name()));
      } catch (UnsupportedEncodingException e) {
        System.err.println("Unsupported Encoding for URL: " + loc);
        System.err.println(e);
        file = new File(loc.getPath());
      }
      return file.getPath();
    } else {
      throw new ClassNotFoundException("Cannot find the location of the requested className <" + clazz.getName() + "> in classpath.");
    }
  }

}
