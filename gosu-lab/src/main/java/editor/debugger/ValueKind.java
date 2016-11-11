package editor.debugger;

/**
 */
public enum ValueKind
{
  Null(false),
  Primitive(false),
  Object(false),
  Array(false),
  Collection(true),
  Map(true);

  private boolean _special;

  ValueKind( boolean special )
  {
    _special = special;
  }

  public boolean isSpecial()
  {
    return _special;
  }

  public void setSpecial( boolean special )
  {
    _special = special;
  }
}
