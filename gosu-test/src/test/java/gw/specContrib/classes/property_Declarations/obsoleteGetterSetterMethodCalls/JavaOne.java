package gw.specContrib.classes.property_Declarations.obsoleteGetterSetterMethodCalls;

public class JavaOne {
    public boolean isEmployee() { return false;}  //Case#1
    public void setEmployee(boolean b ) {}

    public boolean isemployee2() { return false;}  //Case#2
    public boolean getemployee2() { return false;}  //Case#2
    public void setemployee2(boolean b) {}

    public boolean is_Employee3( ) { return false;}  //Case#3
    public void set_Employee3(boolean b) {}

    public boolean get_Employee33( ) { return false;}  //Case#3
    public void set_Employee33(boolean b) {}

    public boolean is_employee4( ) { return false;}  //Case#4
    public void set_employee4(boolean b) {}
}
