/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.expression;

import gw.config.CommonServices;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRElement;
import gw.lang.ir.IRType;
import gw.lang.UnstableAPI;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.java.JavaTypes;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

@UnstableAPI
public class IRCompositeExpression extends IRExpression {
  private List<IRElement> _elements;

  public IRCompositeExpression(IRElement... elements) {
    _elements = new ArrayList<IRElement>(Arrays.asList(elements));

    for(IRElement element : _elements) {
      element.setParent(this);
    }
  }

  public IRCompositeExpression(List<IRElement> elements) {
    _elements = elements;

    for(IRElement element : _elements) {
      element.setParent(this);
    }
  }

  public List<IRElement> getElements() {
    return _elements;
  }

  public void addElement(IRElement element) {
    _elements.add(element);
    element.setParent( this );
  }

  @Override
  public IRType getType() {
    IRElement irElement = _elements.get(_elements.size() - 1);
    if (irElement instanceof IRExpression) {
      IRExpression expr = (IRExpression) irElement;
      return expr.getType();
    } else {
      return CommonServices.getGosuIndustrialPark().getIRTypeResolver().getDescriptor(JavaTypes.pVOID());
    }
  }
}
