package gw.specContrib.classes.property_Declarations.javaClassGosuSubClass;

public class Errant_JavaSuper_FieldVsProperty_1 {
    String MyProperty1;
    String MyProperty2;

    String normalProperty;
    String NotNormalProperty;

    String smallCaseProperty;
    String CapitalCaseProperty;

    //Field is in super class and only property getter in subclass
    String JustTheGetterInSub;
    //Field is in super class and only property setter in subclass
    String JustTheSetterInSub;
}
