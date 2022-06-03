/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java.asm;

import gw.fs.IFile;
import gw.internal.ext.org.objectweb.asm.AnnotationVisitor;
import gw.internal.ext.org.objectweb.asm.Attribute;
import gw.internal.ext.org.objectweb.asm.ClassReader;
import gw.internal.ext.org.objectweb.asm.ClassVisitor;
import gw.internal.ext.org.objectweb.asm.FieldVisitor;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;
import gw.internal.ext.org.objectweb.asm.Opcodes;
import gw.internal.ext.org.objectweb.asm.signature.SignatureReader;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IAsmJavaClassInfo;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.module.IModule;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class AsmClass implements IAsmType, IGeneric {
  public static final AsmClass BYTE;
  public static final AsmClass SHORT;
  public static final AsmClass CHAR;
  public static final AsmClass INT;
  public static final AsmClass LONG;
  public static final AsmClass FLOAT;
  public static final AsmClass DOUBLE;
  public static final AsmClass BOOLEAN;
  public static final AsmClass VOID;
  private static final Map<String, AsmClass> PRIMITIVES = new HashMap<>();
  static {
    PRIMITIVES.put( "byte", BYTE = new AsmClass( AsmPrimitiveType.findPrimitive( "byte" ) ) );
    PRIMITIVES.put( "short", SHORT = new AsmClass( AsmPrimitiveType.findPrimitive( "short" ) ) );
    PRIMITIVES.put( "char", CHAR = new AsmClass( AsmPrimitiveType.findPrimitive( "char" ) ) );
    PRIMITIVES.put( "int", INT = new AsmClass( AsmPrimitiveType.findPrimitive( "int" ) ) );
    PRIMITIVES.put( "long", LONG = new AsmClass( AsmPrimitiveType.findPrimitive( "long" ) ) );
    PRIMITIVES.put( "float", FLOAT = new AsmClass( AsmPrimitiveType.findPrimitive( "float" ) ) );
    PRIMITIVES.put( "double", DOUBLE = new AsmClass( AsmPrimitiveType.findPrimitive( "double" ) ) );
    PRIMITIVES.put( "boolean", BOOLEAN = new AsmClass( AsmPrimitiveType.findPrimitive( "boolean" ) ) );
    PRIMITIVES.put( "void", VOID = new AsmClass( AsmPrimitiveType.findPrimitive( "void" ) ) );
  }
  public static AsmClass findPrimitive( String className ) {
    return PRIMITIVES.get( className );
  }

  private Object _module;
  private Object _file;
  private URI _uri;
  private int _version;
  private int _modifiers;
  private AsmType _type;
  private AsmType _superClass;
  private List<AsmType> _interfaces;
  private AsmType _enclosingType;
  private boolean _bGeneric;
  private Map<String, AsmInnerClassType> _innerClasses;
  private List<AsmField> _fields;
  private List<AsmMethod> _methodsAndCtors;
  private List<AsmAnnotation> _annotations;
  private volatile boolean _inited;
  private volatile boolean _initing;

  AsmClass( String fqn, Object module, URI uri, Object file ) {
    _type = AsmUtil.makeNonPrimitiveType( fqn );
    _module = module;
    _uri = uri;
    _file = file;
  }

  private AsmClass( AsmPrimitiveType ptype ) {
    _type = ptype;
    _inited = true;
    _modifiers = Modifier.PUBLIC | Modifier.STATIC;
    _superClass = null;
    _innerClasses = Collections.emptyMap();
    _interfaces = Collections.emptyList();
    _fields = Collections.emptyList();
    _methodsAndCtors = Collections.emptyList();
    _annotations = Collections.emptyList();
  }

  private void init() {
    if( _inited ) {
      return;
    }
    synchronized( this ) {
      if( _inited || _initing ) {
        return;
      }
      _initing = true;
      byte[] classBytes = getContent();
      ClassReader cr = new ClassReader( classBytes );
      cr.accept( new AsmClassVisitor(), ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES );
      _initing = false;
      _inited = true;
    }
  }

  public URI getUri() {
    return _uri;
  }

  public AsmType getType() {
    init();
    return _type;
  }

  public AsmType getRawType() {
    return _type.getRawType();
  }

  public AsmType getEnclosingType() {
    init();
    return _enclosingType;
  }

  public AsmType getComponentType() {
    return null;
  }

  public AsmType getSuperClass() {
    init();
    return _superClass;
  }
  public void setSuperClass( AsmType type ) {
    _superClass = type;
  }

  public Map<String, AsmInnerClassType> getInnerClasses() {
    init();
    return _innerClasses;
  }

  public List<AsmType> getInterfaces() {
    init();
    return _interfaces;
  }

  public List<AsmField> getDeclaredFields() {
    init();
    return _fields;
  }
  private void addField( AsmField field ) {
    if( _fields.isEmpty() ) {
      _fields = new ArrayList<>();
    }
    _fields.add( field );
  }

  public List<AsmMethod> getDeclaredMethodsAndConstructors() {
    init();
    return _methodsAndCtors;
  }
  private void addMethod( AsmMethod method ) {
    if( _methodsAndCtors.isEmpty() ) {
      _methodsAndCtors = new ArrayList<>();
    }
    _methodsAndCtors.add( method );
  }

  public List<AsmAnnotation> getDeclaredAnnotations() {
    init();
    return _annotations;
  }
  private void addAnnotation( AsmAnnotation annotation ) {
    if( _annotations.isEmpty() ) {
      _annotations = new ArrayList<>();
    }
    _annotations.add( annotation );
  }

  public boolean isGeneric() {
    init();
    return _bGeneric;
  }
  public void setGeneric() {
    _bGeneric = true;
  }

  public int getModifiers() {
    init();
    return _modifiers;
  }

  public int getVersion() {
    init();
    return _version;
  }

  public String toString() {
    return _type.getName();
  }

  public String getName() {
    return _type.getName();
  }

  public String getNameWithArrayBrackets() {
    return _type.getNameWithArrayBrackets();
  }

  @Override
  public String getSimpleName() {
    init();
    String name = _type.getSimpleName();
    int iDollar = _enclosingType == null ? -1 : name.lastIndexOf( '$' );
    if( iDollar > 0 ) {
      name = name.substring( iDollar+1 );
    }
    return name;
  }

  @Override
  public List<AsmType> getTypeParameters() {
    init();
    // Only valid if type params are TYPE VARIABLES
    return _type.getTypeParameters();
  }

  @Override
  public boolean isParameterized() {
    // AsmClass is never parameterized, but can be generic where type params are *type variables*
    return false;
  }

  @Override
  public boolean isArray() {
    return false;
  }

  @Override
  public boolean isTypeVariable() {
    return false;
  }

  @Override
  public boolean isPrimitive() {
    return _type.isPrimitive();
  }

  @Override
  public String getFqn() {
    init();
    return _type.getFqn();
  }

  public boolean isInterface() {
    init();
    return Modifier.isInterface( getModifiers() );
  }

  public boolean isEnum() {
    init();
    return Modifier.isEnum( getModifiers() );
  }

  public boolean isAnnotation() {
    init();
    return (getModifiers() & 0x00002000) != 0;
  }

  public boolean isAnnotationPresent( Class<? extends Annotation> annotationClass ) {
    init();
    return getAnnotation( annotationClass ) != null;
  }

  public AsmAnnotation getAnnotation( Class<?> annotationClass ) {
    init();
    for( AsmAnnotation anno: getDeclaredAnnotations() ) {
      if( annotationClass.getName().equals( anno.getType().getName() ) ) {
        return anno;
      }
    }
    return null;
  }

  private class AsmClassVisitor extends ClassVisitor {
    public AsmClassVisitor() {
      super( Opcodes.ASM7 );
    }

    @Override
    public void visit( int version, int access, String name, String signature, String superName, String[] interfaces ) {
      _type = AsmUtil.makeNonPrimitiveType(name);
      AsmClass outerClass = ensureOuterIsLoadedFirst(); // barf
      _version = version;
      _modifiers = access;
      if( outerClass != null ) {
        barf( outerClass );
      }
      _superClass = Modifier.isInterface( access ) ? null : (superName != null ? AsmUtil.makeType( superName ) : null);
      _innerClasses = Collections.emptyMap();
      _fields = Collections.emptyList();
      _methodsAndCtors = Collections.emptyList();
      _annotations = Collections.emptyList();
      assignInterfaces( interfaces );
      assignGenericInfo( signature );
    }

    private void barf( AsmClass outerClass ) {
      AsmInnerClassType innerClass = outerClass.getInnerClasses().get( _type.getName() );
      if( innerClass !=  null ) {
        _modifiers = innerClass.getModifiers();
      }
    }

    private AsmClass ensureOuterIsLoadedFirst() {
      String typeName = _type.getName();
      int iDollar = typeName.lastIndexOf( '$' );
      if( iDollar > 0 ) {
        String outerName = typeName.substring( 0, iDollar );
        IJavaClassInfo classInfo = TypeSystem.getJavaClassInfo( outerName, (IModule)_module );
        if( classInfo != null ) {
          _enclosingType = AsmUtil.makeType( outerName );
          return (AsmClass)((IAsmJavaClassInfo)classInfo).getAsmType();
        }
      }
      return null;
    }

    @Override
    public void visitSource( String s, String s2 ) {
    }

    @Override
    public void visitOuterClass( String owner, String name, String desc ) {
      _enclosingType = owner == null ? null : AsmUtil.makeType( owner );
    }

    @Override
    public AnnotationVisitor visitAnnotation( String desc, boolean bVisibleAtRuntime ) {
      AsmAnnotation asmAnnotation = new AsmAnnotation( desc, bVisibleAtRuntime );
      addAnnotation( asmAnnotation );
      return new AsmAnnotationVisitor( asmAnnotation );
    }

    @Override
    public void visitAttribute( Attribute attribute ) {
    }

    @Override
    public void visitInnerClass( String name, String outerName, String innerName, int access ) {
      if( outerName != null && !AsmUtil.makeDotName( outerName ).equals( getType().getName() ) ) {
        return;
      }
      if( innerName == null ) {
        // anonymous
        return;
      }

      int iDollar = name.lastIndexOf( '$' );
      if( iDollar >= 0 && iDollar < name.length() - 1 &&
          Character.isDigit( name.charAt( iDollar + 1 ) ) ) {
        // local inner class
        return;
      }

      if( _innerClasses.isEmpty() ) {
        _innerClasses = new HashMap<>( 2 );
      }
      String innerClass = AsmUtil.makeDotName( name );
      _innerClasses.put( innerClass, new AsmInnerClassType( innerClass, access ) );
    }

    @Override
    public FieldVisitor visitField( int access, String name, String desc, String signature, Object value ) {
      AsmField field = new AsmField( AsmClass.this, access, name, desc, value );
      if( signature != null ) {
        SignatureReader sr = new SignatureReader( signature );
        DeclarationPartSignatureVisitor visitor = new DeclarationPartSignatureVisitor();
        sr.accept( visitor );
        field.setType( visitor.getCurrentType() );
      }
      addField( field );
      return new FieldDeclarationVisitor( field );
    }

    @Override
    public MethodVisitor visitMethod( int access, String name, String desc, String signature, String[] exceptions ) {
      AsmMethod method = new AsmMethod( AsmClass.this, access, name, desc, exceptions );
      if( signature != null ) {
        SignatureReader sr = new SignatureReader( signature );
        MethodDeclarationSignatureVisitor visitor = new MethodDeclarationSignatureVisitor( method );
        sr.accept( visitor );
        method.update( visitor.getParamVisitors(), visitor.getReturnVisitor(), visitor.getExceptionVisitors() );
      }
      addMethod( method );
      return new MethodDeclarationVisitor( method );
    }

    @Override
    public void visitEnd() {
    }

    private void assignGenericInfo( String signature ) {
      if( signature != null ) {
        SignatureReader sr = new SignatureReader( signature );
        TypeDeclarationSignatureVisitor visitor = new TypeDeclarationSignatureVisitor( AsmClass.this );
        sr.accept( visitor );
        visitor.update();
      }
    }

    private void assignInterfaces( String[] interfaces ) {
      if( interfaces != null ) {
        List<AsmType> ifaces = new ArrayList<>( interfaces.length );
        //noinspection ForLoopReplaceableByForEach
        for( int i = 0; i < interfaces.length; i++ ) {
          ifaces.add( AsmUtil.makeType( interfaces[i] ) );
        }
        _interfaces = ifaces;
      }
      else {
        _interfaces = Collections.emptyList();
      }
    }
  }

  private byte[] getContent() {
    InputStream is;
    try {
      is = _file instanceof File ? new FileInputStream( (File)_file ) : ((IFile)_file).openInputStream();
    }
    catch( IOException e ) {
      throw new RuntimeException( e );
    }
    byte[] buf = new byte[1024];
    AsmClassLoader.ExposedByteArrayOutputStream out = new AsmClassLoader.ExposedByteArrayOutputStream();
    while( true ) {
      int count;
      try {
        count = is.read( buf );
      }
      catch( IOException e ) {
        throw new RuntimeException( e );
      }
      if( count < 0 ) {
        break;
      }
      out.write( buf, 0, count );
    }
    try {
      out.flush();
      is.close();
      return out.getByteArray();
    }
    catch( Exception e ) {
      throw new RuntimeException( e );
    }
  }
}
