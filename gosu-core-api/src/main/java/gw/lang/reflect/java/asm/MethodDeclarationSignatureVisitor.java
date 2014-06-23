/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java.asm;

import gw.internal.ext.org.objectweb.asm.signature.SignatureVisitor;

/**
 */
public class MethodDeclarationSignatureVisitor extends TypeDeclarationSignatureVisitor {
  private int _iParamCsr;
  private int _iExceptionCsr;

  MethodDeclarationSignatureVisitor( AsmMethod asmMethod, AsmType type ) {
    super( asmMethod, type );
    asmMethod.initGenericParameters();
  }

  @Override
  public SignatureVisitor visitParameterType() {
    AsmMethod asmMethod = (AsmMethod)getGenericType();
    AsmType param = asmMethod.getGenericParameters().get( _iParamCsr++ );
    return new MethodPartSignatureVisitor( param );
  }

  @Override
  public SignatureVisitor visitReturnType() {
    AsmMethod asmMethod = (AsmMethod)getGenericType();
    asmMethod.initGenericReturnType();
    return new MethodPartSignatureVisitor( asmMethod.getGenericReturnType() );
  }

  @Override
  public SignatureVisitor visitExceptionType() {
    AsmMethod asmMethod = (AsmMethod)getGenericType();
    AsmType exception = asmMethod.getExceptions().get( _iExceptionCsr++ );
    return new MethodPartSignatureVisitor( exception );
  }

}
