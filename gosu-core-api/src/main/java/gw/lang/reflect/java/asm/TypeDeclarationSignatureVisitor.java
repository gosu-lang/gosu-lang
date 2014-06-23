/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java.asm;

import gw.internal.ext.org.objectweb.asm.Opcodes;
import gw.internal.ext.org.objectweb.asm.signature.SignatureVisitor;

/**
*/
class TypeDeclarationSignatureVisitor extends SignatureVisitor
{
  private boolean _bOverride;
  private int _iInterfacesVisited;
  private final AsmType _currentType;
  private AsmType _typeArg;
  private int _iDims;
  private IGeneric _asmGenericType;

  TypeDeclarationSignatureVisitor( IGeneric asmGenericType, AsmType type ) {
    super( Opcodes.ASM5 );
    _asmGenericType = asmGenericType;
    _currentType = type;
  }

  TypeDeclarationSignatureVisitor( IGeneric asmGenericType, AsmType type, boolean bOverride ) {
    this( asmGenericType, type );
    _bOverride = bOverride;
  }

  IGeneric getGenericType() {
    return _asmGenericType;
  }

  @Override
  public void visitFormalTypeParameter( String tv ) {
    AsmType typeVar = AsmUtil.makeTypeVariable( tv );
    _currentType.addTypeParameter( typeVar );
    _asmGenericType.setGeneric();
  }

  @Override
  public SignatureVisitor visitClassBound() {
    return new TypeDeclarationSignatureVisitor( _asmGenericType, _currentType.getTypeParameters().get( _currentType.getTypeParameters().size() - 1 ) );
  }

  @Override
  public SignatureVisitor visitInterfaceBound() {
    return new TypeDeclarationSignatureVisitor( _asmGenericType, _currentType.getTypeParameters().get( _currentType.getTypeParameters().size() - 1 ) );
  }

  @Override
  public SignatureVisitor visitSuperclass() {
    return new TypeDeclarationSignatureVisitor( _asmGenericType, ((AsmClass)_asmGenericType).getSuperClass(), true );
  }

  @Override
  public SignatureVisitor visitInterface() {
    return new TypeDeclarationSignatureVisitor( _asmGenericType, ((AsmClass)_asmGenericType).getInterfaces().get( _iInterfacesVisited++ ), true );
  }

  @Override
  public SignatureVisitor visitParameterType() {
    // For method only
    return null;
  }

  @Override
  public SignatureVisitor visitReturnType() {
    // For method only
    return null;
  }

  @Override
  public SignatureVisitor visitExceptionType() {
    // For method only
    return null;
  }

  @Override
  public void visitBaseType( char c ) {
    if( _currentType == null ) {
      return;
    }
    if( _iDims == 0 ) {
      throw new IllegalStateException( "Primitive types are only allowed as array components in a type declaration signature" );
    }
    _typeArg = new AsmType( AsmPrimitiveType.findPrimitive( String.valueOf( c ) ).getName(), _iDims );
    _currentType.addTypeParameter( _typeArg );
  }

  @Override
  public void visitTypeVariable( String tv ) {
    if( _bOverride ) {
      throw new IllegalStateException( "Should never have a type var as an override type i.e., type var can't be a superclass or interface" );
    }
    else {
      _typeArg = AsmUtil.makeTypeVariable( tv );
      _currentType.addTypeParameter( _typeArg );
    }
  }

  @Override
  public SignatureVisitor visitArrayType() {
    TypeDeclarationSignatureVisitor visitor = new TypeDeclarationSignatureVisitor( _asmGenericType, _currentType );
    visitor._typeArg = _typeArg;
    visitor._iDims = _iDims + 1;
    return visitor;
  }

  @Override
  public void visitClassType( String name ) {
    if( _currentType == null ) {
      return;
    }
    if( _bOverride ) {
      if( !_currentType.getName().startsWith( AsmUtil.makeDotName( name ) ) ) {
        throw new IllegalStateException( "Name should match current type" );
      }
      _typeArg = _currentType;
    }
    else {
      _typeArg = AsmUtil.makeType( name );
      _currentType.addTypeParameter( _typeArg );
    }
  }

  @Override
  public void visitInnerClassType( String name ) {
    if( _bOverride ) {
      if( !_currentType.getName().contains( '$' + AsmUtil.makeDotName( name ) ) ) {
        throw new IllegalStateException( "Name should match current type" );
      }
      _typeArg = _currentType;
    }
    else {
      _typeArg = AsmUtil.makeType( name );
      _currentType.addTypeParameter( _typeArg );
    }
  }

  @Override
  public void visitTypeArgument() {
  }

  @Override
  public SignatureVisitor visitTypeArgument( char c ) {
    return new TypeDeclarationSignatureVisitor( _asmGenericType, _typeArg );
  }

  @Override
  public void visitEnd() {
    if( _iDims > 0 ) {
      if( _typeArg.isPrimitive() || _typeArg.isArray() && AsmPrimitiveType.findPrimitive( _typeArg.getName() ) != null ) {
        throw new IllegalStateException( "Not expected " );
      }
      _typeArg.incArrayDims();
    }
  }
}
