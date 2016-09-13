package editor.shipit;

import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.gs.IGosuClass;

/**
 */
public class CompiledClass
{
  private final IGosuClass _gsClass;
  private final byte[] _bytes;
  private final Exception _exception;

  public CompiledClass( IGosuClass gsClass, byte[] bytes, Exception parseException )
  {
    _gsClass = gsClass;
    _bytes = bytes;
    _exception = parseException;
  }

  public IGosuClass getGosuClass()
  {
    return _gsClass;
  }

  public byte[] getBytes()
  {
    return _bytes;
  }

  public Exception getException()
  {
    return _exception;
  }
}
