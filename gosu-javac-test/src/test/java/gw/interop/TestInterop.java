package gw.interop;

import org.junit.Test;

public class TestInterop
{
  @Test
  public void testInterop()
  {
    InteropTest it = new InteropTest();
    it.testConstruct();
    it.testMethodCall();
  }
}
