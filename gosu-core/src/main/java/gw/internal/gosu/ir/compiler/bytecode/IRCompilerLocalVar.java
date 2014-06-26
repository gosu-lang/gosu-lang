/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode;

import gw.internal.ext.org.objectweb.asm.Label;
import gw.internal.gosu.ir.nodes.JavaClassIRType;
import gw.lang.GosuShop;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.IRType;

public class IRCompilerLocalVar {
  private String _name;
  private IRType _type;
  private int _index;
  private IRCompilerScope _scope;
  private Label _startLabel;
  private Label _endLabel;
  private boolean _temp;

  public IRCompilerLocalVar(IRSymbol symbol, int index, IRCompilerScope scope) {
    _name = symbol.getName();
    _type = maybeEraseStructuralType( symbol.getType() );
    _temp = symbol.isTemp();
    _index = index;
    _scope = scope;
  }

  public String getName() {
    return _name;
  }

  public IRType getType() {
    return _type;
  }

  public int getIndex() {
    return _index;
  }

  public void setIndex(int index) {
    _index = index;
  }

  public IRCompilerScope getScope() {
    return _scope;
  }

  public Label getStartLabel() {
    return _startLabel;
  }

  public Label getEndLabel() {
    return _endLabel;
  }

  public void setStartLabel(Label startLabel) {
    _startLabel = startLabel;
  }

  public void setEndLabel(Label endLabel) {
    _endLabel = endLabel;
  }

  public boolean isTemp() {
    return _temp;
  }

  public int getWidth() {
    return (_type.getName().equals("long") || _type.getName().equals("double") ? 2 : 1);
  }

  private static IRType maybeEraseStructuralType( IRType type ) {
    IRType originalType = type;
    int iArrayDims = 0;
    while( type.isArray() ) {
      iArrayDims++;
      type = type.getComponentType();
    }
    if( type.isStructural() ) {
      type = GosuShop.getIRTypeResolver().getDescriptor( Object.class );
      while( iArrayDims-- > 0 ) {
        type = type.getArrayType();
      }
    }
    else {
      type = originalType;
    }
    return type;
  }
}
