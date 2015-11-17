package gw.gosudoc

uses com.example.bootstrap.MethodsClass
uses com.example.bootstrap.PropertiesClass
uses gw.gosudoc.util.BaseGosuDocTest
uses org.jsoup.Jsoup
uses org.junit.Assert
uses org.junit.Ignore
uses org.junit.Test

class PropertyDocsTest extends BaseGosuDocTest{

  @Test
  function varBasedPropertyIsInDocs() {
    var docs = gosuDocForType( PropertiesClass )
    var doc = Jsoup.parse( docs.read() )
    var propertyDocs = doc.findPropertyList( PropertiesClass#PublicVarBasedProp1 )
    Assert.assertNotNull( propertyDocs )
    Assert.assertContains( propertyDocs.text(), "var-derived property")
  }

  @Test
  function publicGetPropertyKeywordBasedPropertyIsInDocs() {
    var docs = gosuDocForType( PropertiesClass )
    var doc = Jsoup.parse( docs.read() )
    var propertyDocs = doc.findPropertyList( PropertiesClass#PublicPropertyKeywordBasedProp )
    Assert.assertNotNull( propertyDocs )
    Assert.assertContains( propertyDocs.text(), "public property-keyword based property")
  }

  @Test
  function publicPropertyKeywordBasedPropertyWithCommentOnlyOnGetIsInDocs() {
    var docs = gosuDocForType( PropertiesClass )
    var doc = Jsoup.parse( docs.read() )
    var propertyDocs = doc.findPropertyList( PropertiesClass#PublicPropertyKeywordBasedPropWithGetAndSetCommentOnGet )
    Assert.assertNotNull( propertyDocs )
    Assert.assertContains( propertyDocs.text(), "public property-keyword with get-and-set GET")
  }

  @Test
  @Ignore("Comment on set only does not appear to work properly")
  function publicPropertyKeywordBasedPropertyWithCommentOnlyOnSetIsInDocs() {
    var docs = gosuDocForType( PropertiesClass )
    var doc = Jsoup.parse( docs.read() )
    var propertyDocs = doc.findPropertyList( PropertiesClass#PublicPropertyKeywordBasedPropWithGetAndSetCommentOnSet )
    Assert.assertNotNull( propertyDocs )
    Assert.assertContains( propertyDocs.text(), "public property-keyword with get-and-set SET")
  }

  @Test
  function publicPropertyKeywordBasedPropertyWithCommentOnGetAndSetFavorsGetInDocs() {
    var docs = gosuDocForType( PropertiesClass )
    var doc = Jsoup.parse( docs.read() )
    var propertyDocs = doc.findPropertyList( PropertiesClass#PublicPropertyKeywordBasedPropWithGetAndSetCommentOnGetAndSet )
    Assert.assertNotNull( propertyDocs )
    Assert.assertContains( propertyDocs.text(), "public property-keyword with get-and-set GET")
  }

  @Test
  function publicPropertyKeywordBasedPropertyWithSetOnlyHasComment() {
    var docs = gosuDocForType( PropertiesClass )
    var doc = Jsoup.parse( docs.read() )
    var propertyDocs = doc.findPropertyList( PropertiesClass#PublicPropertyKeywordBasedPropWithSetCommentOnSet )
    Assert.assertNotNull( propertyDocs )
    Assert.assertContains( propertyDocs.text(), "public property-keyword with get-and-set SET")
  }

  @Test
  function privatePropertyIsNotInDocs() {
    var docs = gosuDocForType( MethodsClass )
    var doc = Jsoup.parse(docs.read())
    var propertyDocs = doc.findPropertyList( PropertiesClass#PrivateProperty )
    Assert.assertNull( propertyDocs )
  }


}