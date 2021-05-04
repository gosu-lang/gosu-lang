package gw.gosudoc.doc

uses  gw.gosudoc.com.sun.javadoc.AnnotationDesc
uses  gw.gosudoc.com.sun.javadoc.AnnotationTypeDoc
uses  gw.gosudoc.com.sun.javadoc.ClassDoc
uses  gw.gosudoc.com.sun.javadoc.PackageDoc
uses gw.lang.reflect.IType

uses java.lang.IllegalArgumentException
uses java.lang.NullPointerException
uses java.util.Map

class GSPackageDocImpl extends GSDocImpl implements PackageDoc{

  var _includedClasses: List<GSClassDocImpl> = {}
  var _includedClassesByName: Map<String, GSClassDocImpl> = {}
  var _classesByShortName: Map<String, GSClassDocImpl> = {}

  construct( packageName: String, rootDoc: GSRootDocImpl ){
    super( packageName, rootDoc )
    if( packageName == null ){
      throw new NullPointerException( "Package Name cannot be null" )
    }
  }

  override function allClasses( filter: boolean ): ClassDoc[]{
    return allClasses()
  }

  override function allClasses(): ClassDoc[]{
    return _includedClasses.toArray( new ClassDoc[_includedClasses.size()] )
  }

  override function ordinaryClasses(): ClassDoc[]{
    return allClasses()
  }

  override function exceptions(): ClassDoc[]{
    return new ClassDoc[0]
  }

  override function errors(): ClassDoc[]{
    return new ClassDoc[0]  //To change body of implemented methods use File | Settings | File Templates.
  }

  override function enums(): ClassDoc[]{
    return new ClassDoc[0]  //To change body of implemented methods use File | Settings | File Templates.
  }

  override function interfaces(): ClassDoc[]{
    return new ClassDoc[0]  //To change body of implemented methods use File | Settings | File Templates.
  }

  override function annotationTypes(): AnnotationTypeDoc[]{
    return new AnnotationTypeDoc[0]  //To change body of implemented methods use File | Settings | File Templates.
  }

  override function annotations(): AnnotationDesc[]{
    return new AnnotationDesc[0]  //To change body of implemented methods use File | Settings | File Templates.
  }

  /**
   * @className The fully qualified name name of the class
   */
  override function findClass( className: String ): GSClassDocImpl{
    var obligatoryPrefix = name() + "."
    if( !className.startsWith( obligatoryPrefix ) ){
      return null
    }
    // remove the package name from
    var shortName = className.substring( obligatoryPrefix.length(), className.length() )
    return _classesByShortName.get( shortName )
  }

  override property get Included(): boolean{
    return _includedClasses.size() > 0
  }

  //==========Function METHODS==========//
  function addClass( iType: IType ): GSClassDocImpl{
    var genType = iType.getGenericType()
    if( genType != null ){
      iType = genType
    }
    var clazz = _classesByShortName.get( iType.getRelativeName() )
    if( clazz == null ){
      clazz = new GSClassDocImpl( iType, getRootDoc() )
      _classesByShortName.put( iType.getRelativeName(), clazz )
      var supertype = iType.Supertype
      // special case:  handle enums.
      if( iType.isEnum() && supertype != null && !supertype.isEnum() ){
        supertype = null
      }
      var supertypeClass: GSClassDocImpl
      try{
        if( supertype != null ){
          supertypeClass = getRootDoc().getOrCreateClass( supertype )
        }
      } catch( e ){
        getRootDoc().printWarning( "Could not add superclass to class " + iType.getName() + ": " + e.getMessage() )
      }

      if( clazz.Included ){
        _includedClasses.add( clazz )
        var dupe = _includedClassesByName.put( clazz.qualifiedName(), clazz )
        if( dupe != null ){
          throw new IllegalArgumentException( "Duplicate classes " + clazz.qualifiedName() )
        }
      }

      // if the super type is null and this is a plain class, we need to set the
      // super type to Object for the doclet generator
      if( !iType.isEnum() && !iType.isInterface()  && supertypeClass == null ) {
        supertypeClass = new GSClassDocImpl( Object, getRootDoc() )
      }

      clazz.setSuperClass( supertypeClass )
      clazz.initialize()
    }
    return clazz
  }

}