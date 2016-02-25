package editor;

import javax.swing.text.AttributeSet;

/**
 * Copyright 2010 Guidewire Software, Inc.
 */
public interface IColorConsole
{

  public void setAttributes( AttributeSet attributes );

  public void suspendUpdates();

  public void resumeUpdates();

}
