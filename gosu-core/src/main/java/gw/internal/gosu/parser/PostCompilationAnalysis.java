package gw.internal.gosu.parser;

import gw.lang.parser.IParsedElement;

public class PostCompilationAnalysis
{
  /**
   * Perform post-compilation analysis/rewriting on the given ParsedElement e.g., metaprogramming, linters, etc.
   */
  public static void analyze( IParsedElement pe )
  {
    RestrictionsChecker.instance().enforceRestrictions( pe );

    //todo: other analysis/metaprogramming tasks can be added dynamically via SPI
  }
}
