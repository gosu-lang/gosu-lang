/*
 * Copyright 2026 Guidewire Software, Inc.
 */

package gw.internal.gosu.incremental;

import gw.internal.ext.org.objectweb.asm.*;
import gw.internal.ext.org.objectweb.asm.signature.SignatureReader;
import gw.internal.ext.org.objectweb.asm.signature.SignatureVisitor;
import gw.lang.ir.Internal;
import gw.lang.parser.IExpression;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IOptionalParamCapable;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuClassTypeInfo;
import gw.lang.reflect.java.ICompileTimeConstantValue;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * One ASM walk over a freshly compiled Gosu class file, doing two jobs in the same pass:
 * <ul>
 *   <li><b>Dependency extraction</b> -- every type the class references is recorded as a
 *       {@code producer -> consumer} edge in the {@link IncrementalCompilationManager}'s
 *       dependency graph, where the consumer is the class being visited.</li>
 *   <li><b>ABI hashing</b> -- the class's consumer-visible surface is assembled into a canonical
 *       text whose SHA-1 is exposed as {@link #getAbiHash()} once the walk ends. Two class files with
 *       the same ABI -- differing only in method bodies, private members, debug info, or member
 *       order -- hash identically, so the incremental driver can stop a cascade at a type whose
 *       recompile changed none of its consumer-visible surface.</li>
 * </ul>
 * The walk runs with {@code SKIP_FRAMES} only: dependency extraction needs method bodies and
 * local-variable tables, and the ABI side records nothing from any code-level callback.
 *
 * <h3>Dependency extraction: a two-phase walk</h3>
 * <p>
 * Edges are gathered in two phases against the same {@link ClassReader}:
 *
 * <ol>
 *   <li><b>Constant-pool scan</b> ({@link #collectClassDependenciesFromConstantPool},
 *       called from the constructor) -- a fast O(n) sweep over the constant pool that
 *       catches every {@code CONSTANT_Class} entry. This picks up references from
 *       method bodies (local variables, instantiations, casts, ...) without paying
 *       the cost of a full instruction-level visitor.</li>
 *   <li><b>Structural visit</b> ({@code reader.accept(visitor, SKIP_FRAMES)}, run by
 *       the caller in {@link IncrementalCompilationManager#trackDependencies}) -- the
 *       standard ASM {@link ClassVisitor} callbacks add signature-level type refs
 *       that the constant pool alone wouldn't surface (e.g. generic type parameters
 *       reachable only through the {@code Signature} attribute, annotation descriptors,
 *       local-variable signatures inside method bodies, and the descriptors of the methods
 *       and fields an instruction invokes or accesses).</li>
 * </ol>
 * <p>
 * Both phases route every discovered type through {@link #maybeAddDependentType}, which
 * filters via {@link IncrementalCompilationManager#shouldTrackType}. Duplicate
 * registrations of the same producer are harmless -- the manager's consumer set is a
 * {@code Set}.
 *
 * <p>The split is deliberate: each phase covers cases the other misses, and the
 * constant-pool scan is fast enough that the redundancy isn't a perf concern.
 *
 * <h3>Bytecode ABI</h3>
 * <p>
 * The canonical text holds the class file version, access flags, name, generic signature and
 * superclass; the member classes the class declares (its {@code InnerClasses} entries, minus
 * block classes, anonymous classes and private member classes, none of which a consumer can
 * name through it); its interfaces, sorted; its annotations and type annotations; and every
 * consumer-visible field (access, name, descriptor, signature, {@code ConstantValue}, annotations,
 * type annotations) and method (access, name, descriptor, signature, exceptions, annotations,
 * parameter and type annotations, an annotation member's {@code AnnotationDefault}, and
 * {@code MethodParameters} names). gosuc writes {@code AnnotationDefault} but neither type
 * annotations nor {@code MethodParameters}; those two are hashed for completeness. Every
 * list is sorted before hashing, so emission order is irrelevant, with one exception: the elements
 * of an array-valued annotation argument keep their class-file order, because that order, and
 * their number, is part of the argument's value. An annotation's named members are sorted like
 * everything else; gosuc writes them in the annotation type's declaration order and looks each one
 * up by name, so a usage site's argument order never reaches the class file anyway.
 *
 * <p><b>Member visibility follows Gosu, not the JVM.</b> gosuc never emits {@code ACC_PRIVATE}
 * for ordinary members: a Gosu-private member (explicit, or a {@code var} with no modifier) is
 * written as package-private so nested classes can reach it, and a member declared
 * {@code internal} is written as package-private with a {@code @gw.lang.ir.Internal} annotation.
 * A member is therefore ABI iff it is public or protected, or package-private and annotated
 * {@code @Internal}; see {@link #isSourceCodePrivate}.
 *
 * <h3>Gosu compile-time surface</h3>
 * <p>
 * Three things the compiler bakes from a producer's <em>source</em> into a consumer's bytecode
 * never reach the producer's own class file, so {@link #appendGosuCompileTimeSurface} adds them
 * from the producer's type info: the values of non-private {@code static final} compile-time
 * constants (gosuc initializes fields in {@code <clinit>} and emits no {@code ConstantValue}, yet
 * consumers fold constants into string concatenations, switch cases and annotation arguments),
 * the parameter names of non-private methods and constructors (named-argument call sites bind
 * against them), and their default parameter value expressions (the parser splices the default
 * into every call site that omits the argument).
 *
 * <p>Every compiled class is hashed, anonymous and block classes included. A hash that cannot be
 * computed is not tolerated: the exception aborts the compile rather than degrading to a cascade.
 * Constructed with {@code verbose} on, it also prints the canonical text of every class;
 * {@link IncrementalCompilationManager#trackDependencies} leaves that flag off.
 */
class DependenciesClassVisitor extends ClassVisitor
{
  private static final int CONSTANT_CLASS_TAG = 7;
  private static final int ASM_API_VERSION = Opcodes.ASM5;
  /**
   * What {@link DepAnnotationVisitor} renders for the {@code @gw.lang.ir.Internal} annotation gosuc puts on
   * every member declared {@code internal}: the descriptor, {@code T} for runtime-visible, and an empty
   * value list. This is rendered text, not a descriptor, so it has to follow that rendering; the
   * internal-var assertion of {@code AbiHashIT.testInternalMembersAreAbi} is what catches drift. See
   * {@link #isSourceCodePrivate}.
   */
  private static final String INTERNAL_ANNOTATION_TEXT = "@" + Type.getDescriptor( Internal.class ) + "T[]";
  private boolean isClassPrivate;

  private final IncrementalCompilationManager incrementalCompilationManager;
  private final String consumerFqcn;
  private final IGosuClass gosuClass;

  private String abiHash;
  private final StringBuilder abiStr;
  private final List<String> abiInterfaces;
  private final List<String> abiFields;
  private final List<String> abiMethods;
  private final List<String> abiAnnotations;
  private final List<String> abiInnerClasses;
  private final boolean verbose;

  // TODO consider using a string interner here and in the IncrementalCompilationManager
  public DependenciesClassVisitor(IGosuClass gosuClass,  ClassReader reader, IncrementalCompilationManager incrementalCompilationManager, boolean verbose )
  {
    super( ASM_API_VERSION );
    this.gosuClass = gosuClass;
    isClassPrivate = false;
    consumerFqcn = getFqcn( reader.getClassName() );
    abiStr = new StringBuilder();
    abiFields = new ArrayList<>();
    abiMethods = new ArrayList<>();
    abiAnnotations = new ArrayList<>();
    abiInnerClasses = new ArrayList<>();
    abiInterfaces = new ArrayList<>();
    this.incrementalCompilationManager = incrementalCompilationManager;
    this.verbose = verbose;
    // Mark consumerFqcn as present in this session's dependency tracking as producer.
    // The type will appear in the dep file even if no consumer relationships are
    // recorded for it. This is called  for every compiled type to maintain a
    // complete registry.
    incrementalCompilationManager.getOrCreateCurrentConsumerSet( consumerFqcn );
    collectClassDependenciesFromConstantPool( reader );
  }

  private static String getFqcn( String internalClassName )
  {
    return Type.getObjectType( internalClassName ).getClassName();
  }

  @Override
  public void visit( int version, int access, String name, String signature, String superName, String[] interfaces )
  {
    isClassPrivate = isPrivate( access );
    if( isClassPrivate )
    {
      abiStr.append( "PRIVATE_CLASS" );
    }
    else
    {
      abiStr.append( version );
      abiStr.append( ' ' );
      abiStr.append( access );
      abiStr.append( " class " );
      abiStr.append( name );
      if( signature != null )
      {
        abiStr.append( signature );
      }
      abiStr.append( " extends " );
      abiStr.append( superName );
      if( interfaces != null )
      {
        abiInterfaces.addAll(  Arrays.asList( interfaces ) );
      }
    }
    maybeAddClassTypesFromSignature( signature );
    if( superName != null )
    {
      Type type = Type.getObjectType( superName );
      maybeAddDependentType( type );
    }
    if( interfaces != null )
    {
      for( String s : interfaces )
      {
        Type interfaceType = Type.getObjectType( s );
        maybeAddDependentType( interfaceType );
      }
    }
  }


  private static String sha1( String input )
  {
    try
    {
      MessageDigest md = MessageDigest.getInstance( "SHA-1" );
      byte[] digest = md.digest( input.getBytes( StandardCharsets.UTF_8 ) );
      return toHex( digest );
    }
    catch( NoSuchAlgorithmException e )
    {
      // SHA-1 is guaranteed to exist on every JVM, so this never happens in practice.
      throw new IllegalStateException( "SHA-1 not available", e );
    }
  }

  private static String toHex( byte[] bytes )
  {
    StringBuilder sb = new StringBuilder( bytes.length * 2 );
    for( byte b : bytes )
    {
      sb.append( Character.forDigit( (b >> 4) & 0xF, 16 ) );
      sb.append( Character.forDigit( b & 0xF, 16 ) );
    }
    return sb.toString();
  }


  private static void appendAbiList( StringBuilder sb, List<String> list, String terminator )
  {
    Collections.sort( list );
    for( String elem : list )
    {
      sb.append( elem );
      sb.append( terminator );
    }
  }

  @Override
  public void visitEnd()
  {
    abiStr.append( " inners " );
    appendAbiList( abiStr, abiInnerClasses, ", " );
    abiStr.append( " implements " );
    appendAbiList( abiStr, abiInterfaces, ", " );
    abiStr.append( "\nannotations:\n" );
    appendAbiList( abiStr, abiAnnotations, "\n" );
    abiStr.append( "\nfields:\n" );
    appendAbiList( abiStr, abiFields, "\n" );
    abiStr.append( "\nmethods:\n" );
    appendAbiList( abiStr, abiMethods, "\n" );
    appendGosuCompileTimeSurface( gosuClass, abiStr );
    if( verbose )
    {
      System.out.println( abiStr );
    }
    abiHash = sha1( abiStr.toString() );
  }

  /**
   * Appends, sorted, the parts of {@code gosuClass}'s consumer-visible surface that gosuc never
   * writes into its class file.
   */
  static void appendGosuCompileTimeSurface( IGosuClass gosuClass, StringBuilder out )
  {
    IGosuClassTypeInfo typeInfo = gosuClass.getTypeInfo();
    List<String> lines = new ArrayList<>();

    for( IPropertyInfo property : typeInfo.getDeclaredProperties() )
    {
      if( property.isPrivate() || !property.isStatic() || !(property instanceof ICompileTimeConstantValue) )
      {
        continue;
      }
      ICompileTimeConstantValue constant = (ICompileTimeConstantValue)property;
      if( constant.isCompileTimeConstantValue() )
      {
        lines.add( "const " + property.getName() +
                   " : " + property.getFeatureType().getName() +
                   " = " + String.valueOf( ( constant.doCompileTimeEvaluation() ) ));
      }
    }

    for( IMethodInfo method : typeInfo.getDeclaredMethods() )
    {
      if( !method.isPrivate() && method instanceof IOptionalParamCapable )
      {
        lines.add( "method " + method.getName() + parameterSurface( method.getParameters(), (IOptionalParamCapable)method ) );
      }
    }

    for( IConstructorInfo constructor : typeInfo.getDeclaredConstructors() )
    {
      if( !constructor.isPrivate() && constructor instanceof IOptionalParamCapable )
      {
        lines.add( "constructor" + parameterSurface( constructor.getParameters(), (IOptionalParamCapable)constructor ) );
      }
    }

    Collections.sort( lines );
    for( String line : lines )
    {
      out.append( line ).append( '\n' );
    }
  }

  /**
   * {@code (types) names=[...] defaults=[...]} for one method or constructor. Parameter types
   * make the line unique per overload.
   */
  private static String parameterSurface( IParameterInfo[] parameters, IOptionalParamCapable optional )
  {
    StringBuilder out = new StringBuilder( "(" );
    for( IParameterInfo parameter : parameters )
    {
      out.append( " " ).append( parameter.getFeatureType().getName() );
    }
    out.append( ") names=" ).append( Arrays.toString( optional.getParameterNames() ) ).append( " defaults=[" );
    // A parameter without a default renders as "none" whether the array carries a null entry for it or, as
    // IOptionalParamCapable also allows, is empty; an explicit `= null` default is a NullExpression and renders
    // as "null". The two must differ: a caller may omit the argument only in the second case.
    IExpression[] defaults = optional.getDefaultValueExpressions();
    for( int i = 0; i < parameters.length; i++ )
    {
      String defaultValue = "none";
      if( defaults != null && i < defaults.length && defaults[i] != null )
      {
        defaultValue = String.valueOf( defaults[i] );
      }
      out.append( " " ).append( defaultValue );
    }
    return out.append( ']' ).toString();
  }


  // Phase 1 of the two-phase walk (see class Javadoc).
  private void collectClassDependenciesFromConstantPool( ClassReader reader )
  {
    char[] charBuffer = new char[reader.getMaxStringLength()];
    for( int i = 1; i < reader.getItemCount(); i++ )
    {
      int itemOffset = reader.getItem( i );
      // See the JVM Spec.
      if( itemOffset > 0 && reader.readByte( itemOffset - 1 ) == CONSTANT_CLASS_TAG )
      {
        // A CONSTANT_Class entry, read the class descriptor
        String classDescriptor = reader.readUTF8( itemOffset, charBuffer );
        Type type = Type.getObjectType( classDescriptor );
        maybeAddDependentType( type );
      }
    }
  }

  private void maybeAddClassTypesFromSignature( String signature )
  {
    if( signature != null )
    {
      SignatureReader signatureReader = new SignatureReader( signature );
      signatureReader.accept( new SignatureVisitor( ASM_API_VERSION )
      {
        @Override
        public void visitClassType( String className )
        {
          Type type = Type.getObjectType( className );
          maybeAddDependentType( type );
        }
      } );
    }
  }

  private void maybeAddDependentType( Type type )
  {
    while( type.getSort() == Type.ARRAY )
    {
      type = type.getElementType();
    }
    if( type.getSort() != Type.OBJECT )
    {
      return;
    }
    String producerFqcn = type.getClassName();

    if( incrementalCompilationManager.shouldTrackType( producerFqcn ) )
    {
      incrementalCompilationManager.recordTypeDependency( producerFqcn, consumerFqcn );
    }
  }

  @Override
  public void visitInnerClass( String name, String outerName, String innerName, int access )
  {
    // Member classes are part of the enclosing class's surface: a consumer resolves Outer.Inner through Outer.
    // Block and anonymous classes come and go with method bodies, and a private member class is nameable by
    // nothing outside this source file, so none of those belong in the hash.
    if( !isPrivate( access ) && !innerName.startsWith( GosuClassTypeLoader.BLOCK_PREFIX ) && !innerName.startsWith( IGosuClass.ANONYMOUS_PREFIX ) )
    {
      StringBuilder abiInner = new StringBuilder();
      abiInner.append( access ).append( ' ' ).append( name ).append( '(' ).append( innerName ).append( ')' );
      abiInner.append( " outer " ).append( outerName );
      abiInnerClasses.add( abiInner.toString() );
    }
  }

  @Override
  public FieldVisitor visitField( int access, String name, String desc, String signature, Object value )
  {
    maybeAddClassTypesFromSignature( signature );
    maybeAddDependentType( Type.getType( desc ) );
    return new DepFieldVisitor( access, name, desc, signature, value );
  }

  @Override
  public MethodVisitor visitMethod( int access, String name, String desc, String signature, String[] exceptions )
  {
    maybeAddClassTypesFromSignature( signature );
    addTypesFromMethodDescriptor( desc );
    if( exceptions != null )
    {
      for( String s : exceptions )
      {
        Type exceptionType = Type.getObjectType( s );
        maybeAddDependentType( exceptionType );
      }
    }
    return new DepMethodVisitor( access, name, desc, signature, exceptions );
  }

  private void addTypesFromMethodDescriptor( String desc )
  {
    Type methodType = Type.getMethodType( desc );
    maybeAddDependentType( methodType.getReturnType() );
    for( Type argType : methodType.getArgumentTypes() )
    {
      maybeAddDependentType( argType );
    }
  }

  @Override
  public AnnotationVisitor visitAnnotation( String desc, boolean visible )
  {
    maybeAddDependentType( Type.getType( desc ) );
    return new DepAnnotationVisitor( abiAnnotations, desc, visible, isClassPrivate, null );
  }

  @Override
  public AnnotationVisitor visitTypeAnnotation( int typeRef, TypePath typePath, String descriptor, boolean visible )
  {
    // A type annotation on the class declaration itself: on a supertype or a type parameter.
    maybeAddDependentType( Type.getType( descriptor ) );
    return new DepAnnotationVisitor( abiAnnotations, typeAnnotationText( typeRef, typePath, descriptor ), visible, isClassPrivate, null );
  }

  @Override
  public void visitOuterClass( String owner, String name, String descriptor )
  {
    // EnclosingMethod of a local or anonymous class: edges only. The enclosing class is a constant-pool entry and
    // already recorded; the enclosing method's parameter and return types are present only in this descriptor.
    // Nothing is hashed, since no consumer can name the class, let alone its enclosing method.
    if( descriptor != null )
    {
      addTypesFromMethodDescriptor( descriptor );
    }
  }

  /** What a type annotation contributes to the ABI text: where it sits ({@code typeRef}, {@code typePath}) and its type. */
  private static String typeAnnotationText( int typeRef, TypePath typePath, String descriptor )
  {
    StringBuilder text = new StringBuilder();
    text.append( typeRef ).append( ' ' );
    if( typePath != null )
    {
      text.append( typePath ).append( ' ' );
    }
    text.append( descriptor );
    return text.toString();
  }

  private static boolean isPrivate( int access )
  {
    return (access & Opcodes.ACC_PRIVATE) == Opcodes.ACC_PRIVATE;
  }

  /**
   * Whether a member with {@code access} is Gosu-private, i.e. nameable by nothing outside its own
   * source file. gosuc writes both private and {@code internal} members as package-private (see
   * {@code AbstractElementTransformer.getModifiers}) and tells them apart only by the
   * {@code @gw.lang.ir.Internal} annotation it adds to the latter, so a member is Gosu-private iff
   * it is neither public nor protected and carries no such annotation.
   *
   * <p>The visibility bits are masked out rather than the whole value compared to zero because
   * {@code getModifiers} ORs {@code ACC_STATIC}, {@code ACC_FINAL}, {@code ACC_ABSTRACT},
   * {@code ACC_ENUM}, {@code ACC_TRANSIENT} and {@code ACC_DEPRECATED} onto the same value: a
   * private {@code static var} arrives as {@code ACC_STATIC}, not {@code 0}, and is no less private
   * for it. Comparing to zero treated every such member as ABI and recompiled its consumers for
   * nothing.
   */
  private static boolean isSourceCodePrivate( int access, List<String> annotations )
  {
    return (access & (Opcodes.ACC_PUBLIC | Opcodes.ACC_PROTECTED)) == 0 && !annotations.contains( INTERNAL_ANNOTATION_TEXT );
  }

  public String getAbiHash()
  {
    return abiHash;
  }

  /** The bytecode-shape FQCN of the class being visited, i.e. the consumer side of every edge it records. */
  public String getConsumerFqcn()
  {
    return consumerFqcn;
  }

  private class DepFieldVisitor extends FieldVisitor
  {
    private final int fieldAccess;
    private final StringBuilder abiField;
    private final List<String> abiFieldAnnotations;

    public DepFieldVisitor( int access, String name, String desc, String signature, Object value )
    {
      super( ASM_API_VERSION );
      abiField = new StringBuilder();
      abiFieldAnnotations = new ArrayList<>();
      fieldAccess = access;
      if( !isPrivate( access ) )
      {
        abiField.append( access );
        abiField.append( ' ' );
        abiField.append( name );
        abiField.append( ": " );
        abiField.append( desc );
        if( signature != null )
        {
          abiField.append( signature );
        }
        if( value != null )
        {
          abiField.append( ' ' );
          abiField.append( value );
        }
      }
    }

    @Override
    public AnnotationVisitor visitAnnotation( String descriptor, boolean visible )
    {
      maybeAddDependentType( Type.getType( descriptor ) );
      return new DepAnnotationVisitor( abiFieldAnnotations, descriptor, visible, isPrivate( fieldAccess ), null );
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation( int typeRef, TypePath typePath, String descriptor, boolean visible )
    {
      maybeAddDependentType( Type.getType( descriptor ) );
      return new DepAnnotationVisitor( abiFieldAnnotations, typeAnnotationText( typeRef, typePath, descriptor ), visible, isPrivate( fieldAccess ), null );
    }

    @Override
    public void visitEnd()
    {
      if( isPrivate( fieldAccess ) || isSourceCodePrivate( fieldAccess, abiFieldAnnotations ) )
      {
        return;
      }
      abiField.append( '\n' );
      appendAbiList( abiField, abiFieldAnnotations, "\n" );
      abiFields.add( abiField.toString() );
    }
  }

  private class DepMethodVisitor extends MethodVisitor
  {
    private final StringBuilder abiMethod;
    private final List<String> abiMethodAnnotations;
    private final List<String> abiParamsAnnotations;
    private final List<String> abiTypesAnnotations;
    private final List<String> abiMethodDefault;
    private final List<String> abiParameters;
    private final int methodAccess;

    protected DepMethodVisitor( int access, String name, String desc, String signature, String[] exceptions )
    {
      super( ASM_API_VERSION );
      abiMethod = new StringBuilder();
      abiMethodAnnotations = new ArrayList<>();
      abiParamsAnnotations = new ArrayList<>();
      abiTypesAnnotations = new ArrayList<>();
      abiMethodDefault = new ArrayList<>();
      abiParameters = new ArrayList<>();
      methodAccess = access;
      if( !isPrivate( access ) )
      {
        abiMethod.append( access );
        abiMethod.append( ' ' );
        abiMethod.append( name );
        abiMethod.append( ": " );
        abiMethod.append( desc );
        if( signature != null )
        {
          abiMethod.append( signature );
        }
        if( exceptions != null )
        {
          abiMethod.append( " throws " );
          appendAbiList( abiMethod, new ArrayList<>( Arrays.asList( exceptions ) ), ", " );
        }
      }
    }

    @Override
    public void visitLocalVariable( String name, String desc, String signature, Label start, Label end, int index )
    {
      maybeAddClassTypesFromSignature( signature );
      maybeAddDependentType( Type.getType( desc ) );
    }

    @Override
    public AnnotationVisitor visitAnnotation( String descriptor, boolean visible )
    {
      maybeAddDependentType( Type.getType( descriptor ) );
      return new DepAnnotationVisitor( abiMethodAnnotations, descriptor, visible, isPrivate( methodAccess ), null );
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation( int parameter, String descriptor, boolean visible )
    {
      maybeAddDependentType( Type.getType( descriptor ) );
      descriptor = parameter + " " + descriptor;
      return new DepAnnotationVisitor( abiParamsAnnotations, descriptor, visible, isPrivate( methodAccess ), null );
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation( int typeRef, TypePath typePath, String descriptor, boolean visible )
    {
      maybeAddDependentType( Type.getType( descriptor ) );
      return new DepAnnotationVisitor( abiTypesAnnotations, typeAnnotationText( typeRef, typePath, descriptor ), visible, isPrivate( methodAccess ), null );
    }

    @Override
    public AnnotationVisitor visitAnnotationDefault()
    {
      // The default value of an annotation member. gosuc bakes it into every user that omits the member, so it is
      // ABI, and a class literal, enum or nested annotation inside it is a dependency, which the visitor records.
      return new DepAnnotationVisitor( abiMethodDefault, null, false, isPrivate( methodAccess ), null );
    }

    @Override
    public void visitParameter( String name, int access )
    {
      // MethodParameters: a parameter's name and flags, which named-argument call sites bind against. Position
      // matters, so the entries keep their order and carry their index.
      if( !isPrivate( methodAccess ) )
      {
        abiParameters.add( "parameter " + abiParameters.size() + ' ' + access + ' ' + name );
      }
    }

    @Override
    public void visitInvokeDynamicInsn( String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments )
    {
      addTypesFromMethodDescriptor( descriptor );
      maybeAddDependentType( Type.getObjectType( bootstrapMethodHandle.getOwner() ) );

      for( Object arg : bootstrapMethodArguments )
      {
        addDependentTypeFromBootstrapMethodArgument( arg );
      }
    }

    @Override
    public void visitFieldInsn( int opcode, String owner, String name, String descriptor )
    {
      // The owner is a constant-pool class entry and already recorded; the field's type is present only in this
      // descriptor.
      maybeAddDependentType( Type.getType( descriptor ) );
    }

    @Override
    public void visitMethodInsn( int opcode, String owner, String name, String descriptor, boolean isInterface )
    {
      // The owner is a constant-pool class entry and already recorded; The invoked method's parameter and return types
      // are present only in this descriptor.
      addTypesFromMethodDescriptor( descriptor );
    }

    @Override
    public void visitEnd()
    {
      if( isPrivate( methodAccess ) || isSourceCodePrivate( methodAccess, abiMethodAnnotations ) )
      {
        return;
      }
      abiMethod.append( '\n' );
      appendAbiList( abiMethod, abiMethodAnnotations, "\n" );
      appendAbiList( abiMethod, abiParamsAnnotations, "\n" );
      appendAbiList( abiMethod, abiTypesAnnotations, "\n" );
      for( String value : abiMethodDefault )
      {
        abiMethod.append( "default " ).append( value ).append( '\n' );
      }
      for( String parameter : abiParameters )
      {
        abiMethod.append( parameter ).append( '\n' );
      }
      abiMethods.add( abiMethod.toString() );
    }

    private void addDependentTypeFromBootstrapMethodArgument( Object arg )

    {
      if( arg instanceof Type )
      {
        maybeAddDependentType( (Type)arg );
      }
      else if( arg instanceof Handle )
      {
        maybeAddDependentType( Type.getObjectType( ((Handle)arg).getOwner() ) );
      }
    }
  }

  private class DepAnnotationVisitor extends AnnotationVisitor
  {
    private final boolean isAnnotationPrivate;
    private final boolean isArrayContainer;
    private final StringBuilder abiAnnotation;
    private final List<String> abiAnnotationVals;
    private String containerName;
    private final DepAnnotationVisitor parent;
    private final List<String> outermostAnnotations;

    public DepAnnotationVisitor( List<String> outermostAnnotations, String descriptor, boolean visible, boolean isPrivate, DepAnnotationVisitor parent )
    {
      super( DependenciesClassVisitor.ASM_API_VERSION );
      isAnnotationPrivate = isPrivate;
      isArrayContainer = descriptor == null;
      this.outermostAnnotations = outermostAnnotations;
      this.parent = parent;
      containerName = null;
      abiAnnotation = new StringBuilder();
      abiAnnotationVals = new ArrayList<>();
      if( !isPrivate && descriptor != null )
      {
        abiAnnotation.append( '@' );
        abiAnnotation.append( descriptor ).append( visible ? "T" : "F" );
      }
    }

    @Override
    public void visit( String name, Object value )
    {
      if( value instanceof Type )
      {
        maybeAddDependentType( (Type)value );
      }
      if( !isAnnotationPrivate )
      {
        String value_str;
        if( value instanceof byte[] )
        {
          value_str = Arrays.toString( (byte[])value );
        }
        else if( value instanceof boolean[] )
        {
          value_str = Arrays.toString( (boolean[])value );
        }
        else if( value instanceof short[] )
        {
          value_str = Arrays.toString( (short[])value );
        }
        else if( value instanceof char[] )
        {
          value_str = Arrays.toString( (char[])value );
        }
        else if( value instanceof int[] )
        {
          value_str = Arrays.toString( (int[])value );
        }
        else if( value instanceof long[] )
        {
          value_str = Arrays.toString( (long[])value );
        }
        else if( value instanceof float[] )
        {
          value_str = Arrays.toString( (float[])value );
        }
        else if( value instanceof double[] )
        {
          value_str = Arrays.toString( (double[])value );
        }
        else
        {
          value_str = value.toString();
        }
        abiAnnotationVals.add( name + " " + value_str );
      }
    }

    @Override
    public void visitEnum( String name, String desc, String value )
    {
      // The enum's type is present only in this descriptor, not as a constant-pool class entry.
      maybeAddDependentType( Type.getType( desc ) );
      if( !isAnnotationPrivate )
      {
        abiAnnotationVals.add( name + " " + desc + " " + value );
      }
    }

    @Override
    public AnnotationVisitor visitArray( String name )
    {
      containerName = name;
      return new DepAnnotationVisitor( new ArrayList<>(), null, false, isAnnotationPrivate, this );
    }

    @Override
    public AnnotationVisitor visitAnnotation( String name, String descriptor )
    {
      maybeAddDependentType( Type.getType( descriptor ) );
      containerName = name;
      return new DepAnnotationVisitor( new ArrayList<>(), descriptor, false, isAnnotationPrivate, this );
    }

    @Override
    public void visitEnd()
    {
      if( isAnnotationPrivate )
      {
        return;
      }

      if( !isArrayContainer )
      {
        abiAnnotation.append( "[" );
        // Named members carry no order in the class file; the elements of an array container keep theirs, which is
        // part of the value.
        Collections.sort( abiAnnotationVals );
      }
      for( String val : abiAnnotationVals )
      {
        abiAnnotation.append( val ).append( ',' );
      }
      if( !isArrayContainer )
      {
        abiAnnotation.append( "]" );
      }
      if( parent != null )
      {
        String val;
        if( parent.containerName == null )
        {
          val = "{ " + abiAnnotation + " }";
        }
        else
        {
          val = parent.containerName + " = { " + abiAnnotation + " }";
        }
        parent.abiAnnotationVals.add( val );
      }
      else
      {
        outermostAnnotations.add( abiAnnotation.toString() );
      }
    }
  }
}
