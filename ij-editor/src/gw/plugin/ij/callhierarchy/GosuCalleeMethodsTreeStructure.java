/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.callhierarchy;

import com.intellij.ide.hierarchy.HierarchyNodeDescriptor;
import com.intellij.ide.hierarchy.HierarchyTreeStructure;
import com.intellij.ide.hierarchy.call.CallHierarchyNodeDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMember;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiNewExpression;
import com.intellij.psi.PsiReferenceExpression;
import com.intellij.psi.search.searches.OverridingMethodsSearch;
import com.intellij.util.ArrayUtil;
import com.intellij.util.containers.HashMap;
import gw.plugin.ij.lang.psi.impl.expressions.GosuBeanMethodCallExpressionImpl;

import java.util.ArrayList;

public class GosuCalleeMethodsTreeStructure extends HierarchyTreeStructure {
  private final String myScopeType;

  /**
   * Should be called in read action
   */
  public GosuCalleeMethodsTreeStructure(final Project project, final PsiMethod method, final String scopeType) {
    super(project, new CallHierarchyNodeDescriptor(project, null, method, true, false));
    myScopeType = scopeType;
  }

  protected final Object[] buildChildren(final HierarchyNodeDescriptor descriptor) {
    final PsiMember enclosingElement = ((CallHierarchyNodeDescriptor) descriptor).getEnclosingElement();
    if (!(enclosingElement instanceof PsiMethod)) {
      return ArrayUtil.EMPTY_OBJECT_ARRAY;
    }
    final PsiMethod method = (PsiMethod) enclosingElement;

    final ArrayList<PsiMethod> methods = new ArrayList<PsiMethod>();

    final PsiCodeBlock body = method.getBody();
    if (body != null) {
      visitor(body, methods);
    }

    final PsiMethod baseMethod = (PsiMethod) ((CallHierarchyNodeDescriptor) getBaseDescriptor()).getTargetElement();
    final PsiClass baseClass = baseMethod.getContainingClass();

    final HashMap<PsiMethod, CallHierarchyNodeDescriptor> methodToDescriptorMap = new HashMap<PsiMethod, CallHierarchyNodeDescriptor>();

    final ArrayList<CallHierarchyNodeDescriptor> result = new ArrayList<CallHierarchyNodeDescriptor>();

    for (final PsiMethod calledMethod : methods) {
      if (!isInScope(baseClass, calledMethod, myScopeType)) {
        continue;
      }

      CallHierarchyNodeDescriptor d = methodToDescriptorMap.get(calledMethod);
      if (d == null) {
        d = new CallHierarchyNodeDescriptor(myProject, descriptor, calledMethod, false, false);
        methodToDescriptorMap.put(calledMethod, d);
        result.add(d);
      } else {
        d.incrementUsageCount();
      }
    }

    // also add overriding methods as children
    final PsiMethod[] overridingMethods = OverridingMethodsSearch.search(method, true).toArray(PsiMethod.EMPTY_ARRAY);
    for (final PsiMethod overridingMethod : overridingMethods) {
      if (!isInScope(baseClass, overridingMethod, myScopeType)) {
        continue;
      }
      final CallHierarchyNodeDescriptor node = new CallHierarchyNodeDescriptor(myProject, descriptor, overridingMethod, false, false);
      if (!result.contains(node)) {
        result.add(node);
      }
    }

    return ArrayUtil.toObjectArray(result);
  }


  private static void visitor(final PsiElement element, final ArrayList<PsiMethod> methods) {
    final PsiElement[] children = element.getChildren();
    for (final PsiElement child : children) {
      visitor(child, methods);
      if (child instanceof PsiMethodCallExpression) {
        final PsiMethodCallExpression callExpression = (PsiMethodCallExpression) child;
        final PsiReferenceExpression methodExpression = callExpression.getMethodExpression();
        final PsiMethod method = (PsiMethod) methodExpression.resolve();
        if (method != null) {
          methods.add(method);
        }
      } else if (child instanceof GosuBeanMethodCallExpressionImpl) {
        final PsiElement method = ((GosuBeanMethodCallExpressionImpl) child).resolve();
        if (method instanceof PsiMethod) {
          methods.add((PsiMethod) method);
        }
      } else if (child instanceof PsiNewExpression) {
        final PsiNewExpression newExpression = (PsiNewExpression) child;
        final PsiMethod method = newExpression.resolveConstructor();
        if (method != null) {
          methods.add(method);
        }
      }
    }
  }
}
