/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.actions;

import com.intellij.ide.PasteProvider;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import gw.plugin.ij.icons.GosuIcons;
import gw.plugin.ij.util.GosuBundle;
import gw.plugin.ij.util.transform.java.JavaToGosu;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;


public class PasteAsGosuAction extends AnAction implements DumbAware {

  public PasteAsGosuAction() {
    super(GosuBundle.message("paste.java.as.gosu.action.text"), GosuBundle.message("paste.java.as.gosu.action.description"), GosuIcons.PASTE_AS_GOSU);
  }

  public void update(AnActionEvent e) {
    Presentation presentation = e.getPresentation();
    VirtualFile f = e.getData(PlatformDataKeys.VIRTUAL_FILE);
    if(f == null) {
      return;
    }
    String extension = f.getExtension();
    if(extension == null) {
      return;
    }
    String lowExt = extension.toLowerCase();
    boolean visible = ("gsp".equals(lowExt) || "gs".equals(lowExt) ||
                       "gst".equals(lowExt) || "gsx".equals(lowExt) ||
                       "gr".equals(lowExt));
    presentation.setEnabled(visible);
  }

  @Override
  public void actionPerformed(AnActionEvent e) {
    CopyPasteManager copyPasteManager = CopyPasteManager.getInstance();
    Transferable content = copyPasteManager.getContents();
    if(content == null) {
      return;
    }
    String text = null;
    try {
      text = (String) content.getTransferData(DataFlavor.stringFlavor);
    } catch (Exception ex) {
      // ignore;
    }
    if (text != null) {
      String GosuSource = JavaToGosu.ConvertString(text);
      if("".equals(GosuSource)) {
        Messages.showMessageDialog( e.getProject(), GosuBundle.message("paste.java.as.gosu.action.error"),
                                    GosuBundle.message("paste.java.as.gosu.action.text"), Messages.getErrorIcon() );
        return;
      }
      copyPasteManager.setContents(new StringSelection(GosuSource));
      DataContext dataContext = e.getDataContext();
      PasteProvider provider = PlatformDataKeys.PASTE_PROVIDER.getData(dataContext);
      if (provider != null && provider.isPasteEnabled(dataContext)) {
        provider.performPaste(dataContext);
      }
      copyPasteManager.setContents(new StringSelection(text));
    }
  }
}
