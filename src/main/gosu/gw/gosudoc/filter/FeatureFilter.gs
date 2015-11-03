package gw.gosudoc.filter

uses gw.lang.reflect.IFeatureInfo

structure FeatureFilter {
  function shouldIncludeFeature( m : IFeatureInfo ) : boolean
}