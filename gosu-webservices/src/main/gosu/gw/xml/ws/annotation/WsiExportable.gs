/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.ws.annotation

uses gw.lang.annotation.UsageTarget 
uses gw.lang.annotation.UsageModifier
uses gw.lang.annotation.AnnotationUsage
uses gw.lang.parser.IDeclarationSiteValidator
uses gw.lang.parser.IParsedElement   
uses gw.lang.parser.statements.IClassStatement
uses gw.lang.parser.resources.Res
uses gw.internal.xml.config.XmlServices
uses java.lang.IllegalArgumentException
uses java.util.HashMap
uses gw.lang.reflect.gs.IGosuClass
uses gw.config.CommonServices

@AnnotationUsage(UsageTarget.TypeTarget, UsageModifier.One)
class WsiExportable implements IAnnotation, IDeclarationSiteValidator {

  private var _targetNamespace : String as readonly TargetNamespace

  construct() {
    _targetNamespace = null
  }

  construct( namespace : String ) {
    _targetNamespace = namespace
  }

  override function validate(feature : IParsedElement) {
    //TODO-dp remove this
    if (CommonServices.getPlatformHelper().isInIDE()) {
      return
    }

    if (feature typeis IClassStatement) {
      var clazz = feature.GosuClass
      if (clazz.Enum) {
        return
      }
      if (WsiWebService.isWsiWebService(clazz)) {
        feature.addParseException(Res.WS_ERR_Export_Not_WebService, { })
      }
      if (!clazz.Final) {
        feature.addParseException(Res.WS_ERR_Export_Not_Final, { })
      }
      if (clazz.Supertype != null) {
        feature.addParseException(Res.WS_ERR_Export_No_Extends, { })
      }
      var encClazz = clazz.EnclosingType
      if (encClazz typeis IGosuClass) { 
//        if (!WsiWebService.isWsiWebService(encClazz)) {
          // only inner classes in WsiWebService are acceptabled
          feature.addParseException(Res.WS_ERR_Export_Inner_Only_On_WebService, { })
//        }
//        else {
//          for (m in encClazz.TypeInfo.Methods) {
//            if (clazz.RelativeName.equalsIgnoreCase(m.Name)) {
//              feature.addParseException(Res.WS_ERR_Export_Inner_Not_Name_Of_Method, { m.Name })
//            }
//            if (clazz.RelativeName.equalsIgnoreCase(m.Name + "Response")) {
//              feature.addParseException(Res.WS_ERR_Export_Inner_Not_Name_Of_Response, { m.Name })
//            }
//          }
//        }
      }
      var typeInfo = clazz.TypeInfo
      var constructor = typeInfo.getConstructor({})
      if (!constructor.Public) {
        feature.addParseException(Res.WS_ERR_Export_No_Constructor, { })
      }
      var seenNamespaces = new HashMap<String, Object>()
      XmlServices.checkWsiWebServiceType(feature, "", clazz, seenNamespaces)
    }
    else {
      throw new IllegalArgumentException("Only on class")
    }
  }
  
}
