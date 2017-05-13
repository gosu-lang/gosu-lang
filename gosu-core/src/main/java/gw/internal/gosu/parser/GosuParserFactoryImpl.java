/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser;

import manifold.api.service.BaseService;
import manifold.internal.javac.JavaParser;
import manifold.internal.javac.IJavaParser;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.IGosuParserFactory;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.parser.ISymbolTable;
import gw.lang.reflect.gs.IGosuClassParser;
import gw.lang.parser.StandardSymbolTable;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.IGosuProgramParser;
import gw.lang.parser.IGosuFragmentParser;
import java.util.Collections;
import java.util.List;

/**
 * Defines a factory for constructing concrete IGosuParser implementations.
 */
public class GosuParserFactoryImpl extends BaseService implements IGosuParserFactory
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
  public IGosuParser createParser(
    String strSource, ISymbolTable symTable, IScriptabilityModifier scriptabilityConstraint )
  {
    IGosuParser parser = new GosuParser( symTable, scriptabilityConstraint );
    parser.setScript( strSource );
    return parser;
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
  public IGosuParser createParser(
    String strSource, ISymbolTable symTable, IScriptabilityModifier scriptabilityConstraint, ITypeUsesMap tuMap )
  {
    IGosuParser parser = new GosuParser( symTable, scriptabilityConstraint, tuMap );
    parser.setScript( strSource );
    return parser;
  }

  /**
   * Creates an IGosuParser appropriate for parsing and executing Gosu.
   *
   * @param symTable          The symbol table the parser uses to parse and execute script.
   * @return A parser appropriate for parsing Gosu source.
   */
  public IGosuParser createParser(
    ISymbolTable symTable, IScriptabilityModifier scriptabilityConstraint )
  {
    IGosuParser parser = new GosuParser( symTable, scriptabilityConstraint );
    return parser;
  }

  /**
   * Creates an IGosuParser appropriate for parsing and executing Gosu.
   *
   * @param strSource The text of the the rule source
   * @param symTable  The symbol table the parser uses to parse and execute the rule
   *
   * @return A parser appropriate for parsing Gosu source.
   */
  public IGosuParser createParser( String strSource, ISymbolTable symTable )
  {
    return createParser( strSource, symTable, null );
  }

  public IGosuParser createParser( String strSource )
  {
    return createParser( strSource, new StandardSymbolTable( true ), null );
  }

  public IGosuClassParser createClassParser( IGosuParser parser )
  {
    return new GosuClassParser( (GosuParser)parser );
  }

  public IGosuProgramParser createProgramParser()
  {
    return new GosuProgramParser();
  }

  @Override
  public IGosuFragmentParser createFragmentParser()
  {
    return GosuFragmentParser.getInstance();
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> List<T> getInterface( Class<T> apiInterface )
  {
    if( apiInterface == IJavaParser.class )
    {
      return Collections.singletonList( (T)JavaParser.instance() );
    }
    return super.getInterface( apiInterface );
  }
}
