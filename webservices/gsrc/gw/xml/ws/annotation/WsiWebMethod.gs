/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.ws.annotation

/**
  * This annotation is used to modify the default behavior for a web method.
  */
uses gw.lang.annotation.UsageTarget
uses gw.lang.annotation.UsageModifier
uses gw.lang.annotation.AnnotationUsage
uses gw.internal.xml.ws.server.IWsiWebMethod

@AnnotationUsage( UsageTarget.MethodTarget, UsageModifier.One )
class WsiWebMethod implements IWsiWebMethod, IAnnotation {

  private var _operationName : String
  private var _exclude: boolean
  
  construct() {
    _operationName = null
    _exclude = false
  }

  construct( opName : String ) {
    _operationName = opName
    _exclude = false
  }

  construct( excl : boolean ) {
    _operationName = null
    _exclude = excl
  }

  construct( opName : String, excl : boolean ) {
    _operationName = opName
    _exclude = excl
  }

  override property get Exclude() : boolean {
    return _exclude
  }

  override property get OperationName() : String {
    return _operationName
  }

}