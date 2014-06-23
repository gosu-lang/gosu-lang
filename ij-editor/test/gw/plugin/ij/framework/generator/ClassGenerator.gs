/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.framework.generator;

public class ClassGenerator implements ResourceGenerator {
  private var packageName: String;
  private var className: String;

  construct(pkg: String, cn: String) {
    this.packageName = pkg
    this.className = cn
  }

  function generate(body: String): GosuClassFile {
    return generateClass("", body)
  }

  function generateClass(usesList: String, body: String): GosuClassFile {
    return new GosuClassFile(usesList, packageName, className, body);
  }

}
