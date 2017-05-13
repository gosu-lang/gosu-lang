/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.reflect.gs.IGosuClassParser;
import gw.lang.reflect.IScriptabilityModifier;
import manifold.api.service.IService;

public interface IGosuParserFactory extends IService
{
  IGosuParser createParser( String strSource, ISymbolTable symTable, IScriptabilityModifier scriptabilityConstraint, ITypeUsesMap tuMap );

  IGosuParser createParser( String strSource, ISymbolTable symTable, IScriptabilityModifier scriptabilityConstraint );

  /**
   * Creates an IGosuParser appropriate for parsing and executing Gosu.
   *
   * @param symTable          The symbol table the parser uses to parse and execute script.
   *
   * @return A parser appropriate for parsing Gosu source.
   */
  IGosuParser createParser( ISymbolTable symTable, IScriptabilityModifier scriptabilityConstraint );

  /**
   * Creates an IGosuParser appropriate for parsing and executing Gosu.
   *
   * @param strSource The text of the the rule source
   * @param symTable  The symbol table the parser uses to parse and execute the rule
   *
   * @return A parser appropriate for parsing Gosu source.
   */
  IGosuParser createParser( String strSource, ISymbolTable symTable );

  IGosuParser createParser( String strSource );

  IGosuClassParser createClassParser( IGosuParser parser );

  IGosuProgramParser createProgramParser();

  IGosuFragmentParser createFragmentParser();
}
