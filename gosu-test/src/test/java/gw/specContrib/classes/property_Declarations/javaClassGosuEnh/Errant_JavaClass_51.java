package gw.specContrib.classes.property_Declarations.javaClassGosuEnh;

public class Errant_JavaClass_51 {
    //Case 5 : Conflict check with disagreement on type

    void setMyProperty1(Integer a) {}

    //But in case of getters, it will show the conflict as there is no argument
    Integer getMyProperty3() {return null;}

}
