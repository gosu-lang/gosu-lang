/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.statement;

import gw.internal.ext.org.objectweb.asm.signature.SignatureVisitor;
import gw.internal.ext.org.objectweb.asm.signature.SignatureWriter;
import gw.lang.UnstableAPI;
import gw.lang.ir.IRAnnotation;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.IRType;
import gw.lang.ir.SignatureUtil;
import gw.lang.reflect.ICompoundType;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.java.JavaTypes;
import gw.util.GosuStringUtil;

import java.util.Collections;
import java.util.List;

@UnstableAPI
public class IRMethodStatement extends IRStatement {
  private IRStatement _methodBody;
  private String _name;
  private int _modifiers;
  private List<IRSymbol> _parameters;
  private IRType _returnType;
  private List<IRAnnotation> _annotations;
  private boolean _explicitInternal;
  private Object[] _annotationDefault;
  private String _genericSignature;

  public IRMethodStatement(IRStatement methodBody, String name, int modifiers, boolean explicitInternal, IRType returnType, List<IRSymbol> parameters) {
    this( methodBody, name, modifiers, explicitInternal, returnType, parameters, null );
  }
  public IRMethodStatement(IRStatement methodBody, String name, int modifiers, boolean explicitInternal, IRType returnType, List<IRSymbol> parameters, Object[] annotationDefault) {
    this( methodBody, name, modifiers, explicitInternal, returnType, null, parameters, null, null, annotationDefault );
  }

  public IRMethodStatement( IRStatement methodBody, String name, int modifiers, boolean explicitInternal, IRType returnType, IType returnIType, List<IRSymbol> parameters, IType[] argTypes, IType methodType, Object[] annotationDefault ) {
    _methodBody = methodBody;
    _name = name;
    _modifiers = modifiers;
    _explicitInternal = explicitInternal;
    _returnType = maybeEraseStructuralType( returnType );
    _parameters = maybeEraseStructuralSymbolTypes( parameters );
    _annotations = Collections.emptyList();
    setParentToThis( methodBody );
    _annotationDefault = annotationDefault;
    _genericSignature = makeGenericSignature( methodType, returnIType, argTypes );
  }

  private String makeGenericSignature(IType type, IType rtype, IType[] args) {
    if( type == null || rtype == null || args == null ) {
      return null;
    }
    /*
      ( visitFormalTypeParameter visitClassBound? visitInterfaceBound* )* ( visitParameterType* visitReturnType visitExceptionType* )
    */
    boolean[] bGeneric = {false};
    SignatureWriter sw = new SignatureWriter();
    if( type.isGenericType() ) {
      bGeneric[0] = true;
      for( IGenericTypeVariable tv: type.getGenericTypeVariables() ) {
        sw.visitFormalTypeParameter( tv.getName() );
        IType boundingType = tv.getBoundingType();
        if( boundingType != null ) {
          IType[] types;
          if( boundingType instanceof ICompoundType) {
            types = ((ICompoundType) boundingType).getTypes().toArray(new IType[0]);
          } else {
            types = new IType[] {boundingType};
          }
          SignatureVisitor sv;
          for(int i = types.length-1; i >= 0 ; i--) {
            if( types[i].isInterface() ) {
              sv = sw.visitInterfaceBound();
            }
            else {
              sv = sw.visitClassBound();
            }
            SignatureUtil.visitType( sv, SignatureUtil.getPureGenericType(types[i]), bGeneric );
          }
        }
        else {
          SignatureVisitor sv = sw.visitClassBound();
          SignatureUtil.visitType( sv, JavaTypes.OBJECT(), bGeneric );
        }
      }
    }
    SignatureVisitor sv;
    sv = sw.visitParameterType();
    for( int i = 0; i < _parameters.size() - args.length; i++ )
    {
      IRSymbol implicitParam = _parameters.get( i );
      SignatureUtil.visitIrType( sv, implicitParam.getType() );
    }
    for( int i = 0; i < args.length; i++ )
    {
      IType arg = args[i];
      SignatureUtil.visitType( sv, arg, bGeneric );
    }
    sv = sw.visitReturnType();
    SignatureUtil.visitType( sv, rtype, bGeneric );
    if( bGeneric[0] ) {
      return sw.toString();
    }
    return null;
  }

  public IRStatement getMethodBody() {
    return _methodBody;
  }

  public String getName() {
    return _name;
  }

  public int getModifiers() {
    return _modifiers;
  }

  public List<IRSymbol> getParameters() {
    return _parameters;
  }

  public IRType getReturnType() {
    return _returnType;
  }

  @Override
  public IRTerminalStatement getLeastSignificantTerminalStatement() {
    return null;
  }

  @Override
  public String toString()
  {
    return signature();
  }

  public String signature() {
    return _name + "(" + GosuStringUtil.join( _parameters, ", " ) + "):" + _returnType;
  }

  public void setAnnotations( List<IRAnnotation> irAnnotations )
  {
    _annotations = irAnnotations;
  }

  public List<IRAnnotation> getAnnotations()
  {
    return _annotations;
  }

  public Object[] getAnnotationDefault() {
    return _annotationDefault;
  }

  public String getGenericSignature() {
    return _genericSignature;
  }

  public boolean isExplicitInternal() {
    return _explicitInternal;
  }
}
