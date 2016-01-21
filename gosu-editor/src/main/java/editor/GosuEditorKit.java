/*
 *
 *  Copyright 2010 Guidewire Software, Inc.
 *
 */
package editor;

import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;

/**
 * A simple EditorKit for editing GosuDocuments.
 */
public class GosuEditorKit extends DefaultEditorKit
{
  private static GosuStyleContext g_defPreferences;
  private GosuStyleContext _preferences;

  /**
   * @return The GosuStyleContext controlling the visual preferences.
   */
  public static GosuStyleContext getStylePreferences()
  {
    if( g_defPreferences == null )
    {
      g_defPreferences = new GosuStyleContext();
    }
    return g_defPreferences;
  }

  //------------------------------------------------------------------------------
  //-- EditorKit methods --

  /**
   * Get the MIME type of the data that this
   * kit represents support for.  This kit supports
   * the type <code>text/gosu</code>.
   */
  public String getContentType()
  {
    return "text/gosu";
  }

  /**
   * Creates an uninitialized text storage model that is appropriate for this
   * type of editor.
   *
   * @return The document model
   */
  public Document createDefaultDocument()
  {
    return new GosuDocument();
  }

  /**
   * @return The view factory
   */
  public final GosuStyleContext getViewFactory()
  {
    if( _preferences != null )
    {
      return _preferences;
    }
    return getStylePreferences();
  }

  public void setStylePreferences( GosuStyleContext preferences )
  {
    _preferences = preferences;
  }
}







