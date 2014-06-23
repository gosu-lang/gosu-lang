/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.typeinfo;

import com.intellij.icons.AllIcons;
import com.intellij.ide.DataManager;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.impl.EditorComponentImpl;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.PsiManagerImpl;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.popup.NotLookupOrSearchCondition;
import com.intellij.ui.popup.PopupPositionManager;
import com.intellij.xml.util.XmlStringUtil;
import gw.lang.parser.IExpression;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.expressions.INameInDeclaration;
import gw.lang.parser.expressions.IVarStatement;
import gw.lang.parser.statements.IClassDeclaration;
import gw.lang.parser.statements.IFunctionStatement;
import gw.lang.reflect.IType;
import gw.plugin.ij.actions.TypeSystemAwareAction;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.util.GosuBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class TypeInfoAction extends TypeSystemAwareAction implements DumbAware {
  private static final Logger LOG = Logger.getInstance(TypeInfoAction.class);

  public TypeInfoAction() {
    super(GosuBundle.message("typeinfo.name"), GosuBundle.message("typeinfo.description"), null);
  }

  public void actionPerformed(@NotNull AnActionEvent e) {
    final Object data = e.getDataContext().getData(PlatformDataKeys.CONTEXT_COMPONENT.getName());
    if (!(data instanceof EditorComponentImpl)) {
      return;
    }
    final EditorComponentImpl editorComponent = (EditorComponentImpl) data;
    final EditorImpl editor = editorComponent.getEditor();
    final Project project = editor.getProject();
    final int offset = editor.getCaretModel().getOffset();

    final VirtualFile virtualFile = editor.getVirtualFile();
    final PsiFile file = PsiManagerImpl.getInstance(project).findFile(virtualFile);
    final PsiElement element = file.findElementAt(offset);

    if (!showTooltip(editor, element, offset)) {
      final PsiElement injectedElement = InjectedLanguageManager.getInstance(project).findInjectedElementAt(file, offset);
      if (injectedElement != null) {
        showTooltip(editor, injectedElement, offset);
      }
    }
  }

  protected boolean showTooltip(EditorImpl editor, PsiElement element, int offset) {
    final IGosuPsiElement impl = PsiTreeUtil.getParentOfType(element, IGosuPsiElement.class);
    if (impl != null) {
      final IParsedElement parsedElement = impl.getParsedElement();
      if (parsedElement != null) {
        final IType type = unwrapMetaType(getType(parsedElement));
        if (type != null) {
          showInfoTooltip(type.getName(), editor);
          return true;
        }
      }
    }
    return false;
  }

  protected IType unwrapMetaType(IType type) {
    return type; //type instanceof IMetaType ? ((IMetaType) type).getType() : type;
  }

  @Nullable
  protected IType getType(@NotNull IParsedElement element) {
    if (element instanceof IClassDeclaration) {
      return ((IClassDeclaration) element).getGSClass();
    } else if (element instanceof IFunctionStatement) {
      return element.getReturnType();
    } else if (element instanceof INameInDeclaration) {
      return getType(element.getParent());
    } else if (element instanceof IExpression) {
      //TODO-dp delete these and have INameInDeclaration.getType() work
      return ((IExpression) element).getType();
    } else if (element instanceof IVarStatement) {
      return ((IVarStatement) element).getType();
    } else {
      LOG.warn("Case for " + element + " is not supported yet.");
      return null;
    }
  }

  private static class CopyAction extends AnAction {
    private String _typeName;

    CopyAction(String typeName) {
      super("Copy", "Copy", AllIcons.Actions.Copy);
      _typeName = typeName;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
      Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      clipboard.setContents(new StringSelection(_typeName), null);
    }
  }

  public static void showInfoTooltip(String typeName, @NotNull final Editor editor) {
    final String text = String.format("<html><b>%s</b></html>", XmlStringUtil.escapeString(typeName));
    Project project = editor.getProject();

    JBPanel panel = new JBPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
    panel.setBackground(Color.white);
    JBLabel textComponent = new JBLabel(text);
    panel.add(textComponent, BorderLayout.WEST);

    DefaultActionGroup actions = new DefaultActionGroup();
    actions.add(new CopyAction(typeName));
    JComponent component = ActionManager.getInstance().createActionToolbar(ActionPlaces.EDITOR_POPUP, actions, true).getComponent();
    component.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    component.setOpaque(false);
    panel.add(component, BorderLayout.EAST);

    final JBPopup hint = JBPopupFactory.getInstance().createComponentPopupBuilder(panel, null)
        .setRequestFocusCondition(project, NotLookupOrSearchCondition.INSTANCE)
        .setProject(project)
        .setMovable(true)
        .setCancelOnClickOutside(true)
        .setCouldPin(null)
        .setModalContext(false)
        .createPopup();

    final Component focusOwner = IdeFocusManager.getInstance(project).getFocusOwner();
    DataContext dataContext = DataManager.getInstance().getDataContext(focusOwner);
    PopupPositionManager.positionPopupInBestPosition(hint, editor, dataContext);
  }
}
