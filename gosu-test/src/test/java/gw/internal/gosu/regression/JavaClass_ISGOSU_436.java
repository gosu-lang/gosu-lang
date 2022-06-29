package gw.internal.gosu.regression;

public class JavaClass_ISGOSU_436
{
  public int foo(int... arg1) {return arg1[0];}
  public int foo(int[] arg1, String... arg2) {return arg1[1];}

  public <T> int checkNotNull(T reference, String errorMessageTemplate, Object p1) { return 1; }
  public <T> int checkNotNull(T reference, String errorMessageTemplate, Object... errorMessageArgs) { return 2; }
}
