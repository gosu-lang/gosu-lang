package com.example.bootstrap

class PropertiesClass{

  /*
   * var-derived property
   */
  var varBasedProp1 : String as PublicVarBasedProp1

  /*
   * public property-keyword based property
   */
  property get PublicPropertyKeywordBasedProp() : String{
    return null
  }

  /*
   * public property-keyword with get-and-set GET
   */
  property get PublicPropertyKeywordBasedPropWithGetAndSetCommentOnGet() : String{
    return null
  }
  property set PublicPropertyKeywordBasedPropWithGetAndSetCommentOnGet(s : String) {
  }

  property get PublicPropertyKeywordBasedPropWithGetAndSetCommentOnSet() : String{
    return null
  }
  /*
   * public property-keyword with get-and-set SET
   */
  property set PublicPropertyKeywordBasedPropWithGetAndSetCommentOnSet(s : String) {
  }


  /*
   * public property-keyword with get-and-set GET
   */
  property get PublicPropertyKeywordBasedPropWithGetAndSetCommentOnGetAndSet() : String{
    return null
  }
  /*
   * public property-keyword with get-and-set SET
   */
  property set PublicPropertyKeywordBasedPropWithGetAndSetCommentOnGetAndSet(s : String) {
  }

  /*
   * public property-keyword with get-and-set SET
   */
  property set PublicPropertyKeywordBasedPropWithSetCommentOnSet(s : String) {
  }

  private property get PrivateProperty() : String {
    return null
  }


}