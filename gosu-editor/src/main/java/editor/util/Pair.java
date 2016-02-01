package editor.util;

public class Pair<A, B>
{
  A _a;
  B _b;


  public Pair( A a, B b )
  {
    _a = a;
    _b = b;
  }

  public A getFirst()
  {
    return _a;
  }

  public B getSecond()
  {
    return _b;
  }
}
