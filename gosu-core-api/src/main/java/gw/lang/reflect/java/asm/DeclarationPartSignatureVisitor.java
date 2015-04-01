/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java.asm;

import gw.internal.ext.org.objectweb.asm.Opcodes;
import gw.internal.ext.org.objectweb.asm.signature.SignatureVisitor;


/**
 */
public class DeclarationPartSignatureVisitor extends SignatureVisitor {
  private AsmType _currentType;
  private AsmType _typeArg;
  private int _iArrayDims;
  private Boolean _variance; // null = none, true = covariant, false = contravariant

  DeclarationPartSignatureVisitor() {
    super( Opcodes.ASM5 );
  }
  DeclarationPartSignatureVisitor( AsmType type ) {
    super( Opcodes.ASM5 );
    _currentType = type;
  }
  DeclarationPartSignatureVisitor( AsmType type, char wildcardVariance ) {
    this( type );
    _variance = wildcardVariance == '+';
  }

  public AsmType getCurrentType() {
    return _currentType;
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
    AsmType type = AsmPrimitiveType.findPrimitive( String.valueOf( c ) );
    if( _iArrayDims > 0 ) {
      // primitive types are immutable...
      type = AsmUtil.makeType( type.getName() );
    }
    if( _currentType == null ) {
      _currentType = type;
      if( _variance != null ) {
        _currentType = new AsmWildcardType( _currentType, _variance );
      }
      for( int i = 0; i < _iArrayDims; i++ ) {
        _currentType.incArrayDims();
      }
    }
    else {
      _typeArg = type;
      if( _variance != null ) {
        _typeArg = new AsmWildcardType( _typeArg, _variance );
      }
      for( int i = 0; i < _iArrayDims; i++ ) {
        _typeArg.incArrayDims();
      }
      _currentType.addTypeParameter( _typeArg );
    }
  }

  @Override
  public void visitTypeVariable( String tv ) {
    if( _currentType == null ) {
      _currentType = AsmUtil.makeTypeVariable( tv );
      for( int i = 0; i < _iArrayDims; i++ ) {
        _currentType.incArrayDims();
      }
      if( _variance != null ) {
        _currentType = new AsmWildcardType( _currentType, _variance );
      }
    }
    else {
      AsmType typeArg = AsmUtil.makeTypeVariable( tv );
      for( int i = 0; i < _iArrayDims; i++ ) {
        typeArg.incArrayDims();
      }
      if( _variance != null ) {
        typeArg = new AsmWildcardType( typeArg, _variance );
      }
      _currentType.addTypeParameter( typeArg );
    }
  }

  @Override
  public SignatureVisitor visitArrayType() {
    _iArrayDims++;
    return this;
  }

  @Override
  public void visitClassType( String name ) {
    if( _currentType == null ) {
      _currentType = AsmUtil.makeType( name );
      if( _variance != null ) {
        _currentType = new AsmWildcardType( _currentType, _variance );
      }
    }
    else {
      _typeArg = AsmUtil.makeType( name );
      if( _variance != null ) {
        _typeArg = new AsmWildcardType( _typeArg, _variance );
      }
    }
  }

  @Override
  public void visitInnerClassType( String name ) {
    if( _currentType == null ) {
      _currentType = AsmUtil.makeType( name );
      if( _variance != null ) {
        _currentType = new AsmWildcardType( _currentType, _variance );
      }
    }
    else {
      if( _typeArg != null ) {
        _typeArg = AsmUtil.makeType( (_typeArg instanceof AsmWildcardType ? ((AsmWildcardType)_typeArg).getBound().getName() : _typeArg.getName()) + "$" + name );
        if( _variance != null ) {
          _typeArg = new AsmWildcardType( _typeArg, _variance );
        }
      }
      else {
        _currentType = AsmUtil.makeType( (_currentType instanceof AsmWildcardType ? ((AsmWildcardType)_currentType).getBound().getName() : _currentType.getName()) + "$" + name );
        if( _variance != null ) {
          _currentType = new AsmWildcardType( _currentType, _variance );
        }
      }
    }
  }

  @Override
  public void visitTypeArgument() {
    if( _typeArg != null ) {
      _typeArg.addTypeParameter( new AsmWildcardType( null, true ) );
    }
    else {
      _currentType.addTypeParameter( new AsmWildcardType( null, true ) );
    }
  }

  @Override
  public SignatureVisitor visitTypeArgument( char wildcard ) {
    if( wildcard != '=' ) {
      return new DeclarationPartSignatureVisitor( _typeArg == null ? _currentType : _typeArg, wildcard );
    }
    return new DeclarationPartSignatureVisitor( _typeArg == null ? _currentType : _typeArg );
  }

  @Override
  public void visitEnd() {
    if( _iArrayDims > 0 ) {
      if( _typeArg != null ) {
        for( int i = 0; i < _iArrayDims; i++ ) {
          _typeArg.incArrayDims();
        }
      }
      else {
        for( int i = 0; i < _iArrayDims; i++ ) {
          _currentType.incArrayDims();
        }
      }
    }
    if( _typeArg != null ) {
      _currentType.addTypeParameter( _typeArg );
    }
  }
}
