/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.actions;

import com.intellij.ide.util.ClassFilter;
import com.intellij.ide.util.TreeJavaClassChooserDialog;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import gw.plugin.ij.lang.psi.impl.GosuProgramFileImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuClassDefinitionImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 */
public class ConfigJarDialog extends TreeJavaClassChooserDialog {
  private JCheckBox _cbBundleGosu;
  @Nullable
  private String _strProgramName;
  private boolean _bBundleGosu;

  public ConfigJarDialog(@NotNull Project project) {
    super("Create Executable Jar for Distribution",
          project, GlobalSearchScope.projectScope( project ),
          new ProgramClassFilter(), null );
  }

  public boolean isBundleGosu() {
    return _bBundleGosu;
  }

  @Nullable
  public String getProgramName() {
    return _strProgramName;
  }

  @Override
  protected JComponent createCenterPanel() {
    final JPanel panel = new JPanel(new GridBagLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

    final GridBagConstraints c = new GridBagConstraints();

    int iY = 0;

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets(2, 2, 8, 0);
    panel.add( new JLabel( "<html>The resulting <b>" + getProject().getName() + ".jar</b> file will be placed in the project's root folder" ), c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets(2, 2, 0, 0);
    panel.add( _cbBundleGosu = new JCheckBox( "Bundle Gosu runtime" ), c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets(2, 2, 0, 0);
    panel.add(new JLabel("Program to run:"), c);

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.BOTH;
    c.gridx = 0;
    c.gridy = iY;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 1;
    c.insets = new Insets(2, 2, 0, 0);
    panel.add(super.createCenterPanel(), c);

    return panel;
  }

  @Override
  protected void doOKAction() {
    PsiClass psiClass = super.calcSelectedClass();
    _strProgramName = psiClass == null ? null : psiClass.getQualifiedName();
    _bBundleGosu = _cbBundleGosu.isSelected();
    if( getProgramName() != null ) {
      super.doOKAction();
    }
  }

  private static class ProgramClassFilter implements ClassFilter {
    public boolean isAccepted( @NotNull PsiClass psiClass ) {
      return psiClass instanceof GosuClassDefinitionImpl &&
             psiClass.getContainingFile() instanceof GosuProgramFileImpl;
    }
  }
}

