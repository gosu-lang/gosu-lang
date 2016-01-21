package editor.search;

import editor.IScriptEditor;


/**
 */
public class StandardLocalSearch
{
  public static void performLocalSearch( IScriptEditor editorHost, boolean bReplaceMode )
  {
    SearchLocalDialog2 dlg = new SearchLocalDialog2( editorHost, bReplaceMode );
    dlg.show();
  }

  public static boolean canRepeatFind( IScriptEditor editorHost )
  {
    if( editorHost == null )
    {
      return false;
    }
    return SearchLocalDialog2.canRepeatFind();
  }

  public static void repeatFind( IScriptEditor editorHost )
  {
    if( editorHost == null )
    {
      return;
    }
    SearchLocalDialog2 dlg = new SearchLocalDialog2( editorHost, "", SearchLocalDialog2.g_bReplaceMode );
    dlg.repeatFind();
  }

  public static void repeatFindBackwards( IScriptEditor editorHost )
  {
    if( editorHost == null )
    {
      return;
    }
    SearchLocalDialog2 dlg = new SearchLocalDialog2( editorHost, "", SearchLocalDialog2.g_bReplaceMode );
    dlg.repeatFindBackwards();
  }
}
