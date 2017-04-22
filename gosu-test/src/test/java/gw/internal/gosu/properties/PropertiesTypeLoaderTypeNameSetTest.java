/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.properties;

import gw.test.TestClass;

import java.util.Arrays;
import java.util.HashSet;

public class PropertiesTypeLoaderTypeNameSetTest extends TestClass {
  
  public void testFindsExactMatch() {
    PropertiesSourceProducer.TypeNameSet typeNameSet = createNameList( "alpha", "beta", "gamma");
    assertListEquals(Arrays.asList("alpha"), typeNameSet.findMatchesFor("alpha"));
  }

  public void testFindsCaseInsensitiveMatch() {
    PropertiesSourceProducer.TypeNameSet typeNameSet = createNameList( "alpha", "beta", "gamma");
    assertListEquals(Arrays.asList("beta"), typeNameSet.findMatchesFor("Beta"));
  }

  public void testFindsPrefixMatch() {
    PropertiesSourceProducer.TypeNameSet typeNameSet = createNameList( "gamma", "beta", "alpha");
    assertListEquals(Arrays.asList("alpha"), typeNameSet.findMatchesFor("alpha.beta"));
  }

  public void testFindsCaseInsensitivePrefixMatch() {
    PropertiesSourceProducer.TypeNameSet typeNameSet = createNameList( "alpha", "beta", "gamma");
    assertListEquals(Arrays.asList("beta"), typeNameSet.findMatchesFor("Beta.Gamma"));
  }

  public void testOnlyFindsPrefixMatchIfPeriodFollowsPrefixInFullName() {
    PropertiesSourceProducer.TypeNameSet typeNameSet = createNameList( "st", "sta", "start", "starts");
    assertListEquals(Arrays.asList("start"), typeNameSet.findMatchesFor("Start.Follow"));
  }

  public void testFindsMultipleMatches() {
    PropertiesSourceProducer.TypeNameSet typeNameSet = createNameList(
            "start.follow1.follow2", "start.follow1.foll",
            "st", "start",
            "start.fo", "start.follow1"
    );
    assertListEquals(
            Arrays.asList("start.follow1.follow2", "start.follow1", "start"),
            typeNameSet.findMatchesFor("Start.Follow1.Follow2")
    );
  }

  private PropertiesSourceProducer.TypeNameSet createNameList( String... values) {
    return new PropertiesSourceProducer.TypeNameSet( new HashSet<String>( Arrays.asList( values)));
  }
}
