/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LanguageUtil;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.injected.InjectedFileViewProvider;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.IStubFileElementType;
import com.intellij.psi.tree.TokenSet;
import gw.plugin.ij.filetypes.GosuCodeFileType;
import gw.plugin.ij.lang.GosuElementType;
import gw.plugin.ij.lang.GosuLanguage;
import gw.plugin.ij.lang.GosuLexer;
import gw.plugin.ij.lang.GosuTokenSets;
import gw.plugin.ij.lang.GosuTokenTypes;
import gw.plugin.ij.lang.psi.impl.GosuProgramFileImpl;
import gw.plugin.ij.lang.psi.stubs.elements.GosuStubFileElementType;
import org.jetbrains.annotations.NotNull;

import static com.intellij.lang.ParserDefinition.SpaceRequirements.MUST_LINE_BREAK;

public class GosuCodeParserDefinition implements ParserDefinition {
  public static final IStubFileElementType GOSU_FILE = new GosuStubFileElementType("gosu.FILE", GosuLanguage.instance());

  @NotNull
  public Lexer createLexer(Project project) {
    return new GosuLexer();
  }

  @NotNull
  public PsiParser createParser(Project project) {
    throw new UnsupportedOperationException("Should never be called, parse tree transformation happens in GosuStubFileElementType");
  }

  @NotNull
  public IFileElementType getFileNodeType() {
    return GOSU_FILE;
  }

  @NotNull
  public TokenSet getWhitespaceTokens() {
    return GosuTokenSets.WHITE_SPACE_TOKEN_SET;
  }

  @NotNull
  public TokenSet getCommentTokens() {
    return GosuTokenSets.COMMENTS_TOKEN_SET;
  }

  @NotNull
  public TokenSet getStringLiteralElements() {
    return GosuTokenSets.STRING_LITERALS;
  }

  @NotNull
  public PsiElement createElement(ASTNode node) {
    return GosuPsiCreator.createElement(node);
  }

  public PsiFile createFile(@NotNull FileViewProvider viewProvider) {
    if (viewProvider instanceof InjectedFileViewProvider || viewProvider.getVirtualFile().getName().contains("Fragment")) {
      //final InjectedFileViewProvider p = (InjectedFileViewProvider) viewProvider;
      return new GosuProgramFileImpl(viewProvider);
    }

    final VirtualFile file = viewProvider.getVirtualFile();
    final FileType type = file.getFileType();

    return ((GosuCodeFileType) type).createPsiFile(viewProvider);
  }

  public ParserDefinition.SpaceRequirements spaceExistanceTypeBetweenTokens(@NotNull ASTNode left, @NotNull ASTNode right) {
    final IElementType l = left.getElementType();
    final IElementType r = right.getElementType();

    if (r == GosuTokenTypes.TT_uses && l != GosuTokenTypes.TT_WHITESPACE) {
      return MUST_LINE_BREAK;
    }

    if (l == GosuTokenTypes.TT_OP_semicolon || l == GosuTokenTypes.TT_COMMENT_LINE) {
      return MUST_LINE_BREAK;
    }

    if (hasParentLikeElementType(left, GosuElementTypes.ELEM_TYPE_UsesStatement) && r != GosuTokenTypes.TT_WHITESPACE) {
      return MUST_LINE_BREAK;
    }

    return LanguageUtil.canStickTokensTogetherByLexer(left, right, new GosuLexer());
  }

  private boolean hasParentLikeElementType(@NotNull ASTNode node, GosuElementType elementType) {
    ASTNode parent = node.getTreeParent();
    while (parent != null) {
      if (parent.getElementType() == elementType) {
        return true;
      }
      parent = parent.getTreeParent();
    }
    return false;
  }
}
