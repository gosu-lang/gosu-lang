/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.actions;

import com.intellij.ide.actions.ElementCreator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import gw.plugin.ij.util.GosuBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class CreateSimpleDialog extends DialogWrapper {
  private JTextField _fieldName;
  @Nullable
  private ElementCreator myCreator;
  private String templateName;

  public CreateSimpleDialog(@NotNull Project project) {

    super(project, true);
    init();
  }

  @Override
  protected JComponent createCenterPanel() {
    JPanel panel = new JPanel( new GridBagLayout() );
    panel.setBorder( BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
    GridBagConstraints c = new GridBagConstraints();

    int iY = 0;

    JLabel labelName = new JLabel(GosuBundle.message("new.dlg.name"));
    _fieldName = new IdentifierTextField();

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 2, 2, 0, 0 );
    panel.add( labelName, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = 1;
    c.insets = new Insets( 2, 2, 0, 0 );
    panel.add( _fieldName, c );

    return panel;
  }

  @Override
  protected void doOKAction() {
    if (myCreator.tryCreate(getEnteredName()).length == 0) {
      return;
    }
    super.doOKAction();
  }

  @Override
  public JComponent getPreferredFocusedComponent() {
    return _fieldName;
  }

  public String getEnteredName() {
    return _fieldName.getText();
  }

  public void setTemplateName(String tn) {
    templateName = tn;
  }

  public String getTemplateName() {
    return templateName;
  }

  @NotNull
  public static <T extends PsiElement> Builder createDialog( @NotNull final Project project )
  {
    final CreateSimpleDialog dialog = new CreateSimpleDialog(project);
    return new BuilderImpl(dialog, project);
  }

  private static class BuilderImpl implements Builder {

    private final CreateSimpleDialog myDialog;
    private final Project myProject;

    public BuilderImpl(CreateSimpleDialog dialog, Project project) {
      myDialog = dialog;
      myProject = project;
    }

    @NotNull
    @Override
    public Builder setTitle(String title) {
      myDialog.setTitle(title);
      return this;
    }

    @Override
    public void setTemplateName(String templateName) {
      myDialog.setTemplateName(templateName);
    }

    @Override
    public String getTemplateName() {
      return myDialog.getTemplateName();
    }

    public <T extends PsiElement> T show(@NotNull String errorTitle,
                                         @NotNull final String templateName,
                                         @NotNull final FileCreator<T> creator) {
      final Ref<T> created = Ref.create(null);
      myDialog.myCreator = new ElementCreator(myProject, errorTitle) {
//        @Override
//        protected void checkBeforeCreate(String newName) throws IncorrectOperationException {
//          creator.checkBeforeCreate(newName, templateName);
//        }

        @Nullable
        @Override
        protected PsiElement[] create(@NotNull String newName) throws Exception {
          final T element = creator.createFile(newName, templateName);
          created.set(element);
          if (element != null) {
            return new PsiElement[]{element};
          }
          return PsiElement.EMPTY_ARRAY;
        }

        @NotNull
        @Override
        protected String getActionName(@NotNull String newName) {
          return creator.getActionName(newName, templateName);
        }
      };
      myDialog.show();
      if (myDialog.getExitCode() == OK_EXIT_CODE) {
        return created.get();
      }
      return null;
    }
  }

  public interface Builder {
    @NotNull
    Builder setTitle(String title);
    void setTemplateName(String templateName);
    String getTemplateName();

    @Nullable
    <T extends PsiElement> T show(@NotNull String errorTitle, @NotNull final String templateName, @NotNull final FileCreator<T> creator);
  }

  public interface FileCreator<T> {
    void checkBeforeCreate(@NotNull String name, @NotNull String templateName) throws IncorrectOperationException;

    @Nullable
    T createFile(@NotNull String name, @NotNull String templateName);

    @NotNull
    String getActionName(@NotNull String name, @NotNull String templateName);
  }

}
