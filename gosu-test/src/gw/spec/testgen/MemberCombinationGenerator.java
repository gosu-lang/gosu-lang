/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.testgen;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Nov 3, 2009
 * Time: 5:31:28 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MemberCombinationGenerator {
  public List<? extends Member> generateAllPossibleCombinations(DeclarationContext declContext);
}
