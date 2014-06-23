/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.ws.annotation

uses gw.lang.annotation.AnnotationUsage
uses gw.lang.annotation.UsageTarget
uses gw.lang.annotation.UsageModifier
uses java.io.InputStream

/**
 * Allows transformation of the outgoing response prior to sending.
 */
@AnnotationUsage( UsageTarget.TypeTarget, UsageModifier.One )
class WsiResponseTransform implements IAnnotation {

  var _transform( is : InputStream ) : InputStream

  construct( xform( is : InputStream ) : InputStream ) {
    _transform = xform
  }
  
  property get Transform() : block( is : InputStream ) : InputStream {
    return _transform
  }

}