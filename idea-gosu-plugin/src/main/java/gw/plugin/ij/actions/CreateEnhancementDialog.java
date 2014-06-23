/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.actions;

import com.intellij.ide.actions.ElementCreator;
import com.intellij.ide.util.TreeJavaClassChooserDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.IncorrectOperationException;
import gw.plugin.ij.util.GosuBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class CreateEnhancementDialog extends TreeJavaClassChooserDialog {
  static final String ENHANCEMENT_TEMPLATE_NAME = GosuTemplatesFactory.GOSU_ENHANCEMENT_TEMPLATE;
  private JTextField _fieldName;

  @Nullable
  private ElementCreator myCreator;

  public CreateEnhancementDialog(@NotNull Project project) {
    super( "Create Enhancement", project, GlobalSearchScope.allScope( project ), null, null );
  }

  public String getEnteredName() {
    return _fieldName.getText();
  }

  @Nullable
  public String getEnhancedClassName() {
    PsiClass psiClass = super.calcSelectedClass();
    return psiClass == null ? null : psiClass.getQualifiedName();
  }

  public String getTemplateName() {
    return ENHANCEMENT_TEMPLATE_NAME;
  }

  @Override
  protected JComponent createCenterPanel() {
    final JPanel panel = new JPanel(new GridBagLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

    final GridBagConstraints c = new GridBagConstraints();

    int iY = 0;

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY++;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets(2, 2, 0, 0);
    panel.add(new JLabel(GosuBundle.message("new.dlg.name")), c);

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets(2, 2, 0, 0);
    panel.add(_fieldName = new IdentifierTextField(), c);

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 0;
    c.insets = new Insets(2, 2, 0, 0);
    panel.add(new JLabel(GosuBundle.message("new.enhancement.dlg.enhancedType")), c);

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
    if (myCreator.tryCreate(getEnteredName()).length == 0) {
      if (getTabbedPane().getSelectedIndex() == 0) {
        // Workaround: AbstractTreeClassChooserDialog isn't intended to be stopped here, so GotoByNamePanel is
        // already deactivated at this point. We have to restore it (and there is no way to set previous selection)
        getGotoByNamePanel().rebuildList(false);
      }
      return;
    }
    super.doOKAction();
  }

  @Override
  public JComponent getPreferredFocusedComponent() {
    return _fieldName;
  }

  @NotNull
  public static <T extends PsiElement> Builder createDialog(@NotNull final Project project) {
    final CreateEnhancementDialog dialog = new CreateEnhancementDialog(project);
    return new BuilderImpl(dialog, project);
  }

  public interface Builder {
    @NotNull
    Builder setTitle(String title);

    @Nullable
    <T extends PsiElement> T show(@NotNull String errorTitle, @NotNull FileCreator<T> creator);
  }

  private static class BuilderImpl implements Builder {
    private final CreateEnhancementDialog myDialog;
    private final Project myProject;

    public BuilderImpl(CreateEnhancementDialog dialog, Project project) {
      myDialog = dialog;
      myProject = project;
    }

    @NotNull
    @Override
    public Builder setTitle(String title) {
      myDialog.setTitle(title);
      return this;
    }

    public <T extends PsiElement> T show(@NotNull String errorTitle,
                                         @NotNull final FileCreator<T> creator) {
      final Ref<T> created = Ref.create(null);
      myDialog.myCreator = new ElementCreator(myProject, errorTitle) {
        @Nullable
        @Override
        protected PsiElement[] create(@NotNull String newName) throws Exception {
          final T element = creator.createFile(newName, myDialog.getEnhancedClassName(), myDialog.getTemplateName());
          created.set(element);
          if (element != null) {
            return new PsiElement[]{element};
          }
          return PsiElement.EMPTY_ARRAY;
        }

        @NotNull
        @Override
        protected String getActionName(@NotNull String newName) {
          return creator.getActionName(newName, myDialog.getTemplateName());
        }
      };
      myDialog.show();
      if (myDialog.getExitCode() == OK_EXIT_CODE) {
        return created.get();
      }
      return null;
    }
  }

  public interface FileCreator<T> {
    void checkBeforeCreate(@NotNull String name, @NotNull String enhancedClassName, @NotNull String templateName) throws IncorrectOperationException;

    @Nullable
    T createFile(@NotNull String name, @NotNull String enhancedClassName, @NotNull String templateName);

    @NotNull
    String getActionName(@NotNull String name, @NotNull String templateName);
  }
}
