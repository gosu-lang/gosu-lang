/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java.asm;

import gw.internal.ext.org.objectweb.asm.Opcodes;
import gw.internal.ext.org.objectweb.asm.signature.SignatureVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
*/
class TypeDeclarationSignatureVisitor extends SignatureVisitor
{
  private AsmClass _asmClass;
  private DeclarationPartSignatureVisitor _superType;
  private List<DeclarationPartSignatureVisitor> _interfaces = Collections.emptyList();
  private AsmType _csrTypeVar;


  TypeDeclarationSignatureVisitor( AsmClass asmClass ) {
    super( Opcodes.ASM5 );
    _asmClass = asmClass;
  }

  public void update() {
    if( _superType != null ) {
      _asmClass.setSuperClass( _superType.getCurrentType() );
    }
    _asmClass.getInterfaces().clear();
    for( DeclarationPartSignatureVisitor visitor: _interfaces ) {
      _asmClass.getInterfaces().add( visitor.getCurrentType() );
    }
  }

  @Override
  public void visitFormalTypeParameter( String tv ) {
    _csrTypeVar = AsmUtil.makeTypeVariable( tv );
    _asmClass.setGeneric();
    _asmClass.getType().addTypeParameter( _csrTypeVar );
  }

  @Override
  public SignatureVisitor visitClassBound() {
    return new DeclarationPartSignatureVisitor( _csrTypeVar );
  }

  @Override
  public SignatureVisitor visitInterfaceBound() {
    return new DeclarationPartSignatureVisitor( _csrTypeVar );
  }

  @Override
  public SignatureVisitor visitSuperclass() {
    DeclarationPartSignatureVisitor visitor = new DeclarationPartSignatureVisitor();
    _superType = visitor;
    return visitor;
  }

  @Override
  public SignatureVisitor visitInterface() {
    DeclarationPartSignatureVisitor visitor = new DeclarationPartSignatureVisitor();
    if( _interfaces.isEmpty() ) {
      _interfaces = new ArrayList<DeclarationPartSignatureVisitor>( 2 );
    }
    _interfaces.add( visitor );
    return visitor;
  }

  @Override
  public void visitTypeVariable( String tv ) {
    throw new IllegalStateException();
  }

  @Override
  public SignatureVisitor visitArrayType() {
    throw new IllegalStateException();
  }

  @Override
  public void visitEnd() {
    throw new IllegalStateException();
  }
}
