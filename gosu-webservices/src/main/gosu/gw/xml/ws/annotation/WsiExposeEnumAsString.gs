/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.ws.annotation

uses gw.lang.annotation.UsageModifier
uses gw.lang.annotation.UsageTarget
uses gw.lang.annotation.AnnotationUsage
uses gw.internal.xml.ws.server.IWsiExposeEnumAsString
uses gw.lang.reflect.IType

@AnnotationUsage( UsageTarget.TypeTarget, UsageModifier.Many )
class WsiExposeEnumAsString implements IAnnotation, IWsiExposeEnumAsString {

  private var _type : Type as readonly EnumType
  
  construct( t : Type ) {
    _type = t
  }


  override property get Type() : IType {
    return _type
  }

}
