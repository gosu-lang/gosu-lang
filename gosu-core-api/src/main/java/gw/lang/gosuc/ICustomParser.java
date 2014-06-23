/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.gosuc;

import java.util.List;

/**
 * Implementers of this interface must provide a default, no argument constructor
 * that is invokable from a command line build.
 */
public interface ICustomParser {
  GosucProject createProject( String name, GosucSdk sdk, List<GosucModule> modules, List<String> globalLoaders );
  void parse( GosucProjectParser parser );
}
