/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.regression;

import java.util.Collection;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Jan 29, 2010
 * Time: 11:15:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class HasParameterizedMethod {

  protected <T> boolean areCollectionSizesEquals(Collection<T> expected, Collection<T> results) {
    return expected.size() == results.size();
  }
}
