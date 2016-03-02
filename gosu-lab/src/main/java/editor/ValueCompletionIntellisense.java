package editor;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 */
public class ValueCompletionIntellisense
{
  private static final ValueCompletionIntellisense INSTANCE = new ValueCompletionIntellisense();

  private static final WeakHashMap<GosuEditor, List<? extends IValueCompletionHandler>> HANDLERS_BY_EDITOR = new WeakHashMap<GosuEditor, List<? extends IValueCompletionHandler>>();

  private static final List<Class<? extends IValueCompletionHandler>> HANDLER_TYPES =
    new ArrayList<Class<? extends IValueCompletionHandler>>();

  static
  {
    HANDLER_TYPES.add( MemberAccessValueCompletion.class );
    HANDLER_TYPES.add( BeanMethodCallValueCompletion.class );
    HANDLER_TYPES.add( BooleanValueCompletion.class );
    HANDLER_TYPES.add( NumberValueCompletion.class );
    HANDLER_TYPES.add( EnumerationValueCompletion.class );
    HANDLER_TYPES.add( StringValueCompletion.class );
  }

  public static ValueCompletionIntellisense instance()
  {
    return INSTANCE;
  }

  private ValueCompletionIntellisense()
  {
  }

  public boolean complete( GosuEditor gsEditor )
  {
    List<? extends IValueCompletionHandler> handlers = HANDLERS_BY_EDITOR.get( gsEditor );
    if( handlers == null )
    {
      handlers = initHandlers( gsEditor );
    }
    for( IValueCompletionHandler handler : handlers )
    {
      if( handler.handleCompleteValue() )
      {
        return true;
      }
    }
    return false;
  }

  private List<? extends IValueCompletionHandler> initHandlers( GosuEditor gsEditor )
  {
    List<IValueCompletionHandler> handlers = new ArrayList<IValueCompletionHandler>( HANDLER_TYPES.size() );
    for( Class<? extends IValueCompletionHandler> type : HANDLER_TYPES )
    {
      try
      {
        IValueCompletionHandler handler = type.newInstance();
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
