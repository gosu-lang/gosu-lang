/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

public class Modifier extends java.lang.reflect.Modifier
{
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
  public static final int ENUM = 0x00004000; // Match the Java value for the enum modifier

  public static final int DEPRECATED = 0x000020000; // Java's undocumented modifier for Javadoc @deprecated

  public static final int ANNOTATION  = 0x00002000; // Match the Java value for the annotation modifier

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
