/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java;

import gw.internal.ext.org.objectweb.asm.AnnotationVisitor;
import gw.internal.ext.org.objectweb.asm.Opcodes;
import gw.internal.gosu.ir.compiler.bytecode.IRAnnotationCompiler;
import gw.internal.gosu.ir.transform.util.IRTypeResolver;
import gw.internal.gosu.parser.DynamicFunctionSymbol;
import gw.internal.gosu.parser.GosuAnnotationInfo;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.lang.ir.IRAnnotation;
import gw.lang.ir.IRType;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.test.TestClass;

import java.util.HashMap;
import java.util.Map;

public class SourceAnnotationTest extends TestClass {

  public IGosuClassInternal getGosuClass() throws Exception {
    IGosuClassInternal gosuClass = (IGosuClassInternal) TypeSystem.getByFullNameIfValid("gw.data.TestGosuClass");
    assertTrue( gosuClass.isValid() );
    return gosuClass;
  }

  public void testPlainString() throws Exception {
    verify("test1", "value", "cause I want to");
  }

  public void testStringAddition() throws Exception {
    verify("test2", "value", "cause I want to");
  }

//  public void testStringConstantInSameCass() throws Exception {
//    verify("test3", "value", "cause I want to");
//  }

//  public void testStringConstantExpression() throws Exception {
//    verify("test4", "value", "cause I want to");
//  }

//  public void testStringConstantInOtherClass() throws Exception {
//    verify("test5", "value", "cause I want to");
//  }

  public void testIntegerAddition() throws Exception {
    verify("test6", "value", new Integer(3));
  }

  public void testEnum() throws Exception {
    verify("test7", "value", "Lgw/data/JavaEnum;-TWO");
  }

  private void verify(String funcName, String field, Object value) throws Exception {
    DynamicFunctionSymbol dfs = getGosuClass().getParseInfo().getMemberFunctions().get( funcName + "()" );
    GosuAnnotationInfo annotation = new GosuAnnotationInfo( dfs.getAnnotations().get(0), getGosuClass().getTypeInfo(), getGosuClass() );
    MockVisitor visitor = new MockVisitor();
    IType type = annotation.getType();
    IRType irType = IRTypeResolver.getDescriptor(type);
    IRAnnotation irAnnotation = new IRAnnotation(irType, true, annotation);
    IRAnnotationCompiler compiler = new IRAnnotationCompiler(visitor, irAnnotation);
    compiler.compile();
    assertEquals(value, visitor.values.get(field));
  }


  static class MockVisitor extends AnnotationVisitor {
    Map<String, Object> values = new HashMap<String, Object>();

    public MockVisitor() {
      super( Opcodes.ASM5 );
    }

    @Override
    public void visit(String s, Object o) {
      values.put(s, o);
    }

    @Override
    public void visitEnum(String s, String s1, String s2) {
      values.put(s, s1 + "-" + s2);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String s, String s1) {
      return null;  
    }

    @Override
    public AnnotationVisitor visitArray(String s) {
      return null;  
    }
  }
}
