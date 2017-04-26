package gw.specContrib.sourceproducers;

import junit.framework.TestCase;
import gw.lang.SystemProperties;

public class JavaPropertiesTest extends TestCase
{
  public void testMe() {
    assertEquals( "My value", MyProperties.MyProperty.toString() );
    assertEquals( "1st", MyProperties.MyProperty.First );
    assertEquals( "2nd", MyProperties.MyProperty.Second );
    assertNotNull( MyProperties.MyProperty.Tens );
    assertEquals( "10", MyProperties.MyProperty.Tens.Ten );
    assertEquals( "11", MyProperties.MyProperty.Tens.Eleven );
    assertEquals( "other property", MyProperties.Other );
    assertNotNull( SystemProperties.user );
    assertNotNull( SystemProperties.user.dir );
  }
}