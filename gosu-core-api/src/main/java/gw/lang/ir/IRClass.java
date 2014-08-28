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
