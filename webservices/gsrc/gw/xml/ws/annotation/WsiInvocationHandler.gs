/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.ws.annotation

uses gw.lang.annotation.AnnotationUsage
uses gw.lang.annotation.UsageTarget
uses gw.lang.annotation.UsageModifier
uses gw.xml.ws.DefaultWsiInvocationHandler

/**
 * Allows specification of additional schemas to be referenced from the generated WSDL for this service.
 */
@AnnotationUsage( UsageTarget.TypeTarget, UsageModifier.One )
class WsiInvocationHandler implements IAnnotation {

  public var _invocationHandler : DefaultWsiInvocationHandler as InvocationHandler

  construct( handler : DefaultWsiInvocationHandler ) {
    _invocationHandler = handler
  }

  property get InvocationHandler() : DefaultWsiInvocationHandler {
    return _invocationHandler
  }

}