/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java.asm;

import gw.internal.ext.org.objectweb.asm.signature.SignatureVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 */
public class MethodDeclarationSignatureVisitor extends TypeDeclarationSignatureVisitor {
  private List<DeclarationPartSignatureVisitor> _paramVisitors;
  private DeclarationPartSignatureVisitor _returnVisitor;
  private List<DeclarationPartSignatureVisitor> _exceptionVisitors;

  MethodDeclarationSignatureVisitor( AsmMethod asmMethod, AsmType type ) {
    super( asmMethod, type );
    _paramVisitors = Collections.emptyList();
    _exceptionVisitors = Collections.emptyList();
  }

  public List<DeclarationPartSignatureVisitor> getParamVisitors() {
    return _paramVisitors;
  }

  public DeclarationPartSignatureVisitor getReturnVisitor() {
    return _returnVisitor;
  }

  public List<DeclarationPartSignatureVisitor> getExceptionVisitors() {
    return _exceptionVisitors;
  }

  @Override
  public SignatureVisitor visitParameterType() {
    if( _paramVisitors.isEmpty() ) {
      _paramVisitors = new ArrayList<DeclarationPartSignatureVisitor>();
    }
    DeclarationPartSignatureVisitor visitor = new DeclarationPartSignatureVisitor();
    _paramVisitors.add( visitor );
    return visitor;
  }

  @Override
  public SignatureVisitor visitReturnType() {
    AsmMethod asmMethod = (AsmMethod)getGenericType();
    asmMethod.initGenericReturnType();
    return _returnVisitor = new DeclarationPartSignatureVisitor();
  }

  @Override
  public SignatureVisitor visitExceptionType() {
    if( _exceptionVisitors.isEmpty() ) {
      _exceptionVisitors = new ArrayList<DeclarationPartSignatureVisitor>();
    }
    DeclarationPartSignatureVisitor visitor = new DeclarationPartSignatureVisitor();
    _exceptionVisitors.add( visitor );
    return visitor;
  }
}
