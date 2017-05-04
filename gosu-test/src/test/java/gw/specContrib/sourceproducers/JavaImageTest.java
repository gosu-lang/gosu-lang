package gw.specContrib.sourceproducers;

import junit.framework.TestCase;

public class JavaImageTest extends TestCase
{
  public void testMe() {
    benis_png benis = benis_png.get();
    assertEquals( benis.getIconWidth(), 32 );
  }
}