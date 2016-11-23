package editor.util;

import editor.FileTree;
import editor.GosuPanel;
import editor.LabFrame;
import editor.NewIdentifierDialog;
import editor.plugin.typeloader.ITypeFactory;
import gw.lang.reflect.gs.ClassType;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 */
public class SourceFileCreator
{
  private static final SourceFileCreator INSTANCE = new SourceFileCreator();

  public static SourceFileCreator instance()
  {
    return INSTANCE;
  }

  private File _created;

  private SourceFileCreator()
  {
  }

  public File getCreated()
  {
    return _created;
  }
  public void clearCreated()
  {
    _created = null;
  }

  public File getOrMakeUntitledProgram( Experiment experiment )
  {
    File srcDir = new File( experiment.getSourcePath().get( 0 ) );
    //noinspection ResultOfMethodCallIgnored
    srcDir.mkdirs();
    File scratchPackage = new File( srcDir, "scratch" );
    //noinspection ResultOfMethodCallIgnored
    scratchPackage.mkdirs();
    File file = new File( scratchPackage, "RunMe.gsp" );
    try
    {
      if( file.createNewFile() )
      {
        try( FileWriter writer = new FileWriter( file ) )
        {
          writer.write( "//\n// Run this from the Run menu or press F5\n//\nprint(\"Hello, World!\")\n" );
        }
        _created = file;
      }
      return file;
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
  }

  public void create( ClassType classType )
  {
    NewIdentifierDialog dlg = new NewIdentifierDialog( classType );
    dlg.setVisible( true );
    if( dlg.getClassName() != null )
    {
      create( new File( getParentContext(), dlg.getClassName() + classType.getExt() ), classType );
    }
  }

  public void create( ITypeFactory factory )
  {
    NewIdentifierDialog dlg = new NewIdentifierDialog( factory );
    dlg.setVisible( true );
    if( dlg.getClassName() != null )
    {
      File file = new File( getParentContext(), dlg.getClassName() + factory.getFileExtension() );
      String fqn = TypeNameUtil.getTypeNameForFile( file );
      create( file, factory, fqn );
    }
  }

  public void createNamespace()
  {
    NewIdentifierDialog dlg = new NewIdentifierDialog();
    dlg.setVisible( true );
    if( dlg.getClassName() != null )
    {
      File dir = new File( getParentContext(), dlg.getClassName() );
      //noinspection ResultOfMethodCallIgnored
      dir.mkdirs();
    }
  }

  public void createTextFile()
  {
    NewIdentifierDialog dlg = new NewIdentifierDialog( true );
    dlg.setVisible( true );
    if( dlg.getClassName() != null )
    {
      File file = new File( getParentContext(), dlg.getClassName() );
      //noinspection ResultOfMethodCallIgnored
      file.getParentFile().mkdirs();
      try
      {
        if( file.createNewFile() )
        {
          _created = file;
        }
      }
      catch( IOException e )
      {
        throw new RuntimeException( e );
      }
    }
  }

  private File getParentContext()
  {
    GosuPanel gosuPanel = LabFrame.instance().getGosuPanel();
    FileTree selection = gosuPanel.getExperimentView().getSelectedTree();
    File parent = null;
    if( selection != null && selection.getParent() != null )
    {
      if( selection.isFile() )
      {
        selection = selection.getParent();
      }
      parent = selection.getFileOrDir();
    }
    else
    {
      File currentEditor = gosuPanel.getCurrentFile();
      if( currentEditor != null )
      {
        parent = currentEditor.getParentFile();
      }
    }
    return parent;
  }

  public void create( File file, ITypeFactory factory, String typeName )
  {
    try
    {
      if( file.createNewFile() )
      {
        try( FileWriter writer = new FileWriter( file ) )
        {
          writer.write( factory.createNewFileContents( factory.makeDefaultParams( typeName ) ).toString() );
        }
        catch( Exception e )
        {
          //noinspection ResultOfMethodCallIgnored
          file.delete();
          throw new RuntimeException( e );
        }
      }
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
    _created = file;
  }

  public void create( File selectedFile, ClassType classType )
  {
    try
    {
      if( selectedFile.createNewFile() )
      {
        if( !writeStub( selectedFile, classType ) )
        {
          //noinspection ResultOfMethodCallIgnored
          selectedFile.delete();
          return;
        }
      }
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
    _created = selectedFile;
  }

  private boolean writeStub( File file, ClassType classType )
  {
    String strFile = file.getName().toLowerCase();
    if( strFile.endsWith( ".gs" ) )
    {
      return writeClassStub( file, classType );
    }
    if( strFile.endsWith( ".gsx" ) )
    {
      return writeEnhancementStub( file );
    }
    else if( strFile.endsWith( ".gst" ) )
    {
      return writeTempateStub( file );
    }
    else if( classType == null )
    {
      return file.mkdirs();
    }
    return true;
  }

  private boolean writeClassStub( File file, ClassType classType )
  {
    String strName = TypeNameUtil.getTypeNameForFile( file );
    if( strName == null )
    {
      int iOption = displayTypeWarning( file );
      if( iOption != JOptionPane.YES_OPTION )
      {
        return false;
      }
      if( file.getParentFile() == null )
      {
        JOptionPane.showMessageDialog( LabFrame.instance(), "A class must have a parent directory", "Gosu Lab", JOptionPane.ERROR_MESSAGE );
        return false;
      }
      strName = file.getParentFile().getName() + '.' + file.getName().substring( 0, file.getName().lastIndexOf( '.' ) );
    }
    int iLastDot = strName.lastIndexOf( '.' );
    String strRelativeName = strName.substring( iLastDot + 1 );
    String strPackage = iLastDot > 0 ? strName.substring( 0, iLastDot ) : "";

    try
    {
      FileWriter writer = new FileWriter( file );
      String eol = System.getProperty( "line.separator" );
      writer.write( "package " + strPackage + eol +
                    eol +
                    classType.keyword() + ' ' + strRelativeName + " {" + eol +
                    eol +
                    "}" );
      writer.flush();
      writer.close();
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
    return true;
  }

  private boolean writeTempateStub( File file )
  {
    String strName = TypeNameUtil.getTypeNameForFile( file );
    if( strName == null )
    {
      int iOption = displayTypeWarning( file );
      if( iOption != JOptionPane.YES_OPTION )
      {
        return false;
      }
      if( file.getParentFile() == null )
      {
        JOptionPane.showMessageDialog( LabFrame.instance(), "A template must have a parent directory", "Gosu Lab", JOptionPane.ERROR_MESSAGE );
        return false;
      }
      strName = file.getParentFile().getName() + '.' + file.getName().substring( 0, file.getName().lastIndexOf( '.' ) );
    }
    int iLastDot = strName.lastIndexOf( '.' );
    String strRelativeName = strName.substring( iLastDot + 1 );

    try
    {
      FileWriter writer = new FileWriter( file );
      String eol = System.getProperty( "line.separator" );
      writer.write( "<%@ params( myParam: String ) %>" + eol +
                    eol +
                    "The content of my param is: ${myParam}" + eol +
                    eol +
                    "Note you can render this template from a class or program" + eol +
                    "simply by calling one of its render methods:" + eol +
                    eol +
                    "  " + strRelativeName + ".renderToString( \"wow\" )" );
      writer.flush();
      writer.close();
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
    return true;
  }

  private int displayTypeWarning( File file )
  {
    return
      JOptionPane.showConfirmDialog( LabFrame.instance(),
         "<html>The class " + file.getName() + " is not on the current classpath.  " +
         "Create the class anyway and put it's parent directory in the classpath?  " +
         "<br><br>" +
         "WARNING!!!  Ensure that the parent directory does not cover other files and directories you don't want in your class path." +
         "<br><br>" +
         "Consider creating a \"src\" directory and create package folders in there.", "Gosu Lab", JOptionPane.YES_NO_OPTION );
  }

  private boolean writeEnhancementStub( File file )
  {
    String strName = TypeNameUtil.getTypeNameForFile( file );
    if( strName == null )
    {
      int iOption = displayTypeWarning( file );
      if( iOption != JOptionPane.YES_OPTION )
      {
        return false;
      }
      if( file.getParentFile() == null )
      {
        JOptionPane.showMessageDialog( LabFrame.instance(), "A class must have a parent directory", "Gosu Lab", JOptionPane.ERROR_MESSAGE );
        return false;
      }
      strName = file.getParentFile().getName() + '.' + file.getName().substring( 0, file.getName().lastIndexOf( '.' ) );
    }
    int iLastDot = strName.lastIndexOf( '.' );
    String strRelativeName = strName.substring( iLastDot + 1 );
    String strPackage = iLastDot > 0 ? strName.substring( 0, iLastDot ) : "";

    try
    {
      FileWriter writer = new FileWriter( file );
      String eol = System.getProperty( "line.separator" );
      writer.write( "package " + strPackage + eol +
                    eol +
                    "enhancement " + strRelativeName + " : Object //## todo: change me " + eol +
                    "{" + eol +
                    eol +
                    "}" );
      writer.flush();
      writer.close();
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
    return true;
  }
}
