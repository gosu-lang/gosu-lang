package gw.specContrib.classes.property_Declarations;

/**
 */
public class JavaClassWithOverloadedPropertyMethod
{
  public boolean isGood() {
    return true;
  }
  public boolean isGood( String s ) {
    return false;
  }

  public String setGood( boolean value ) {
    return "lol";
  }
}
