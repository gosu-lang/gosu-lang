/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.gosuc;

import gw.lang.gosuc.GosucDependency;
import gw.lang.gosuc.GosucModule;
import gw.lang.gosuc.GosucProject;
import gw.lang.gosuc.GosucProjectParser;
import gw.lang.gosuc.GosucSdk;
import gw.test.TestClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 */
public class GosuProjectParserTest extends TestClass  {
  public void testCanReadAndWriteProject() {
    GosucProject project = new GosucProject( "yay", makeSdk(), makeModules(), Collections.<String>emptyList() );
    String out = project.write();
    GosucProject parsedProject = GosucProjectParser.parse( out, null );
    assertEquals( project, parsedProject );
    String outParsed = parsedProject.write();
    assertEquals( out, outParsed );
  }

  private GosucSdk makeSdk() {
    return new GosucSdk( Arrays.asList(
        "I/am/an/sdk/path.jar",
        "me/too/.jar"
      ),
      Collections.emptyList()
    );
  }

  private List<GosucModule> makeModules() {
    return Arrays.asList(
      makeModule( "mod_1", 2, 2, "mod1_out", Arrays.asList( "mod_2", "mod_4" ), 3 ),
      makeModule( "mod_2", 1, 2, "mod2_out", Arrays.asList( "mod_3" ), 2 ),
      makeModule( "mod_3", 2, 2, "mod3_out", Arrays.asList( "mod_4" ), 1 ),
      makeModule( "mod_4", 0, 0, "mod4_out", Collections.<String>emptyList(), 0 ) );
  }

  private GosucModule makeModule( String modName, int iSrcSize, int iCpSize, String outpath, List<String> deps, int iExclSize ) {
    List<String> sources = new ArrayList<String>();
    for( int i = 0; i < iSrcSize; i++ ) {
      sources.add( "/some/source/path/" + i + ".whatever" );
    }
    List<String> exclusions = new ArrayList<String>();
    for( int i = 0; i < iExclSize; i++ ) {
      sources.add( "/some/excluded/path/" + i + ".whatever" );
    }
    List<String> classpath = new ArrayList<String>();
    for( int i = 0; i < iCpSize; i++ ) {
      classpath.add( "/some/classpath/" + i + ".whatever" );
    }
    List<GosucDependency> dependencies = new ArrayList<GosucDependency>();
    boolean exported = true;
    for( String dep: deps ) {
      dependencies.add( new GosucDependency( dep, !exported ) );
    }
    return new GosucModule( modName, sources, classpath, Collections.emptyList(), outpath, dependencies, exclusions );
  }
}
