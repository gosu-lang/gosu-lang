/*
 * Copyright 2014 Guidewire Software, Inc.
 */

import gw.lang.Gosu;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Launcher {
  public static void main( String[] args ) throws IOException {
    try {
      String jarLoc = new File( Launcher.class.getProtectionDomain().getCodeSource().getLocation().toURI() ).getAbsolutePath();
      Properties props = new Properties();
      InputStream inProps = Launcher.class.getClassLoader().getResource( "main.properties" ).openStream();
      props.load( inProps );
      String strProgram = props.getProperty( "Program" );
      String strBundled = props.getProperty( "BundledGosu" );
      if( strBundled != null && strBundled.equalsIgnoreCase( "true" ) ) {
        Gosu.main( new String[] { "-classpath", "\"" + jarLoc + "\"", strProgram} );
      }
      else {
        launchFromInstalledGosu( jarLoc, strProgram );
      }
    }
    catch( Throwable e ) {
      JOptionPane.showMessageDialog( null, e + " : " + e.getMessage() );
      System.exit( 0 );
    }
  }

  private static void launchFromInstalledGosu( String jarLoc, String strProgram ) throws IOException {
    //JOptionPane.showMessageDialog( null, strProgram );
    String[] cmd = isWindows()
                   ? new String[]{"gosu.cmd", "-classpath", "\"" + jarLoc + "\"", "-fqn", strProgram}
                   : new String[]{"gosu", "-classpath", "\"" + jarLoc + "\"", "-fqn", strProgram};
    Process p = Runtime.getRuntime().exec( cmd );
    DataInputStream errIn = new DataInputStream( p.getErrorStream() );
    try {
      String strLine;
      FileWriter writer = new FileWriter( new File( System.getProperty( "java.io.tmpdir" ), "err." + strProgram + ".txt" ) );
      while( (strLine = errIn.readLine()) != null ) {
        writer.write( strLine + "\n" );
      }
      writer.flush();
      writer.close();
    }
    catch( IOException e ) {
      JOptionPane.showMessageDialog( null, e.getMessage() );
      System.exit( 0 );
    }
  }

  private static boolean isWindows() {
    String strOSName = System.getProperty( "os.name" );
    if( strOSName != null ) {
      strOSName = strOSName.toLowerCase();

      if( strOSName.startsWith( "windows" ) ) {
        return true;
      }
    }
    return false;
  }
}
