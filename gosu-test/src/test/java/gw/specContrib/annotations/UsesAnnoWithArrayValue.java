package gw.specContrib.annotations;

public class UsesAnnoWithArrayValue {
  @AnnoWithArrayValue(value = {1, 2, 3})
  public String doSomething(String arg) {
    return "";
  }
}
