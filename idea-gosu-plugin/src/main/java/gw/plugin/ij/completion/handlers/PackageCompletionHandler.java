/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.handlers;

import com.intellij.codeInsight.completion.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import gw.lang.reflect.INamespaceType;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.completion.model.BeanInfoModel;
import gw.plugin.ij.completion.model.BeanTree;
import gw.plugin.ij.completion.model.PackageInfoModel;
import gw.plugin.ij.completion.model.PackageType;
import gw.plugin.ij.completion.proposals.RawCompletionProposal;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.api.statements.IGosuUsesStatement;
import gw.plugin.ij.lang.psi.impl.expressions.GosuPropertyMemberAccessExpressionImpl;
import gw.plugin.ij.util.GosuModuleUtil;
import org.jetbrains.annotations.NotNull;

public class PackageCompletionHandler extends AbstractPathCompletionHandler {
  private final PrefixMatcher _matcher;
  public PackageCompletionHandler(@NotNull CompletionParameters params, @NotNull CompletionResultSet results) {
    super(params.getOriginalFile().getProject(), params, results.getPrefixMatcher(), results);
    _matcher = results.getPrefixMatcher();
  }

  public void handleCompletePath() {
    // head back to the other side of the dot

    PsiElement prevSibling = getContext().getPosition().getPrevSibling();
    if (prevSibling == null || prevSibling.getPrevSibling() == null) {
      return;
    }

    PsiElement csr = prevSibling.getPrevSibling();
    if (csr instanceof LeafPsiElement) {
      csr = csr.getParent();
    }

    if (csr instanceof GosuPropertyMemberAccessExpressionImpl ||
        (csr.getPrevSibling() == null && csr.getParent() instanceof GosuPropertyMemberAccessExpressionImpl)) {
      IModule module = GosuModuleUtil.findModuleForPsiElement(getContext().getPosition());
      PackageType pkgType = PackageType.create((IGosuPsiElement) csr, module);
      if (pkgType != null) {
        IModule mod = GosuModuleUtil.findModuleForPsiElement(csr);
        PackageInfoModel model = new PackageInfoModel(pkgType, pkgType, mod, atAnnotation(_params.getPosition().getParent()));
        makeProposals(model);
        if (getContext().getPosition().getText().equals(CompletionInitializationContext.DUMMY_IDENTIFIER_TRIMMED) &&
            isInUsingStatement() ) {
          addCompletion(new RawCompletionProposal("*"));
        }
      }

      IType type = TypeSystem.getByFullNameIfValid(csr.getText());
      if (type != null) {
        if (getContext().getPosition().getText().equals(CompletionInitializationContext.DUMMY_IDENTIFIER_TRIMMED) &&
            isInUsingStatement() ) {
          addCompletion(new RawCompletionProposal("*"));
        }
      }
    }
  }

  @Override
  protected void makeProposals( @NotNull BeanInfoModel model ) {
    if( model instanceof PackageInfoModel ) {
      IType type = model.getRoot().getBeanNode().getType();
      if( type instanceof PackageType ) {
        IModule module = GosuModuleUtil.findModuleForPsiElement( getContext().getPosition() );
        INamespaceType namespace = TypeSystem.getNamespace( type.getName(), module );
        if( namespace != null ) {
          BeanTree root = model.getRoot();
          for( int i = 0; i < root.getChildCount(); i++ ) {
            BeanTree child = root.getChildAt( i );
            IType t;
            if( child.getBeanNode().getType() instanceof PackageType ) {
              t = TypeSystem.getNamespace( child.getBeanNode().toString(), module );
            }
            else {
              t = TypeSystem.getByFullNameIfValid( child.getBeanNode().toString(), module );
            }
            if( t != null ) {
              if( _matcher == null || _matcher.prefixMatches( child.getBeanNode().getName() ) ) {
                addCompletion( makeProposal( child ) );
              }
            }
          }
        }
      }
    }
    else {
      IType type = model.getRoot().getBeanNode().getType();
      IModule module = GosuModuleUtil.findModuleForPsiElement( getContext().getPosition() );
      IType t = TypeSystem.getByFullNameIfValid( type.getName(), module );
      if( t != null ) {
        super.makeProposals( model );
      }
    }
  }

  @NotNull
  @Override
  public String getStatusMessage() {
    return "Package completion...";
  }

  public boolean isInUsingStatement() {
    PsiElement position = getContext().getPosition();
    do {
      position = position.getParent();
    } while (position != null && !(position instanceof IGosuUsesStatement));

    return position != null;
  }
}
