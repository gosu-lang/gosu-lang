/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java.asm;

import gw.internal.ext.org.objectweb.asm.Opcodes;
import gw.internal.ext.org.objectweb.asm.signature.SignatureVisitor;

/**
 */
public class MethodPartSignatureVisitor extends SignatureVisitor {
  private AsmType _currentType;
  private AsmType _typeArg;
  private int _iDims;
  private Boolean _variance; // null = none, true = covariant, false = contravariant

  MethodPartSignatureVisitor( AsmType type ) {
    super( Opcodes.ASM5 );
    _currentType = type;
  }
  MethodPartSignatureVisitor( AsmType type, char wildcardVariance ) {
    this( type );
    _typeArg = type;
    _variance = wildcardVariance == '=' ? null : wildcardVariance == '+';
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
    if( _currentType == null ) {
      return;
    }
    if( _iDims == 0 ) {
      return;
    }
    if( _currentType instanceof AsmPrimitiveType ) {
      throw new IllegalStateException( "Current type should not be a cached Primitive type" );
    }
    String name = String.valueOf( c );
    AsmPrimitiveType primitive = AsmPrimitiveType.findPrimitive( name );
    if( !primitive.equals( _currentType ) ) {
      _typeArg = AsmUtil.makeType( primitive.getName() );
      if( _variance != null ) {
        _typeArg = new AsmWildcardType( _typeArg, _variance );
      }
      _currentType.addTypeParameter( _typeArg );
      _currentType = _typeArg;
    }
    for( int i = 0; i < _iDims; i++ ) {
      _currentType.incArrayDims();
    }
  }

  @Override
  public void visitTypeVariable( String tv ) {
    if( _typeArg == null ) {
      _currentType.setName( tv );
      _currentType.setTypeVariable();
      if( !AsmUtil.makeDotName( tv ).equals( _currentType.getName() ) ) {
        throw new IllegalStateException( "Name should match current type" );
      }
      if( _iDims > 0 ) {
        for( int i = 0; i < _iDims; i++ ) {
          _currentType.incArrayDims();
        }
      }
      _typeArg = _currentType;
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
    MethodPartSignatureVisitor visitor = new MethodPartSignatureVisitor( _currentType );
    visitor._typeArg = _typeArg;
    visitor._iDims = _iDims + 1;
    return visitor;
  }

  @Override
  public void visitClassType( String name ) {
    if( _typeArg == null ) {
      if( !_currentType.getName().startsWith( AsmUtil.makeDotName( name ) ) ) {
        throw new IllegalStateException( "Name should match current type" );
      }
      _typeArg = _currentType;
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
    if( _typeArg == null ) {
      if( !_currentType.getName().contains( '$' + AsmUtil.makeDotName( name ) ) ) {
        throw new IllegalStateException( "Name should match current type" );
      }
      _typeArg = _currentType;
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
    _typeArg.addTypeParameter( new AsmWildcardType( null, true ) );
  }

  @Override
  public SignatureVisitor visitTypeArgument( char wildcard ) {
    return new MethodPartSignatureVisitor( _typeArg, wildcard );
  }

  @Override
  public void visitEnd() {
    if( _iDims > 0 ) {
      if( _typeArg.isPrimitive() || _typeArg.isArray() && AsmPrimitiveType.findPrimitive( _typeArg.getName() ) != null ) {
        throw new IllegalStateException( "Not expected " );
      }
      for( int i = 0; i < _iDims; i++ ) {
        _typeArg.incArrayDims();
      }
    }
  }
}
