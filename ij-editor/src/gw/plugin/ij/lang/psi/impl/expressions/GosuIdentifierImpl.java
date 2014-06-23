/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.expressions;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaToken;
import com.intellij.psi.PsiMember;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.codeStyle.CodeEditUtil;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import gw.plugin.ij.lang.GosuTokenTypes;
import gw.plugin.ij.lang.psi.IGosuFile;
import gw.plugin.ij.lang.psi.api.expressions.IGosuIdentifier;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuTypeDefinition;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.statements.GosuFieldImpl;
import gw.plugin.ij.lang.psi.impl.statements.GosuVariableImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.members.GosuMethodBaseImpl;
import gw.plugin.ij.lang.psi.util.GosuPsiParseUtil;
import gw.plugin.ij.util.InjectedElementEditor;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GosuIdentifierImpl extends LeafPsiElement implements PsiJavaToken, IGosuIdentifier {
  public GosuIdentifierImpl(IElementType tokType, CharSequence text) {
    super(tokType, text);
  }

  @NotNull
  public IElementType getTokenType() {
    return GosuTokenTypes.TT_IDENTIFIER;
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitIdentifier(this);
    }
    else {
      visitor.visitElement( this );
    }
  }

  public PsiReference getReference() {
    if (InjectedElementEditor.isInEmbeddedEditor(this) && (
            getParent() instanceof GosuFieldImpl ||
                    getParent() instanceof GosuVariableImpl ||
                    getParent() instanceof GosuMethodBaseImpl)) {
      final PsiElement originalElement = InjectedElementEditor.getOriginalElement(this);
      if (originalElement != null) {
        return new FakeReference(this, originalElement.getParent());
      }
    }
    return null;
  }

  @Override
  public PsiElement replace(@NotNull PsiElement newElement) throws IncorrectOperationException {
    if (InjectedElementEditor.isInEmbeddedEditor(this)) {
      return InjectedElementEditor.getOriginalElement(this).replace(newElement);
    }
    PsiElement result = super.replace(newElement);

    // We want to reformat method parameters on method name change as well because there is a possible situation that they are aligned
    // and method name change breaks the alignment.
    // Example:
    //     public void test(int i,
    //                      int j) {}
    // Suppose we're renaming the method to test123. We get the following if parameter list is not reformatted:
    //     public void test123(int i,
    //                     int j) {}
    PsiElement methodCandidate = result.getParent();
    if (methodCandidate instanceof PsiMethod) {
      PsiMethod method = (PsiMethod) methodCandidate;
      CodeEditUtil.markToReformat(method.getParameterList().getNode(), true);
    }

    return result;
  }

  @NotNull
  public String toString() {
    return "PsiIdentifier: " + getText();
  }

  @Override
  public String getName() {
    return getText();
  }

  @Nullable
  public PsiElement setName(@NonNls @NotNull String newName) throws IncorrectOperationException {
    PsiFile fileToRename = getFileToRenameIfRenamingType();
    PsiElement me = renameJustMe(newName);
    if (fileToRename != null) {
      renameMyTypesFile(newName, fileToRename);
    }
    return me;
  }

  private void renameMyTypesFile(String newName, @NotNull PsiFile fileToRename) {
    String fileName = fileToRename.getName();
    int dotIndex = fileName.lastIndexOf('.');
    fileToRename.setName(dotIndex >= 0 ? newName + "." + fileName.substring(dotIndex + 1) : newName);
  }

  @Nullable
  private PsiElement renameJustMe(String name) {
    final PsiElement element = GosuPsiParseUtil.parseExpression(name, getManager());
    final PsiElement child = element.getFirstChild();
    replace(child);
    return child;
  }

  @Nullable
  private PsiFile getFileToRenameIfRenamingType() {
    final PsiElement parent = getParent();
    if (parent instanceof IGosuTypeDefinition) {
      final PsiFile psiFile = parent.getContainingFile();
      if (psiFile instanceof IGosuFile) {
        final String name = getName();
        final VirtualFile file = psiFile.getVirtualFile();
        if (file != null && name != null && name.equals(file.getNameWithoutExtension())) {
          return psiFile;
        }
      }
    }
    return null;
  }
}
