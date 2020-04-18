package gw.abc;

public abstract class JavaBase
{
  private final String _name;

  protected JavaBase(String name) {
    _name = name;
  }

  public String getName() {
    return _name;
  }

  protected abstract String baseMethod(int num);
}
