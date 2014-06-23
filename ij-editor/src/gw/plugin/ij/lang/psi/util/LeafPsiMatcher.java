/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.util;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiMatcher;
import com.intellij.psi.util.PsiMatcherExpression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeafPsiMatcher implements PsiMatcher {

  private final boolean reverse;

  private PsiElement element;

  public LeafPsiMatcher(PsiElement element) {
    this(element, false);
  }

  public LeafPsiMatcher(PsiElement element, boolean reverse) {
    this.element = element;
    this.reverse = reverse;
  }

  public PsiMatcher parent(PsiMatcherExpression e) {
    element = element.getParent();
    if (element == null || (e != null && e.match(element) != Boolean.TRUE)) return NullPsiMatcherImpl.INSTANCE;
    return this;
  }

  public PsiMatcher firstChild(PsiMatcherExpression e) {
    final PsiElement[] children = getChildren();
    for (PsiElement child : children) {
      element = child;
      if (e == null || e.match(element) == Boolean.TRUE) {
        return this;
      }
    }
    return NullPsiMatcherImpl.INSTANCE;
  }

  private PsiElement[] getChildren() {
    final List<PsiElement> children = new ArrayList<>();
    element.acceptChildren(new PsiElementVisitor() {
      public void visitElement(PsiElement element) {
        children.add(element);
      }
    });
    if (reverse) {
      Collections.reverse(children);
    }
    return children.toArray(PsiElement.EMPTY_ARRAY);
  }

  public PsiMatcher ancestor(PsiMatcherExpression e) {
    while (element != null) {
      Boolean res = e == null ? Boolean.TRUE : e.match(element);
      if (res == Boolean.TRUE) break;
      if (res == null) return NullPsiMatcherImpl.INSTANCE;
      element = element.getParent();
    }
    if (element == null) return NullPsiMatcherImpl.INSTANCE;
    return this;
  }

  public PsiMatcher descendant(PsiMatcherExpression e) {
    final PsiElement[] children = getChildren();
    for (PsiElement child : children) {
      element = child;
      final Boolean res = e == null ? Boolean.TRUE : e.match(element);
      if (res == Boolean.TRUE) {
        return this;
      } else if (res == Boolean.FALSE) {
        final PsiMatcher grandChild = descendant(e);
        if (grandChild != NullPsiMatcherImpl.INSTANCE) return grandChild;
      }
    }
    return NullPsiMatcherImpl.INSTANCE;
  }

  public PsiMatcher dot(PsiMatcherExpression e) {
    return e == null || e.match(element) == Boolean.TRUE ? this : NullPsiMatcherImpl.INSTANCE;
  }


  public PsiElement getElement() {
    return element;
  }

  private static class NullPsiMatcherImpl implements PsiMatcher {
    public PsiMatcher parent(PsiMatcherExpression e) {
      return this;
    }

    public PsiMatcher firstChild(PsiMatcherExpression e) {
      return this;
    }

    public PsiMatcher ancestor(PsiMatcherExpression e) {
      return this;
    }

    public PsiMatcher descendant(PsiMatcherExpression e) {
      return this;
    }

    public PsiMatcher dot(PsiMatcherExpression e) {
      return this;
    }

    public PsiElement getElement() {
      return null;
    }

    private static final NullPsiMatcherImpl INSTANCE = new NullPsiMatcherImpl();
  }
}
