package gw.specContrib.classes.java_Package_Visibility

class GosuTestClass {
  function testFun() {
    var gc = new GosuSubClass()
    //IDE-1237 - There should not be any error for non-private members.

    //Fields
    print(gc.packageInt)
    print(gc.protectedInt)
    print(gc.publicInt)
    print(gc.privateInt)      //## issuekeys: 'PRIVATEINT' HAS PRIVATE ACCESS IN 'GW.SPECCONTRIB.CLASSES.JAVA_PACKAGE_VISIBILITY.JAVACLASS'

    //Methods
    gc.packageFun()
    gc.protectedFun()
    gc.publicFun()
    gc.privateFun()      //## issuekeys: 'PRIVATEFUN()' HAS PRIVATE ACCESS IN 'GW.SPECCONTRIB.CLASSES.JAVA_PACKAGE_VISIBILITY.JAVACLASS'
  }
}