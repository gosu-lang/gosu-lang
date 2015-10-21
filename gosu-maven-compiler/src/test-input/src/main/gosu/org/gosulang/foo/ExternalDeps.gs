package org.gosulang.foo

uses org.apache.commons.lang.StringUtils

class ExternalDeps {

  function hello(str : String) {
    print(StringUtils.upperCase(str))
  }

}