package gw.internal.gosu.compiler.featureliteral
uses gw.lang.reflect.features.IFeatureReference

class FeatureAnnotation implements IAnnotation {
  
  var _features : List<IFeatureReference> as Features
  
  construct( f : List<IFeatureReference> ) {
    _features = f;
  }

}
