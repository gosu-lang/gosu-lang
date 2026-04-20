/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler.signature;

import gw.internal.ext.org.objectweb.asm.ClassReader;
import gw.internal.ext.org.objectweb.asm.ClassVisitor;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;
import gw.internal.ext.org.objectweb.asm.Opcodes;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.test.TestClass;

/**
 * Regression tests for the JVM generic signature emitted on Gosu classes and
 * methods whose type parameters have a mix of class and interface bounds.
 * <p>
 * Prior to the fix in {@code IRClass.emitBounds}, the backward iteration over
 * bound types combined with the fact that ASM's {@code SignatureWriter.visitClassBound()}
 * is a no-op produced signatures like {@code <P::LI;LC;>} (missing {@code :} before
 * the class bound), which are malformed per JVM spec
 * ({@code TypeParameter ::= Identifier ClassBound InterfaceBound*}) and which
 * {@code javap -v} itself cannot parse.
 */
public class MalformedSignatureTest extends TestClass {

  private static final String PKG = "gw.internal.gosu.compiler.sample.signature.";
  private static final String PKG_SLASH = "gw/internal/gosu/compiler/sample/signature/";

  public void testClass_noBound() {
    // Single interface bound — regression-check that ordinary generics still round-trip.
    assertClassSignatureStartsWith(
        "Sig_InterfaceBound",
        "<P::L" + PKG_SLASH + "Sig_I1;>"
    );
  }

  public void testClass_twoInterfaceBounds() {
    // Empty class bound, two interface bounds. All colons required. Source order preserved.
    assertClassSignatureStartsWith(
        "Sig_TwoInterfaces",
        "<P::L" + PKG_SLASH + "Sig_I1;:L" + PKG_SLASH + "Sig_I2;>"
    );
  }

  public void testClass_classBoundFirst() {
    // Single non-interface bound. One colon before the class type.
    assertClassSignatureStartsWith(
        "Sig_ClassBound",
        "<P:L" + PKG_SLASH + "Sig_Class;>"
    );
  }

  public void testClass_classBoundThenInterface() {
    // Mixed: class bound must be emitted first, then the interface.
    // Regression: used to be emitted as ::LI;LC; (class bound last, no :).
    assertClassSignatureStartsWith(
        "Sig_ClassAndInterface",
        "<P:L" + PKG_SLASH + "Sig_Class;:L" + PKG_SLASH + "Sig_I1;>"
    );
  }

  public void testClass_classBoundThenTwoInterfaces() {
    // The most common real-world trigger: class + multiple interfaces in source order.
    assertClassSignatureStartsWith(
        "Sig_ClassAndTwoInterfaces",
        "<P:L" + PKG_SLASH + "Sig_Class;:L" + PKG_SLASH + "Sig_I1;:L" + PKG_SLASH + "Sig_I2;>"
    );
  }

  public void testMethod_classAndInterfaceBound() {
    // Same bug exists in IRMethodStatement.makeGenericSignature.
    String sig = readMethodSignature( PKG + "Sig_MethodClassAndInterface", "foo" );
    assertNotNull( "foo() should carry a generic signature", sig );
    // Expect <P:LSig_Class;:LSig_I1;>(LP;)V or similar — order: class bound before interfaces.
    assertTrue(
        "method signature should start with class bound then interface bound, got: " + sig,
        sig.startsWith( "<P:L" + PKG_SLASH + "Sig_Class;:L" + PKG_SLASH + "Sig_I1;>" )
    );
  }

  // ---- helpers ----

  private void assertClassSignatureStartsWith( String simpleName, String expectedPrefix ) {
    String actual = readClassSignature( PKG + simpleName );
    assertNotNull( "class " + simpleName + " should have a generic signature", actual );
    assertTrue(
        "expected signature to start with: " + expectedPrefix + "\nbut was: " + actual,
        actual.startsWith( expectedPrefix )
    );
  }

  private String readClassSignature( String fqn ) {
    byte[] bytes = getClassBytes( fqn );
    String[] holder = new String[1];
    new ClassReader( bytes ).accept( new ClassVisitor( Opcodes.ASM9 ) {
      @Override
      public void visit( int version, int access, String name, String signature,
                         String superName, String[] interfaces ) {
        holder[0] = signature;
      }
    }, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES );
    return holder[0];
  }

  private String readMethodSignature( String fqn, String methodName ) {
    byte[] bytes = getClassBytes( fqn );
    String[] holder = new String[1];
    new ClassReader( bytes ).accept( new ClassVisitor( Opcodes.ASM9 ) {
      @Override
      public MethodVisitor visitMethod( int access, String name, String descriptor,
                                        String signature, String[] exceptions ) {
        if( name.equals( methodName ) && signature != null ) {
          holder[0] = signature;
        }
        return null;
      }
    }, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES );
    return holder[0];
  }

  private byte[] getClassBytes( String fqn ) {
    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( fqn );
    assertNotNull( "could not resolve type " + fqn, gsClass );
    byte[] bytes = TypeSystem.getGosuClassLoader().getBytes( gsClass );
    assertNotNull( "class loader produced no bytes for " + fqn, bytes );
    return bytes;
  }
}
