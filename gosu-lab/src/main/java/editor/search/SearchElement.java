package editor.search;

import gw.lang.parser.IParsedElement;
import gw.lang.reflect.IType;

/**
 */
public class SearchElement
{
  private final Object _element;
  private final IType _enclosingType;
  private final int _offset;
  private int _length;
  private int _line;
  private int _column;

  public SearchElement( Object element, IType enclosingType, int offset )
  {
    _element = element;
    _enclosingType = enclosingType;
    _offset = offset;
    //## todo: line, etc.
  }

  public SearchElement( IParsedElement pe )
  {
    _element = pe;
    _enclosingType = pe.getGosuClass();
    _offset = pe.getLocation().getOffset();
    _length = pe.getLocation().getLength();
    _line = pe.getLineNum();
    _column = pe.getColumn();
  }

  public Object getElement()
  {
    return _element;
  }

  public IType getEnclosingType()
  {
    return _enclosingType;
  }

  public int getOffset()
  {
    return _offset;
  }

  public int getLength()
  {
    return _length;
  }

  public int getLine()
  {
    return _line;
  }

  public int getColumn()
  {
    return _column;
  }
}
