/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.ws.annotation

uses gw.lang.annotation.AnnotationUsage
uses gw.lang.annotation.UsageTarget
uses gw.lang.annotation.UsageModifier
uses gw.xml.XmlSerializationOptions

/**
 * Allows specification of the serialization options to be used when parsing an incoming soap envelope.
 */
@AnnotationUsage( UsageTarget.TypeTarget, UsageModifier.One )
class WsiSerializationOptions implements IAnnotation {

  var _serializationOptions : XmlSerializationOptions 

  construct( options : XmlSerializationOptions ) {
    _serializationOptions = options
  }
  
  property get SerializationOptions() : XmlSerializationOptions {
    return _serializationOptions
  }

}