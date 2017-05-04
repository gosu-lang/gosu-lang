package gw.lang.reflect.json;

import gw.lang.reflect.Modifier;
import gw.lang.reflect.gs.gen.SrcClass;
import gw.lang.reflect.gs.gen.SrcConstructor;
import gw.lang.reflect.gs.gen.SrcField;
import gw.lang.reflect.gs.gen.SrcGetProperty;
import gw.lang.reflect.gs.gen.SrcMethod;
import gw.lang.reflect.gs.gen.SrcRawStatement;
import gw.lang.reflect.gs.gen.SrcSetProperty;
import gw.lang.reflect.gs.gen.SrcStatementBlock;
import java.util.List;
import java.util.Map;
import javax.script.Bindings;
import javax.script.SimpleBindings;

/**
 */
public class JsonImplCodeGen
{
  private final String _fqn;
  private final JsonStructureType _model;

  public JsonImplCodeGen( JsonStructureType model, String topLevelFqn )
  {
    _model = model;
    _fqn = topLevelFqn;
  }

  public SrcClass make()
  {
    return genClass( _fqn, JsonImplSourceProducer.deriveBaseFqn( _fqn ), _model );
    //return genClass( _fqn, _fqn.substring( 0, _fqn.length() - JsonImplSourceProducer.IMPL_SUFFIX.length() ), _model );
  }

  private SrcClass genClass( String fqnImpl, String fqnIface, JsonStructureType model )
  {
    SrcClass srcClass = new SrcClass( fqnImpl, SrcClass.Kind.Class )
      .modifiers( !fqnImpl.equals( _fqn ) ? Modifier.STATIC : 0 )
      .uses( Bindings.class )
      .uses( SimpleBindings.class )
      .uses( IJsonIO.class )
      //.iface( fqnIface )
      .superClass( JsonImplBase.class );
    addConstructors( srcClass );
    addProperties( srcClass, model );
    addInnerTypes( srcClass, fqnIface, model.getInnerTypes() );
    return srcClass;
  }

  private void addConstructors( SrcClass srcClass )
  {
    srcClass.addConstructor( new SrcConstructor()
                       .body( new SrcStatementBlock<SrcConstructor>()
                                .addStatement( new SrcRawStatement()
                                                 .rawText( "super()" ) ) ) );
    srcClass.addConstructor( new SrcConstructor()
                       .addParam( "bindings", Bindings.class.getSimpleName() )
                       .body( new SrcStatementBlock<SrcConstructor>()
                                .addStatement( new SrcRawStatement()
                                                 .rawText( "super(bindings)" ) ) ) );
  }

  private void addInnerTypes( SrcClass srcClass, String fqnIface, Map<String, IJsonParentType> innerClasses )
  {
    for( Map.Entry<String, IJsonParentType> e : innerClasses.entrySet() )
    {
      IJsonParentType type = e.getValue();
      if( type instanceof JsonStructureType )
      {
        String simpleInnerIface = e.getValue().getName();
        String fqnInnerIface = fqnIface + '.' + simpleInnerIface;

        String fqnInnerImpl = srcClass.getPackage() + '.' + srcClass.getSimpleName() + '.' + simpleInnerIface;

        srcClass.addInnerClass( genClass( fqnInnerImpl, fqnInnerIface, (JsonStructureType)type ) );
      }
      else if( type instanceof JsonListType )
      {
        addInnerTypes( srcClass, fqnIface, ((JsonListType)type).getInnerTypes() );
      }
    }
  }

  private void addProperties( SrcClass srcClass, JsonStructureType model )
  {
    Map<String, IJsonType> members = model.getMembers();
    for( Map.Entry<String, IJsonType> e : members.entrySet() )
    {
      String key = e.getKey();
      String identifier = makePropertyName( key );
      IJsonType type = e.getValue();
      if( type instanceof JsonStructureType )
      {
        String implType = type.getName();

        srcClass
          .addGetProperty( new SrcGetProperty( identifier, implType )
                             .body( new SrcStatementBlock<SrcGetProperty>()
                                      .addStatement( new SrcRawStatement().rawText( "return new " + implType + "(_bindings.get(\"" + key + "\") as " + Bindings.class.getSimpleName() + ")" ) ) ) )
          .addSetProperty( new SrcSetProperty( identifier, Object.class.getSimpleName() ) // Object to be structurally assignable to the none impl structures
                             .body( new SrcStatementBlock<SrcSetProperty>()
                                      .addStatement( new SrcRawStatement().rawText( "_bindings.put(\"" + key + "\", getBindings(" + SrcSetProperty.VALUE_PARAM + "))" ) ) ) );

      }
      else
      {
        IJsonType componentType = getComponentTypeSimple( type );
        if( type instanceof JsonListType && componentType instanceof JsonStructureType )
        {
          String implType = type.getName();
          srcClass
            .addGetProperty( new SrcGetProperty( identifier, implType )
                               .body( new SrcStatementBlock<SrcGetProperty>()
                                        .addStatement( new SrcRawStatement().rawText( "return wrapList(_bindings.get(\"" + key + "\") as List, \\ b ->  new " + componentType.getName() + "(b)) as " + implType ) ) ) )
            .addSetProperty( new SrcSetProperty( identifier, List.class.getSimpleName() ) // Just List to be structurally assignable to the none impl structures
                               .body( new SrcStatementBlock<SrcSetProperty>()
                                        .addStatement( new SrcRawStatement().rawText( "_bindings.put(\"" + key + "\", unwrapList(" + SrcSetProperty.VALUE_PARAM + "))" ) ) ) );

        }
        else
        {
          srcClass
            .addGetProperty( new SrcGetProperty( identifier, type.getName() )
                               .body( new SrcStatementBlock<SrcGetProperty>()
                                        .addStatement( new SrcRawStatement().rawText( "return _bindings.get(\"" + key + "\") as " + type.getName() ) ) ) )
            .addSetProperty( new SrcSetProperty( identifier, type.getName() )
                               .body( new SrcStatementBlock<SrcSetProperty>()
                                        .addStatement( new SrcRawStatement().rawText( "_bindings.put(\"" + key + "\", " + SrcSetProperty.VALUE_PARAM + ")" ) ) ) );

        }
      }
    }
  }

  private String makePropertyName( String key )
  {
    StringBuilder name = new StringBuilder( Json.makeIdentifier( key ) );
    char firstChar = name.charAt( 0 );
    if( Character.isLowerCase( firstChar ) )
    {
      name.setCharAt( 0, Character.toUpperCase( firstChar ) );
    }
    return name.toString();
  }

  private IJsonType getComponentTypeSimple( IJsonType type )
  {
    if( type instanceof JsonListType )
    {
      IJsonType componentType = ((JsonListType)type).getComponentType();
      if( componentType instanceof JsonSimpleType )
      {
        return componentType;
      }

      return getComponentTypeSimple( componentType );
    }
    return type;
  }
}