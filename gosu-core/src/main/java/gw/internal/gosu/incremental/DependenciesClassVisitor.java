/*
 * Copyright 2026 Guidewire Software, Inc.
 */

package gw.internal.gosu.incremental;

import gw.internal.ext.org.objectweb.asm.*;
import gw.internal.ext.org.objectweb.asm.signature.SignatureReader;
import gw.internal.ext.org.objectweb.asm.signature.SignatureVisitor;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;
//TODO document ABI creation

/**
 * Extracts the set of types referenced by a compiled Gosu class file and records each
 * as a {@code producer -> consumer} edge in the {@link IncrementalCompilationManager}'s
 * dependency graph, where the consumer is the class being visited.
 *
 * <h3>Two-phase walk</h3>
 * <p>
 * The visitor runs in two phases against the same {@link ClassReader}:
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
 *       local-variable signatures inside method bodies).</li>
 * </ol>
 * <p>
 * Both phases route every discovered type through {@link #maybeAddDependentType}, which
 * filters via {@link IncrementalCompilationManager#shouldTrackType}. Duplicate
 * registrations of the same producer are harmless -- the manager's consumer set is a
 * {@code Set}.
 *
 * <p>The split is deliberate: each phase covers cases the other misses, and the
 * constant-pool scan is fast enough that the redundancy isn't a perf concern.
 */
class DependenciesClassVisitor extends ClassVisitor
{
  private static final int CONSTANT_CLASS_TAG = 7;
  private static final int ASM_API_VERSION = Opcodes.ASM5;
  private boolean isClassPrivate;

  private final IncrementalCompilationManager incrementalCompilationManager;
  public final String consumerFqcn;

  public String AbiHash;
  private final StringBuilder abiStr;
  private List<String> abiInterfaces;
  private final List<String> abiFields;
  private final List<String> abiMethods;
  private final List<String> abiAnnotations;
  private final List<String> abiInnerClasses;
  private final boolean verbose;

  /* TODO
  A flag to skip the SourceFile, SourceDebugExtension, LocalVariableTable, LocalVariableTypeTable, LineNumberTable and MethodParameters attributes. If this flag is set these attributes are neither parsed nor visited (i.e. ClassVisitor.visitSource,
   MethodVisitor.visitLocalVariable, MethodVisitor.visitLineNumber and MethodVisitor.visitParameter are not called).
   */

  // TODO consider using a string interner here and in the IncrementalCompilationManager
  public DependenciesClassVisitor( ClassReader reader, IncrementalCompilationManager incrementalCompilationManager, boolean verbose )
  {
    super( ASM_API_VERSION );
    isClassPrivate = false;
    consumerFqcn = getFqcn( reader.getClassName() );
    abiStr = new StringBuilder();
    abiFields = new ArrayList<>();
    abiMethods = new ArrayList<>();
    abiAnnotations = new ArrayList<>();
    abiInnerClasses = new ArrayList<>();
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
      abiInterfaces = new ArrayList<>( Arrays.asList( interfaces ) );
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


  public static String sha1( String input )
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
    if  (verbose)
    {
      System.out.println( abiStr );
    }
    AbiHash = sha1( abiStr.toString() );
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

  protected void maybeAddDependentType( Type type )
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


  //TODO also check for other visitor methods to override
  @Override
  public void visitInnerClass( String name, String outerName, String innerName, int access )
  {
    if (!isPrivate( access ))
    {
      StringBuilder abiInner = new StringBuilder();
      abiInner.append( access ).append( ' ' ).append( name ).append( '(' ).append( innerName ).append( ')' );
      abiInner.append( " outer " ).append( outerName );
      abiAnnotations.add( abiInner.toString() );
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

  private static boolean isPrivate( int access )
  {
    return (access & Opcodes.ACC_PRIVATE) == Opcodes.ACC_PRIVATE;
  }

  private static boolean isSourceCodePrivate( int access, List<String> annotations )
  {
    return access == 0 && !annotations.contains("@Lgw/lang/ir/Internal;T[]");
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
      if( !isPrivate(access) )
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
    public void visitEnd()
    {
      if( isPrivate( fieldAccess ) || isSourceCodePrivate( fieldAccess, abiFieldAnnotations ))
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
    private final int methodAccess;

    protected DepMethodVisitor( int access, String name, String desc, String signature, String[] exceptions )
    {
      super( ASM_API_VERSION );
      abiMethod = new StringBuilder();
      abiMethodAnnotations = new ArrayList<>();
      abiParamsAnnotations = new ArrayList<>();
      abiTypesAnnotations = new ArrayList<>();
      methodAccess = access;
      if( !isPrivate(access) )
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
      super.visitLocalVariable( name, desc, signature, start, end, index );
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
      StringBuilder desc = new StringBuilder( descriptor );
      maybeAddDependentType( Type.getType( descriptor ) );
      if( !isPrivate( methodAccess ) )
      {
        desc.append( typeRef );
        desc.append( ' ' );
        if( typePath != null )
        {
          desc.append( typePath );
          desc.append( ' ' );
        }
        desc.append( descriptor );
      }
      return new DepAnnotationVisitor( abiTypesAnnotations, desc.toString(), visible, isPrivate( methodAccess ), null );
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
    public void visitEnd()
    {
      if( isPrivate( methodAccess ) || isSourceCodePrivate( methodAccess, abiMethodAnnotations ))
      {
        return;
      }
      abiMethod.append( '\n' );
      appendAbiList( abiMethod, abiMethodAnnotations, "\n" );
      appendAbiList( abiMethod, abiParamsAnnotations, "\n" );
      appendAbiList( abiMethod, abiTypesAnnotations, "\n" );
      abiMethods.add( abiMethod.toString() );
    }

    private void addDependentTypeFromBootstrapMethodArgument( Object arg )

    {  if( arg instanceof Type )
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
    private final TreeSet<String> abiAnnotationVals;
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
      abiAnnotationVals = new TreeSet<>();
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
