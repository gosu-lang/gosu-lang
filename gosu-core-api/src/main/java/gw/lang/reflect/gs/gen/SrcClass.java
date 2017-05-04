package gw.lang.reflect.gs.gen;

import gw.lang.parser.Keyword;
import gw.util.GosuClassUtil;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 */
public class SrcClass extends SrcStatement<SrcClass>
{
  private String _package;
  private List<String> _uses;
  private final Kind _kind;
  private SrcType _superClass;
  private final SrcClass _enclosingClass;
  private List<SrcType> _interfaces = new ArrayList<>();
  private List<SrcField> _fields = new ArrayList<>();
  private List<SrcField> _enumConsts = new ArrayList<>();
  private List<SrcConstructor> _constructors = new ArrayList<>();
  private List<SrcMethod> _methods = new ArrayList<>();
  private TreeMap<String, SrcGetProperty> _getProperties = new TreeMap<>();
  private TreeMap<String, SrcSetProperty> _setProperties = new TreeMap<>();
  private List<SrcClass> _innerClasses = new ArrayList<>();

  public SrcClass( String fqn, Kind kind )
  {
    this( fqn, null, kind );
  }
  public SrcClass( String fqn, SrcClass enclosingClass, Kind kind )
  {
    super( enclosingClass );
    fullName( fqn );
    _uses = new ArrayList<>();
    _enclosingClass = enclosingClass;
    _kind = kind;
  }

  private SrcClass fullName( String fqn )
  {
    _package = GosuClassUtil.getPackage( fqn );
    return name( GosuClassUtil.getShortClassName( fqn ) );
  }

  public SrcClass superClass( SrcType superClass )
  {
    _superClass = superClass;
    _superClass.setOwner( this );
    return this;
  }
  public SrcClass superClass( Class superClass )
  {
    SrcType t = new SrcType( superClass );
    return superClass( t );
  }
  public SrcClass superClass( String superClass )
  {
    SrcType t = new SrcType( superClass );
    return superClass( t );
  }

  public SrcClass iface( SrcType iface )
  {
    _interfaces.add( iface );
    iface.setOwner( this );
    return this;
  }
  public SrcClass iface( Class iface )
  {
    SrcType t = new SrcType( iface );
    return iface( t );
  }
  public SrcClass iface( String iface )
  {
    SrcType t = new SrcType( iface );
    return iface( t );
  }

  public SrcClass addField( SrcField field )
  {
    _fields.add( field );
    field.setOwner( this );
    return this;
  }

  public SrcClass addEnumConst( SrcField enumConst )
  {
    _enumConsts.add( enumConst );
    enumConst.setOwner( this );
    return this;
  }

  public SrcClass addConstructor( SrcConstructor ctor )
  {
    _constructors.add( ctor );
    ctor.setOwner( this );
    return this;
  }

  public SrcClass addMethod( SrcMethod method )
  {
    _methods.add( method );
    method.setOwner( this );
    return this;
  }

  public SrcClass addGetProperty( SrcGetProperty property )
  {
    _getProperties.put( property.getSimpleName(), property );
    property.setOwner( this );
    return this;
  }
  public SrcClass addSetProperty( SrcSetProperty property )
  {
    _setProperties.put( property.getSimpleName(), property );
    property.setOwner( this );
    return this;
  }

  public SrcClass addInnerClass( SrcClass innerClass )
  {
    _innerClasses.add( innerClass );
    innerClass.setOwner( this );
    return this;
  }

  public String getPackage()
  {
    return _package;
  }

  public Kind getKind()
  {
    return _kind;
  }

  public SrcType getSuperClass()
  {
    return _superClass;
  }

  public SrcClass getEnclosingClass()
  {
    return _enclosingClass;
  }

  public List<SrcType> getInterfaces()
  {
    return _interfaces;
  }

  public List<SrcField> getFields()
  {
    return _fields;
  }

  public List<SrcField> getEnumConsts()
  {
    return _enumConsts;
  }

  public List<SrcConstructor> getConstructors()
  {
    return _constructors;
  }

  public List<SrcMethod> getMethods()
  {
    return _methods;
  }

  public List<SrcClass> getInnerClasses()
  {
    return _innerClasses;
  }

  @Override
  public StringBuilder render( StringBuilder sb, int indent )
  {
    return render( sb, indent, true );
  }
  public StringBuilder render( StringBuilder sb, int indent, boolean includePackage )
  {
    if( includePackage )
    {
      renderPackageAndUses( sb );
    }

    if( _kind == SrcClass.Kind.Enum )
    {
      renderEnum( sb, indent );
    }
    else if( _kind == SrcClass.Kind.Annotation )
    {
      renderAnnotation( sb, indent );
    }
    else
    {
      renderClassOrInterface( sb, indent );
    }
    return sb;
  }

  private void renderPackageAndUses( StringBuilder sb )
  {
    sb.append( "/* Generated */\n" )
      .append( Keyword.KW_package.getName() ).append( ' ' ).append( _package ).append( "\n\n" )
      .append( Keyword.KW_uses.getName() ).append( " gw.lang.reflect.*\n" );
    for( String u: _uses )
    {
      sb.append( Keyword.KW_uses.getName() ).append( ' ' ).append( u ).append( "\n" );
    }
    sb.append( "\n" );
  }

  private void renderAnnotation( StringBuilder sb, int indent )
  {
    renderAnnotations( sb, indent, false );
    indent( sb, indent );
    renderModifiers( sb, getModifiers() & ~(Modifier.FINAL | Modifier.ABSTRACT), false, 0 );
    sb.append( "annotation " ).append( getSimpleName() )//.append( renderTypeVariables( type ) )
      .append( renderClassImplements( sb ) )
      .append( " {\n" );

    renderClassFeatures( sb, indent + INDENT );

    indent( sb, indent );
    sb.append( "}\n\n" );
  }

  private void renderEnum( StringBuilder sb, int indent )
  {
    renderAnnotations( sb, indent, false );
    indent( sb, indent );
    renderModifiers( sb, getModifiers() & ~Modifier.FINAL, false, 0 );
    sb.append( "enum " ).append( getSimpleName() )//.append( getTypeVariables( type ) )
      .append( renderClassImplements( sb ) )
      .append( " {\n" );

    renderEnumConstants( sb, indent + INDENT );
    renderClassFeatures( sb, indent + INDENT );

    indent( sb, indent );
    sb.append( "}\n\n" );
  }

  private void renderEnumConstants( StringBuilder sb, int indent )
  {
    for( int i = 0; i < _enumConsts.size(); i++ )
    {
      SrcField c = _enumConsts.get( i );
      c.renderAnnotations( sb, indent, false );
      sb.append( i > 0 ? ",\n" : "" )
        .append( indent( sb, indent ) )
        .append( c.getSimpleName() )
        .append( i == _enumConsts.size()-1 ? "\n\n" : "" );
    }
  }

  private void renderClassOrInterface( StringBuilder sb, int indent )
  {
    renderAnnotations( sb, indent, false );
    indent( sb, indent );
    renderModifiers( sb, false, 0 );
    sb.append( _kind.keyword() ).append( ' ' ).append( getSimpleName() )//.append( getTypeVariables( type ) )
      .append( genClassExtends( sb ) )
      .append( renderClassImplements( sb ) )
      .append( " {\n" );

    renderClassFeatures( sb, indent + INDENT );

    indent( sb, indent );
    sb.append( "}\n\n" );
  }
  
  private void renderClassFeatures( StringBuilder sb, int indent )
  {
    renderFields( sb, indent );
    renderConstructors( sb, indent );
    renderProperties( sb, indent );
    renderMethods( sb, indent );
    renderInnerClasses( sb, indent );
  }

  private String renderClassImplements( StringBuilder sb )
  {
    if( _interfaces.size() == 0 )
    {
      return "";
    }

    sb.append( " implements " );
    for( int i = 0; i < _interfaces.size(); i++ )
    {
      SrcType iface = _interfaces.get( i );
      sb.append( i > 0 ? ", " : "" );
      iface.render( sb, 0 );
    }
    return "";
  }

  private String genClassExtends( StringBuilder sb )
  {
    if( _superClass == null )
    {
      return "";
    }
    sb.append( " extends " );
    _superClass.render( sb, 0 );
    return "";
  }

  private void renderFields( StringBuilder sb, int indent )
  {
    sb.append( "\n" ).append( indent( sb, indent )).append( "// fields //\n" );
    for( SrcField field : _fields )
    {
      field.render( sb, indent );
    }
  }
  
  private void renderMethods( StringBuilder sb, int indent )
  {
    sb.append( "\n" ).append( indent( sb, indent )).append( "// methods //\n" );
    for( SrcMethod method : _methods )
    {
      method.render( sb, indent );
    }
  }

  private void renderProperties( StringBuilder sb, int indent )
  {
    sb.append( "\n" ).append( indent( sb, indent )).append( "// properties //\n" );
    for( Map.Entry<String, SrcGetProperty> entry: _getProperties.entrySet() )
    {
      entry.getValue().render( sb, indent );
      SrcSetProperty srcSetProperty = _setProperties.get( entry.getKey() );
      if( srcSetProperty != null )
      {
        srcSetProperty.render( sb, indent );
      }
      sb.append( "\n" );
    }
    for( Map.Entry<String, SrcSetProperty> entry: _setProperties.entrySet() )
    {
      SrcGetProperty srcGetProperty = _getProperties.get( entry.getKey() );
      if( srcGetProperty == null )
      {
        entry.getValue().render( sb, indent );
      }
    }
  }

  private void renderConstructors( StringBuilder sb, int indent )
  {
    sb.append( "\n" ).append( indent( sb, indent )).append( "// constructors //\n" );
    for( SrcConstructor ctor : _constructors )
    {
      ctor.render( sb, indent );
    }
  }

//  private String getTypeVariables( StringBuilder sb )
//  {
//    if( _typeVars.isEmpty() )
//    {
//      return "";
//    }
//
//    sb.append( '<' );
//    for( int i = 0; i < gtvs.size(); i++ )
//    {
//      SrcTypeVar gtv = gtvs[i];
//      sb.append( i > 0 ? ", " : "" ).append( gtv.getSimpleName() );
//      String boundingType = gtv.getBoundingType();
//      if( boundingType != null )
//      {
//        sb.append( " extends ").append( boundingType );
//      }
//    }
//    sb.append( "> " );
//    return sb.toString();
//  }
  
  private void renderInnerClasses( StringBuilder sb, int indent )
  {
    sb.append( "\n" ).append( indent( sb, indent )).append( "// inner classes //\n" );
    for( SrcClass innerClass : _innerClasses )
    {
      innerClass.render( sb, indent, false );
    }
  }

  public SrcClass uses( Class<?>... classes )
  {
    for( Class c: classes )
    {
      _uses.add( c.getName() );
    }
    return this;
  }

  public enum Kind
  {
    Class( Keyword.KW_class ),
    Interface( Keyword.KW_interface ),
    Structure( Keyword.KW_structure ),
    Annotation( Keyword.KW_annotation ),
    Enum( Keyword.KW_enum );

    private String _keyword;

    Kind( Keyword keyword )
    {
      _keyword = keyword.getName();
    }

    public String keyword()
    {
      return _keyword;
    }
  }
}
