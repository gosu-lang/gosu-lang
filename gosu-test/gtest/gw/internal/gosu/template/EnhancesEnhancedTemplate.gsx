package gw.internal.gosu.template

enhancement EnhancesEnhancedTemplate : EnhancedTemplate {

  static function passThroughToRender() : String {
    return EnhancedTemplate.renderToString()
  }

}