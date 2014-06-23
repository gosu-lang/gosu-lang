/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.ws.annotation

uses gw.lang.annotation.AnnotationUsage
uses gw.lang.annotation.UsageTarget
uses gw.lang.annotation.UsageModifier
uses gw.xml.XmlParseOptions

/**
 * Allows specification of the parse options to be used when parsing an incoming soap envelope.
 */
@AnnotationUsage( UsageTarget.TypeTarget, UsageModifier.One )
class WsiParseOptions implements IAnnotation {

  var _parseOptions : XmlParseOptions 

  construct( options : XmlParseOptions ) {
    _parseOptions = options
  }
  
  property get ParseOptions() : XmlParseOptions {
    return _parseOptions
  }

}