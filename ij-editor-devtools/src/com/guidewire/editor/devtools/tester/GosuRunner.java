/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package com.guidewire.editor.devtools.tester;

import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.IGosuProgramParser;
import gw.lang.parser.IParseResult;
import gw.lang.parser.ParserOptions;
import gw.lang.parser.StandardSymbolTable;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.module.IModule;

import java.io.PrintStream;

public class GosuRunner {

  private static final GosuRunner instance = new GosuRunner();

  public static GosuRunner getInstance() {
    return instance;
  }

  /**
   * @param module   the module to push to TypeSystem (guaranteed to be popped at the end of the method)
   * @param gosuText the Gosu script to execute (text)
   * @param stream   provide a PrintStream if you want to change default System.out and System.err streams.
   *                 if NULL, system.out and system.err are NOT changed.
   */
  public void runScript(IModule module, String gosuText, PrintStream stream) {
    PrintStream savedSysout = System.out;
    PrintStream savedSyserr = System.err;

    TypeSystem.pushModule(module);
    try {
      if (stream != null) {
        System.setOut(stream);
        System.setErr(stream);
      }
      eval(gosuText);
    } finally {
      if (stream != null) {
        System.setOut(savedSysout);
        System.setErr(savedSyserr);
      }
      TypeSystem.popModule(module);
    }
  }

  private static void eval(String script) {
    TypeSystem.pushGlobalModule();
    try {
      final IGosuProgramParser parser = GosuParserFactory.createProgramParser();
      final IParseResult result = parser.parseExpressionOrProgram(script, new StandardSymbolTable(true), new ParserOptions());
      final IGosuProgram program = result.getProgram();

      final Object output = program.evaluate(null);
      if (output != null) {
        System.out.println(output);
      }
    } catch (ParseResultsException e) {
      System.err.println("Parse error: " + e.getMessage());
    } finally {
      TypeSystem.popGlobalModule();
    }
  }
}
