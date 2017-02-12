package gw.lang.reflect.java;

public class Parameter
{
  private String _name;
  private int _access;

  public Parameter( String name, int access )
  {
    _name = name;
    _access = access;
  }

  public String getName()
  {
    return _name;
  }

  public int getAccess()
  {
    return _access;
  }
}