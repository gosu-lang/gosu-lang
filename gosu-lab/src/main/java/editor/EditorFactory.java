package editor;

import editor.undo.AtomicUndoManager;
import java.nio.file.Path;
import gw.lang.parser.IScriptPartId;
import gw.lang.parser.ScriptabilityModifiers;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuClass;
import gw.util.PathUtil;

/**
 */
public class EditorFactory
{
  public static EditorHost createEditor( Path file, IScriptPartId partId )
  {
    FileTree fileTree = FileTreeUtil.find( file, partId == null ? null : partId.getContainingTypeName() );
    IType type = fileTree.getType();
    return createEditor( file, type );
  }

  private static EditorHost createEditor( Path file, IType type )
  {
    if( type instanceof IGosuClass )
    {
      GosuEditor editor = new GosuEditor( new GosuClassLineInfoManager(),
                            new AtomicUndoManager( 10000 ),
                            ScriptabilityModifiers.SCRIPTABLE,
                            new DefaultContextMenuHandler(),
                            file,
                            false, true );
      initEditorMode( file, editor );
      return editor;
    }
    else
    {
      return new StandardEditor( new SimpleLineInfoManager(), type );
      //return new PlainTextEditor();
    }

  }

  private static GosuEditor initEditorMode( Path file, GosuEditor editor )
  {
    if( file != null && PathUtil.getName( file ) != null )
    {
      if( PathUtil.getName( file ).endsWith( ".gsx" ) )
      {
        editor.setProgram( false );
        editor.setTemplate( false );
        editor.setClass( false );
        editor.setEnhancement( true );
      }
      else if( PathUtil.getName( file ).endsWith( ".gs" ) )
      {
        editor.setProgram( false );
        editor.setTemplate( false );
        editor.setClass( true );
        editor.setEnhancement( false );
      }
      else if( PathUtil.getName( file ).endsWith( ".gst" ) )
      {
        editor.setProgram( false );
        editor.setTemplate( true );
        editor.setClass( false );
        editor.setEnhancement( false );
      }
      else
      {
        editor.setProgram( true );
        editor.setTemplate( false );
        editor.setClass( false );
        editor.setEnhancement( false );
      }
    }
    return editor;
  }

}
