/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.statement;

import gw.internal.ext.org.objectweb.asm.signature.SignatureWriter;
import gw.lang.UnstableAPI;
import gw.lang.ir.IRAnnotation;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRType;
import gw.lang.ir.SignatureUtil;
import gw.lang.reflect.IType;

import java.util.Collections;
import java.util.List;

@UnstableAPI
public class IRFieldDecl extends IRStatement {
  private int _modifiers;
  private String _name;
  private IRType _type;
  private Object _value;
  private boolean _explicitInternal;
  private List<IRAnnotation> _annotations = Collections.emptyList();
  private String _genericSignature;

  public IRFieldDecl( int modifiers, boolean explicitInternal, String name, IRType type, IType iType,  Object value ) {
    _modifiers = modifiers;
    _explicitInternal = explicitInternal;
    _name = name;
    _type = maybeEraseStructuralType( type );
    _value = value;
    _genericSignature = makeGenericSignature(iType);
  }

  public IRFieldDecl( int modifiers, boolean explicitInternal, String name, IRType type, Object value ) {
    this( modifiers, explicitInternal, name, type, null, value );
  }

  private String makeGenericSignature( IType type ) {
    if( type == null ) {
      return null;
    }
    boolean[] bGeneric = {false};
    SignatureWriter sw = new SignatureWriter();
    SignatureUtil.visitType( sw, type, bGeneric );
    if( bGeneric[0] ) {
      return sw.toString();
    }
    return null;
  }

  public int getModifiers() {
    return _modifiers;
  }

  public String getName() {
    return _name;
  }

  public IRType getType() {
    return _type;
  }

  public Object getValue() {
    return _value;
  }

  @Override
  public IRTerminalStatement getLeastSignificantTerminalStatement() {
    return null;
  }

  public void setAnnotations( List<IRAnnotation> irAnnotations )
  {
    _annotations = irAnnotations;
  }

  public List<IRAnnotation> getAnnotations()
  {
    return _annotations;
  }

  public String getGenericSignature() {
    return _genericSignature;
  }

  public boolean isExplicitInternal() {
    return _explicitInternal;
  }
}
