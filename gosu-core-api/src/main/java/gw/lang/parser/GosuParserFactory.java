/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.config.CommonServices;
import gw.lang.reflect.gs.IGosuClassParser;
import gw.lang.reflect.IScriptabilityModifier;

public class GosuParserFactory
{
  /**
   * Creates an IGosuParser appropriate for parsing and executing Gosu.
   *
   * @param strSource               The text of the the rule source
   * @param symTable                The symbol table the parser uses to parse and execute the rule
   * @param scriptabilityConstraint Specifies the types of methods/properties that are visible
   *
   * @return A parser appropriate for parsing Gosu source.
   */
  public static IGosuParser createParser( String strSource, ISymbolTable symTable, IScriptabilityModifier scriptabilityConstraint )
  {
    return CommonServices.getGosuParserFactory().createParser( strSource, symTable, scriptabilityConstraint );
  }

  /**
   * Creates an IGosuParser appropriate for parsing and executing Gosu.
   *
   * @param strSource               The text of the the rule source
   * @param symTable                The symbol table the parser uses to parse and execute the rule
   * @param scriptabilityConstraint Specifies the types of methods/properties that are visible
   *
   * @return A parser appropriate for parsing Gosu source.
   */
  public static IGosuParser createParser( String strSource, ISymbolTable symTable, IScriptabilityModifier scriptabilityConstraint, ITypeUsesMap tuMap)
  {
    return CommonServices.getGosuParserFactory().createParser( strSource, symTable, scriptabilityConstraint, tuMap );
  }

  /**
   * Creates an IGosuParser appropriate for parsing and executing Gosu.
   *
   * @param symTable          The symbol table the parser uses to parse and execute script.
   *
   * @return A parser appropriate for parsing Gosu source.
   */
  public static IGosuParser createParser( ISymbolTable symTable, IScriptabilityModifier scriptabilityConstraint )
  {
    return CommonServices.getGosuParserFactory().createParser( symTable, scriptabilityConstraint );
  }

  /**
   * Creates an IGosuParser appropriate for parsing and executing Gosu.
   *
   * @param strSource The text of the the rule source
   * @param symTable  The symbol table the parser uses to parse and execute the rule
   *
   * @return A parser appropriate for parsing Gosu source.
   */
  public static IGosuParser createParser( String strSource, ISymbolTable symTable )
  {
    return CommonServices.getGosuParserFactory().createParser( strSource, symTable );
  }

  public static IGosuParser createParser( String strSource )
  {
    return CommonServices.getGosuParserFactory().createParser( strSource );
  }

  public static IGosuClassParser createClassParser( IGosuParser parser )
  {
    return CommonServices.getGosuParserFactory().createClassParser( parser );
  }

  public static IGosuProgramParser createProgramParser()
  {
    return CommonServices.getGosuParserFactory().createProgramParser();
  }

  public static IGosuFragmentParser createFragmentParser() {
    return CommonServices.getGosuParserFactory().createFragmentParser();
  }
}
