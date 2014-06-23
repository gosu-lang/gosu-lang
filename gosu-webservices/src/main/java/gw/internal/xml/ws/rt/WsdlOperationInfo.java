/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.rt;

/**
 * This class represents the information on operation.  It is used to
 * generify the dispatch and async response.
 *
 */
import gw.internal.xml.ws.typeprovider.WsdlOperationInputInfo;
import gw.internal.xml.ws.typeprovider.WsdlOperationOutputInfo;
import gw.internal.xml.xsd.typeprovider.schema.WsdlPort;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuObject;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.java.JavaTypes;

@SuppressWarnings({"UnusedDeclaration"})
public class WsdlOperationInfo<T> implements IGosuObject {
  final private String _name;
  final private String _soapAction;
  final private WsdlOperationInputInfo _inputInfo;
  final private WsdlOperationOutputInfo _outputInfo;
  final private WsdlPort _wsdlPort;

  /**
   *
   * @param name the operation name
   * @param soapAction the http soap action header
   * @param inputInfo
   * @param outputInfo
   * @param wsdlPort
   */
  public WsdlOperationInfo( String name, String soapAction, WsdlOperationInputInfo inputInfo, WsdlOperationOutputInfo outputInfo, WsdlPort wsdlPort ) {
    _name = name;
    _soapAction = soapAction;
    _inputInfo = inputInfo;
    _outputInfo = outputInfo;
    _wsdlPort = wsdlPort;
  }

  /**
   *
   * @return the name of the operation
   */
  public String getName() {
    return _name;
  }

  /**
   *
   * @return if the xml type just wraps a type this is the wrapped type, e.g., String
   */
  @Override
  public IType getIntrinsicType() {
    return TypeSystem.get( WsdlOperationInfo.class ).getParameterizedType(
            (_outputInfo == null || _outputInfo.getReturnParameterInfo() == null) ? JavaTypes.pVOID() : _outputInfo.getReturnParameterInfo().getType() );
  }

  /**
   *
   * @return the soapAction of the operation
   */
  public String getSoapAction() {
    return _soapAction;
  }

  /**
   *
   * @return will return _outputWrapperType _operationName(_inputWrapperType)
   */
  public String toString() {
    return (_outputInfo.getReturnParameterInfo() != null ? _outputInfo.getReturnParameterInfo().getType().getName() : "void") +
            " " + _name + "(" + _inputInfo.getRequestElementType().getName() + ")";
  }

  public WsdlOperationOutputInfo getOutputInfo() {
    return _outputInfo;
  }

  public WsdlOperationInputInfo getInputInfo() {
    return _inputInfo;
  }

  public WsdlPort getWsdlPort() {
    return _wsdlPort;
  }

}
