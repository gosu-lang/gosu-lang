/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.cli;

import gw.test.TestClass;
import gw.internal.ext.org.apache.commons.cli.Options;
import gw.internal.ext.org.apache.commons.cli.Option;

/**
 * @author ctucker
 */
public class GosuHelpFormatterTest extends TestClass {

  public void testFilterHiddenOptionsReturnsEmptyOptionsListIfNoOptionsAreSpecified() {
    Options options = new Options();
    assertEquals("Expected initial Options to be of size 0", 0, options.getOptions().size());
    assertCollectionEquals( options.getOptions(), new GosuHelpFormatter().filterHiddenOptions(options).getOptions() );
  }

  public void testFilterHiddenOptionsReturnsMatchingOptionsListIfNotHiddenOptionsAreSpecified() {
    Options options = new Options();
    options.addOption( new Option( "foo", "foo option" ) );
    options.addOption( new GosuOption( "bar", "bar option" ) );

    assertEquals("Expected initial Options to be of size 2", 2, options.getOptions().size());
    assertCollectionEquals( options.getOptions(), new GosuHelpFormatter().filterHiddenOptions(options).getOptions() );
  }

  public void testFilterHiddenOptionsReturnsEmptyOptionsListIfOnlyHiddenOptionsAreSpecified() {
    Options options = new Options();
    GosuOption option = new GosuOption("bar", "bar option");
    option.setHidden( true );
    options.addOption(option);

    assertEquals("Expected initial Options to be of size 1", 1, options.getOptions().size());
    assertEquals("Expected filtered Options to be of size 0", 0, new GosuHelpFormatter().filterHiddenOptions(options).getOptions().size());
  }

  public void testFilterHiddenOptionsReturnsOptionsListWithoutHiddenOptions() {
    Options options = new Options();
    options.addOption( new Option( "foo", "foo option" ) );
    GosuOption option = new GosuOption("bar", "bar option");
    option.setHidden( true );
    options.addOption(option);

    assertEquals("Expected initial Options to be of size 2", 2, options.getOptions().size());
    assertEquals("Expected filtered Options to be of size 1", 1, new GosuHelpFormatter().filterHiddenOptions(options).getOptions().size());
  }
}
