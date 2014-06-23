/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.navigation

uses com.intellij.psi.PsiElement
uses com.intellij.psi.PsiNamedElement
uses com.intellij.psi.impl.source.tree.LeafPsiElement
uses gw.plugin.ij.framework.GosuTestCase
uses gw.plugin.ij.lang.psi.impl.statements.GosuFieldImpl

abstract class GosuUpDownMethodTest extends GosuTestCase {

  function findNamedElement(element: PsiElement ): PsiElement {
    if (element.Parent typeis GosuFieldImpl ) {
      var asFound = false
      for (c in element.Parent.Node.getChildren(null)) {
        if (c typeis LeafPsiElement and c.Text == "as") {
          asFound = true
        }
        if (asFound and c.Psi == element) {
          return element
        }
      }
    }
    while (element != null and !(element typeis PsiNamedElement )) {
      element = element.Parent
    }
    return element
  }

}