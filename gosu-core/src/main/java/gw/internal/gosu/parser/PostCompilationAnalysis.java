package gw.internal.gosu.parser;

import gw.lang.parser.AstProcessorProvider;
import gw.lang.parser.IParsedElement;
import gw.lang.reflect.gs.IGosuClass;

public class PostCompilationAnalysis
{
  /**
   * Perform post-compilation analysis/rewriting on the given ParsedElement e.g., metaprogramming, linters, etc.
   */
  public static void analyze( IParsedElement pe )
  {
    if( pe == null )
    {
      return;
    }

    // process the program is it exists, it covers the wrapped class
    IGosuClass gosuClass = pe.getGosuProgram();
    if( gosuClass == null )
    {
      gosuClass = pe.getGosuClass();
    }
    if( gosuClass != null )
    {
      for( AstProcessorProvider provider: gosuClass.getAstProcessorProviders() )
      {
        provider.getAstProcessor().process( pe );
      }
    }
  }
}
