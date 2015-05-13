/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode;

import gw.internal.ext.org.objectweb.asm.AnnotationVisitor;
import gw.internal.ext.org.objectweb.asm.Attribute;
import gw.internal.ext.org.objectweb.asm.ClassVisitor;
import gw.internal.ext.org.objectweb.asm.ClassWriter;
import gw.internal.ext.org.objectweb.asm.FieldVisitor;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;
import gw.internal.ext.org.objectweb.asm.Opcodes;
import gw.internal.ext.org.objectweb.asm.TypePath;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaBackedTypeData;

public class GosuClassWriter extends ClassVisitor
{
  private final ClassWriter _cw;

  public GosuClassWriter()
  {
    super( Opcodes.ASM5 );
    _cw = new MyWriter();
  }

  @Override
  public MethodVisitor visitMethod( int i, String s, String s2, String s3, String[] strings )
  {
    return new GosuMethodVisitor( _cw.visitMethod( i, s, s2, s3, strings ) );
  }

  public byte[] toByteArray() {
    return _cw.toByteArray();
  }


  @Override
  public void visit( int version, int access, String name, String signature, String superName, String[] interfaces ) {
    _cw.visit( version, access, name, signature, superName, interfaces );
  }

  @Override
  public AnnotationVisitor visitAnnotation( String desc, boolean visible ) {
    return _cw.visitAnnotation( desc, visible );
  }

  @Override
  public void visitAttribute( Attribute attribute ) {
    _cw.visitAttribute( attribute );
  }

  @Override
  public void visitEnd() {
    _cw.visitEnd();
  }

  @Override
  public FieldVisitor visitField( int access, String name, String desc, String signature, Object value ) {
    return _cw.visitField( access, name, desc, signature, value );
  }

  @Override
  public void visitInnerClass( String name, String outerName, String innerName, int access ) {
    _cw.visitInnerClass( name, outerName, innerName, access );
  }

  @Override
  public void visitOuterClass( String owner, String name, String desc ) {
    _cw.visitOuterClass( owner, name, desc );
  }

  @Override
  public void visitSource( String source, String debug ) {
    _cw.visitSource( source, debug );
  }

  @Override
  public AnnotationVisitor visitTypeAnnotation( int i, TypePath typePath, String s, boolean b ) {
    return _cw.visitTypeAnnotation( i, typePath, s, b );
  }

  private static class MyWriter extends ClassWriter {
    public MyWriter() {
      super( ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES );
    }

    @Override
    protected String getCommonSuperClass( String type1, String type2 ) {
      IType t1 = TypeSystem.getByFullNameIfValid( type1.replace( '/', '.' ).replace( '$', '.' ) );
      IType t2 = TypeSystem.getByFullNameIfValid( type2.replace( '/', '.' ).replace( '$', '.' ) );
      if( t1 != null && t2 != null ) {
        if( t1.isAssignableFrom( t2 ) ) {
          return type1;
        }
        if( t2.isAssignableFrom( t1 ) ) {
          return type2;
        }
        if( t1.isInterface() || t2.isInterface() ) {
          return "java/lang/Object";
        }
      }
      do {
        t1 = t1.getSupertype();
        if( t1 == null ) {
          return "java/lang/Object";
        }
      } while( !t1.isAssignableFrom( t2 ) );

      String superTypeName;

      if( t1 instanceof IJavaBackedTypeData ) {
        // Ensure we use the backing Java class's name e.g., entity types use a different name
        superTypeName = ((IJavaBackedTypeData)t1).getBackingClassInfo().getName();
      }
      else {
        superTypeName = t1.getName();
      }
      return superTypeName.replace( '.', '/' );
    }
  }
}
