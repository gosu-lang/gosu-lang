/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.expressions;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaToken;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.impl.source.codeStyle.CodeEditUtil;
import com.intellij.psi.impl.source.tree.CompositePsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import gw.plugin.ij.lang.GosuTokenTypes;
import gw.plugin.ij.lang.psi.IGosuFile;
import gw.plugin.ij.lang.psi.api.expressions.IGosuIdentifier;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuTypeDefinition;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LightGosuIdentifierImpl extends CompositePsiElement implements PsiJavaToken, PsiNamedElement, IGosuIdentifier {
  private CharSequence _text;

  public LightGosuIdentifierImpl(CharSequence text) {
    super(GosuTokenTypes.TT_IDENTIFIER);

    _text = text;
  }

  @NotNull
  @Override
  public CompositePsiElement clone() {
    return new LightGosuIdentifierImpl(_text.toString());
  }

  @NotNull
  public IElementType getTokenType() {
    return GosuTokenTypes.TT_IDENTIFIER;
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    visitor.visitElement(this);
  }

  @Override
  public PsiElement replace(@NotNull PsiElement newElement) throws IncorrectOperationException {
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
    return _text.toString();
  }

  @NotNull
  public PsiElement setName(@NonNls @NotNull String newName) throws IncorrectOperationException {
    PsiFile fileToRename = getFileToRenameIfRenamingType();
    PsiElement me = renameJustMe(newName);
    if (fileToRename != null) {
      renameMyTypesFile(newName, fileToRename);
    }
    return me;
  }

  @NotNull
  private PsiElement renameJustMe(String name) {
    LightGosuIdentifierImpl elem = (LightGosuIdentifierImpl) copy();
    elem._text = name;
    replace(elem);
    return elem;
  }

  private void renameMyTypesFile(String newName, @NotNull PsiFile fileToRename) {
    String fileName = fileToRename.getName();
    int dotIndex = fileName.lastIndexOf('.');
    fileToRename.setName(dotIndex >= 0 ? newName + "." + fileName.substring(dotIndex + 1) : newName);
  }

  @Nullable
  private PsiFile getFileToRenameIfRenamingType() {
    if (getParent() instanceof IGosuTypeDefinition) {
      IGosuTypeDefinition typeDef = (IGosuTypeDefinition) getParent();
      if (typeDef.getParent() instanceof PsiFile) {
        PsiFile file = typeDef.getContainingFile();
        if (!(file instanceof IGosuFile)) {
          return null;
        }

        final IGosuFile gosuFile = (IGosuFile) file;
        final String name = getName();
        final VirtualFile vFile = gosuFile.getVirtualFile();
        return vFile != null && name != null && name.equals(vFile.getNameWithoutExtension())
            ? file
            : null;
      }
    }
    return null;
  }
}
