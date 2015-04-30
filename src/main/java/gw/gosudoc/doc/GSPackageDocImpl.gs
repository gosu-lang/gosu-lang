package gw.gosudoc.doc

uses com.sun.javadoc.AnnotationDesc
uses com.sun.javadoc.AnnotationTypeDoc
uses com.sun.javadoc.ClassDoc
uses com.sun.javadoc.PackageDoc
uses gw.lang.reflect.IType

uses java.lang.IllegalArgumentException
uses java.lang.NullPointerException
uses java.lang.Override
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

  @Override
  function allClasses( filter: boolean ): ClassDoc[]{
    return allClasses()
  }

  @Override
  function allClasses(): ClassDoc[]{
    return _includedClasses.toArray( new ClassDoc[_includedClasses.size()] )
  }

  @Override
  function ordinaryClasses(): ClassDoc[]{
    return allClasses()
  }

  @Override
  function exceptions(): ClassDoc[]{
    return new ClassDoc[0]
  }

  @Override
  function errors(): ClassDoc[]{
    return new ClassDoc[0]  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  function enums(): ClassDoc[]{
    return new ClassDoc[0]  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  function interfaces(): ClassDoc[]{
    return new ClassDoc[0]  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  function annotationTypes(): AnnotationTypeDoc[]{
    return new AnnotationTypeDoc[0]  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  function annotations(): AnnotationDesc[]{
    return new AnnotationDesc[0]  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  /**
   * @className The fully qualified name name of the class
   */
  function findClass( className: String ): GSClassDocImpl{
    var obligatoryPrefix = name() + "."
    if( !className.startsWith( obligatoryPrefix ) ){
      return null
    }
    // remove the package name from
    var shortName = className.substring( obligatoryPrefix.length(), className.length() )
    return _classesByShortName.get( shortName )
  }

  @Override
  property get Included(): boolean{
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

      if( clazz.isIncluded() ){
        _includedClasses.add( clazz )
        var dupe = _includedClassesByName.put( clazz.qualifiedName(), clazz )
        if( dupe != null ){
          throw new IllegalArgumentException( "Duplicate classes " + clazz.qualifiedName() )
        }
      }
      clazz.setSuperClass( supertypeClass )
      clazz.initialize()
    }
    return clazz
  }

}