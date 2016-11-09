package editor.plugin.typeloader.java;

import javax.swing.text.Document;
import javax.swing.text.StyledEditorKit;

public class JavaEditorKit extends StyledEditorKit
{
  @Override
  public Document createDefaultDocument()
  {
    return new JavaDocument();
  }
}