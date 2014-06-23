/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.typeprovider;

import gw.internal.xml.ws.typeprovider.paraminfo.AnonymousElementException;
import gw.internal.xml.ws.typeprovider.paraminfo.IdentityWsdlOperationParameterInfo;
import gw.internal.xml.ws.typeprovider.paraminfo.WsdlOperationParameterInfo;
import gw.internal.xml.xsd.typeprovider.XmlSchemaTypeSchemaInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.java.JavaTypes;
import gw.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

public class WsdlOperationOutputInfo {

  private final IType _responseElementType;
  private final List<Pair<IType,QName>> _unwrapLevels = new ArrayList<Pair<IType, QName>>();
  private WsdlOperationParameterInfo _returnParameterInfo;
  private XmlSchemaTypeSchemaInfo _schemaInfo; // schema info for the final unwrapped element where the parameters are
  private boolean _useParentElement;

  public WsdlOperationOutputInfo( IType responseElementType, QName responseElementName ) throws AnonymousElementException {
    _responseElementType = responseElementType;
    _returnParameterInfo = new IdentityWsdlOperationParameterInfo( responseElementType, responseElementName, true );
  }

  public void addUnwrapLevel( IType type, QName elementName ) {
    _unwrapLevels.add( new Pair<IType, QName>( type, elementName ) );
  }

  public WsdlOperationParameterInfo getReturnParameterInfo() {
    return _returnParameterInfo;
  }

  public void setReturnParameterInfo( WsdlOperationParameterInfo paramInfo ) {
    _returnParameterInfo = paramInfo;
  }

  public List<Pair<IType, QName>> getUnwrapLevels() {
    return Collections.unmodifiableList( _unwrapLevels );
  }

  public IType getResponseElementType() {
    return _responseElementType;
  }

  public void setSchemaInfo( XmlSchemaTypeSchemaInfo schemaInfo ) {
    _schemaInfo = schemaInfo;
  }

  public XmlSchemaTypeSchemaInfo getSchemaInfo() {
    return _schemaInfo;
  }

  public boolean getUseParentElement() {
    return _useParentElement;
  }

  public void setUseParentElement( boolean useParentElement ) {
    _useParentElement = useParentElement;
  }

  public IType getReturnType() {
    return _returnParameterInfo == null ? JavaTypes.pVOID() : _returnParameterInfo.getType();
  }
  
}
