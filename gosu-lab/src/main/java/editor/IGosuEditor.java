package editor;

import editor.util.Experiment;
import java.nio.file.Path;
import gw.lang.parser.IScriptPartId;

/**
 * Copyright 2010 Guidewire Software, Inc.
 */
public interface IGosuEditor
{
  void showMe();

  void openInitialFile( IScriptPartId partId, Path program );

  void openFile( Path sourceFile );

  void restoreState( Experiment experiment );

  void reset();

  GosuPanel getGosuPanel();
}
