package editor.util;

import editor.FileTree;
import editor.GosuPanel;
import editor.LabFrame;
import editor.NewIdentifierDialog;
import editor.plugin.typeloader.ITypeFactory;
import manifold.api.templ.DisableStringLiteralTemplates;
import manifold.api.type.ClassType;

import gw.util.PathUtil;
import java.io.Writer;
import java.nio.file.Path;
import javax.swing.*;
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

  private Path _created;

  private SourceFileCreator()
  {
  }

  public Path getCreated()
  {
    return _created;
  }
  public void clearCreated()
  {
    _created = null;
  }

  public Path getOrMakeUntitledProgram( Experiment experiment )
  {
    Path srcDir = PathUtil.create( experiment.getSourcePath().get( 0 ) );
    //noinspection ResultOfMethodCallIgnored
    PathUtil.mkdirs( srcDir );
    Path scratchPackage = PathUtil.create( srcDir, "scratch" );
    //noinspection ResultOfMethodCallIgnored
    PathUtil.mkdirs( scratchPackage );
    Path file = PathUtil.create( scratchPackage, "RunMe.gsp" );
    try
    {
      if( PathUtil.createNewFile( file ) )
      {
        try( Writer writer = PathUtil.createWriter( file ) )
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
      create( PathUtil.create( getParentContext(), dlg.getClassName() + classType.getExt() ), classType );
    }
  }

  public void create( ITypeFactory factory )
  {
    NewIdentifierDialog dlg = new NewIdentifierDialog( factory );
    dlg.setVisible( true );
    if( dlg.getClassName() != null )
    {
      Path file = PathUtil.create( getParentContext(), dlg.getClassName() + factory.getFileExtension() );
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
      Path dir = PathUtil.create( getParentContext(), dlg.getClassName() );
      //noinspection ResultOfMethodCallIgnored
      PathUtil.mkdirs( dir );
    }
  }

  public void createTextFile()
  {
    NewIdentifierDialog dlg = new NewIdentifierDialog( true );
    dlg.setVisible( true );
    if( dlg.getClassName() != null )
    {
      Path file = PathUtil.create( getParentContext(), dlg.getClassName() );
      //noinspection ResultOfMethodCallIgnored
      PathUtil.mkdirs( file.getParent() );
      if( PathUtil.createNewFile( file ) )
      {
        _created = file;
      }
    }
  }

  private Path getParentContext()
  {
    GosuPanel gosuPanel = LabFrame.instance().getGosuPanel();
    FileTree selection = gosuPanel.getExperimentView().getSelectedTree();
    Path parent = null;
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
      Path currentEditor = gosuPanel.getCurrentFile();
      if( currentEditor != null )
      {
        parent = currentEditor.getParent();
      }
    }
    return parent;
  }

  public void create( Path file, ITypeFactory factory, String typeName )
  {
    if( PathUtil.createNewFile( file ) )
    {
      try( Writer writer = PathUtil.createWriter( file ) )
      {
        writer.write( factory.createNewFileContents( factory.makeDefaultParams( typeName ) ).toString() );
      }
      catch( Exception e )
      {
        //noinspection ResultOfMethodCallIgnored
        PathUtil.delete( file );
        throw new RuntimeException( e );
      }
    }
    _created = file;
  }

  public void create( Path selectedFile, ClassType classType )
  {
    if( PathUtil.createNewFile( selectedFile ) )
    {
      if( !writeStub( selectedFile, classType ) )
      {
        //noinspection ResultOfMethodCallIgnored
        PathUtil.delete( selectedFile );
        return;
      }
    }
    _created = selectedFile;
  }

  private boolean writeStub( Path file, ClassType classType )
  {
    String strFile = PathUtil.getName( file ).toLowerCase();
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
    else if( strFile.endsWith( ".java" ) )
    {
      return writeJavaStub( file, classType );
    }
    else if( classType == null )
    {
      return PathUtil.mkdirs( file );
    }
    return true;
  }

  private boolean writeClassStub( Path file, ClassType classType )
  {
    String strName = TypeNameUtil.getTypeNameForFile( file );
    if( strName == null )
    {
      int iOption = displayTypeWarning( file );
      if( iOption != JOptionPane.YES_OPTION )
      {
        return false;
      }
      if( file.getParent() == null )
      {
        JOptionPane.showMessageDialog( LabFrame.instance(), "A class must have a parent directory", "Gosu Lab", JOptionPane.ERROR_MESSAGE );
        return false;
      }
      strName = PathUtil.getName( file.getParent() ) + '.' + PathUtil.getName( file ).substring( 0, PathUtil.getName( file ).lastIndexOf( '.' ) );
    }
    int iLastDot = strName.lastIndexOf( '.' );
    String strRelativeName = strName.substring( iLastDot + 1 );
    String strPackage = iLastDot > 0 ? strName.substring( 0, iLastDot ) : "";

    try
    {
      Writer writer = PathUtil.createWriter( file );
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

  private boolean writeJavaStub( Path file, ClassType classType )
  {
    String strName = TypeNameUtil.getTypeNameForFile( file );
    if( strName == null )
    {
      int iOption = displayTypeWarning( file );
      if( iOption != JOptionPane.YES_OPTION )
      {
        return false;
      }
      if( file.getParent() == null )
      {
        JOptionPane.showMessageDialog( LabFrame.instance(), "A class must have a parent directory", "Gosu Lab", JOptionPane.ERROR_MESSAGE );
        return false;
      }
      strName = PathUtil.getName( file.getParent() ) + '.' + PathUtil.getName( file ).substring( 0, PathUtil.getName( file ).lastIndexOf( '.' ) );
    }
    int iLastDot = strName.lastIndexOf( '.' );
    String strRelativeName = strName.substring( iLastDot + 1 );
    String strPackage = iLastDot > 0 ? strName.substring( 0, iLastDot ) : "";

    try
    {
      Writer writer = PathUtil.createWriter( file );
      String eol = System.getProperty( "line.separator" );
      writer.write( "package " + strPackage + ";" + eol +
                    eol +
                    "public " + classType.keyword() + ' ' + strRelativeName + " {" + eol +
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

  @DisableStringLiteralTemplates
  private boolean writeTempateStub( Path file )
  {
    String strName = TypeNameUtil.getTypeNameForFile( file );
    if( strName == null )
    {
      int iOption = displayTypeWarning( file );
      if( iOption != JOptionPane.YES_OPTION )
      {
        return false;
      }
      if( file.getParent() == null )
      {
        JOptionPane.showMessageDialog( LabFrame.instance(), "A template must have a parent directory", "Gosu Lab", JOptionPane.ERROR_MESSAGE );
        return false;
      }
      strName = PathUtil.getName( file.getParent() ) + '.' + PathUtil.getName( file ).substring( 0, PathUtil.getName( file ).lastIndexOf( '.' ) );
    }
    int iLastDot = strName.lastIndexOf( '.' );
    String strRelativeName = strName.substring( iLastDot + 1 );

    try
    {
      Writer writer = PathUtil.createWriter( file );
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

  private int displayTypeWarning( Path file )
  {
    return
      JOptionPane.showConfirmDialog( LabFrame.instance(),
         "<html>The class " + PathUtil.getName( file ) + " is not on the current classpath.  " +
         "Create the class anyway and put it's parent directory in the classpath?  " +
         "<br><br>" +
         "WARNING!!!  Ensure that the parent directory does not cover other files and directories you don't want in your class path." +
         "<br><br>" +
         "Consider creating a \"src\" directory and create package folders in there.", "Gosu Lab", JOptionPane.YES_NO_OPTION );
  }

  private boolean writeEnhancementStub( Path file )
  {
    String strName = TypeNameUtil.getTypeNameForFile( file );
    if( strName == null )
    {
      int iOption = displayTypeWarning( file );
      if( iOption != JOptionPane.YES_OPTION )
      {
        return false;
      }
      if( file.getParent() == null )
      {
        JOptionPane.showMessageDialog( LabFrame.instance(), "A class must have a parent directory", "Gosu Lab", JOptionPane.ERROR_MESSAGE );
        return false;
      }
      strName = PathUtil.getName( file.getParent() ) + '.' + PathUtil.getName( file ).substring( 0, PathUtil.getName( file ).lastIndexOf( '.' ) );
    }
    int iLastDot = strName.lastIndexOf( '.' );
    String strRelativeName = strName.substring( iLastDot + 1 );
    String strPackage = iLastDot > 0 ? strName.substring( 0, iLastDot ) : "";

    try
    {
      Writer writer = PathUtil.createWriter( file );
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
