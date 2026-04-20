/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir;

import gw.internal.ext.org.objectweb.asm.signature.SignatureVisitor;
import gw.internal.ext.org.objectweb.asm.signature.SignatureWriter;
import gw.lang.ir.statement.IRFieldDecl;
import gw.lang.ir.statement.IRMethodStatement;
import gw.lang.UnstableAPI;
import gw.lang.reflect.ICompoundType;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.java.JavaTypes;

import java.util.List;
import java.util.ArrayList;

@UnstableAPI
public class IRClass {
  private int _modifiers;
  private String _name;
  private IRType _thisType;
  private IRType _superType;
  private List<IRType> _interfaces = new ArrayList<IRType>();
  private List<InnerClassInfo> _innerClasses = new ArrayList<InnerClassInfo>();
  private String _sourceFile;
  private List<IRMethodStatement> _methods = new ArrayList<IRMethodStatement>();
  private List<IRFieldDecl> _fields = new ArrayList<IRFieldDecl>();
  private List<IRAnnotation> _annotations = new ArrayList<IRAnnotation>();
  private String _genericSignature;

  public IRClass() {
  }

  public int getModifiers() {
    return _modifiers;
  }

  public void setModifiers(int modifiers) {
    _modifiers = modifiers;
  }

  public String getName() {
    return _name;
  }

  public void setName(String name) {
    _name = name;
  }

  public IRType getThisType() {
    return _thisType;
  }

  public void setThisType(IRType thisType) {
    _thisType = thisType;
  }

  public IRType getSuperType() {
    return _superType;
  }

  public void setSuperType(IRType superType) {
    _superType = superType;
  }

  public void addInterface(IRType iface) {
    _interfaces.add(iface);
  }

  public List<IRType> getInterfaces() {
    return _interfaces;
  }

  public String getSourceFile() {
    return _sourceFile;
  }

  public void setSourceFile(String sourceFile) {
    _sourceFile = sourceFile;
  }

  public void addInnerClass(IRType innerClass, IRType enclosingType, int modifiers) {
    _innerClasses.add(new InnerClassInfo(innerClass, enclosingType, modifiers));
  }

  public void addMethod(IRMethodStatement method) {
    _methods.add(method);
  }

  public void addField(IRFieldDecl field) {
    _fields.add(field);
  }

  public List<InnerClassInfo> getInnerClasses() {
    return _innerClasses;
  }

  public List<IRMethodStatement> getMethods() {
    return _methods;
  }

  public List<IRFieldDecl> getFields() {
    return _fields;
  }

  public List<IRAnnotation> getAnnotations() {
    return _annotations;
  }

  public void setAnnotations( List<IRAnnotation> annotations )
  {
    _annotations = annotations;
  }

  public void makeGenericSignature( IType type ) {
    boolean[] bGeneric = {false};
    SignatureWriter sw = new SignatureWriter();
    //sw.visitClassType( _name.replace( '.', '/' ) );
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
          emitBounds( sw, types, bGeneric );
        }
        else {
          SignatureVisitor sv = sw.visitClassBound();
          SignatureUtil.visitType( sv, JavaTypes.OBJECT(), bGeneric );
        }
      }
    }
    if( type.getSupertype() != null ) {
      SignatureVisitor sv = sw.visitSuperclass();
      SignatureUtil.visitType( sv, type.getSupertype(), bGeneric );
    }
    else {
      SignatureVisitor sv = sw.visitSuperclass();
      SignatureUtil.visitType( sv, JavaTypes.OBJECT(), bGeneric );
    }

    if( type.getInterfaces() != null ) {
      for( IType iface: type.getInterfaces() ) {
        SignatureVisitor sv = sw.visitInterface();
        SignatureUtil.visitType( sv, iface, bGeneric );
      }
    }
    if( bGeneric[0] ) {
      _genericSignature = sw.toString();
    }
  }

  // JVM spec: TypeParameter ::= Identifier ClassBound InterfaceBound*
  // ASM's SignatureWriter.visitClassBound() is a no-op — the ':' that introduces the class
  // bound is written by visitFormalTypeParameter, so the class bound must be emitted first
  // (and at most one). Emitting interface bounds first, or a class bound after any interface
  // bound, produces a malformed signature (e.g. "<P::LI;LC;>" with a missing ':' before LC).
  public static void emitBounds( SignatureWriter sw, IType[] types, boolean[] bGeneric ) {
    IType classBound = null;
    List<IType> interfaceBounds = new ArrayList<>( types.length );
    for( IType t : types ) {
      if( t.isInterface() ) {
        interfaceBounds.add( t );
      } else if( classBound == null ) {
        classBound = t;
      } else {
        // More than one non-interface bound is not legal, but we keep one and
        // demote the rest to interface slots to avoid producing garbage — the
        // parser should have rejected this case earlier.
        interfaceBounds.add( t );
      }
    }
    if( classBound != null ) {
      SignatureVisitor sv = sw.visitClassBound();
      SignatureUtil.visitType( sv, SignatureUtil.getPureGenericType( classBound ), bGeneric );
    }
    for( IType iface : interfaceBounds ) {
      SignatureVisitor sv = sw.visitInterfaceBound();
      SignatureUtil.visitType( sv, SignatureUtil.getPureGenericType( iface ), bGeneric );
    }
  }

  public String getGenericSignature() {
    return _genericSignature;
  }

  public static class InnerClassInfo {
    private IRType _innerClass;
    private IRType _enclosingType;
    private int _modifiers;

    public InnerClassInfo(IRType innerClass, IRType enclosingType, int modifiers) {
      _innerClass = innerClass;
      _enclosingType = enclosingType;
      _modifiers = modifiers;
    }

    public IRType getInnerClass() {
      return _innerClass;
    }

    public IRType getEnclosingType() {
      return _enclosingType;
    }

    public int getModifiers() {
      return _modifiers;
    }
  }
}
