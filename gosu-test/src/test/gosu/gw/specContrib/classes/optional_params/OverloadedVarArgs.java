package gw.specContrib.classes.optional_params;

/**
 */
public class OverloadedVarArgs
{
  public static Object overloaded(Object o, Object... args) { return null; }
  public static String overloaded(String s, Object... args) { return null; }
    
  public static Object overloaded2(String s, Object... args) { return null; }
  public static String overloaded2(String s, String... args) { return null; }
}
