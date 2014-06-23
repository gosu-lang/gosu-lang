/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.ws;

import gw.lang.PublishInGosu;
import gw.xml.XmlElement;

import javax.xml.namespace.QName;
import java.net.URI;

@PublishInGosu
public class WsdlFault extends RuntimeException {

  private XmlElement _detail;
  private FaultCode _code;
  private QName _codeQName;
  private URI _actorRole;

  /**
   * Constructs a WsdlFault.
   */
  public WsdlFault() {
  }

  /**
   * Constructs a WsdlFault.
   *
   * @param message
   */
  public WsdlFault( String message ) {
    super( message );
  }

  /**
   * Constructs a WsdlFault.
   *
   * @param cause the cause of the wsdl fault
   */
  public WsdlFault( Throwable cause ) {
    super( cause );
  }

  /**
   * Constructs a WsdlFault.
   *
   * @param message
   * @param cause the cause of the wsdl fault
   */
  public WsdlFault( String message, Throwable cause ) {
    super( message, cause );
  }

  /**
   * Gets the fault code.
   *
   * @return the fault code
   */
  public FaultCode getCode() {
    return _code;
  }

  /**
   * Sets the fault code.
   *
   * @param code the fault code
   */
  public void setCode( FaultCode code ) {
    _code = code;
  }

  /**
   * Gets the fault code QName.
   *
   * @return the fault code QName
   */
  public QName getCodeQName() {
    return _codeQName;
  }

  /**
   * Sets the fault code QName.
   *
   * @param codeQName
   */
  public void setCodeQName( QName codeQName ) {
    _codeQName = codeQName;
  }

  /**
   * Gets the detail element.
   *
   * @return the detail element
   */
  public XmlElement getDetail() {
    return _detail;
  }

  /**
   * Sets the detail element.
   *
   * @param detail the detail element
   */
  public void setDetail( XmlElement detail ) {
    _detail = detail;
  }

  /**
   * Gets the SOAP 1.1 actor role.
   *
   * @return the actor role
   */
  public URI getActorRole() {
    return _actorRole;
  }

  /**
   * Sets the SOAP 1.1 actor role.
   *
   * @param actorRole the actor role
   */
  public void setActorRole( URI actorRole ) {
    _actorRole = actorRole;
  }

  public static enum FaultCode {
    DataEncodingUnknown,
    MustUnderstand,
    Receiver,
    Sender,
    VersionMismatch,
    Other
  }

}
