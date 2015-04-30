package gw.gosudoc.doc

uses com.sun.javadoc.AnnotationDesc
uses com.sun.javadoc.ClassDoc
uses com.sun.javadoc.PackageDoc
uses com.sun.javadoc.ProgramElementDoc
uses gw.lang.reflect.IType

uses java.lang.Override


abstract class GSProgramElementDocImpl extends GSDocImpl implements ProgramElementDoc {

  var _clazz: GSClassDocImpl as ClassDoc
  var _ownersType: IType as OwnersType

  construct( name: String, rootDoc: GSRootDocImpl, ownersType: IType ){
    super( name, rootDoc )
    _ownersType = ownersType
  }

//==========function METHODS IMPLEMENTING INTERFACES==========//
// Note: returns the class if this is a constructor/field, etc.  If a class, must return null. Make sure not to create an infinite loop.
  @Override
  function containingClass() : ClassDoc{
      return _clazz
  }

@Override
    function  containingPackage() : PackageDoc{
    return containingClass().containingPackage()
    }

    @Override
    function qualifiedName() : String{
    return name()
    }

    @Override
    function modifierSpecifier() : int {
    return 0  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    function modifiers() : String {
    return"public"
    }

    @Override
    function annotations()  : AnnotationDesc[]{
      return new AnnotationDesc[0]  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    property get Public() : boolean {
    return true
    }

    @Override
    property get Protected() : boolean{
      return false  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    property get Private() : boolean {
      return false  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    property get PackagePrivate() : boolean{
    return false  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    property get Static() : boolean {
    return false  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    property get Final() : boolean {
    return false  //To change body of implemented methods use File | Settings | File Templates.
    }

// TODO cgross - enable for java 8
//    function asAnnotatedType() : AnnotatedType
//    {
//    return null  //To change body of implemented methods use File | Settings | File Templates.
//    }


    //==========PROTECTED METHODS==========//
    abstract protected function initialize()


}