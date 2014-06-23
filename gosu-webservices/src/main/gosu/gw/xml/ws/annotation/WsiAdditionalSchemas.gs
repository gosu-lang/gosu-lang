/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.ws.annotation

uses gw.lang.annotation.AnnotationUsage
uses gw.lang.annotation.UsageTarget
uses gw.lang.annotation.UsageModifier
uses gw.xml.XmlSchemaAccess
uses gw.internal.xml.ws.server.IWsiAdditionalSchemas

/**
 * Allows specification of additional schemas to be referenced from the generated WSDL for this service.
 */
@AnnotationUsage( UsageTarget.TypeTarget, UsageModifier.One )
class WsiAdditionalSchemas implements IAnnotation, IWsiAdditionalSchemas {

  var _additionalSchemas : List<XmlSchemaAccess>

  construct( schemas : List<XmlSchemaAccess> ) {
    _additionalSchemas = schemas
  }

  override property get AdditionalSchemas() : List<XmlSchemaAccess> {
    return _additionalSchemas
  }

}