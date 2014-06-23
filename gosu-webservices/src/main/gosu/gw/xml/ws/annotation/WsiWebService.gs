/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.ws.annotation

/**
  * This annotation is used to indicate that this class is exposed as a web service.
  */
uses gw.lang.parser.resources.Res
uses gw.lang.annotation.UsageTarget
uses gw.lang.annotation.UsageModifier
uses gw.lang.annotation.AnnotationUsage
uses gw.lang.parser.IDeclarationSiteValidator
uses java.util.HashSet
uses gw.lang.reflect.gs.IGosuClass
uses gw.lang.reflect.java.IJavaType
uses gw.lang.reflect.IType
uses gw.lang.parser.IParsedElement
uses gw.internal.xml.config.XmlServices
uses java.util.HashMap
uses gw.lang.reflect.gs.IGosuMethodInfo
uses javax.xml.namespace.QName
uses java.net.URI
uses gw.internal.xml.ws.server.IWsiWebService
uses gw.lang.parser.statements.IClassStatement
uses gw.lang.parser.statements.IFunctionStatement
uses gw.internal.xml.ws.server.WsiUtilities
uses java.lang.Exception
uses gw.xml.ws.WsiInvocationContext
uses gw.lang.reflect.java.JavaTypes

@AnnotationUsage(UsageTarget.TypeTarget, UsageModifier.One)
class WsiWebService implements IWsiWebService, IAnnotation, IDeclarationSiteValidator {

  private var _targetNamespace : String as readonly TargetNamespace
  private var _serviceNameLocalPart : String as readonly ServiceNameLocalPart
  
  construct() {
    _targetNamespace = null
    _serviceNameLocalPart = null
  }

  construct(namespace : String) {
    _targetNamespace = namespace
    _serviceNameLocalPart = null
  }

  construct(serviceName : QName) {
    _targetNamespace = serviceName.NamespaceURI
    _serviceNameLocalPart = serviceName.LocalPart
  }

  override function validate(feature : gw.lang.parser.IParsedElement) {
    var typeInfo = (feature as gw.lang.parser.statements.IClassStatement).GosuClass.TypeInfo
    var type = typeInfo.OwnersType as IGosuClass
    try {
      var seenNamespaces = new HashMap<String, Object>()
      try {
         new URI(TargetNamespace == null ? XmlServices.createTargetNamespace(type) : TargetNamespace).toString()
      } catch ( e : Exception ) {
       type.ClassStatement.addParseException(Res.WS_ERR_Annotation_Invalid_Namespace, {e.Message})
      }
      seenNamespaces.put(_targetNamespace, this)
      var seenNames = new HashSet<String>()
      // Iterate each method and check the args+return type
      for ( m in typeInfo.DeclaredMethods ) {
        //Since this annotation value is retrieved at compile time, it must be evaluated rather than directly requested
        if (WsiUtilities.isOperation( m, null )) {
          var name = m.DisplayName
          var methodParsedElement = type.getFunctionStatement(m)
          var parsedElement = methodParsedElement == null ? feature : methodParsedElement
          if (name.endsWith("Response")) {
            parsedElement.addParseException(Res.WS_ERR_Annotation_Operation_Response_Reserved, {})
          }
          if (!seenNames.add(name)) {
            parsedElement.addParseException(Res.WS_ERR_Annotation_Operation_Duplicate, { name })
          }
          else if (JavaTypes.pVOID() != m.ReturnType) {
            XmlServices.checkWsiWebServiceType(parsedElement, (parsedElement == feature ? (name + "."): "") + "return", m.ReturnType, seenNamespaces)
          }
          for (param in m.Parameters) {
            if ( param.FeatureType != WsiInvocationContext ) {
              XmlServices.checkWsiWebServiceType(parsedElement, (parsedElement == feature ? (name + "."): "") + param.Name, param.FeatureType, seenNamespaces)

            }
          }
        }
      }
    }
    catch ( e : Exception ) {
     XmlServices.getLogger(Loading).warn("On " + feature , e)
     type.ClassStatement.addParseException(Res.WS_ERR_Annotation_Exception, {e.Message})
    }
  }

  static function validateOnWebService(feature : IParsedElement, itype : IType) {
    if (feature typeis IClassStatement) {
      if (!isWsiWebService(feature.GosuClass)) {
        feature.addParseException(Res.WS_ERR_Annotation_Only_For_WebService, { itype.RelativeName })
      }
    }
    else if (feature typeis IFunctionStatement) {
      if (!isWsiWebService(feature.DynamicFunctionSymbol.GosuClass)) {
        feature.addParseException(Res.WS_ERR_Annotation_Only_For_WebService, { itype.RelativeName })
      }
    }
    else {
      feature.addParseException(Res.WS_ERR_Annotation_Only_For_WebService, { itype.RelativeName })
    }
  }

  static function isWsiWebService(clazz : IGosuClass) : boolean {
    return clazz.TypeInfo.hasAnnotation(WsiWebService)
  }

}