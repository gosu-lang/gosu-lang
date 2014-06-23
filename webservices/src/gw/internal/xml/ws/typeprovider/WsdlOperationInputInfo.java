/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.typeprovider;

import gw.internal.xml.ws.typeprovider.paraminfo.AnonymousElementException;
import gw.internal.xml.ws.typeprovider.paraminfo.IdentityWsdlOperationParameterInfo;
import gw.internal.xml.ws.typeprovider.paraminfo.WsdlOperationParameterInfo;
import gw.internal.xml.xsd.typeprovider.XmlSchemaTypeSchemaInfo;
import gw.lang.reflect.IType;
import gw.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

public class WsdlOperationInputInfo {

  private final IType _requestElementType;
  private final List<Pair<IType,QName>> _unwrapLevels = new ArrayList<Pair<IType, QName>>();
  private List<Pair<WsdlOperationParameterInfo, Boolean>> _parameterInfos;
  private XmlSchemaTypeSchemaInfo _schemaInfo; // schema info for the final unwrapped element where the parameters are

  public WsdlOperationInputInfo( IType requestElementType, QName requestElementName ) throws AnonymousElementException {
    _requestElementType = requestElementType;
    _parameterInfos = Collections.singletonList( new Pair<WsdlOperationParameterInfo, Boolean>( new IdentityWsdlOperationParameterInfo( requestElementType, requestElementName, true ), false ) );
  }

  public void addUnwrapLevel( IType type, QName elementName ) {
    _unwrapLevels.add( new Pair<IType, QName>( type, elementName ) );
  }

  public List<Pair<WsdlOperationParameterInfo, Boolean>> getParameterInfos() {
    return _parameterInfos;
  }

  public void setParameterInfos( List<Pair<WsdlOperationParameterInfo,Boolean>> paramInfos ) {
    _parameterInfos = paramInfos;
  }

  public List<Pair<IType, QName>> getUnwrapLevels() {
    return Collections.unmodifiableList( _unwrapLevels );
  }

  public IType getRequestElementType() {
    return _requestElementType;
  }

  public void setSchemaInfo( XmlSchemaTypeSchemaInfo schemaInfo ) {
    _schemaInfo = schemaInfo;
  }

  public XmlSchemaTypeSchemaInfo getSchemaInfo() {
    return _schemaInfo;
  }

  public boolean isUnwrapped() {
    return _schemaInfo != null;
  }

}
