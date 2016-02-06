/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.testgen;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Oct 29, 2009
 * Time: 10:07:09 AM
 * To change this template use File | Settings | File Templates.
 */
public interface DeclarationContext {
  List<? extends Member> getMembers();

  String testClassPrefix();

  String typeName();

  String memberSuffix();

  int memberIntSuffix();

  boolean isInterface();
}
