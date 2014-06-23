/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

public final class XmlSchemaAny extends XmlSchemaParticle<XmlSchemaAny> {

  public enum ProcessContents { skip, lax, strict }
  private final String _namespace;
  private final String _targetNamespace;
  private final ProcessContents _processContents;

  private String _namespaceSpec;
  private boolean _anyNamespace;
  private boolean _otherNamespace;
  private List<String> _targetNamespaces = new ArrayList<String>();

  public XmlSchemaAny( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, String namespace, String targetNamespace, long minOccurs, long maxOccurs, ProcessContents processContents ) {
    super( schemaIndex, locationInfo, minOccurs, maxOccurs );
    _targetNamespace = targetNamespace;
    _namespace = namespace;
    _processContents = processContents;
    //////////////////
    //##any
    //Elements from any namespace can be present.
    //##other
    //Elements from any namespace that is not the target namespace of the parent element containing this element can be present.
    //##local
    //Elements that are not qualified with a namespace can be present.
    //##targetNamespace
    //Elements from the target namespace of the parent element containing this element can be present.
    //List of {URI references, ##targetNamespace, ##local}
    //Elements from a space-delimited list of the namespaces can be present. The list can contain the following: URI references of namespaces, ##targetNamespace, and ##local.

    _namespaceSpec = getNamespace();
    if ( _namespaceSpec == null ) {
      _namespaceSpec = "##any";
    }
    if ( _namespaceSpec.equals( "##any" ) ) {
      _anyNamespace = true;
    }
    else if ( _namespaceSpec.equals( "##other" ) ) {
      _otherNamespace = true;
    }
    else {
      String[] parts = _namespaceSpec.split( " " );
      for ( String part : parts ) {
        if ( part.equals( "##local" ) ) {
          _targetNamespaces.add( XMLConstants.NULL_NS_URI );
        }
        else if ( part.equals( "##targetNamespace" ) ) {
          _targetNamespaces.add( getTargetNamespace() );
        }
        else if ( part.length() > 0 ) {
          _targetNamespaces.add( part );
        }
      }
    }

  }

  public String getNamespace() {
    return _namespace;
  }

  public String getTargetNamespace() {
    return _targetNamespace;
  }

  public ProcessContents getProcessContents() {
    return _processContents;
  }

  public boolean accept( QName elementName ) {
    if ( _anyNamespace ) {
      return true;
    }
    else if ( _otherNamespace ) {
      return ! elementName.getNamespaceURI().equals( _targetNamespace );
    }
    else {
      return _targetNamespaces.contains( elementName.getNamespaceURI() );
    }
  }

  public String getNamespaceSpec() {
    return _namespaceSpec;
  }

  @Override
  public XmlSchemaAny copy( XmlSchemaIndex schemaIndex ) {
    return new XmlSchemaAny( schemaIndex, getLocationInfo(), _namespace, _targetNamespace, getMinOccurs(), getMaxOccurs(), _processContents );
  }

}
