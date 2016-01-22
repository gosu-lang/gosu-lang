package editor;

import editor.util.Project;
import gw.lang.parser.IScriptPartId;

import java.io.File;

/**
 * Copyright 2010 Guidewire Software, Inc.
 */
public interface IGosuEditor
{
  void showMe();

  void openInitialFile( IScriptPartId partId, File program );

  void openFile( File sourceFile );

  void restoreState( Project project );

  void reset();
}
