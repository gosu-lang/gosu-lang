/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.fragments.GosuFragment;
import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.IExpression;
import gw.lang.parser.IFileContext;
import gw.lang.parser.IFunctionSymbol;
import gw.lang.parser.IGosuFragmentParser;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ParserOptions;
import gw.lang.parser.ScriptPartId;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.FragmentCache;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuFragment;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class GosuFragmentParser implements IGosuFragmentParser {

  private static final GosuFragmentParser _instance = new GosuFragmentParser();
  private static AtomicInteger _fragmentCount = new AtomicInteger();
  public static GosuFragmentParser getInstance() {
    return _instance;
  }

  private GosuFragmentParser() { }

  public IGosuFragment parseExpressionOnly( String script, ISymbolTable table, ParserOptions options) throws ParseResultsException {
    return parseImpl( script, table, options, determineName( options.getFileContext() ), determineExternalSymbols( table, options ), true );
  }

  public IGosuFragment parseProgramOnly( String script, ISymbolTable table, ParserOptions options) throws ParseResultsException {
    return parseImpl( script, table, options, determineName( options.getFileContext() ), determineExternalSymbols( table, options ), false );
  }

  public IGosuFragment parseExpressionOrProgram(String script, ISymbolTable table, ParserOptions options) throws ParseResultsException {
    String name = determineName(options.getFileContext());
    HashMap<String, ISymbol> externalSymbolNames = determineExternalSymbols( table, options );
    try {
      return parseImpl( script, table, options, name, externalSymbolNames, true );
    } catch (ParseResultsException pe) {
      try {
        return parseImpl( script, table, options, name, externalSymbolNames, false );
      } catch (ParseResultsException peProg) {
        throw pe;
      }
    }
  }

  private IGosuFragment parseImpl( String script, ISymbolTable table, ParserOptions options, String name, HashMap<String, ISymbol> externalSymbols, boolean parseExpression ) throws ParseResultsException {
    IGosuParser parser = GosuParserFactory.createParser( script );
    parser.putDfsDeclsInTable( table );
    options.setParserOptions( parser );

    GosuFragment fragment = new GosuFragment( name, externalSymbols, options.getTypeUsesMap() );
    FragmentCache.instance().addFragment(fragment);
    TypeSystem.pushSymTableCtx(table);
    try{
      parser.setSymbolTable( TypeSystem.getCompiledGosuClassSymbolTable() ); // Set up the symbol table
      // Create the fragment and put it in the map so that references during parsing can find it

      IExpression result;
      CompiledGosuClassSymbolTable.instance().pushCompileTimeSymbolTable( fragment, table );
      try {
        if (parseExpression) {
          result = parser.parseExp( new ScriptPartId(fragment, null), options.getExpectedType(), options.getFileContext(), false );
        } else {
          
          result = parser.parseProgram( new ScriptPartId(fragment, null), true, true, options.getExpectedType(), options.getFileContext(), false );
        }
      } finally {
        CompiledGosuClassSymbolTable.instance().popCompileTimeSymbolTable( fragment );
      }
      fragment.setExpression( result );     
      return fragment;
    } catch (ParseResultsException e) {
      if (e.hasOnlyParseWarnings()) {
        IExpression result = (IExpression) e.getParsedElement();
        fragment.setExpression(result);
        return fragment;
      } else {
        throw e;
      }
    } finally {
      TypeSystem.popSymTableCtx();
    }
  }

  private String determineName( IFileContext fileContext ) {
    String name = fileContext == null ? null : fileContext.getClassName();
    if( name == null )
    {
      name = GosuFragment.FRAGMENT_NAME_PREFIX + _fragmentCount.getAndIncrement();
    }
    if( fileContext != null && fileContext.getContextString() != null)
    {
      name += "_" + fileContext.getContextString();
    }
    return name;
  }

  private HashMap<String, ISymbol> determineExternalSymbols( ISymbolTable symbolTable, ParserOptions options ) {
    Map symbols = symbolTable.getSymbols();
    if( symbols == null && options.getAdditionalDFSDecls() == null && options.getDeclSymbols() == null)
    {
      return new HashMap<String, ISymbol>( 0 );
    }

    HashMap<String, ISymbol> symbolNames = new HashMap<String, ISymbol>( 8 );
    if (symbols != null) {
      //noinspection unchecked
      for (ISymbol sym : (Collection<ISymbol>) symbols.values()) {
        if (!(sym instanceof CommonSymbolsScope.LockedDownSymbol) && sym != null) {
          symbolNames.put( (String)sym.getName(), sym);
        }
      }
    }

    ISymbolTable decls = options.getAdditionalDFSDecls();
    if (decls != null) {
      //noinspection unchecked
      for (ISymbol sym : (Collection<ISymbol>) decls.getSymbols().values()) {
        if (!(sym instanceof CommonSymbolsScope.LockedDownSymbol) && sym != null) {
          symbolNames.put( (String)sym.getName(), sym);
        }
      }
    }

    Map<String, Set<IFunctionSymbol>> declSymbolMap = options.getDeclSymbols();
    if (declSymbolMap != null) {
      for (Set<IFunctionSymbol> symbolSet : declSymbolMap.values()) {
        for (IFunctionSymbol sym : symbolSet) {
          if (!(sym instanceof CommonSymbolsScope.LockedDownSymbol) && sym != null) {
            symbolNames.put( (String)sym.getName(), sym);
          }
        }
      }
    }

    return symbolNames;
  }

}
