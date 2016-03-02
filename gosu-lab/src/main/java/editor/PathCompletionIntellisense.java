package editor;

import gw.lang.parser.ISymbolTable;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 */
public class PathCompletionIntellisense
{
  private static final PathCompletionIntellisense INSTANCE = new PathCompletionIntellisense();

  private static final WeakHashMap<GosuEditor, List<? extends IPathCompletionHandler>> HANDLERS_BY_EDITOR = new WeakHashMap<GosuEditor, List<? extends IPathCompletionHandler>>();

  private static final List<Class<? extends IPathCompletionHandler>> HANDLER_TYPES =
    new ArrayList<Class<? extends IPathCompletionHandler>>();

  static
  {
    HANDLER_TYPES.add( MemberPathCompletionHandler.class );
    HANDLER_TYPES.add( FeaturePathCompletionHandler.class );
    HANDLER_TYPES.add( StaticMemberPathCompletionHandler.class );
    HANDLER_TYPES.add( PackageCompletionHandler.class );
    HANDLER_TYPES.add( AnnotationCompletionHandler.class );
    HANDLER_TYPES.add( SymbolCompletionHandler.class );
    HANDLER_TYPES.add( InitializerCompletionHandler.class );
  }

  public static PathCompletionIntellisense instance()
  {
    return INSTANCE;
  }

  private PathCompletionIntellisense()
  {
  }

  public void complete( GosuEditor gsEditor, ISymbolTable transientSymTable )
  {
    List<? extends IPathCompletionHandler> handlers = HANDLERS_BY_EDITOR.get( gsEditor );
    if( handlers == null )
    {
      handlers = initHandlers( gsEditor );
    }
    for( IPathCompletionHandler handler : handlers )
    {
      if( handler.handleCompletePath( transientSymTable ) )
      {
        break;
      }
    }
  }

  private List<? extends IPathCompletionHandler> initHandlers( GosuEditor gsEditor )
  {
    List<IPathCompletionHandler> handlers = new ArrayList<IPathCompletionHandler>( HANDLER_TYPES.size() );
    for( Class<? extends IPathCompletionHandler> type : HANDLER_TYPES )
    {
      try
      {
        IPathCompletionHandler handler = type.newInstance();
        handlers.add( handler );
        handler.setGosuEditor( gsEditor );
      }
      catch( Exception e )
      {
        throw new RuntimeException( e );
      }
    }
    HANDLERS_BY_EDITOR.put( gsEditor, handlers );
    return handlers;
  }
}
