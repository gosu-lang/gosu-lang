/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import java.util.StringJoiner;

public class Modifier
{
  /**
   * Return {@code true} if the integer argument includes the
   * {@code public} modifier, {@code false} otherwise.
   *
   * @param   mod a set of modifiers
   * @return {@code true} if {@code mod} includes the
   * {@code public} modifier; {@code false} otherwise.
   */
  public static boolean isPublic(int mod) {
    return (mod & PUBLIC) != 0;
  }

  /**
   * Return {@code true} if the integer argument includes the
   * {@code private} modifier, {@code false} otherwise.
   *
   * @param   mod a set of modifiers
   * @return {@code true} if {@code mod} includes the
   * {@code private} modifier; {@code false} otherwise.
   */
  public static boolean isPrivate(int mod) {
    return (mod & PRIVATE) != 0;
  }

  /**
   * Return {@code true} if the integer argument includes the
   * {@code protected} modifier, {@code false} otherwise.
   *
   * @param   mod a set of modifiers
   * @return {@code true} if {@code mod} includes the
   * {@code protected} modifier; {@code false} otherwise.
   */
  public static boolean isProtected(int mod) {
    return (mod & PROTECTED) != 0;
  }

  /**
   * Return {@code true} if the integer argument includes the
   * {@code static} modifier, {@code false} otherwise.
   *
   * @param   mod a set of modifiers
   * @return {@code true} if {@code mod} includes the
   * {@code static} modifier; {@code false} otherwise.
   */
  public static boolean isStatic(int mod) {
    return (mod & STATIC) != 0;
  }

  /**
   * Return {@code true} if the integer argument includes the
   * {@code final} modifier, {@code false} otherwise.
   *
   * @param   mod a set of modifiers
   * @return {@code true} if {@code mod} includes the
   * {@code final} modifier; {@code false} otherwise.
   */
  public static boolean isFinal(int mod) {
    return (mod & FINAL) != 0;
  }

  /**
   * Return {@code true} if the integer argument includes the
   * {@code synchronized} modifier, {@code false} otherwise.
   *
   * @param   mod a set of modifiers
   * @return {@code true} if {@code mod} includes the
   * {@code synchronized} modifier; {@code false} otherwise.
   */
  public static boolean isSynchronized(int mod) {
    return (mod & SYNCHRONIZED) != 0;
  }

  /**
   * Return {@code true} if the integer argument includes the
   * {@code volatile} modifier, {@code false} otherwise.
   *
   * @param   mod a set of modifiers
   * @return {@code true} if {@code mod} includes the
   * {@code volatile} modifier; {@code false} otherwise.
   */
  public static boolean isVolatile(int mod) {
    return (mod & VOLATILE) != 0;
  }

  /**
   * Return {@code true} if the integer argument includes the
   * {@code transient} modifier, {@code false} otherwise.
   *
   * @param   mod a set of modifiers
   * @return {@code true} if {@code mod} includes the
   * {@code transient} modifier; {@code false} otherwise.
   */
  public static boolean isTransient(int mod) {
    return (mod & TRANSIENT) != 0;
  }

  /**
   * Return {@code true} if the integer argument includes the
   * {@code native} modifier, {@code false} otherwise.
   *
   * @param   mod a set of modifiers
   * @return {@code true} if {@code mod} includes the
   * {@code native} modifier; {@code false} otherwise.
   */
  public static boolean isNative(int mod) {
    return (mod & NATIVE) != 0;
  }

  /**
   * Return {@code true} if the integer argument includes the
   * {@code interface} modifier, {@code false} otherwise.
   *
   * @param   mod a set of modifiers
   * @return {@code true} if {@code mod} includes the
   * {@code interface} modifier; {@code false} otherwise.
   */
  public static boolean isInterface(int mod) {
    return (mod & INTERFACE) != 0;
  }

  /**
   * Return {@code true} if the integer argument includes the
   * {@code abstract} modifier, {@code false} otherwise.
   *
   * @param   mod a set of modifiers
   * @return {@code true} if {@code mod} includes the
   * {@code abstract} modifier; {@code false} otherwise.
   */
  public static boolean isAbstract(int mod) {
    return (mod & ABSTRACT) != 0;
  }

  /**
   * Return {@code true} if the integer argument includes the
   * {@code strictfp} modifier, {@code false} otherwise.
   *
   * @param   mod a set of modifiers
   * @return {@code true} if {@code mod} includes the
   * {@code strictfp} modifier; {@code false} otherwise.
   */
  public static boolean isStrict(int mod) {
    return (mod & STRICT) != 0;
  }

  /**
   * Return a string describing the access modifier flags in
   * the specified modifier. For example:
   * <blockquote><pre>
   *    public final synchronized strictfp
   * </pre></blockquote>
   * The modifier names are returned in an order consistent with the
   * suggested modifier orderings given in sections 8.1.1, 8.3.1, 8.4.3, 8.8.3, and 9.1.1 of
   * <cite>The Java Language Specification</cite>.
   * The full modifier ordering used by this method is:
   * <blockquote> {@code
   * public protected private abstract static final transient
   * volatile synchronized native strictfp
   * interface } </blockquote>
   * The {@code interface} modifier discussed in this class is
   * not a true modifier in the Java language and it appears after
   * all other modifiers listed by this method.  This method may
   * return a string of modifiers that are not valid modifiers of a
   * Java entity; in other words, no checking is done on the
   * possible validity of the combination of modifiers represented
   * by the input.
   *
   * Note that to perform such checking for a known kind of entity,
   * such as a constructor or method, first AND the argument of
   * {@code toString} with the appropriate mask from a method like
   * {@link #constructorModifiers} or {@link #methodModifiers}.
   *
   * @param   mod a set of modifiers
   * @return  a string representation of the set of modifiers
   * represented by {@code mod}
   */
  public static String toString(int mod) {
    StringJoiner sj = new StringJoiner(" ");

    if ((mod & PUBLIC) != 0)        sj.add("public");
    if ((mod & PROTECTED) != 0)     sj.add("protected");
    if ((mod & PRIVATE) != 0)       sj.add("private");

    /* Canonical order */
    if ((mod & ABSTRACT) != 0)      sj.add("abstract");
    if ((mod & STATIC) != 0)        sj.add("static");
    if ((mod & FINAL) != 0)         sj.add("final");
    if ((mod & TRANSIENT) != 0)     sj.add("transient");
    if ((mod & VOLATILE) != 0)      sj.add("volatile");
    if ((mod & SYNCHRONIZED) != 0)  sj.add("synchronized");
    if ((mod & NATIVE) != 0)        sj.add("native");
    if ((mod & STRICT) != 0)        sj.add("strictfp");
    if ((mod & INTERFACE) != 0)     sj.add("interface");

    return sj.toString();
  }

  /*
   * Access modifier flag constants from tables 4.1, 4.4, 4.5, and 4.7 of
   * <cite>The Java Virtual Machine Specification</cite>
   */

  /**
   * The {@code int} value representing the {@code public}
   * modifier.
   */
  public static final int PUBLIC           = 0x00000001;

  /**
   * The {@code int} value representing the {@code private}
   * modifier.
   */
  public static final int PRIVATE          = 0x00000002;

  /**
   * The {@code int} value representing the {@code protected}
   * modifier.
   */
  public static final int PROTECTED        = 0x00000004;

  /**
   * The {@code int} value representing the {@code static}
   * modifier.
   */
  public static final int STATIC           = 0x00000008;

  /**
   * The {@code int} value representing the {@code final}
   * modifier.
   */
  public static final int FINAL            = 0x00000010;

  /**
   * The {@code int} value representing the {@code synchronized}
   * modifier.
   */
  public static final int SYNCHRONIZED     = 0x00000020;

  /**
   * The {@code int} value representing the {@code volatile}
   * modifier.
   */
  public static final int VOLATILE         = 0x00000040;

  /**
   * The {@code int} value representing the {@code transient}
   * modifier.
   */
  public static final int TRANSIENT        = 0x00000080;

  /**
   * The {@code int} value representing the {@code native}
   * modifier.
   */
  public static final int NATIVE           = 0x00000100;

  /**
   * The {@code int} value representing the {@code interface}
   * modifier.
   */
  public static final int INTERFACE        = 0x00000200;

  /**
   * The {@code int} value representing the {@code abstract}
   * modifier.
   */
  public static final int ABSTRACT         = 0x00000400;

  /**
   * The {@code int} value representing the {@code strictfp}
   * modifier.
   */
  public static final int STRICT           = 0x00000800;

  // Bits not (yet) exposed in the public API either because they
  // have different meanings for fields and methods and there is no
  // way to distinguish between the two in this class, or because
  // they are not Java programming language keywords
  public static final int BRIDGE    = 0x00000040;
  public static final int VARARGS   = 0x00000080;
  public static final int SYNTHETIC = 0x00001000;
  public static final int ANNOTATION  = 0x00002000;
  public static final int ENUM      = 0x00004000;
  public static final int MANDATED  = 0x00008000;
  public static boolean isSynthetic(int mod) {
    return (mod & SYNTHETIC) != 0;
  }

  public static boolean isMandated(int mod) {
    return (mod & MANDATED) != 0;
  }

  // Note on the FOO_MODIFIERS fields and fooModifiers() methods:
  // the sets of modifiers are not guaranteed to be constants
  // across time and Java SE releases. Therefore, it would not be
  // appropriate to expose an external interface to this information
  // that would allow the values to be treated as Java-level
  // constants since the values could be constant folded and updates
  // to the sets of modifiers missed. Thus, the fooModifiers()
  // methods return an unchanging values for a given release, but a
  // value that can potentially change over time.

  /**
   * The Java source modifiers that can be applied to a class.
   * @jls 8.1.1 Class Modifiers
   */
  private static final int CLASS_MODIFIERS =
    java.lang.reflect.Modifier.PUBLIC         | java.lang.reflect.Modifier.PROTECTED    | java.lang.reflect.Modifier.PRIVATE |
      java.lang.reflect.Modifier.ABSTRACT       | java.lang.reflect.Modifier.STATIC       | java.lang.reflect.Modifier.FINAL   |
      java.lang.reflect.Modifier.STRICT;

  /**
   * The Java source modifiers that can be applied to an interface.
   * @jls 9.1.1 Interface Modifiers
   */
  private static final int INTERFACE_MODIFIERS =
    java.lang.reflect.Modifier.PUBLIC         | java.lang.reflect.Modifier.PROTECTED    | java.lang.reflect.Modifier.PRIVATE |
      java.lang.reflect.Modifier.ABSTRACT       | java.lang.reflect.Modifier.STATIC       | java.lang.reflect.Modifier.STRICT;


  /**
   * The Java source modifiers that can be applied to a constructor.
   * @jls 8.8.3 Constructor Modifiers
   */
  private static final int CONSTRUCTOR_MODIFIERS =
    java.lang.reflect.Modifier.PUBLIC         | java.lang.reflect.Modifier.PROTECTED    | java.lang.reflect.Modifier.PRIVATE;

  /**
   * The Java source modifiers that can be applied to a method.
   * @jls 8.4.3  Method Modifiers
   */
  private static final int METHOD_MODIFIERS =
    java.lang.reflect.Modifier.PUBLIC         | java.lang.reflect.Modifier.PROTECTED    | java.lang.reflect.Modifier.PRIVATE |
      java.lang.reflect.Modifier.ABSTRACT       | java.lang.reflect.Modifier.STATIC       | java.lang.reflect.Modifier.FINAL   |
      java.lang.reflect.Modifier.SYNCHRONIZED   | java.lang.reflect.Modifier.NATIVE       | java.lang.reflect.Modifier.STRICT;

  /**
   * The Java source modifiers that can be applied to a field.
   * @jls 8.3.1 Field Modifiers
   */
  private static final int FIELD_MODIFIERS =
    java.lang.reflect.Modifier.PUBLIC         | java.lang.reflect.Modifier.PROTECTED    | java.lang.reflect.Modifier.PRIVATE |
      java.lang.reflect.Modifier.STATIC         | java.lang.reflect.Modifier.FINAL        | java.lang.reflect.Modifier.TRANSIENT |
      java.lang.reflect.Modifier.VOLATILE;

  /**
   * The Java source modifiers that can be applied to a method or constructor parameter.
   * @jls 8.4.1 Formal Parameters
   */
  private static final int PARAMETER_MODIFIERS =
    java.lang.reflect.Modifier.FINAL;

  static final int ACCESS_MODIFIERS =
    java.lang.reflect.Modifier.PUBLIC | java.lang.reflect.Modifier.PROTECTED | java.lang.reflect.Modifier.PRIVATE;

  /**
   * Return an {@code int} value OR-ing together the source language
   * modifiers that can be applied to a class.
   * @return an {@code int} value OR-ing together the source language
   * modifiers that can be applied to a class.
   *
   * @jls 8.1.1 Class Modifiers
   * @since 1.7
   */
  public static int classModifiers() {
    return CLASS_MODIFIERS;
  }

  /**
   * Return an {@code int} value OR-ing together the source language
   * modifiers that can be applied to an interface.
   * @return an {@code int} value OR-ing together the source language
   * modifiers that can be applied to an interface.
   *
   * @jls 9.1.1 Interface Modifiers
   * @since 1.7
   */
  public static int interfaceModifiers() {
    return INTERFACE_MODIFIERS;
  }

  /**
   * Return an {@code int} value OR-ing together the source language
   * modifiers that can be applied to a constructor.
   * @return an {@code int} value OR-ing together the source language
   * modifiers that can be applied to a constructor.
   *
   * @jls 8.8.3 Constructor Modifiers
   * @since 1.7
   */
  public static int constructorModifiers() {
    return CONSTRUCTOR_MODIFIERS;
  }

  /**
   * Return an {@code int} value OR-ing together the source language
   * modifiers that can be applied to a method.
   * @return an {@code int} value OR-ing together the source language
   * modifiers that can be applied to a method.
   *
   * @jls 8.4.3 Method Modifiers
   * @since 1.7
   */
  public static int methodModifiers() {
    return METHOD_MODIFIERS;
  }

  /**
   * Return an {@code int} value OR-ing together the source language
   * modifiers that can be applied to a field.
   * @return an {@code int} value OR-ing together the source language
   * modifiers that can be applied to a field.
   *
   * @jls 8.3.1 Field Modifiers
   * @since 1.7
   */
  public static int fieldModifiers() {
    return FIELD_MODIFIERS;
  }

  /**
   * Return an {@code int} value OR-ing together the source language
   * modifiers that can be applied to a parameter.
   * @return an {@code int} value OR-ing together the source language
   * modifiers that can be applied to a parameter.
   *
   * @jls 8.4.1 Formal Parameters
   * @since 1.8
   */
  public static int parameterModifiers() {
    return PARAMETER_MODIFIERS;
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * The <code>int</code> value representing the <code>override</code> modifier.
   */
  public static final int OVERRIDE = 0x00010000;

  /**
   * The <code>int</code> value representing the <code>hide</code> modifier.
   */
  public static final int HIDE = 0x02000000;

  /**
   * The code indicating something is a class member
   */
  public static final int CLASS_MEMBER = 0x00040000;

  /**
   * The <code>int</code> value representing the <code>internal</code> modifier.
   */
  public static final int INTERNAL = 0x00080000;

  /**
   * The <code>int</code> value representing the <code>reified</code> modifier.
   */
  public static final int REIFIED = 0x00100000;

  /**
   * The <code>int</code> value representing the <code>enum</code> modifier.  This value should match
   * the Java version of the enum modifier (which isn't publically exposed).  Note that the enum modifier
   * may mean different things in different contexts.
   */
  //public static final int ENUM = 0x00004000; // Match the Java value for the enum modifier

  public static final int DEPRECATED = 0x000020000; // Java's undocumented modifier for Javadoc @deprecated

  //public static final int ANNOTATION  = 0x00002000; // Match the Java value for the annotation modifier

  public static int getModifiersFrom( IAttributedFeatureInfo afi )
  {
    int iModifiers = 0;
    iModifiers = Modifier.setBit( iModifiers, afi.isPublic(), PUBLIC );
    iModifiers = Modifier.setBit( iModifiers, afi.isPrivate(), PRIVATE );
    iModifiers = Modifier.setBit( iModifiers, afi.isProtected(), PROTECTED );
    iModifiers = Modifier.setBit( iModifiers, afi.isInternal(), INTERNAL );
    iModifiers = Modifier.setBit( iModifiers, afi.isStatic(), STATIC );
    iModifiers = Modifier.setBit( iModifiers, afi.isFinal(), FINAL );
    iModifiers = Modifier.setBit( iModifiers, afi.isReified(), REIFIED );
    return iModifiers;
  }

  /**
   * Return <tt>true</tt> if the integer argument includes the
   * <tt>hide</tt> modifer, <tt>false</tt> otherwise.
   *
   * @param mod a set of modifers
   *
   * @return <tt>true</tt> if <code>mod</code> includes the
   *         <tt>hide</tt> modifier; <tt>false</tt> otherwise.
   */
  public static boolean isHide( int mod )
  {
    return (mod & HIDE) != 0;
  }

  /**
   * Return <tt>true</tt> if the integer argument includes the
   * <tt>override</tt> modifer, <tt>false</tt> otherwise.
   *
   * @param mod a set of modifers
   *
   * @return <tt>true</tt> if <code>mod</code> includes the
   *         <tt>override</tt> modifier; <tt>false</tt> otherwise.
   */
  public static boolean isOverride( int mod )
  {
    return (mod & OVERRIDE) != 0;
  }

  /**
   * Return <tt>true</tt> if the integer argument includes the
   * <tt>class member</tt> modifer, <tt>false</tt> otherwise.
   *
   * @param mod a set of modifers
   *
   * @return <tt>true</tt> if <code>mod</code> includes the
   *         <tt>class member</tt> modifier; <tt>false</tt> otherwise.
   */
  public static boolean isClassMember( int mod )
  {
    return (mod & CLASS_MEMBER) != 0;
  }

  /**
   * Return <tt>true</tt> if the integer argument includes the
   * <tt>internal</tt> modifer, <tt>false</tt> otherwise.
   *
   * @param mod a set of modifers
   *
   * @return <tt>true</tt> if <code>mod</code> includes the
   *         <tt>internal</tt> modifier; <tt>false</tt> otherwise.
   */
  public static boolean isInternal( int mod )
  {
    return (mod & INTERNAL) != 0;
  }

   /**
   * Return <tt>true</tt> if the integer argument includes the
   * <tt>enum</tt> modifer, <tt>false</tt> otherwise.
   *
   * @param mod a set of modifers
   *
   * @return <tt>true</tt> if <code>mod</code> includes the
   *         <tt>enum</tt> modifier; <tt>false</tt> otherwise.
   */
  public static boolean isEnum( int mod )
  {
    return (mod & ENUM) != 0;
  }

  public static boolean isDeprecated( int mod )
  {
    return (mod & DEPRECATED) != 0;
  }

  public static boolean isAnnotation( int mod )
  {
    return (mod & ANNOTATION) != 0;
  }

  public static boolean isReified( int mod )
  {
    return (mod & REIFIED) != 0;
  }

  public static int setPublic( int mod, boolean bValue )
  {
    return setBit( mod, bValue, PUBLIC );
  }

  public static int setPrivate( int mod, boolean bValue )
  {
    return setBit( mod, bValue, PRIVATE );
  }

  public static int setProtected( int mod, boolean bValue )
  {
    return setBit( mod, bValue, PROTECTED );
  }

  public static int setStatic( int mod, boolean bValue )
  {
    return setBit( mod, bValue, STATIC );
  }

  public static int setAbstract( int mod, boolean bValue )
  {
    return setBit( mod, bValue, ABSTRACT );
  }

  public static int setFinal( int mod, boolean bValue )
  {
    return setBit( mod, bValue, FINAL );
  }

  public static int setOverride( int mod, boolean bValue )
  {
    return setBit( mod, bValue, OVERRIDE );
  }

  public static int setHide( int mod, boolean bValue )
  {
    return setBit( mod, bValue, HIDE );
  }

  public static int setClassMember( int mod, boolean bValue )
  {
    return setBit( mod, bValue, CLASS_MEMBER );
  }

  public static int setTransient( int mod, boolean bValue )
  {
    return setBit( mod, bValue, TRANSIENT );
  }

  public static int setInternal( int mod, boolean bValue )
  {
    return setBit(mod, bValue, INTERNAL);
  }

  public static int setEnum( int mod, boolean bValue )
  {
    return setBit(mod, bValue, ENUM);
  }

  public static int setDeprecated( int mod, boolean bValue )
  {
    return setBit(mod, bValue, DEPRECATED);
  }

  public static int setReified( int mod, boolean bValue )
  {
    return setBit( mod, bValue, REIFIED );
  }

  private static int setBit( int mod, boolean bValue, int bit )
  {
    if( bValue )
    {
      return mod |= bit;
    }
    else
    {
      return mod &= ~bit;
    }
  }

  public static String toModifierString(int mod) {
    StringBuffer sb = new StringBuffer();
    int len;

    if ((mod & PUBLIC) != 0) sb.append("public ");
    if ((mod & PROTECTED) != 0) sb.append("protected ");
    if ((mod & INTERNAL) != 0) sb.append("internal ");
    if ((mod & PRIVATE) != 0) sb.append("private ");

    /* Canonical order */
    if ((mod & ABSTRACT) != 0) sb.append("abstract ");
    if ((mod & STATIC) != 0) sb.append("static ");
    if ((mod & FINAL) != 0) sb.append("final ");
    if ((mod & TRANSIENT) != 0) sb.append("transient ");
    if ((mod & VOLATILE) != 0) sb.append("volatile ");
    if ((mod & SYNCHRONIZED) != 0) sb.append("synchronized ");
    if ((mod & NATIVE) != 0) sb.append("native ");
    if ((mod & STRICT) != 0) sb.append("strictfp ");
    if ((mod & REIFIED) != 0) sb.append("reified ");

    if ((len = sb.length()) > 0)  /* trim trailing space */
      return sb.toString().substring(0, len - 1);
    return "";
  }
}
