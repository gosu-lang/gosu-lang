package gw.specContrib.classes.property_Declarations;

import gw.test.TestClass;
import junit.framework.TestCase;

import java.util.Date;

public class OverloadedPropertyBase extends TestClass {

  public void setCurrentTime(Date d) {}
  
  public void setCurrentTime(String d) {}

}