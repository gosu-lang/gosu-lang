/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.nodes;

import gw.config.ExecutionMode;
import gw.internal.gosu.ir.transform.util.IRTypeResolver;
import gw.internal.gosu.parser.ClassJavaClassInfo;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.lang.ir.IJavaClassIRType;
import gw.lang.ir.IRType;
import gw.lang.ir.SyntheticIRType;
import gw.lang.parser.ILanguageLevel;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.TypeSystemShutdownListener;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaType;
import gw.util.GosuClassUtil;

import gw.util.Array;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class JavaClassIRType implements IJavaClassIRType {

  private IJavaClassInfo _class;
  private boolean _isArray;
  private boolean _isPrimitive;
  private String _slashName;
  private String _descriptor;

  // These objects don't have to be singletons; it's just cheaper to keep them around instead of re-creating them
  // every single time, since we want to cache information on them
  private static final ConcurrentHashMap<IJavaClassInfo, JavaClassIRType> IR_TYPES_BY_CLASS_INFO = new ConcurrentHashMap<IJavaClassInfo, JavaClassIRType>();
  static {
    TypeSystem.addShutdownListener(new TypeSystemShutdownListener() {
      public void shutdown() {
        IR_TYPES_BY_CLASS_INFO.clear();
      }
    });
  }

  public static IRType get( Class cls ) {
    IJavaClassInfo clsInfo = TypeSystem.getJavaClassInfo(cls, TypeSystem.getGlobalModule());
    return get( clsInfo );
  }

  public static IRType get( IJavaClassInfo cls ) {
    JavaClassIRType javaClassIRType = IR_TYPES_BY_CLASS_INFO.get(cls);
    // NOTE pdalbora 11-Oct-2012 -- There are certain classes, in particular, the entity proxy classes, which get
    // re-defined during tests. So, I added this check to update the cache if the ClassLoader of the incoming class
    // does not match that of the cached class. This means the cache is holding a stale version of the class.
    if (javaClassIRType == null || shouldReplaceAnyway(cls, javaClassIRType)) {
      javaClassIRType = new JavaClassIRType( cls );
      IR_TYPES_BY_CLASS_INFO.put(cls, javaClassIRType);
    }
    return javaClassIRType;
  }

  private static boolean shouldReplaceAnyway(IJavaClassInfo cls, JavaClassIRType javaClassIRType) {
    if( ILanguageLevel.Util.STANDARD_GOSU() )
    {
      return false;
    }

    // Barf...

    return ExecutionMode.isRuntime() && !equal(javaClassIRType.getJavaClassInfo().getBackingClass().getClassLoader(), cls.getBackingClass().getClassLoader());
  }

  private static boolean equal(Object o1, Object o2) {
    return o1 == null ? o2 == null : o1.equals(o2);
  }

  private JavaClassIRType(IJavaClassInfo aClass) {
    _class = aClass;
    
    _isArray = aClass.isArray();
    _isPrimitive = aClass.isPrimitive();

    _slashName = computeSlashName();
    _descriptor = computeDescriptor();
  }

  @Override
  public String getName() {
    return _class.getName();
  }

  @Override
  public String getRelativeName() {
    return _class.getRelativeName();
  }

  @Override
  public String getDescriptor() {
    return _descriptor;
  }

  public IJavaClassInfo getJavaClassInfo() {
    return _class;
  }

  private String computeDescriptor() {
    if (isByte()) {
      return "B";
    } else if (isChar()) {
      return "C";
    } else if (isDouble()) {
      return "D";
    } else if (isFloat()) {
      return "F";
    } else if (isInt()) {
      return "I";
    } else if (isLong()) {
      return "J";
    } else if (isShort()) {
      return "S";
    } else if (isBoolean()) {
      return "Z";
    } else if (isVoid()) {
      return "V";
    } else if (isArray()) {
      return '[' + getComponentType().getDescriptor();
    } else {
      return 'L' + getSlashName() + ';';
    }
  }

  @Override
  //## todo: remove this method; class loading should not happen during compilation
  public Class getJavaClass() {
    if (_class instanceof ClassJavaClassInfo) {
      return ((ClassJavaClassInfo) _class).getJavaClass();
    } else {
      if (isArray()) {
        return Array.newInstance(getComponentType().getJavaClass(), 0).getClass();
      } else {
        if (isPrimitive()) {
          return getPrimitiveClass();
        } else {
//            Class.forName(_class.getName())
          try {
            Class backingClass = _class.getBackingClass();
            return backingClass == null ? thisShouldNeverHappenButDoes() : _class.getBackingClass();
          }
          catch( ClassNotFoundException e ) {
            throw new RuntimeException( e );
          }
        }
      }
    }
  }

  private Class<?> thisShouldNeverHappenButDoes() throws ClassNotFoundException {
    return Class.forName( _class.getName() );
  }

  @Override
  public String getSlashName() {
    return _slashName;
  }

  private String computeSlashName() {
    if (isArray()) {
      return getComponentType().getSlashName() + "[]";
    }

    IType outerType = _class.getEnclosingType();
    if( outerType != null )
    {
      return IRTypeResolver.getDescriptor(outerType).getSlashName( ) + "$" + GosuClassUtil.getNameNoPackage( _class.getName() );
    }

    return _class.getName().replace( '.', '/' );
  }

  @Override
  public boolean isStructural() {
    return false;
  }

  @Override
  public boolean isStructuralAndErased( IRType ownersType ) {
    return false;
  }

  @Override
  public IRType getArrayType() {
    return get( _class.getArrayType() );
  }

  @Override
  public IRType getComponentType() {
    return get( _class.getComponentType() );
  }

  public IType getType() {
    return TypeSystem.get( _class );
  }

  @Override
  public boolean isArray() {
    return _isArray;
  }

  @Override
  public boolean isAssignableFrom(IRType otherType) {
    if (_class.getName().equals(Object.class.getName()) && !otherType.isPrimitive()) {
      return true;
    }

    if (isArray() && otherType.isArray()) {
      return getComponentType().isAssignableFrom(otherType.getComponentType());
    } else if (isArray() || otherType.isArray()) {
      return false;
    }

    if (otherType instanceof JavaClassIRType) {
      return _class.isAssignableFrom(((JavaClassIRType) otherType)._class);
    } else if (otherType instanceof GosuClassIRType) {
      Set<? extends IType> allTypesInHierarchy = ((GosuClassIRType)otherType).getType().getAllTypesInHierarchy();
      for (IType hierarchyType : allTypesInHierarchy) {
        IJavaClassInfo javaClassForType = resolveJavaClassForType(hierarchyType);
        if (javaClassForType != null && javaClassForType.getName().equals(_class.getName())) {
          return true;
        }
      }

      return false;
    } else if (otherType instanceof SyntheticIRType) {
      return _class.isAssignableFrom(TypeSystem.getJavaClassInfo(((SyntheticIRType) otherType).getSuperClass(), TypeSystem.getGlobalModule()));
    } else {
      return false;
    }
  }

  private IJavaClassInfo resolveJavaClassForType( IType hierarchyType ) {
    if (hierarchyType instanceof IJavaType) {
      return ((IJavaType) hierarchyType).getBackingClassInfo();
    }

    if (hierarchyType instanceof IGosuClassInternal) {
      IGosuClassInternal gosuClass = (IGosuClassInternal) hierarchyType;
      if (gosuClass.isProxy()) {
        return gosuClass.getJavaType().getBackingClassInfo();
      }
    }

    return null;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof JavaClassIRType && _class.getNameSignature().equals(((JavaClassIRType) obj)._class.getNameSignature());
  }

  @Override
  public boolean isByte() {
    return _class.getName().equals( byte.class.getName() );
  }

  @Override
  public boolean isBoolean() {
    return _class.getName().equals( boolean.class.getName() );
  }

  @Override
  public boolean isShort() {
    return _class.getName().equals( short.class.getName() );
  }

  @Override
  public boolean isChar() {
    return _class.getName().equals( char.class.getName() );
  }

  @Override
  public boolean isInt() {
    return _class.getName().equals( int.class.getName() );
  }

  @Override
  public boolean isLong() {
    return _class.getName().equals( long.class.getName() );
  }

  @Override
  public boolean isFloat() {
    return _class.getName().equals( float.class.getName() );
  }

  @Override
  public boolean isDouble() {
    return _class.getName().equals( double.class.getName() );
  }

  @Override
  public boolean isVoid() {
    return _class.getName().equals( void.class.getName() );
  }

  @Override
  public boolean isPrimitive() {
    return _isPrimitive;
  }

  @Override
  public boolean isInterface() {
    return _class.isInterface();
  }

  @Override
  public String toString()
  {
    return getName();
  }

  public Class getPrimitiveClass() {
    if (isBoolean()) {
      return boolean.class;
    } else if (isByte()) {
      return byte.class;
    } else if (isChar()) {
      return char.class;
    } else if (isShort()) {
      return short.class;
    } else if (isInt()) {
      return int.class;
    } else if (isLong()) {
      return long.class;
    } else if (isFloat()) {
      return float.class;
    } else if (isDouble()) {
      return double.class;
    } else if (isVoid()) {
      return void.class;
    } else {
      throw new IllegalStateException("Cannot ask for primitive class from " + getName());
    }
  }
}
