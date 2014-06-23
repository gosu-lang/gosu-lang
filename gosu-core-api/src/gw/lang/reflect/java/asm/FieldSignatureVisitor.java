/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java.asm;

import gw.internal.ext.org.objectweb.asm.Opcodes;
import gw.internal.ext.org.objectweb.asm.signature.SignatureVisitor;

/**
 */
public class FieldSignatureVisitor extends SignatureVisitor {
  private AsmField _asmField;
  private AsmType _currentType;
  private AsmType _typeArg;
  private boolean _bArray;
  private Boolean _variance; // null = none, true = covariant, false = contravariant

  FieldSignatureVisitor( AsmField field ) {
    super( Opcodes.ASM5 );
    _asmField = field;
  }
  FieldSignatureVisitor( AsmField field, AsmType type ) {
    this( field );
    _currentType = type;
  }
  FieldSignatureVisitor( AsmField field, AsmType type, char wildcardVariance ) {
    this( field, type );
    _variance = wildcardVariance == '+';
  }

  @Override
  public void visitFormalTypeParameter( String tv ) {
    throw new IllegalStateException();
  }

  @Override
  public SignatureVisitor visitClassBound() {
    throw new IllegalStateException();
  }

  @Override
  public SignatureVisitor visitInterfaceBound() {
    throw new IllegalStateException();
  }

  @Override
  public SignatureVisitor visitSuperclass() {
    return this;
  }

  @Override
  public SignatureVisitor visitInterface() {
    throw new IllegalStateException();
  }

  @Override
  public SignatureVisitor visitParameterType() {
    // For method only
    throw new IllegalStateException();
  }

  @Override
  public SignatureVisitor visitReturnType() {
    // For method only
    throw new IllegalStateException();
  }

  @Override
  public SignatureVisitor visitExceptionType() {
    // For method only
    throw new IllegalStateException();
  }

  @Override
  public void visitBaseType( char c ) {
  }

  @Override
  public void visitTypeVariable( String tv ) {
    if( _currentType == null ) {
      _currentType = AsmUtil.makeTypeVariable( tv );
      if( _variance != null ) {
        _currentType = new AsmWildcardType( _currentType, _variance );
      }
      _asmField.setType( _currentType );
    }
    else {
      _typeArg = AsmUtil.makeTypeVariable( tv );
      if( _variance != null ) {
        _typeArg = new AsmWildcardType( _typeArg, _variance );
      }
      _currentType.addTypeParameter( _typeArg );
    }
  }

  @Override
  public SignatureVisitor visitArrayType() {
    FieldSignatureVisitor visitor = new FieldSignatureVisitor( _asmField, _currentType );
    visitor._typeArg = _typeArg;
    visitor._bArray = true;
    return visitor;
  }

  @Override
  public void visitClassType( String name ) {
    if( _currentType == null ) {
      _currentType = AsmUtil.makeType( name );
      if( _variance != null ) {
        _currentType = new AsmWildcardType( _currentType, _variance );
      }
      _asmField.setType( _currentType );
    }
    else {
      _typeArg = AsmUtil.makeType( name );
      if( _variance != null ) {
        _typeArg = new AsmWildcardType( _typeArg, _variance );
      }
      _currentType.addTypeParameter( _typeArg );
    }
  }

  @Override
  public void visitInnerClassType( String name ) {
    if( _currentType == null ) {
      _currentType = AsmUtil.makeType( name );
      if( _variance != null ) {
        _currentType = new AsmWildcardType( _currentType, _variance );
      }
      _asmField.setType( _currentType );
    }
    else {
      _typeArg = AsmUtil.makeType( name );
      if( _variance != null ) {
        _typeArg = new AsmWildcardType( _typeArg, _variance );
      }
      _currentType.addTypeParameter( _typeArg );
    }
  }

  @Override
  public void visitTypeArgument() {
    _currentType.addTypeParameter( new AsmWildcardType( null, true ) );
  }

  @Override
  public SignatureVisitor visitTypeArgument( char wildcard ) {
    if( wildcard != '=' ) {
      return new FieldSignatureVisitor( _asmField, _typeArg == null ? _currentType : _typeArg, wildcard );
    }
    return new FieldSignatureVisitor( _asmField, _typeArg == null ? _currentType : _typeArg );
  }

  @Override
  public void visitEnd() {
    if( _bArray ) {
      _currentType.incArrayDims();
    }
  }
}
