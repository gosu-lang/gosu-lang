package editor;

import gw.lang.parser.exceptions.ParseException;

/**
 */
public interface IParseExceptionResolver
{
  public void setGosuEditor( GosuEditor gsEditor );

  public GosuEditor getGosuEditor();

  public boolean canResolve( ParseException e );

  public void resolve( ParseException e );
}
