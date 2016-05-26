package gw.internal.gosu.parser;

public class TestTypeExtensionImpl {
  public String intToString(int i) {
    return String.valueOf(i);
  }

  public void doThrow() throws Exception {
    throw new Exception("bad");
  }
}
