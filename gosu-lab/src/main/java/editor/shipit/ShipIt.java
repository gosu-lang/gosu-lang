package editor.shipit;

import editor.LabFrame;
import editor.util.Experiment;
import java.io.File;
import java.nio.file.Path;
import gw.util.PathUtil;
import gw.lang.Gosu;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

/**
 */
public class ShipIt
{
  private static ShipIt INSTANCE;

  private JarOutputStream _jo;
  private Set<String> _entries;
  private List<String> _jars;


  public static ShipIt instance()
  {
    return INSTANCE == null ? INSTANCE = new ShipIt() : INSTANCE;
  }

  public boolean shipIt( Experiment experiment )
  {
    ShipItDialog dlg = new ShipItDialog( experiment );
    dlg.setVisible( true );
    String programName = dlg.getProgramName();
    if( programName == null )
    {
      return false;
    }
    String strProgramName = dlg.getProgramName();

    List<String> progClasspath = new ArrayList<>();
    Path outFile = PathUtil.create( experiment.getExperimentDir(), experiment.getName() + ".jar" );
    if( PathUtil.exists( outFile ) )
    {
      PathUtil.delete( outFile );
    }

    _entries = new HashSet<>();
    _jars = new ArrayList<>();
    try
    {
      BufferedOutputStream bo = new BufferedOutputStream( PathUtil.createOutputStream( outFile ) );
      _jo = new JarOutputStream( bo );

      // Add source paths
      AddExperimentFilesAndDependencies( experiment, progClasspath );

      // Create the Main.class file
      JarEntry je = new JarEntry( "Launcher.class" );
      _jo.putNextEntry( je );
      InputStream inMain = getClass().getResource( "/Launcher.class" ).openStream();
      writeBytes( inMain );
      inMain.close();

      // Bundle Gosu
      boolean bBundleGosu = dlg.isBundleGosu();
      if( bBundleGosu )
      {
        bundleGosu( progClasspath );
      }

      // Precompile classes
      boolean bCompile = dlg.isPrecompiled();
      if( bCompile )
      {
        if( !ExperimentBuild.instance().rebuild( this::addPrecompiledClass ) )
        {
          _jo.close();
          PathUtil.delete( outFile );
          return false;
        }
      }

      // Create the jar-repo.txt file
      createJarRepoFile();

      // Create the main.properties file
      createMainPropertiesFile( strProgramName, bBundleGosu );

      // Create the MANIFEST
      makeManifest();

      _jo.close();

      LabFrame.openFileOrDir( outFile );

      return true;
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }

  private boolean addPrecompiledClass( CompiledClass cs )
  {
    // Create the Main.class file
    String javaName = cs.getType().getJavaName();
    javaName = javaName.replace( '.', '/' ) + ".class";
    JarEntry je = new JarEntry( javaName );
    try
    {
      byte[] bytes = cs.getBytes();
      if( bytes == null || bytes.length == 0 )
      {
        return false;
      }
      else
      {
        _jo.putNextEntry( je );
        _jo.write( bytes );
      }
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
    return true;
  }

  private void makeManifest() throws IOException
  {
    JarEntry je = new JarEntry( "META-INF/MANIFEST.MF" );
    _jo.putNextEntry( je );
    Writer writer = new OutputStreamWriter( _jo );
    writer.write( "Manifest-Version: 1.0\n" +
                  "Main-Class: Launcher\n" );
    writer.flush();
  }

  private void bundleGosu( List<String> progClasspath ) throws IOException
  {
    String[] classpath = System.getProperty( "java.class.path" ).split( File.pathSeparator );
    for( String path : classpath )
    {
      if( path.contains( "gw-asm-all" ) ||
          path.contains( "gosu-core" ) ||
          path.contains( "tools.jar" ) )
      {
        addClasspathEntry( progClasspath, PathUtil.create( path ) );
      }
    }
  }

  private void AddExperimentFilesAndDependencies( Experiment experiment, List<String> progClasspath ) throws IOException
  {
    String javaHomePath = System.getProperty( "java.home" );

    List<String> srcPaths = experiment.getSourcePath();
    for( String path : srcPaths )
    {
      if( path.startsWith( javaHomePath ) )
      {
        // don't pack jre jars
        continue;
      }

      addClasspathEntry( progClasspath, PathUtil.create( path ) );
    }
  }

  private void createMainPropertiesFile( String strProgramName, boolean bBundleGosu ) throws IOException
  {
    JarEntry je = new JarEntry( "main.properties" );
    _jo.putNextEntry( je );
    Writer writer = new OutputStreamWriter( _jo );
    writer.write( "Program=" + strProgramName + "\n" +
                  (bBundleGosu
                   ? "BundledGosu=true\n"
                   : "") +
                  "Classpath=\n" );
    writer.flush();
  }

  private void addClasspathEntry( List<String> progClasspath, Path csr ) throws IOException
  {
    if( PathUtil.isDirectory( csr ) )
    {
      for( Path fileOrDir: PathUtil.listFiles( PathUtil.getAbsolutePath( csr ) ) )
      {
        addEntry( fileOrDir, "" );
        progClasspath.add( PathUtil.getAbsolutePathName( fileOrDir ) );
      }
    }
    else
    {
      String lowercaseName = PathUtil.getName( csr ).toLowerCase();
      if( lowercaseName.endsWith( ".jar" ) || lowercaseName.endsWith( ".zip" ) )
      {
        addJarEntry( csr );
        //addZipEntry( new ZipFile( csr ) );
      }
    }
  }

  private void addJarEntry( Path jarFile ) throws IOException
  {
    addEntry( jarFile, Gosu.JAR_REPO_DIR );
    _jars.add( PathUtil.getName( jarFile ) );
  }

  private void createJarRepoFile() throws IOException
  {
    JarEntry je;
    je = new JarEntry( Gosu.JAR_REPO_DIR + '/' + Gosu.JAR_REPO_TXT );
    _jo.putNextEntry( je );
    Writer writer = new OutputStreamWriter( _jo );
    writer.write( UUID.randomUUID().toString() + "\n" );
    for( String jar: _jars )
    {
      writer.write( jar + "\n" );
    }
    writer.flush();
  }

  private void addEntry( Path file, String strPath )
  {
    if( PathUtil.isDirectory( file ) )
    {
      String strDir = (strPath.length() > 0 ? strPath + '/' : "") + PathUtil.getName( file );
      for( Path csr: PathUtil.listFiles( file ) )
      {
        addEntry( csr, strDir );
      }
    }
    else if( PathUtil.isFile( file ) )
    {
      if( PathUtil.getName( file ).toLowerCase().equals( "manifest.mf" ) )
      {
        //## todo: maybe merge all manifests into one?  but really the corresponding path should be in a jar, not unjarred files
        return;
      }

      String strFile = (strPath.length() > 0 ? strPath + '/' : "") + PathUtil.getName( file );
      if( _entries.contains( strFile ) )
      {
        return;
      }
      _entries.add( strFile );
      JarEntry je = new JarEntry( strFile );
      try
      {
        _jo.putNextEntry( je );
        InputStream in = PathUtil.createInputStream( file );
        writeBytes( in );
        in.close();
      }
      catch( IOException e )
      {
        throw new RuntimeException( e );
      }
    }
    else
    {
      throw new RuntimeException( file + " is not a file" );
    }
  }

  private void writeBytes( InputStream in ) throws IOException
  {
    byte[] buf = new byte[1024];
    while( true )
    {
      int iCount = in.read( buf, 0, buf.length );
      if( iCount <= 0 )
      {
        break;
      }
      _jo.write( buf, 0, iCount );
    }
  }

//  private void addZipEntry( ZipFile file ) throws IOException
//  {
//    Enumeration<? extends ZipEntry> en = file.entries();
//    while( en.hasMoreElements() )
//    {
//      ZipEntry entry = en.nextElement();
//      if( !entry.isDirectory() && !entry.getName().toLowerCase().endsWith( "manifest.mf" ) )
//      {
//        String strFile = entry.getName();
//        if( _entries.contains( strFile ) )
//        {
//          return;
//        }
//        _entries.add( strFile );
//        JarEntry je = new JarEntry( strFile );
//        _jo.putNextEntry( je );
//        InputStream in = file.getInputStream( entry );
//        byte[] buf = new byte[1024];
//        while( true )
//        {
//          int iCount = in.read( buf, 0, buf.length );
//          if( iCount <= 0 )
//          {
//            break;
//          }
//          _jo.write( buf, 0, iCount );
//        }
//        in.close();
//      }
//    }
//  }
}
