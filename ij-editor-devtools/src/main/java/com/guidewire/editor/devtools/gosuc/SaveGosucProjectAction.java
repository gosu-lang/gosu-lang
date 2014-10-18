package com.guidewire.editor.devtools.gosuc;

import com.intellij.openapi.actionSystem.AnActionEvent;
import gw.lang.gosuc.GosucProject;
import gw.lang.reflect.TypeSystem;
import gw.plugin.ij.actions.TypeSystemAwareAction;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveGosucProjectAction extends TypeSystemAwareAction {
  @Override
  public void actionPerformed(AnActionEvent e) {
    JFileChooser fc = new JFileChooser();
    int returnVal = fc.showSaveDialog(null);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File file = fc.getSelectedFile();
      Class prjClass;
      try {
        prjClass = Class.forName( "com.guidewire.pl.gosuc.PlGosucProject" );
      }
      catch( Exception e1 ) {
        prjClass = GosucProject.class;
      }
      String projectContent = TypeSystem.getExecutionEnvironment().makeGosucProjectFile( prjClass.getName() );
      try {
        final FileWriter writer = new FileWriter(file);
        writer.write(projectContent, 0, projectContent.length());
        writer.close();
      } catch (IOException e1) {
        throw new RuntimeException(e1);
      }
    }
  }
}

