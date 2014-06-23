/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.framework.generator;

public class EnhancementGenerator implements ResourceGenerator {
  private var _packageName: String;
  private var _className: String;
  private var _enhancedType: String;

  construct(packageName: String, className: String, enhancedType: String) {
    this._packageName = packageName
    this._className = className
    this._enhancedType = enhancedType;
  }

  function generate(body: String): GosuEnhancementFile {
    return generateClass("", body)
  }

  function generateClass(usesList: String, body: String): GosuEnhancementFile {
    return new GosuEnhancementFile(usesList, _packageName, _className, _enhancedType, body)
  }

}
