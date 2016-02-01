package editor;

import gw.lang.parser.IGosuParser;
import gw.lang.parser.IScriptPartId;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.reflect.IType;

import javax.swing.*;
import java.io.IOException;

/**
 * Copyright 2010 Guidewire Software, Inc.
 */
public interface IGosuPanel
{
  JComponent asJComponent();

  void read( IScriptPartId partId, String strSource, String strDescription ) throws IOException;

  void parse();

  String getText();

  ISymbolTable getSymbolTable();

  IGosuParser getParser();

  void setTypeUsesMap( ITypeUsesMap typeUsesMap );

  void setProgramSuperType( IType baseClass );
}
