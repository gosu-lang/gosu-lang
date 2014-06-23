/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.util.transform.java.Visitor;

import com.sun.source.tree.BlockTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.TreeVisitor;

import java.util.ArrayList;
import java.util.List;


public class DummyBlock implements BlockTree {

  @Override
  public boolean isStatic() {
    return false;
  }

  @Override
  public List<? extends StatementTree> getStatements() {
    return new ArrayList<StatementTree>();
  }

  @Override
  public Kind getKind() {
    return Kind.BLOCK;
  }

  @Override
  public <R, D> R accept(TreeVisitor<R, D> v, D d) {
    return v.visitBlock(this, d);
  }
}
