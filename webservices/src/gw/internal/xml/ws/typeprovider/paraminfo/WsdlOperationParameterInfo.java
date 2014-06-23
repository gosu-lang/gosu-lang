/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.typeprovider.paraminfo;

import gw.internal.xml.xsd.typeprovider.IXmlSchemaElementTypeData;
import gw.internal.xml.xsd.typeprovider.XmlSchemaPropertySpec;
import gw.internal.xml.xsd.typeprovider.XmlSchemaTypeSchemaInfo;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaElement;
import gw.internal.xml.xsd.typeprovider.schemaparser.XmlSchemaParser;
import gw.lang.reflect.IType;
import gw.xml.XmlElement;

import javax.xml.namespace.QName;

public abstract class WsdlOperationParameterInfo {

  private final IType _parameterElementType;
  private final QName _parameterElementName;

  protected WsdlOperationParameterInfo( IType parameterElementType, QName parameterElementName ) {
    _parameterElementType = parameterElementType;
    _parameterElementName = parameterElementName;
  }

  public static WsdlOperationParameterInfo create( XmlSchemaPropertySpec parameterPropertySpec ) throws AnonymousElementException {
    return createComponent( parameterPropertySpec, false, true );
  }

  private static WsdlOperationParameterInfo createComponent( XmlSchemaPropertySpec componentPropertySpec, boolean isComponent, boolean primitiveOk ) throws AnonymousElementException {
    IXmlSchemaElementTypeData componentTypeData = componentPropertySpec.getElementPropertyGosuTypeData();
    XmlSchemaTypeSchemaInfo componentSchemaInfo = componentTypeData.getSchemaInfo();
    // perform pre-checks to see if we have a strategy to unwrap this component
    boolean identity = false;
    XmlSchemaElement element = (XmlSchemaElement) componentPropertySpec.getXmlSchemaObject();
    boolean hasAttributes = hasAttributes( componentSchemaInfo );
    boolean hasAny = hasAny( componentSchemaInfo );
    if ( ! isComponent ) {
      // top level parameter must be single and non-nillable
      if ( componentPropertySpec.isPlural() ) {
        identity = true; // cannot be plural since there is no wrapping list for now
      }
      if ( element.isNillable() ) {
        identity = true;
      }
    }
    if ( componentPropertySpec.hasSimpleContent() && ! hasAttributes && ! hasAny ) {
      if ( isComponent && ! primitiveOk && ! element.isNillable() ) {
        identity = true;
      }
      else {
        return new SimpleContentWsdlOperationParameterInfo( componentPropertySpec, isComponent, primitiveOk );
      }
    }
    if ( identity ) {
      return new IdentityWsdlOperationParameterInfo( componentPropertySpec.getElementPropertyGosuType(), componentPropertySpec.getQName(), ! isComponent );
    }
    if ( ! componentSchemaInfo.getTypeData().isAnonymous() ) {
      return new XmlTypeInstanceWsdlOperationParameterInfo( componentPropertySpec );
    }
    if ( componentSchemaInfo.getProperties().size() == 0 && ! hasAttributes && hasAny ) {
      return new XmlElementWsdlOperationParameterInfo( componentPropertySpec, null );
    }
    if ( componentSchemaInfo.getProperties().size() == 1 && ! hasAttributes && ! hasAny ) {
      XmlSchemaPropertySpec childPropertySpec = componentSchemaInfo.getProperties().get( 0 );
      if ( childPropertySpec.isPlural() ) {
        if ( XmlSchemaParser.VIEWAS_LIST.equals( element.getGwViewAs() ) ) {
          WsdlOperationParameterInfo childParamInfo = createComponent( childPropertySpec, true, false );
          if ( childParamInfo != null ) {
            return new ListWsdlOperationParameterInfo( componentPropertySpec.getElementPropertyGosuType(), componentPropertySpec.getQName(), childParamInfo );
          }
        }
        else {
          WsdlOperationParameterInfo childParamInfo = createComponent( childPropertySpec, true, true );
          if ( childParamInfo != null ) {
            return new ArrayWsdlOperationParameterInfo( componentPropertySpec.getElementPropertyGosuType(), componentPropertySpec.getQName(), childParamInfo );
          }
        }
      }
      IXmlSchemaElementTypeData childTypeData = childPropertySpec.getElementPropertyGosuTypeData();
      if ( ! childTypeData.isAnonymous() ) {
        return new XmlElementWsdlOperationParameterInfo( componentPropertySpec, childPropertySpec );
      }
      else if ( ! childTypeData.getSchemaInfo().getTypeData().isAnonymous() ) {
        return new XmlTypeInstanceWsdlOperationParameterInfo( componentPropertySpec );
      }
    }
    else if ( componentTypeData.isAnonymous() && ! componentSchemaInfo.getTypeData().isAnonymous() ) {
      // we normally prefer elements, but we prefer a non-anonymous typeinstance over an anonymous element
      return new XmlTypeInstanceWsdlOperationParameterInfo( componentPropertySpec );
    }
    return new IdentityWsdlOperationParameterInfo( componentPropertySpec.getElementPropertyGosuType(), componentPropertySpec.getQName(), ! isComponent );
  }

  public static boolean hasAttributes( XmlSchemaTypeSchemaInfo componentSchemaInfo ) {
    return componentSchemaInfo.getXsdType().getAnyAttributeRecursiveIncludingSupertypes() != null || ! componentSchemaInfo.getAttributeNames().isEmpty();
  }

  public static boolean hasAny( XmlSchemaTypeSchemaInfo componentSchemaInfo ) {
    return componentSchemaInfo.getXsdType().getAnyRecursiveIncludingSupertypes() != null;
  }

  public abstract IType getType();

  public abstract Object unwrap( XmlElement node );

  public abstract String getName();

  public abstract XmlElement wrap( Object obj, XmlElement componentElement );

  public IType getParameterElementType() {
    return _parameterElementType;
  }

  public QName getParameterElementName() {
    return _parameterElementName;
  }

}
