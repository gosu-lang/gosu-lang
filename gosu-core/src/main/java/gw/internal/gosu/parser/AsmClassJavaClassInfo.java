/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.SourcePositions;
import gw.internal.gosu.parser.java.classinfo.AsmClassAnnotationInfo;
import gw.internal.gosu.parser.java.classinfo.JavaArrayClassInfo;
import gw.internal.gosu.parser.java.classinfo.PropertyDeriver;
import gw.lang.reflect.java.JavaSourceElement;
import gw.internal.gosu.parser.java.classinfo.JavaSourceMethodDescriptor;
import gw.internal.gosu.parser.java.classinfo.JavaSourceType;
import gw.internal.gosu.parser.java.classinfo.JavaSourceUtil;
import gw.lang.GosuShop;
import gw.lang.javadoc.IClassDocNode;
import gw.lang.reflect.EnumValuePlaceholder;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IEnumValue;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.reflect.IType;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.java.AbstractJavaClassInfo;
import gw.lang.reflect.java.IAsmJavaClassInfo;
import gw.lang.reflect.java.IJavaClassConstructor;
import gw.lang.reflect.java.IJavaClassField;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.IJavaClassTypeVariable;
import gw.lang.reflect.java.IJavaMethodDescriptor;
import gw.lang.reflect.java.IJavaPropertyDescriptor;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.asm.AsmAnnotation;
import gw.lang.reflect.java.asm.AsmBackingSourceFileHandle;
import gw.lang.reflect.java.asm.AsmClass;
import gw.lang.reflect.java.asm.AsmField;
import gw.lang.reflect.java.asm.AsmInnerClassType;
import gw.lang.reflect.java.asm.AsmMethod;
import gw.lang.reflect.java.asm.AsmType;
import gw.lang.reflect.module.IModule;
import gw.util.concurrent.LocklessLazyVar;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import manifold.api.fs.IDirectory;
import manifold.api.fs.IFile;

public class AsmClassJavaClassInfo extends AsmTypeJavaClassType implements IAsmJavaClassInfo {
  private AsmClass _class;
  private IJavaClassMethod[] _declaredMethods;
  private IJavaClassInfo[] _interfaces;
  private IJavaClassInfo _superclass;
  private IJavaClassTypeVariable[] _typeVariables;
  private IJavaClassField[] _declaredFields;
  private IJavaClassConstructor[] _declaredConstructors;
  private IAnnotationInfo[] _declaredAnnotations;
  private IJavaPropertyDescriptor[] _propertyDescriptors;
  private IJavaMethodDescriptor[] _methodDescriptors;
  private IJavaClassField[] _allFields;
  private IJavaClassType[] _genericInterfaces;
  private IJavaClassInfo[] _declaredClasses;
  private LocklessLazyVar<IType> _enclosingClass = new LocklessLazyVar<IType>() {
    protected IType init() {
      AsmType enclosingClass = _class.getEnclosingType();
      return enclosingClass == null ? null : TypeSystem.getByFullName( enclosingClass.getName(), _module );
    }
  };
  private IEnumValue[] _enumConstants;
  private String _simpleName;
  private String _namespace;
  private IType _javaType;
  private ISourceFileHandle _sourceFileHandle;
  private IJavaClassInfo _sourceType;

  public AsmClassJavaClassInfo( AsmClass cls, IModule module ) {
    super( cls, module );
    if( cls == null ) {
      throw new IllegalArgumentException( "Class cannot be null." );
    }
    _class = cls;
    _module = module;
  }

  @Override
  public boolean isAnnotation() {
    return _class.isAnnotation();
  }

  @Override
  public boolean isInterface() {
    return _class.isInterface();
  }

  @Override
  public IJavaClassType getConcreteType() {
    return this;
  }

  @Override
  public String getName() {
    return _class.getNameWithArrayBrackets();
  }

  public String getNameSignature() {
    return GosuShop.toSignature( _class.getFqn() );
  }

  @Override
  public IJavaClassMethod getMethod( String methodName, IJavaClassInfo... paramTypes ) throws NoSuchMethodException {
    outer:
    for( IJavaClassMethod method : getDeclaredMethods() ) {
      if( !method.getName().equals( methodName ) ) {
        continue;
      }
      IJavaClassInfo[] methodParamTypes = method.getParameterTypes();
      if( paramTypes.length != methodParamTypes.length ) {
        continue;
      }
      for( int i = 0; i < paramTypes.length; i++ ) {
        if( !paramTypes[i].equals( methodParamTypes[i] ) ) {
          continue outer;
        }
      }
      return method;
    }

    IJavaClassInfo superclass = getSuperclass();
    if( superclass != null ) {
      // This is to be consistent with Sun's crappy impl:
      // http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6815786
      // http://www.youtube.com/watch?v=AG7LjVCj50Y
      return superclass.getMethod( methodName, paramTypes );
    }

    throw new NoSuchMethodException();
  }

  public IJavaClassMethod getDeclaredMethod( String methodName, IJavaClassInfo... paramTypes ) throws NoSuchMethodException {
    return getMethod( methodName, paramTypes );
  }

  public IJavaClassMethod[] getDeclaredMethods() {
    if( _declaredMethods == null ) {
      List<AsmMethod> asmMethods = _class.getDeclaredMethodsAndConstructors();
      List<IJavaClassMethod> methods = new ArrayList<IJavaClassMethod>( asmMethods.size() );
      for( AsmMethod asmMethod : asmMethods ) {
        if( !asmMethod.isConstructor() && !asmMethod.isSynthetic() ) {
          methods.add( new AsmMethodJavaClassMethod( asmMethod, _module ) );
        }
      }
      _declaredMethods = methods.toArray( new IJavaClassMethod[methods.size()] );
    }
    return _declaredMethods;
  }

  @Override
  public Object newInstance() throws InstantiationException, IllegalAccessException {
    return null;
  }

  @Override
  public Object[] getEnumConstants() {
    if( _enumConstants == null ) {
      List<IEnumValue> enums = new ArrayList<IEnumValue>();
      IJavaClassField[] fields = getFields();
      for( IJavaClassField field : fields ) {
        if( field.isEnumConstant() ) {
          enums.add( new EnumValuePlaceholder( field.getName() ) );
        }
      }
      _enumConstants = enums.toArray( new IEnumValue[enums.size()] );
    }
    return _enumConstants;
  }

  @Override
  public IType getJavaType() {
    return _javaType == null ? (_javaType = TypeSystem.get(this)) : _javaType;
  }
  public void setJavaType( IJavaType javaType ) {
    _javaType = javaType;
  }

  @Override
  public IJavaClassInfo[] getInterfaces() {
    if( _interfaces == null ) {
      List<AsmType> rawInterfaces = _class.getInterfaces();
      List<IJavaClassInfo> interfaces = new ArrayList<>( rawInterfaces.size() );
      for( int i = 0; i < rawInterfaces.size(); i++ ) {
        IJavaClassInfo classInfo = JavaSourceUtil.getClassInfo( rawInterfaces.get( i ).getName(), _module );
        if( classInfo != null ) {
          // only add if non-null, classInfo may be null if not enough info is around to build the type
          interfaces.add( classInfo );
        }
      }
      _interfaces = interfaces.toArray( new IJavaClassInfo[interfaces.size()] );
    }
    return _interfaces;
  }

  @Override
  public IJavaClassInfo getSuperclass() {
    if( _superclass == null ) {
      _superclass = _class.getSuperClass() == null ? NULL_TYPE : JavaSourceUtil.getClassInfo( _class.getSuperClass().getName(), _module );
    }
    return _superclass == NULL_TYPE ? null : _superclass;
  }

  public IJavaClassTypeVariable[] getTypeParameters() {
    if( _typeVariables == null ) {
      List<AsmType> rawTypeVariables = _class.getTypeParameters();
      IJavaClassTypeVariable[] typeVariables = new IJavaClassTypeVariable[rawTypeVariables.size()];
      for( int i = 0; i < rawTypeVariables.size(); i++ ) {
        typeVariables[i] = new AsmTypeVariableJavaClassTypeVariable( rawTypeVariables.get( i ), _module );
      }
      _typeVariables = typeVariables;
    }
    return _typeVariables;
  }

  @Override
  public IJavaClassField[] getDeclaredFields() {
    if( _declaredFields == null ) {
      List<AsmField> rawFields = _class.getDeclaredFields();
      IJavaClassField[] fields = new IJavaClassField[rawFields.size()];
      for( int i = 0; i < rawFields.size(); i++ ) {
        fields[i] = new AsmFieldJavaClassField( rawFields.get( i ), _module );
      }
      _declaredFields = fields;
    }
    return _declaredFields;
  }

  @Override
  public IJavaClassConstructor[] getDeclaredConstructors() {
    if( _declaredConstructors == null ) {
      List<AsmMethod> asmMethods = _class.getDeclaredMethodsAndConstructors();
      List<IJavaClassConstructor> ctors = new ArrayList<IJavaClassConstructor>( asmMethods.size() );
      for( AsmMethod asmMethod : asmMethods ) {
        if( asmMethod.isConstructor() && !asmMethod.isSynthetic() ) {
          ctors.add( new AsmConstructorJavaClassConstructor( asmMethod, _module ) );
        }
      }
      _declaredConstructors = ctors.toArray( new IJavaClassConstructor[ctors.size()] );
    }
    return _declaredConstructors;
  }

  public IJavaClassConstructor getConstructor( IJavaClassInfo... paramTypes ) throws NoSuchMethodException {
    outer:
    for( IJavaClassConstructor ctor : getDeclaredConstructors() ) {
      IJavaClassInfo[] methodParamTypes = ctor.getParameterTypes();
      if( paramTypes.length != methodParamTypes.length ) {
        continue;
      }
      for( int i = 0; i < paramTypes.length; i++ ) {
        if( !paramTypes[i].equals( methodParamTypes[i] ) ) {
          continue outer;
        }
      }
      return ctor;
    }
    throw new NoSuchMethodException();
  }

  @Override
  public boolean isAnnotationPresent( Class<? extends Annotation> annotationClass ) {
    return _class.isAnnotationPresent( annotationClass );
  }

  @Override
  public IAnnotationInfo getAnnotation( Class annotationClass ) {
    for( IAnnotationInfo annotationInfo : getDeclaredAnnotations() ) {
      if( annotationInfo.getName().equals( annotationClass.getName() ) ) {
        return annotationInfo;
      }
    }
    return null;
  }

  @Override
  public IAnnotationInfo[] getDeclaredAnnotations() {
    if( _declaredAnnotations == null ) {
      List<AsmAnnotation> annotations = _class.getDeclaredAnnotations();
      IAnnotationInfo[] declaredAnnotations = new IAnnotationInfo[annotations.size()];
      for( int i = 0; i < declaredAnnotations.length; i++ ) {
        declaredAnnotations[i] = new AsmClassAnnotationInfo( annotations.get( i ), this );
      }
      _declaredAnnotations = declaredAnnotations;
    }
    return _declaredAnnotations;
  }

  @Override
  public IClassDocNode createClassDocNode() {
    return null;
  }

  @Override
  public IJavaPropertyDescriptor[] getPropertyDescriptors() {
    if( _propertyDescriptors == null ) {
      _propertyDescriptors = PropertyDeriver.initPropertyDescriptors( this );
    }
    return _propertyDescriptors;
  }

  @Override
  public IJavaMethodDescriptor[] getMethodDescriptors() {
    if( _methodDescriptors == null ) {
      IJavaClassMethod[] declaredMethods = getDeclaredMethods();
      _methodDescriptors = new IJavaMethodDescriptor[declaredMethods.length];
      for( int i = 0; i < declaredMethods.length; i++ ) {
        _methodDescriptors[i] = new JavaSourceMethodDescriptor( declaredMethods[i] );
      }
    }
    return _methodDescriptors;
  }

  @Override
  public boolean hasCustomBeanInfo() {
    return false;
  }

  @Override
  public String getRelativeName() {
    return getName().substring( getNamespace().length() + 1 );
  }

  @Override
  public String getDisplayName() {
    return getSimpleName();
  }

  @Override
  public String getSimpleName() {
    if( _simpleName == null ) {
      _simpleName = _class.getSimpleName();
    }
    return _simpleName;
  }

  @Override
  public boolean isVisibleViaFeatureDescriptor( IScriptabilityModifier constraint ) {
    return true;
  }

  @Override
  public boolean isHiddenViaFeatureDescriptor() {
    return false;
  }

  @Override
  public IJavaClassField[] getFields() {
    if( _allFields == null ) {
      List<IJavaClassField> fields = new ArrayList<IJavaClassField>();
      IJavaClassField[] declaredFields = getDeclaredFields();
      for( int i = 0; i < declaredFields.length; i++ ) {
        IJavaClassField field = declaredFields[i];
        if( Modifier.isPublic( field.getModifiers() ) ) {
          fields.add( field );
        }
      }
      IJavaClassInfo superclass = getSuperclass();
      if( superclass != null ) {
        fields.addAll( Arrays.asList( superclass.getFields() ) );
      }
      _allFields = fields.toArray( new IJavaClassField[fields.size()] );
    }
    return _allFields;
  }

  public AsmClass getAsmType() {
    return _class;
  }

  @Override
  public IJavaClassInfo getComponentType() {
    return null;
  }

  @Override
  public boolean isArray() {
    return false;
  }

  @Override
  public boolean isEnum() {
    return _class.isEnum();
  }

  @Override
  public int getModifiers() {
    return _class.getModifiers();
  }

  @Override
  public boolean isPrimitive() {
    return _class.isPrimitive();
  }

  @Override
  public IJavaClassInfo getEnclosingClass() {
    AsmType enclosingClass = _class.getEnclosingType();
    if( enclosingClass != null ) {
      return TypeSystem.getJavaClassInfo( enclosingClass.getName(), _module );
    }
    return null;
  }

  @Override
  public IJavaClassInfo getDeclaringClass()
  {
    return this;
  }

  @Override
  public IType getEnclosingType() {
    return _enclosingClass.get();
  }

  @Override
  public String getNamespace() {
    if( _namespace == null ) {
      String name = _class.getName();
      int iDot = name.lastIndexOf( '.' );
      if( iDot < 0 ) {
        _namespace = "";
      }
      else {
        _namespace = name.substring( 0, iDot );
      }
    }
    return _namespace;
  }

  @Override
  public IJavaClassType[] getGenericInterfaces() {
    if( _genericInterfaces == null ) {
      List<AsmType> asmIfaces = _class.getInterfaces();
      List<IJavaClassType> ifaces = new ArrayList<>( asmIfaces.size() );
      for( int i = 0; i < asmIfaces.size(); i++ ) {
        IJavaClassType type = AsmTypeJavaClassType.createType( asmIfaces.get( i ), _module );
        if( type != null ) {
          ifaces.add( type );
        }
      }
      _genericInterfaces = ifaces.toArray( new IJavaClassType[ifaces.size()] );
    }
    return _genericInterfaces;
  }

  @Override
  public IJavaClassType getGenericSuperclass() {
    return AsmTypeJavaClassType.createType( _class.getSuperClass(), _module );
  }

  @Override
  public IJavaClassInfo getArrayType() {
    return new JavaArrayClassInfo( this );
  }

  @Override
  public IJavaClassInfo[] getDeclaredClasses() {
    if( _declaredClasses == null ) {
      Map<String, AsmInnerClassType> innerClasses = _class.getInnerClasses();
      ArrayList<IJavaClassInfo> declaredClasses = new ArrayList<IJavaClassInfo>( innerClasses.size() );
      for( AsmInnerClassType innerClass : innerClasses.values() ) {
        IJavaClassInfo declaredClassInfo = TypeSystem.getJavaClassInfo( innerClass.getName(), _module );
        declaredClasses.add( declaredClassInfo );
      }
      _declaredClasses = declaredClasses.toArray( new IJavaClassInfo[declaredClasses.size()] );
    }
    return _declaredClasses;
  }

  @Override
  public boolean isAssignableFrom( IJavaClassInfo aClass ) {
    return AbstractJavaClassInfo.isAssignableFrom( this, aClass );
  }

  @Override
  public boolean isPublic() {
    return Modifier.isPublic( _class.getModifiers() );
  }

  @Override
  public boolean isProtected() {
    return Modifier.isProtected( _class.getModifiers() );
  }

  @Override
  public boolean isInternal() {
    return !isPublic() && !isProtected() && !isPrivate();
  }

  @Override
  public boolean isPrivate() {
    return Modifier.isPrivate( _class.getModifiers() );
  }

  @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
  @Override
  public boolean equals( Object obj ) {
    return AbstractJavaClassInfo.equals( this, obj );
  }

  @Override
  public int hashCode() {
    return AbstractJavaClassInfo.hashCode( this );
  }

  public String toString() {
    return _class.toString();
  }

  @Override
  public Class getBackingClass() {
    // This should never get called in AsmClassJavaClassInfo but it does for now eg. see DataTypeImpl, JavaTypeInfo.makeLegacyAnnotationConstructor()
    //System.out.println( "!!!! DANGER !!!!!" + "  Class.forName( \"" + getName() + "\" )" );
    return TypeSystem.getDefaultTypeLoader().loadClass( getName() );
  }

  @Override
  public ISourceFileHandle getSourceFileHandle() {
    if( _sourceFileHandle == null )
    {
      _sourceFileHandle = findSourceFile();
    }
    return _sourceFileHandle;
  }

  private ISourceFileHandle findSourceFile()
  {
    IJavaClassInfo csr = this;
    while( csr.getEnclosingClass() != null )
    {
      csr = csr.getEnclosingClass();
    }
    List<IDirectory> backingSourcePath = getModule().getBackingSourcePath();
    if( backingSourcePath != null )
    {
      String classFileName = makeClassFileName();
      for( IDirectory dir: backingSourcePath )
      {
        IFile child = dir.file( classFileName );
        if( child.exists() )
        {
          return new AsmBackingSourceFileHandle( child, ((AsmClassJavaClassInfo)csr).getAsmType(), false );
        }
      }
    }
    return null;
  }

  private String makeClassFileName()
  {
    String classFileName = getName().replace( '.', '/' );
    int i$ = classFileName.indexOf( '$' );
    if( i$ > 0 )
    {
      classFileName = classFileName.substring( 0, i$ );
    }
    classFileName += ".java";
    return classFileName;
  }

  @Override
  public Tree getTree()
  {
    JavaSourceElement sourceType = findSourceClass();
    if( sourceType != null )
    {
      return sourceType.getTree();
    }
    return null;
  }

  @Override
  public CompilationUnitTree getCompilationUnitTree()
  {
    JavaSourceElement sourceType = findSourceClass();
    if( sourceType != null )
    {
      return sourceType.getCompilationUnitTree();
    }
    return null;
  }

  @Override
  public SourcePositions getSourcePositions()
  {
    JavaSourceElement sourceType = findSourceClass();
    if( sourceType != null )
    {
      return sourceType.getSourcePositions();
    }
    return null;
  }

  @Override
  public int getStartPosition()
  {
    JavaSourceElement sourceType = findSourceClass();
    if( sourceType != null )
    {
      return sourceType.getStartPosition();
    }
    return -1;
  }

  @Override
  public int getEndPosition()
  {
    JavaSourceElement sourceType = findSourceClass();
    if( sourceType != null )
    {
      return sourceType.getEndPosition();
    }
    return -1;
  }

  private JavaSourceElement findSourceClass()
  {
    if( _sourceType == null )
    {
      ISourceFileHandle sfh = getSourceFileHandle();
      if( sfh == null )
      {
        return null;
      }

      _sourceType = JavaSourceType.createTopLevel( sfh, getModule() );
      if( _sourceType == null )
      {
        return null;
      }

      IType cls = getJavaType();
      if( cls.getEnclosingType() != null )
      {
        _sourceType = findInnerSourceType( _sourceType, cls.getName() );
      }
    }
    return (JavaSourceElement)_sourceType;
  }

  @Override
  public IModule getModule() {
    return _module;
  }

  @Override
  public IJavaClassType resolveType( String relativeName, int ignoreFlags ) {
    return null;
  }

  @Override
  public IJavaClassInfo getDeepestClassAtOffset( int offset )
  {
    JavaSourceElement sourceType = findSourceClass();
    if( sourceType != null )
    {
      return sourceType.getDeclaringClass().getDeepestClassAtOffset( offset );
    }
    return null;
  }

  @Override
  public IJavaClassType resolveType( String relativeName, IJavaClassInfo whosAskin, int ignoreFlags ) {
    for( AsmInnerClassType innerClass : _class.getInnerClasses().values() ) {
      if( innerClass.getName().equals( getName() + "$" + relativeName ) ) {
        return JavaSourceUtil.getClassInfo( innerClass.getName(), getJavaType().getTypeLoader().getModule() );
      }
    }
    return null;
  }

  @Override
  public IJavaClassType resolveImport( String relativeName ) {
    return null;
  }
}
