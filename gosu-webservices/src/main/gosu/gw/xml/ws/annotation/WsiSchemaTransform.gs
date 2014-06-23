/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.ws.annotation

uses gw.lang.annotation.AnnotationUsage
uses gw.lang.annotation.UsageTarget
uses gw.lang.annotation.UsageModifier
uses gw.xsd.w3c.wsdl.Definitions
uses gw.xsd.w3c.xmlschema.Schema

/**
 * Allows modification of the schema produced for a WSI Web Service.
 */
@AnnotationUsage( UsageTarget.TypeTarget, UsageModifier.One )
class WsiSchemaTransform implements IAnnotation {

  var _transform( wsdl : Definitions, schema : Schema ) 
  var _gwtransform( wsdl : Definitions, schema : Schema, xsdRootPath : String )

  construct( xform( wsdl : Definitions, schema : Schema ) ) {
    _transform = xform
  }
  
  construct( xform( wsdl : Definitions, schema : Schema, xsdRootPath : String ) ) {
    _gwtransform = xform
  }

  property get Transform() : block( wsdl : Definitions, schema : Schema ) {
    return _transform
  }

  property get GWTransform() : block( wsdl : Definitions, schema : Schema, xsdRootPath : String ) {
    return _gwtransform
  }
}