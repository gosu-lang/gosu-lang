/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.ExecutionMode;
import gw.internal.ext.org.objectweb.asm.ClassReader;
import gw.internal.ext.org.objectweb.asm.ClassVisitor;
import gw.internal.ext.org.objectweb.asm.ClassWriter;
import gw.internal.ext.org.objectweb.asm.Label;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;
import gw.internal.ext.org.objectweb.asm.Opcodes;
import gw.internal.ext.org.objectweb.asm.Type;
import gw.internal.ext.org.objectweb.asm.util.CheckClassAdapter;
import gw.internal.ext.org.objectweb.asm.util.TraceClassVisitor;
import gw.lang.GosuShop;
import gw.lang.reflect.IInjectableClassLoader;
import gw.lang.reflect.INonLoadableType;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeRef;
import gw.lang.reflect.ITypeRefFactory;
import gw.lang.reflect.RefreshKind;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuObject;
import gw.lang.reflect.java.IJavaBackedType;
import gw.util.cache.FqnCacheNode;
import gw.util.Predicate;
import gw.util.cache.WeakFqnCache;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * There is one TypeRefFactory per ModuleTypeLoader.
 */
public class TypeRefFactory implements ITypeRefFactory
{
  private static boolean TRACE = false;
  private static boolean VERIFY = false;
  private static boolean ASM_CHECKER = false;
  private static final int JAVA_VER = Opcodes.V1_5;
  private static final Map<Class<? extends IType>, Class<? extends AbstractTypeRef>> ITYPE_PROXY_CLASS_BY_ITYPE_CLASS =
    new HashMap<Class<? extends IType>, Class<? extends AbstractTypeRef>>();
  private final WeakFqnCache<AbstractTypeRef> _refByName;
  private boolean _bClearing;

  public TypeRefFactory()
  {
    _refByName = new WeakFqnCache<AbstractTypeRef>();
  }

  /**
   * Wraps the actual class with a proxy.
   */
  @Override
  public ITypeRef create( IType type )
  {
    // already a proxy? return as is then
    if( type instanceof ITypeRef )
    {
      return (ITypeRef)type;
    }

    if( type instanceof INonLoadableType )
    {
      throw new UnsupportedOperationException( "Type references are not supported for nonloadable types: " + type.getName() );
    }

    String strTypeName = TypeLord.getNameWithQualifiedTypeVariables( type, true );
    if( strTypeName == null || strTypeName.length() == 0 )
    {
      throw new IllegalStateException( "Type has no name" );
    }

    ITypeRef ref;
    if (ExecutionMode.isRuntime()) {
      ref = getRefTheFastWay(type, strTypeName);
    } else {
      ref = getRefTheSafeWay(type, strTypeName);
    }
    return ref;
  }

  private ITypeRef getRefTheFastWay(IType type, String strTypeName) {
    AbstractTypeRef ref = getRef(_refByName, strTypeName, type);
    if (ref == null) {
      TypeSystem.lock();
      try {
        ref = getRef(_refByName, strTypeName, type);
        if (ref == null) {
          ref = createTypeRefProxy(type);
          putRef(_refByName, strTypeName, ref);
          return ref;
        }
      } finally {
        TypeSystem.unlock();
      }
    }

    if (!type.isDiscarded()) {
      ref._setType(type);
    }

    return ref;
  }

  private ITypeRef getRefTheSafeWay(IType type, String strTypeName) {
    TypeSystem.lock();
    try {
      AbstractTypeRef ref = getRef(_refByName, strTypeName, type);
      if (ref == null) {
        ref = createTypeRefProxy(type);
        putRef(_refByName, strTypeName, ref);
        return ref;
      }
      if (!type.isDiscarded()) {
        ref._setType(type);
      }
      return ref;
    } finally {
      TypeSystem.unlock();
    }
  }

  private AbstractTypeRef createTypeRefProxy( IType type )
  {
    Class<? extends IType> typeClass = type.getClass();
    try
    {
      Class<? extends AbstractTypeRef> proxyClass = getOrCreateTypeProxy( typeClass );
      AbstractTypeRef typeRef = proxyClass.newInstance();
      typeRef._setType( type );
      return typeRef;
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }

  private Class<? extends AbstractTypeRef> getOrCreateTypeProxy( Class<? extends IType> typeClass )
  {

    Class<? extends AbstractTypeRef> proxyClass = ITYPE_PROXY_CLASS_BY_ITYPE_CLASS.get( typeClass );
    try
    {
      if( proxyClass == null )
      {
        proxyClass = generateProxyClass( typeClass );
        ITYPE_PROXY_CLASS_BY_ITYPE_CLASS.put( typeClass, proxyClass );
      }
      return proxyClass;
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }

  private Class<? extends AbstractTypeRef> generateProxyClass(Class<? extends IType> typeClass) {
    ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
    ClassVisitor cv = writer;
    StringWriter trace = null;
    if (TRACE) {
      trace = new StringWriter();
      cv = new TraceClassVisitor(cv, new PrintWriter(trace));
      if (ASM_CHECKER) {
        cv = new CheckClassAdapter(cv);
      }
    }

    List<Class> interfaces = new ArrayList<Class>();
    getInterfacesFrom(typeClass, interfaces );

    String strProxyClassName = typeClass.getName() + SYSTEM_PROXY_SUFFIX;
    compileHeader(cv, strProxyClassName, interfaces);
    cv.visitSource( strProxyClassName, null );
    addDefaultConstructor(cv);
    compileInterfaceMembers(cv, typeClass);
    addToString(cv);

    cv.visitEnd();


    if( TRACE ) {
      System.out.println( "========================================================================" );
      System.out.println( strProxyClassName );
      System.out.println( "========================================================================" );
      System.out.println( trace );
    }

    byte[] bytes = writer.toByteArray();
    verify( bytes );

    //noinspection unchecked
    ClassLoader classLoader = typeClass.getClassLoader();
    if(classLoader instanceof IInjectableClassLoader) {
        return ((IInjectableClassLoader) classLoader).defineClass(strProxyClassName, bytes);
    } else {
      try {
          Method method = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
          method.setAccessible(true);
          return (Class<? extends AbstractTypeRef>) method.invoke(classLoader, strProxyClassName, bytes, 0, bytes.length);
      } catch (NoSuchMethodException e) {
          throw new RuntimeException(e);
      } catch (InvocationTargetException e) {
          throw new RuntimeException(e);
      } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
      }
    }
  }

  private void compileHeader(ClassVisitor cv, String name, List<Class> interfaces) {
     cv.visit( JAVA_VER,
               Opcodes.ACC_PUBLIC,
               name.replace( '.', '/' ),
               null,
               getSlashName( AbstractTypeRef.class ),
               getInterfaceNames(interfaces) );
  }

  private String[] getInterfaceNames(List<Class> interfaces) {
    String[] interfaceNames = new String[interfaces.size()];
    for (int i = 0; i < interfaces.size(); i++) {
      interfaceNames[i] = getSlashName(interfaces.get(i));
    }
    return interfaceNames;
  }

  private void addDefaultConstructor(ClassVisitor cv) {
    MethodVisitor mv = cv.visitMethod( Opcodes.ACC_PUBLIC,
                           "<init>",
                           "()V",
                           null, null );
    mv.visitCode();
    mv.visitVarInsn( Opcodes.ALOAD, 0 ); // Load the "this" pointer
    mv.visitMethodInsn(Opcodes.INVOKESPECIAL, getSlashName(AbstractTypeRef.class), "<init>", "()V");
    mv.visitInsn( Opcodes.RETURN );
    mv.visitMaxs(0, 0);
  }

  private void addToString(ClassVisitor cv) {
    MethodVisitor mv = cv.visitMethod( Opcodes.ACC_PUBLIC,
                           "toString",
                           "()Ljava/lang/String;",
                           null, null );
    mv.visitCode();
    mv.visitVarInsn( Opcodes.ALOAD, 0 ); // Load the "this" pointer
    // callMethod(mv, getMethod(AbstractTypeRef.class, "_getType"));
    mv.visitMethodInsn( Opcodes.INVOKEVIRTUAL,
                        getSlashName( AbstractTypeRef.class ),
                        "_getType",
                        "()Lgw/lang/reflect/IType;" );
    // callMethod(mv, getMethod(Object.class, "toString"));
    mv.visitMethodInsn( Opcodes.INVOKEVIRTUAL,
                        getSlashName( Object.class ),
                        "toString",
                        "()Ljava/lang/String;" );
    mv.visitInsn( Opcodes.ARETURN );
    mv.visitMaxs(0, 0);
  }

  private void compileInterfaceMembers(ClassVisitor cv, Class typeClass) {
    for( Method rawMethod : typeClass.getMethods() ) {
      // Note we keep the method from the impl class because we need to keep its modifiers intact
      Method ifaceMethod = getInterfaceMethod(rawMethod);
      if( ifaceMethod == rawMethod )
      {
        continue;
      }
      int nParameters = rawMethod.getParameterTypes().length;
      if( (rawMethod.getName().equals( "unloadTypeInfo" ) && nParameters == 0) ||
          (rawMethod.getName().equals( "isStale" ) && nParameters == 0) ||
          (rawMethod.getName().equals( "readResolve" ) && nParameters == 0) )
      {
        continue;
      }

      genMethod_DevMode( cv, ifaceMethod, rawMethod );
    }
  }

  private void genMethod_DevMode(ClassVisitor cv, Method ifaceMethod, Method proxyMethod) {
    int modifiers = proxyMethod.getModifiers();
    if (proxyMethod.isSynthetic() || proxyMethod.isBridge() ) {
        modifiers = modifiers | Modifier.VOLATILE;
    }
    MethodVisitor mv = cv.visitMethod( modifiers,
                           proxyMethod.getName(),
                           getMethodDescriptor(proxyMethod),
                           null,
                           getMethodExceptions(proxyMethod));
    mv.visitCode();


    // If the method is getName(), insert the short-circuit code
    if(ifaceMethod.getName().equals("getName")) {
      insertGetNameStart(mv);
    }

//  The code we're building up looks essentially like the following:
//    _reload();
//      gw.internal.gosu.parser.IGosuClassInternal   type;
//      try
//      {
//        type = (gw.internal.gosu.parser.IGosuClassInternal)_getType();
//      } catch (ClassCastException ex) {
//        throw new RuntimeException("Type interface changed.  Expected gw.internal.gosu.parser.IGosuClassInternal for " + _getType().getName(), ex);
//      }
//      return (java.util.List)type.getStaticFunctions( $$ );

    int iLastParamIndex = ifaceMethod.getParameterTypes().length + 1; // Use the first index after the this pointer and the arguments
    int typeIndex = iLastParamIndex + 1;
    int classCastExceptionIndex = iLastParamIndex + 2;
    Label tryLabel = new Label();
    Label catchLabel = new Label();
    Label tryEndLabel = new Label();
    Label endLabel = new Label();

    mv.visitTryCatchBlock( tryLabel, tryEndLabel, catchLabel, getSlashName( ClassCastException.class ) );

    //## hack: Before call_reload
    visitDebugLineNumber( 1, mv );

    call_reload(mv);

    // outer try
    mv.visitLabel( tryLabel );
    assignType(ifaceMethod, mv, typeIndex);
    mv.visitLabel( tryEndLabel );
    mv.visitJumpInsn( Opcodes.GOTO, endLabel );

    // inner catch
    mv.visitLabel( catchLabel );
    constructRuntimeException(mv, classCastExceptionIndex);
    mv.visitInsn( Opcodes.ATHROW );

    // call the method
    mv.visitLabel( endLabel );

    //## hack: Before delegateMethodCall
    visitDebugLineNumber( 2, mv );

    delegateMethodCall(ifaceMethod, proxyMethod, mv, typeIndex);

    //## hack: Before return
    visitDebugLineNumber( 3, mv );

    if (ifaceMethod.getReturnType().getName().equals(void.class.getSimpleName())) {
      mv.visitInsn( Opcodes.RETURN );
    } else {
      mv.visitInsn( getIns(Opcodes.IRETURN, ifaceMethod.getReturnType().getName()) );
    }
    mv.visitMaxs(0, 0);
  }

  private void visitDebugLineNumber( int iLine, MethodVisitor mv )
  {
    Label dbgLabel = new Label();
    mv.visitLabel( dbgLabel );
    mv.visitLineNumber(iLine, dbgLabel);
  }

  private void insertGetNameStart(MethodVisitor mv) {
    // For the getName() method, we want to insert this code at the start:
    //  if(_getTypeNameInternal() != null) {
    //    return _getTypeNameInternal();
    //  }
    mv.visitVarInsn( Opcodes.ALOAD, 0 );
    mv.visitMethodInsn( Opcodes.INVOKEVIRTUAL,
            getSlashName( AbstractTypeRef.class ),
            "_getTypeNameInternal",
            "()Ljava/lang/String;" );
    Label afterIf = new Label();
    mv.visitJumpInsn( Opcodes.IFNULL, afterIf );

    mv.visitVarInsn( Opcodes.ALOAD, 0 );
    mv.visitMethodInsn( Opcodes.INVOKEVIRTUAL,
            getSlashName( AbstractTypeRef.class ),
            "_getTypeNameInternal",
            "()Ljava/lang/String;" );
    mv.visitInsn( getIns( Opcodes.IRETURN, String.class) );

    mv.visitLabel(afterIf);
  }

  private void call_reload(MethodVisitor mv) {
    mv.visitVarInsn(Opcodes.ALOAD, 0);
    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
        getSlashName(AbstractTypeRef.class),
        "_reload",
        "()V");
  }

  private void assignType(Method ifaceMethod, MethodVisitor mv, int typeIndex) {
    mv.visitVarInsn( Opcodes.ALOAD, 0 );
    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
        getSlashName(AbstractTypeRef.class),
        "_getType",
        "()Lgw/lang/reflect/IType;");
    mv.visitTypeInsn(Opcodes.CHECKCAST, getSlashName(ifaceMethod.getDeclaringClass()));
    mv.visitVarInsn(Opcodes.ASTORE, typeIndex);
  }

  private void constructRuntimeException(MethodVisitor mv, int exceptionIndex) {
    /*RuntimeException("Type interface changed.  Expected gw.internal.gosu.parser.IGosuClassInternal for " + _getType().getName(), ex);*/
    mv.visitVarInsn( Opcodes.ASTORE, exceptionIndex );
    mv.visitTypeInsn(Opcodes.NEW, getSlashName(TypeRefException.class));
    mv.visitInsn( Opcodes.DUP );
    mv.visitTypeInsn( Opcodes.NEW, getSlashName( StringBuilder.class ) );
    mv.visitInsn( Opcodes.DUP );
    mv.visitLdcInsn("Type interface changed.  Expected gw.internal.gosu.parser.IGosuClassInternal for ");
    mv.visitMethodInsn( Opcodes.INVOKESPECIAL,
            getSlashName( StringBuilder.class ),
            "<init>",
            "(Ljava/lang/String;)V" );
    mv.visitVarInsn( Opcodes.ALOAD, 0 );
    mv.visitMethodInsn( Opcodes.INVOKEVIRTUAL,
            getSlashName( AbstractTypeRef.class ),
            "_getTypeNameInternal",
            "()Ljava/lang/String;" );
    mv.visitMethodInsn( Opcodes.INVOKEVIRTUAL,
            getSlashName( StringBuilder.class),
            "append",
            "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
    mv.visitMethodInsn( Opcodes.INVOKEVIRTUAL,
            getSlashName( StringBuilder.class),
            "toString",
            "()Ljava/lang/String;");

    mv.visitVarInsn( Opcodes.ALOAD, exceptionIndex );
    mv.visitMethodInsn( Opcodes.INVOKESPECIAL,
            getSlashName( TypeRefException.class ),
            "<init>",
            "(Ljava/lang/String;Ljava/lang/Throwable;)V" );
  }

  private void delegateMethodCall(Method ifaceMethod, Method proxyMethod, MethodVisitor mv, int typeIndex) {
    mv.visitVarInsn( Opcodes.ALOAD, typeIndex );
    for(int i = 0; i < ifaceMethod.getParameterTypes().length; i++) {
      mv.visitVarInsn(getIns(Opcodes.ILOAD, ifaceMethod.getParameterTypes()[i].getName()), i + 1);
    }
    callMethod(mv, ifaceMethod);
    if (!ifaceMethod.getReturnType().isPrimitive()) {
      // The cast is necessary when we're invoking a bridge method on the parent; seems like that should never happen,
      // though, and that this should really be unnecessary
      mv.visitTypeInsn( Opcodes.CHECKCAST, getSlashName(proxyMethod.getReturnType().getName()) );
    }
  }

  private boolean isTypeGosuClassInstance(Class<?> declaringClass) {
    return IGosuObject.class.isAssignableFrom(declaringClass) &&
        TypeSystem.getByFullNameIfValid(declaringClass.getName().replace('$', '.')) instanceof IGosuClass;
  }

  private Method getInterfaceMethod( Method method ) {
    Class<?> declaringClass = method.getDeclaringClass();
    do
    {
      if( isTypeGosuClassInstance(declaringClass) )
      {
        String gosuClassName = IGosuClass.ProxyUtil.getNameSansProxy(declaringClass.getName());
        IGosuClassInternal gosuClass = (IGosuClassInternal) TypeSystem.getByFullNameIfValid(gosuClassName);
        if(gosuClass == null) {
          return method;
        }
        for( IType iface : gosuClass.getInterfaces() )
        {
          Class ifaceClass = getJavaClass( iface );
          if( ifaceClass != null )
          {
            try
            {
              method = ifaceClass.getMethod( method.getName(), method.getParameterTypes() );
              return getInterfaceMethod( method );
            }
            catch( NoSuchMethodException e )
            {
              // continue
            }
          }
        }
        IType supertype = gosuClass.getSupertype();
        declaringClass = getJavaClass( supertype );
      }
      else
      {
        for( Class<?> iface : declaringClass.getInterfaces() )
        {
          try
          {
            method = iface.getMethod(method.getName(), method.getParameterTypes());
            return getInterfaceMethod( method );
          }
          catch( NoSuchMethodException e )
          {
            // continue
          }
        }
        declaringClass = declaringClass.getSuperclass();
      }
    } while( declaringClass != null );

    return method;
  }

  @Override
  public ITypeRef get( IType type )
  {
    if( type instanceof ITypeRef )
    {
      return (ITypeRef)type;
    }

    if( type instanceof INonLoadableType )
    {
      throw new UnsupportedOperationException( "Type references are not supported for nonloadable types: " + type.getName() );
    }

    String strTypeName = TypeLord.getNameWithQualifiedTypeVariables( type, true );
    if( strTypeName == null || strTypeName.length() == 0 )
    {
      throw new IllegalStateException( "Type has no name" );
    }

    return getRef( _refByName, strTypeName, type );
  }

  @Override
  public ITypeRef get( String strTypeName )
  {
    return getRef( _refByName, strTypeName, null );
  }

  @Override
  public void clearCaches()
  {
    setClearing( true );
    TypeSystem.lock();
    try {
      // Invalidate types, inner types first
      _refByName.visitDepthFirst(
        new Predicate<AbstractTypeRef>() {
          public boolean evaluate( AbstractTypeRef typeRef ) {
            if( typeRef != null ) {
              typeRef._setStale( RefreshKind.MODIFICATION );
            }
            return true;
          }
        } );

      // Remove dead types
      _refByName.visitNodeDepthFirst(
        new Predicate<FqnCacheNode>() {
          public boolean evaluate( FqnCacheNode node ) {
            @SuppressWarnings("unchecked")
            WeakReference<AbstractTypeRef> ref = (WeakReference<AbstractTypeRef>)node.getUserData();
            if( ref != null ) {
              AbstractTypeRef typeRef = ref.get();
              if( typeRef == null ) {
                node.delete();
              }
            }
            return true;
          }
        } );
    } finally {
      TypeSystem.unlock();
      setClearing( false );
    }
  }

  private int computeSortIndex(Map.Entry<String,WeakReference<AbstractTypeRef>> entry)
  {
    AbstractTypeRef ref = entry.getValue().get();
    return ref != null ? ref._getIndexForSortingFast(entry.getKey()) : 10000;
  }

  private void setClearing( boolean bClearing )
  {
    _bClearing = bClearing;
  }

  @Override
  public boolean isClearing()
  {
    return _bClearing;
  }

  private void getInterfacesFrom( Class<? extends IType> classOfType, List<Class> interfaces )
  {
    if( isTypeGosuClassInstance(classOfType) )
    {
      String gosuClassName = IGosuClass.ProxyUtil.getNameSansProxy(classOfType.getName());
      IGosuClassInternal gosuClass = (IGosuClassInternal) TypeSystem.getByFullName(gosuClassName);
      for(IType iface : gosuClass.getInterfaces()) {
        Class ifaceClass = getJavaClass(iface);
        if(ifaceClass != null && !interfaces.contains(ifaceClass)) {
          interfaces.add(ifaceClass);
        }
      }
      IType supertype = gosuClass.getSupertype();
      if( supertype != null )
      {
        Class ifaceClass = getJavaClass( supertype );
        if( ifaceClass != null )
        {
          getInterfacesFrom( ifaceClass, interfaces );
        }
      }
    }
    else
    {
      for( Class iface : classOfType.getInterfaces() )
      {
        if( !interfaces.contains( iface ) )
        {
          interfaces.add( iface );
        }
      }
      Class superClass = classOfType.getSuperclass();
      if( superClass != null )
      {
        getInterfacesFrom( superClass, interfaces );
      }
      if (Proxy.isProxyClass(classOfType) && !interfaces.contains(ITypeImplementedByProxy.class)) {
        interfaces.add(ITypeImplementedByProxy.class);
      }
    }
  }

  private Class getJavaClass( IType iface )
  {
    Class ifaceClass = null;
    if( iface instanceof IGosuClass )
    {
      ifaceClass = ((IGosuClass)iface).getBackingClass();
    }
    else if( iface instanceof IJavaBackedType )
    {
      ifaceClass = ((IJavaBackedType)iface).getBackingClass();
    }
    return ifaceClass;
  }

  private static void putRef( WeakFqnCache<AbstractTypeRef> map, String key, AbstractTypeRef value )
  {
    map.add( key, value );
  }

  private AbstractTypeRef getRef( WeakFqnCache<AbstractTypeRef> map, String key, IType type )
  {
    AbstractTypeRef typeRef = map.get( key );
    if( typeRef == null )
    {
      return null;
    }
    else
    {
      //make sure we're returning a compatible type ref.
      if( type != null && !typeRef.getClass().getName().startsWith( type.getClass().getName() ) ) {
        return null;
      }
      return typeRef;
    }
  }

  // ------------------ ASM Helpers

  private int getIns( int opcode, Class type )
  {
    if( opcode == Opcodes.DUP )
    {
      return isWide( type ) ? Opcodes.DUP2 : opcode;
    }

    if( opcode == Opcodes.POP )
    {
      return isWide( type ) ? Opcodes.POP2 : opcode;
    }

    switch( opcode )
    {
      case Opcodes.ILOAD:
      case Opcodes.ISTORE:
      case Opcodes.IALOAD:
      case Opcodes.IASTORE:
      case Opcodes.IADD:
      case Opcodes.ISUB:
      case Opcodes.IMUL:
      case Opcodes.IDIV:
      case Opcodes.IREM:
      case Opcodes.INEG:
      case Opcodes.ISHL:
      case Opcodes.ISHR:
      case Opcodes.IUSHR:
      case Opcodes.IAND:
      case Opcodes.IOR:
      case Opcodes.IXOR:
      case Opcodes.IRETURN:
        break;
      default:
        throw new IllegalArgumentException( "Opcode: " + Integer.toHexString( opcode ) + " is not handled" );
    }

    if( type == byte.class )
    {
      return Type.BYTE_TYPE.getOpcode( opcode );
    }
    else if( type == char.class )
    {
      return Type.CHAR_TYPE.getOpcode( opcode );
    }
    else if( type == short.class )
    {
      return Type.SHORT_TYPE.getOpcode( opcode );
    }
    else if( type == boolean.class)
    {
      return Type.BOOLEAN_TYPE.getOpcode( opcode );
    }
    else if( type == int.class)
    {
      return Type.INT_TYPE.getOpcode( opcode );
    }
    else if( type == long.class )
    {
      return Type.LONG_TYPE.getOpcode( opcode );
    }
    else if( type == float.class )
    {
      return Type.FLOAT_TYPE.getOpcode( opcode );
    }
    else if( type == double.class )
    {
      return Type.DOUBLE_TYPE.getOpcode( opcode );
    }
    else // handles array/ref types
    {
      return Type.getType(Object.class).getOpcode( opcode );
    }
  }

  private int getIns( int opcode, String typeName )
  {
    if( opcode == Opcodes.DUP )
    {
      return isWide( typeName ) ? Opcodes.DUP2 : opcode;
    }

    if( opcode == Opcodes.POP )
    {
      return isWide( typeName ) ? Opcodes.POP2 : opcode;
    }

    switch( opcode )
    {
      case Opcodes.ILOAD:
      case Opcodes.ISTORE:
      case Opcodes.IALOAD:
      case Opcodes.IASTORE:
      case Opcodes.IADD:
      case Opcodes.ISUB:
      case Opcodes.IMUL:
      case Opcodes.IDIV:
      case Opcodes.IREM:
      case Opcodes.INEG:
      case Opcodes.ISHL:
      case Opcodes.ISHR:
      case Opcodes.IUSHR:
      case Opcodes.IAND:
      case Opcodes.IOR:
      case Opcodes.IXOR:
      case Opcodes.IRETURN:
        break;
      default:
        throw new IllegalArgumentException( "Opcode: " + Integer.toHexString( opcode ) + " is not handled" );
    }

    if( typeName.equals("byte") )
    {
      return Type.BYTE_TYPE.getOpcode( opcode );
    }
    else if( typeName.equals("char") )
    {
      return Type.CHAR_TYPE.getOpcode( opcode );
    }
    else if( typeName.equals("short") )
    {
      return Type.SHORT_TYPE.getOpcode( opcode );
    }
    else if( typeName.equals("boolean") )
    {
      return Type.BOOLEAN_TYPE.getOpcode( opcode );
    }
    else if( typeName.equals("int") )
    {
      return Type.INT_TYPE.getOpcode( opcode );
    }
    else if( typeName.equals("long") )
    {
      return Type.LONG_TYPE.getOpcode( opcode );
    }
    else if( typeName.equals("float") )
    {
      return Type.FLOAT_TYPE.getOpcode( opcode );
    }
    else if( typeName.equals("double") )
    {
      return Type.DOUBLE_TYPE.getOpcode( opcode );
    }
    else // handles array/ref types
    {
      return Type.getType(Object.class).getOpcode( opcode );
    }
  }

  private boolean isWide( Class type )
  {
    return type == long.class || type == double.class;
  }

  private boolean isWide( String typeName )
  {
    return typeName.equals("long") || typeName.equals("double");
  }

  private String getSlashName(Class type) {
    return getSlashName(type.getName());
    //return Type.getType(type).getInternalName();
  }

  private String getSlashName(String typeName) {
    return typeName.replace('.', '/');
  }

  private String[] getParameterTypeDescriptors(Method _method) {
    Class<?>[] paramTypes = _method.getParameterTypes();
    String[] paramDescriptors = new String[paramTypes.length];
    for (int i = 0; i < paramTypes.length; i++) {
      paramDescriptors[i] = getDescriptor(paramTypes[i]);
//      paramDescriptors[i] = AbstractElementTransformer.getDescriptor(paramTypes[i]).getDescriptor();
    }
    return paramDescriptors;
  }

  private String getReturnTypeDescriptor(Method _method) {
    Class<?> returnType = _method.getReturnType();
    return getDescriptor(returnType);

//    return AbstractElementTransformer.getDescriptor(_method.getReturnType()).getDescriptor();
  }

  private String getDescriptor(Class<?> returnType) {
    String name = returnType.getName();
    if (!name.startsWith("[")) {
      name = GosuShop.toSignature(name);
    }
    name = name.replace('.', '/');
    return name;
  }

  private String getMethodDescriptor(Method m) {
    return getMethodDescriptor(getParameterTypeDescriptors(m), getReturnTypeDescriptor(m));
  }

  private String getMethodDescriptor( String[] paramTypeDescriptors, String returnTypeDescriptor )
  {
    StringBuilder sb = new StringBuilder();
    sb.append('(');
    for (String paramTypeDescriptor : paramTypeDescriptors) {
      sb.append(paramTypeDescriptor);
    }
    sb.append( ')' )
      .append( returnTypeDescriptor );
    return sb.toString();
  }

  private String[] getMethodExceptions( Method method )
  {
    Class<?>[] exceptionTypes = method.getExceptionTypes();
    if (exceptionTypes == null || exceptionTypes.length == 0) {
      return null;
    }
    String[] exceptionTypeNames = new String[exceptionTypes.length];
    for (int i = 0; i < exceptionTypes.length; i++) {
      exceptionTypeNames[i] = getSlashName(exceptionTypes[i]);
    }
    return exceptionTypeNames;
  }

  private void callMethod( MethodVisitor mv, Method method )
  {
    int opCode = Modifier.isStatic( method.getModifiers() )
                 ? Opcodes.INVOKESTATIC
                 : method.getDeclaringClass().isInterface()
                   ? Opcodes.INVOKEINTERFACE
                   : Opcodes.INVOKEVIRTUAL;

    mv.visitMethodInsn( opCode,
                        getSlashName( method.getDeclaringClass() ),
                        method.getName(),
                        getMethodDescriptor( method ) );
  }

  private static void verify( byte[] bytes )
  {
    if( VERIFY )
    {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter( sw );
      CheckClassAdapter.verify( new ClassReader( bytes ), false, pw );
      String out = sw.toString();
      if( out.length() > 0 )
      {
        System.out.println( out );
      }
    }
  }

  @Override
  public List<ITypeRef> getSubordinateRefs(String topLevelTypeName) {
    FqnCacheNode<WeakReference<AbstractTypeRef>> node = _refByName.getNode( topLevelTypeName );
    final List<ITypeRef> types = new ArrayList<ITypeRef>();
    if( node != null ) {
      node.visitNodeDepthFirst(
        new Predicate<FqnCacheNode>() {
          public boolean evaluate( FqnCacheNode node ) {
            @SuppressWarnings("unchecked")
            WeakReference<AbstractTypeRef> ref = (WeakReference<AbstractTypeRef>)node.getUserData();
            if( ref != null ) {
              AbstractTypeRef typeRef = ref.get();
              if( typeRef != null ) {
                types.add( typeRef );
              }
            }
            return true;
          }
        } );
    }
    return types;
  }

  public List<String> getTypesWithPrefix(String namespace, final String prefix) {
    FqnCacheNode<WeakReference<AbstractTypeRef>> node = _refByName.getNode( namespace );
    final List<String> types = new ArrayList<String>();
    if( node != null ) {
      node.visitNodeDepthFirst(
        new Predicate<FqnCacheNode>() {
          public boolean evaluate( FqnCacheNode node ) {
            if (node.getName().startsWith(prefix)) {
              @SuppressWarnings("unchecked")
              WeakReference<AbstractTypeRef> ref = (WeakReference<AbstractTypeRef>)node.getUserData();
              if( ref != null ) {
                AbstractTypeRef typeRef = ref.get();
                if( typeRef != null ) {
                  types.add(node.getFqn());
                }
              }
            }
            return true;
          }
        } );
    }
    return types;
  }
}
