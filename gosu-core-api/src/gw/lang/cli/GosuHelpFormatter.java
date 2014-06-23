/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.cli;

import gw.lang.UnstableAPI;
import gw.internal.ext.org.apache.commons.cli.HelpFormatter;
import gw.internal.ext.org.apache.commons.cli.Options;
import gw.internal.ext.org.apache.commons.cli.Option;
import gw.internal.ext.org.apache.commons.cli.OptionGroup;

@UnstableAPI
public class GosuHelpFormatter extends HelpFormatter {

  @Override
  protected StringBuffer renderOptions(StringBuffer sb, int width, Options options, int leftPad, int descPad) {
    return super.renderOptions( sb, width, filterHiddenOptions( options ), leftPad, descPad );
  }

  /**
   * Iterates over the list of {@link Option}s and adds each Option that is not marked with @Hidden.
   * <br>
   * Note that this makes no effort to exactly recreate the {@link Options} object that is passed in, with respect to
   * {@link OptionGroup}s, etc, because the implementation of <code>HelpFormatter.renderOptions(...)</code> does not
   * require them. 
   *
   * @param options the original {@link Options} list
   * @return a new {@link Options} list with all the hidden options removed
   */
  Options filterHiddenOptions( final Options options ) {
    Options newOptions = new Options();

    for ( Object opt : options.getOptions() ) {
      if ( opt instanceof GosuOption ) {
        GosuOption gOpt = (GosuOption) opt;
        if ( gOpt.isHidden() ) {
          continue;  // Don't add hidden options
        }
      }

      newOptions.addOption((Option) opt);
    }

    return newOptions;
  }
}