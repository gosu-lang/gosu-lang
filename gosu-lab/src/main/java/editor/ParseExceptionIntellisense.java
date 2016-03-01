package editor;

import gw.lang.parser.exceptions.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 */
public class ParseExceptionIntellisense
{
  private static final ParseExceptionIntellisense INSTANCE = new ParseExceptionIntellisense();

  private static final WeakHashMap<GosuEditor, List<? extends IParseExceptionResolver>> HANDLERS_BY_EDITOR = new WeakHashMap<GosuEditor, List<? extends IParseExceptionResolver>>();

  private static final List<Class<? extends IParseExceptionResolver>> HANDLER_TYPES =
    new ArrayList<Class<? extends IParseExceptionResolver>>();

  static
  {
    HANDLER_TYPES.add( BooleanValueCompletion.class );
    HANDLER_TYPES.add( NumberValueCompletion.class );
    HANDLER_TYPES.add( EnumerationValueCompletion.class );
    HANDLER_TYPES.add( StringValueCompletion.class );
  }

  public static ParseExceptionIntellisense instance()
  {
    return INSTANCE;
  }

  private ParseExceptionIntellisense()
  {
  }

  public void resolve( GosuEditor gsEditor, ParseException pe )
  {
    List<? extends IParseExceptionResolver> handlers = getHandlers( gsEditor );
    for( IParseExceptionResolver handler : handlers )
    {
      if( handler.canResolve( pe ) )
      {
        handler.resolve( pe );
        break;
      }
    }
  }

  public boolean canResolve( GosuEditor gsEditor, ParseException pe )
  {
    List<? extends IParseExceptionResolver> handlers = getHandlers( gsEditor );
    for( IParseExceptionResolver handler : handlers )
    {
      if( handler.canResolve( pe ) )
      {
        return true;
      }
    }
    return false;
  }

  private List<? extends IParseExceptionResolver> getHandlers( GosuEditor gsEditor )
  {
    List<? extends IParseExceptionResolver> handlers = HANDLERS_BY_EDITOR.get( gsEditor );
    if( handlers == null )
    {
      handlers = initHandlers( gsEditor );
    }
    return handlers;
  }

  private List<? extends IParseExceptionResolver> initHandlers( GosuEditor gsEditor )
  {
    List<IParseExceptionResolver> handlers = new ArrayList<IParseExceptionResolver>( HANDLER_TYPES.size() );
    for( Class<? extends IParseExceptionResolver> type : HANDLER_TYPES )
    {
      try
      {
        IParseExceptionResolver handler = type.newInstance();
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
