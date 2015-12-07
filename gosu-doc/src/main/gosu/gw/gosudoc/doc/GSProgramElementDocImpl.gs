package gw.gosudoc.doc

uses com.sun.javadoc.AnnotationDesc
uses com.sun.javadoc.ClassDoc
uses com.sun.javadoc.PackageDoc
uses com.sun.javadoc.ProgramElementDoc
uses gw.lang.reflect.IType

uses java.lang.reflect.Modifier

abstract class GSProgramElementDocImpl extends GSDocImpl implements ProgramElementDoc{

  var _clazz: GSClassDocImpl as ClassDoc
  var _ownersType: IType as OwnersType

  construct( name: String, rootDoc: GSRootDocImpl, ownersType: IType ){
    super( name, rootDoc )
    _ownersType = ownersType
  }

  //==========function METHODS IMPLEMENTING INTERFACES==========//
// Note: returns the class if this is a constructor/field, etc.  If a class, must return null. Make sure not to create an infinite loop.
  override function containingClass(): ClassDoc{
    return _clazz
  }

  override function containingPackage(): PackageDoc{
    return containingClass().containingPackage()
  }

  override function qualifiedName(): String{
    return name()
  }

  override function modifierSpecifier(): int {
    var modifiers = 0
    if(Public) {
      modifiers |= Modifier.PUBLIC
    } else if(Protected) {
      modifiers |= Modifier.PROTECTED
    }
    if(Static) {
      modifiers |= Modifier.STATIC
    }
    return modifiers
  }

  override function modifiers() : String {
    var modifiers = ""
    if(Public) {
      modifiers+= "public "
    } else if(Protected) {
      modifiers+= "protected "
    }
    if(Static) {
      modifiers+= "static "
    }
    return modifiers
  }

// TODO cgross - enable for java 8
//    function asAnnotatedType() : AnnotatedType
//    {
//    return null  //To change body of implemented methods use File | Settings | File Templates.
//    }


  //==========PROTECTED METHODS==========//
  abstract protected function initialize()

}