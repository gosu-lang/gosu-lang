package gw.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author isilvestrov
 */
public class LogMessageFormatterTest {
  @Test
  public void testArrayFormat() throws Exception {
    String result = LogMessageFormatter.arrayFormat(
            "This is {} string. To check {}.", new String[]{"test", "formatting"});
    Assert.assertEquals("This is test string. To check formatting.", result);
  }
}
