package editor.shipit;

import gw.lang.parser.IFileRepositoryBasedType;

/**
 */
public class CompiledClass
{
  private final IFileRepositoryBasedType _type;
  private final byte[] _bytes;

  public CompiledClass( IFileRepositoryBasedType type, byte[] bytes )
  {
    _type = type;
    _bytes = bytes;
  }

  public IFileRepositoryBasedType getType()
  {
    return _type;
  }

  public byte[] getBytes()
  {
    return _bytes;
  }

  public boolean isErrant()
  {
    return _bytes == null || _bytes.length == 0;
  }
}
