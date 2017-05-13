/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.testloader;

import manifold.api.fs.IDirectory;
import gw.lang.reflect.IType;
import manifold.api.host.RefreshKind;
import gw.lang.reflect.TypeLoaderBase;
import gw.lang.reflect.TypeSystem;
import gw.spec.core.expressions.memberaccess.MemberAccess_WrappedJavaClass;
import gw.spec.core.expressions.memberaccess.MemberAccess_WrappedJavaClassByJavaBackedType;
import gw.spec.core.expressions.beanmethodcall.generated.BeanMethodCall_WrappedJavaClass;
import gw.spec.core.expressions.beanmethodcall.generated.BeanMethodCall_WrappedJavaClassByJavaBackedType;

import java.util.Set;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.concurrent.Callable;

public class TestTypeLoader extends TypeLoaderBase {
  protected Set<String> _namespaces;
  private Map<String, IType> _types = new HashMap<String, IType>();
  public static final String TEST_CLASS_PACKAGE = "testtypeloader";

  public TestTypeLoader() {
    super( TypeSystem.getCurrentModule() );
  }

  @Override
  protected void doInit() {
    super.doInit();
    // Create some various flavors of test types
    addNonJavaBackedWrappedClass("testtypeloader.StringWrapper", String.class);
    addNonJavaBackedWrappedClass("testtypeloader.ListWrapper", List.class);    //TODO - generic types?
    addNonJavaBackedWrappedClass("testtypeloader.BasicClassWrapperNonJavaBacked", BasicClass.class);
    addNonJavaBackedWrappedClass("testtypeloader.BasicClassWrapperNonJavaBackedImplementsCallableAndRunable", BasicClass.class, Callable.class, Runnable.class);
    addJavaBackedWrappedClass("testtypeloader.BasicClassWrapperJavaBacked", BasicClass.class, BasicClassExtension.class, BasicClass.class);
    addJavaBackedWrappedClass("testtypeloader.BasicClassWrapperJavaBackedImplementsCallableAndRunable", BasicClass.class, BasicClassExtension.class, BasicClass.class, Callable.class, Runnable.class);

    // Codegen related types
    addNonJavaBackedWrappedClass("gw.spec.core.expressions.memberaccess.MemberAccess_WrappedJavaClassWrappedType",
            MemberAccess_WrappedJavaClass.class);

    addNonJavaBackedWrappedClass("gw.spec.core.expressions.beanmethodcall.generated.BeanMethodCall_WrappedJavaClassWrappedType",
            BeanMethodCall_WrappedJavaClass.class);

    addJavaBackedWrappedClass("gw.spec.core.expressions.beanmethodcall.generated.BeanMethodCall_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType",
            BeanMethodCall_WrappedJavaClassByJavaBackedType.class,
            BeanMethodCall_WrappedJavaClassByJavaBackedType.class,
            BeanMethodCall_WrappedJavaClassByJavaBackedType.class);

    addJavaBackedWrappedClass("gw.spec.core.expressions.memberaccess.MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType",
            MemberAccess_WrappedJavaClassByJavaBackedType.class,
            MemberAccess_WrappedJavaClassByJavaBackedType.class,
            MemberAccess_WrappedJavaClassByJavaBackedType.class);
  }

  private void addNonJavaBackedWrappedClass(String fullName, Class classToWrap, Class... interfacesToExpose) {
    _types.put( fullName, new WrappingTestType( this, fullName, TypeSystem.get( classToWrap ), interfacesToExpose ) );
  }

  private void addJavaBackedWrappedClass(String fullName, Class classToWrap, Class implClass, Class javaSuperClass, Class... interfacesToExpose) {
    _types.put( fullName, new WrappingJavaBackedTestType( this, fullName, TypeSystem.get( classToWrap ), implClass, javaSuperClass, interfacesToExpose ) );
  }

  @Override
  public IType getType(String fullyQualifiedName) {
    IType type = _types.get( fullyQualifiedName );
    return type == null ? null : TypeSystem.getOrCreateTypeReference( type );
  }

  @Override
  public Set<String> computeTypeNames() {
    return _types.keySet();
  }

  @Override
  public Set<? extends CharSequence> getAllNamespaces() {
    return _namespaces;
  }

  @Override
  public List<String> getHandledPrefixes() {
    return Collections.emptyList();
  }

  @Override
  public boolean handlesNonPrefixLoads() {
    return true;
  }

  @Override
  public boolean hasNamespace(String namespace) {
    return getAllNamespaces().contains(namespace);
  }

  @Override
  public void refreshedNamespace(String namespace, IDirectory dir, RefreshKind kind) {
    if (_namespaces != null) {
      if (kind == RefreshKind.CREATION) {
        _namespaces.add(namespace);
      } else if (kind == RefreshKind.DELETION) {
        _namespaces.remove(namespace);
      }
    }
  }
}
