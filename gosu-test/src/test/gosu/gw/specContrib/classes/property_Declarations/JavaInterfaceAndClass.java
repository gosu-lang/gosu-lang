package gw.specContrib.classes.property_Declarations;

interface JavaIntf {
  public String getPod();

  public boolean isEditable();
}

class JavaIntfImpl implements JavaIntf {
  @Override
  public String getPod() {
    return null;
  }

  @Override
  public boolean isEditable() {
    return false;
  }
}
