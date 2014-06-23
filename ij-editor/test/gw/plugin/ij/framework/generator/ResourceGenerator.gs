/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.framework.generator;

public interface ResourceGenerator {

function generate(body: String): GosuTestingResource

function generateClass(usesList: String, body: String): GosuTestingResource

}
