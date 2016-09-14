import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Launcher
{
  public static final String JAR_REPO_DIR = "JAR-REPO";     //!! if you change this, also change it in Gosu.java
  public static final String JAR_REPO_TXT = "jar-repo.txt"; //!! "


  public static void main( String[] args ) throws IOException
  {
    try
    {
      String jarLoc = new File( Launcher.class.getProtectionDomain().getCodeSource().getLocation().toURI() ).getAbsolutePath();
      File appRepo = cacheJars( jarLoc );
      Properties props = new Properties();
      ClassLoader loader = Launcher.class.getClassLoader();
      InputStream inProps = loader.getResource( "main.properties" ).openStream();
      props.load( inProps );
      String strProgram = props.getProperty( "Program" );
      String strBundled = props.getProperty( "BundledGosu" );
      inProps.close();

      // Add repo jars directly to the loader's path
      addRepoJarPaths( appRepo );

      if( strBundled == null || !strBundled.equalsIgnoreCase( "true" ) )
      {
        // Gosu Jars are NOT bundled, find them on user's machine (installing Gosu if necessary), add them directly to loader's path
        if( !addGosuJarsToLoader() )
        {
          // Failed to find / install Gosu on user's machine
          return;
        }
      }

      List<String> progArgs = Arrays.asList( args );

      URL progClass = loader.getResource( strProgram.replace( '.', '/' ) + ".class" );
      if( progClass != null )
      {
        // Program is precompiled, run its main() method directly on its .class file

        Class<?> prog = Class.forName( strProgram );
        Method main = prog.getMethod( "main", String[].class );
        main.invoke( null, new Object[]{progArgs.toArray( new String[progArgs.size()] )} );
      }
      else
      {
        // Run program from source, launch it via Gosu.main()

        String jarPaths = getRepoJarPaths( appRepo );
        List<String> gosuArgs = Arrays.asList( "-classpath", "\"" + jarLoc + jarPaths + "\"", "-fqn", strProgram );
        List<String> concat = new ArrayList<>( gosuArgs );

        concat.addAll( progArgs );

        launchGosuMain( concat );
      }
    }
    catch( Throwable e )
    {
      e.printStackTrace();
      System.exit( 0 );
    }
  }

  private static void launchGosuMain( List<String> args ) throws Exception
  {
    Class<?> gosuClass = Class.forName( "gw.lang.Gosu" );
    Method main = gosuClass.getMethod( "main", String[].class );
    main.invoke( null, new Object[]{args.toArray( new String[args.size()] )} );
  }

  private static boolean addGosuJarsToLoader() throws Exception
  {
    File recentGosu = findRecentGosu();
    if( recentGosu == null )
    {
      int res = JOptionPane.showConfirmDialog( null, "This application requires the Gosu Runtime Environment. Would you like to install it?", "Gosu", JOptionPane.YES_NO_OPTION );
      if( res == JOptionPane.YES_OPTION )
      {
        installGosu();
      }
      else
      {
        return false;
      }
    }
    recentGosu = findRecentGosu();
    if( recentGosu != null )
    {
      List<File> gosuJars = getGosuJars( recentGosu );
      if( !gosuJars.isEmpty() )
      {
        URLClassLoader loader = (URLClassLoader)Launcher.class.getClassLoader();
        Method addUrl = getAddUrlMethod();
        for( File csr : gosuJars )
        {
          addUrl.invoke( loader, csr.toURI().toURL() );
        }
        return true;
      }
    }

    return false;
  }

  private static List<File> getGosuJars( File gosuInstallDir )
  {
    List<File> jars = Collections.emptyList();
    for( File csr : gosuInstallDir.listFiles() )
    {
      if( csr.isFile() )
      {
        if( (csr.getName().contains( "gosu-core" ) || csr.getName().contains( "asm" ))
            && csr.getName().endsWith( ".jar" ) )
        {
          if( jars.isEmpty() )
          {
            jars = new ArrayList<>();
          }
          jars.add( csr );
        }
      }
      if( csr.isDirectory() )
      {
        jars = getGosuJars( csr );
        if( !jars.isEmpty() )
        {
          return jars;
        }
      }
    }
    return jars;
  }

  private static void installGosu()
  {
    ProgressMonitor progress = new ProgressMonitor( null, "Installing Gosu...", "", 1, 3 );
    progress.setProgress( 1 );
    progress.setNote( "Downloading latest Gosu release" );
    try
    {
      //## todo: use a static link to LATEST GOSU RELEASE
      URL url = new URL( "https://dl.dropboxusercontent.com/u/10642488/gosu-1.14.1-full.zip" );
      File outDir = new File( new File( getHomeDir(), File.separatorChar + ".Gosu" ), "Gosu-" + System.currentTimeMillis() );
      extractGosu( url, outDir, progress );
      progress.setProgress( 3 );
      progress.setNote( "Finishing up" );
      writeRecentGosuFile( outDir.getAbsolutePath() );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }

  private static void extractGosu( URL url, File to, ProgressMonitor progress ) throws Exception
  {
    try( ZipInputStream ji = new ZipInputStream( url.openStream() ) )
    {
      String outPath = to.getAbsolutePath();
      byte[] buffer = new byte[2048];
      progress.setProgress( 2 );
      progress.setNote( "Storing Gosu runtime files" );
      for( ZipEntry entry = ji.getNextEntry(); entry != null; entry = ji.getNextEntry() )
      {
        String outpath = outPath + "/" + entry.getName();
        if( entry.isDirectory() )
        {
          //noinspection ResultOfMethodCallIgnored
          new File( outpath ).mkdirs();
        }
        else
        {
          try( FileOutputStream output = new FileOutputStream( outpath ) )
          {
            int len;
            while( (len = ji.read( buffer )) > 0 )
            {
              output.write( buffer, 0, len );
            }
          }
        }
      }
    }
  }

  private static void writeRecentGosuFile( String dirName ) throws IOException
  {
    File recent = getRecentFile();
    //noinspection ResultOfMethodCallIgnored
    recent.getParentFile().mkdirs();
    FileWriter writer = new FileWriter( recent );
    writer.write( dirName + "\n" );
    writer.close();
  }

  private static String getHomeDir()
  {
    String homeDir = System.getProperty( "user.home" );
    if( homeDir == null || !new File( homeDir ).exists() )
    {
      homeDir = System.getenv( "USERPROFILE" );
    }
    return homeDir;
  }

  private static File findRecentGosu()
  {
    File recent = getRecentFile();
    if( !recent.isFile() )
    {
      return null;
    }
    try( FileReader reader = new FileReader( recent ) )
    {
      char[] data = new char[1024];
      String path = "";
      int len;
      while( (len = reader.read( data )) > 0 )
      {
        path += new String( data, 0, len );
      }
      return new File( path.trim() );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }

  private static File getRecentFile()
  {
    return new File( getHomeDir() + File.separatorChar + ".Gosu" + File.separatorChar + "most.recent" );
  }

  private static String getRepoJarPaths( File appRepo )
  {
    StringBuilder sb = new StringBuilder();
    for( File csr : appRepo.listFiles() )
    {
      String lowerName = csr.getName().toLowerCase();
      if( lowerName.endsWith( ".jar" ) || lowerName.endsWith( ".zip" ) )
      {
        sb.append( File.pathSeparator );
        sb.append( csr.getAbsolutePath() );
      }
    }
    return sb.toString();
  }

  private static void addRepoJarPaths( File appRepo ) throws Exception
  {
    URLClassLoader loader = (URLClassLoader)Launcher.class.getClassLoader();
    Method addUrl = getAddUrlMethod();
    for( File csr : appRepo.listFiles() )
    {
      String lowerName = csr.getName().toLowerCase();
      if( lowerName.endsWith( ".jar" ) || lowerName.endsWith( ".zip" ) )
      {
        addUrl.invoke( loader, csr.toURI().toURL() );
      }
    }
  }

  private static Method getAddUrlMethod() throws NoSuchMethodException
  {
    Method addUrl = URLClassLoader.class.getDeclaredMethod( "addURL", URL.class );
    addUrl.setAccessible( true );
    return addUrl;
  }

  private static File cacheJars( String jarLoc ) throws IOException
  {
    File appRepoDir = makeAppRepoDir( jarLoc );

    File repoTextFile = new File( appRepoDir, JAR_REPO_TXT );
    if( isAlreadyCached( repoTextFile ) )
    {
      return appRepoDir;
    }

    // clean the dir first
    deleteDir( appRepoDir );

    //noinspection ResultOfMethodCallIgnored
    appRepoDir.mkdirs();

    try( InputStream in = Launcher.class.getResourceAsStream( JAR_REPO_DIR + '/' + JAR_REPO_TXT );
         OutputStream out = new FileOutputStream( repoTextFile ) )
    {
      copy( in, out );
    }

    try( BufferedReader reader = new BufferedReader( new InputStreamReader( Launcher.class.getResourceAsStream( JAR_REPO_DIR + '/' + JAR_REPO_TXT ) ) ) )
    {
      reader.readLine(); // skip UUID
      while( true )
      {
        String jar = reader.readLine();
        if( jar == null || jar.trim().isEmpty() )
        {
          break;
        }
        try( InputStream in = Launcher.class.getResourceAsStream( JAR_REPO_DIR + '/' + jar );
             OutputStream out = new FileOutputStream( new File( appRepoDir, jar ) ) )
        {
          copy( in, out );
        }
      }
    }
    return appRepoDir;
  }

  static void deleteDir( File fileOrDirectory )
  {
    if( fileOrDirectory.isDirectory() )
    {
      for( File child : fileOrDirectory.listFiles() )
      {
        deleteDir( child );
      }
    }
    //noinspection ResultOfMethodCallIgnored
    fileOrDirectory.delete();
  }

  private static boolean isAlreadyCached( File repoTextFile ) throws IOException
  {
    if( repoTextFile.exists() )
    {
      try( BufferedReader reader = new BufferedReader( new FileReader( repoTextFile ) ) )
      {
        UUID existingUid = UUID.fromString( reader.readLine() );
        try( BufferedReader readFromJar = new BufferedReader( new InputStreamReader( Launcher.class.getResourceAsStream( JAR_REPO_DIR + '/' + JAR_REPO_TXT ) ) ) )
        {
          UUID jarUid = UUID.fromString( readFromJar.readLine() );
          if( existingUid.equals( jarUid ) )
          {
            // already cached jars for this version of the app
            return true;
          }
        }
      }
    }
    return false;
  }

  private static File makeAppRepoDir( String jarLoc )
  {
    String homeDir = System.getProperty( "user.home" );
    if( homeDir == null || !new File( homeDir ).exists() )
    {
      homeDir = System.getenv( "USERPROFILE" );
    }
    File home = new File( homeDir );
    String appName = getAppName( jarLoc );
    File repoDir = new File( home, ".Gosu" + File.separatorChar + JAR_REPO_DIR + File.separatorChar + appName );
    //noinspection ResultOfMethodCallIgnored
    repoDir.mkdirs();
    return repoDir;
  }

  // Use the name of the jar minus the .jar extension: FooBar.jar => "FooBar".
  // Also, if the jar name looks like "FooBar (3).jar", the name is just "FooBar".
  // This avoids issues where from a browser multiple downloads of the same jar
  // file are distinguished in the file system by appending a number to the file
  // name.
  private static String getAppName( String jarLoc )
  {
    String appName = new File( jarLoc ).getName();
    int iDot = appName.lastIndexOf( '.' );
    if( iDot >= 0 )
    {
      appName = appName.substring( 0, iDot );
    }
    appName = maybeRemoveAppendage( appName );
    return appName;
  }

  private static String maybeRemoveAppendage( String appName )
  {
    int iSpace = appName.lastIndexOf( ' ' );
    if( iSpace >= 0 )
    {
      String duplicate = appName.substring( iSpace+1, appName.length() );
      char open = duplicate.charAt( 0 );
      char close = duplicate.charAt( duplicate.length() - 1 );
      if( open == '(' && close == ')' && duplicate.length() > 2 )
      {
        String dupNumber = appName.substring( iSpace+2, appName.length()-1 );
        try
        {
          //noinspection ResultOfMethodCallIgnored
          Integer.parseInt( dupNumber );
          appName = appName.substring( 0, iSpace );
        }
        catch( NumberFormatException e )
        {
          // don't care
        }
      }
    }
    return appName;
  }

  public static void copy( InputStream in, OutputStream out ) throws IOException
  {
    byte[] buf = new byte[1024];
    while( true )
    {
      int count = in.read( buf );
      if( count < 0 )
      {
        break;
      }
      out.write( buf, 0, count );
    }
    out.flush();
  }
}


