package gw.gosudoc.util

uses org.junit.Assert

enhancement AssertEnhancement: org.junit.Assert{

  static function assertContains( value: String, strThatShouldBeInValue: String ){
    Assert.assertTrue( "'${strThatShouldBeInValue}' not found in '${value}'",
        value.contains( strThatShouldBeInValue ) )
  }

  static function assertDoesNotContain( value: String, strThatShouldNotBeInValue: String ){
    Assert.assertFalse( "'${strThatShouldNotBeInValue}' found in '${value}'",
        value.contains( strThatShouldNotBeInValue ) )
  }

}
