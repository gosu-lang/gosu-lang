/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.model;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.util.containers.HashSet;
import gw.lang.parser.GosuParserTypes;
import gw.lang.parser.IBlockClass;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.TypeInfoUtil;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.TypeName;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.util.ExceptionUtil;
import gw.util.IFeatureFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class BeanTree implements Comparable<BeanTree> {
  private BeanInfoNode _node;
  private BeanTree _parent;
  private final IType _whosaskin;

  private List<BeanTree> _children;
  private boolean _bIncludeStaticMembers;
  private IFeatureFilter _filter;
  private IModule _moduleCtx;

  public BeanTree(IType classBean, IType whosaskin, boolean bIncludeStaticMembers, IFeatureFilter filter, IModule moduleCtx) {
    _whosaskin = whosaskin;
    _node = new BeanInfoNode(classBean);
    _node.setDisplayName("");
    _bIncludeStaticMembers = bIncludeStaticMembers;
    _filter = filter;
    _moduleCtx = moduleCtx;

    initializeChildren();
  }

  private BeanTree(IMethodInfo descriptor, @NotNull BeanTree parent) {
    this._whosaskin = parent._whosaskin;
    this._node = new MethodNode(descriptor);
    this._parent = parent;
  }

  private BeanTree(IPropertyInfo descriptor, @NotNull BeanTree parent) {
    this._whosaskin = parent._whosaskin;
    this._node = new PropertyInfoNode(descriptor);
    this._parent = parent;
  }

  // ----------------------------------------------------------------------------
  public BeanInfoNode getBeanNode() {
    return _node;
  }

  // ----------------------------------------------------------------------------
  protected void initializeChildren() {
    if (_children != null) {
      return;
    }

    _children = Lists.newArrayList();

    IType type = _node.getType();

    if (!TypeSystem.isBeanType(type) &&
        !(type instanceof IMetaType) &&
        type != GosuParserTypes.BOOLEAN_TYPE() &&
        type != GosuParserTypes.STRING_TYPE() &&
        type != GosuParserTypes.NUMBER_TYPE() &&
        !type.isArray()) {
      return;
    }

    try {
      if (type.isArray()) {
        addPropertyAndMethodNodes(type.getTypeInfo());
        type = type.getComponentType();
      }

      ITypeInfo beanInfo = null;
      try {
        // Note retrieving the typeinfo may throw an exception if the
        // corresponding
        // class cannot be loaded (e.g., when the class is not in Studio's local
        // classpath, but is on the server's). We must handle this gracefully.
        beanInfo = type.getTypeInfo();
      } catch (Throwable t) {
        t.printStackTrace();
      }
      if (beanInfo == null) {
        return;
      }

      addPropertyAndMethodNodes(beanInfo);
      addInnerClassNodes(beanInfo);

//      Collections.sort(_children);
    } catch (ProcessCanceledException e) {
      throw e;
    } catch (Exception e) {
      if (ExceptionUtil.isWrappedCanceled(e)) {
        throw new ProcessCanceledException(e.getCause());
      }
      throw new RuntimeException(e);
    }
  }

  private void addPropertyAndMethodNodes(@NotNull ITypeInfo beanInfo) {
    TypeSystem.pushModule(_moduleCtx);
    try {
      addPropertyNodes(beanInfo);
      addMethodNodes(beanInfo);
    } finally {
      TypeSystem.popModule(_moduleCtx);
    }
  }

  private void addMethodNodes(@NotNull ITypeInfo beanInfo) {
    final List<? extends IMethodInfo> methods = beanInfo instanceof IRelativeTypeInfo ? ((IRelativeTypeInfo) beanInfo).getMethods(_whosaskin) : beanInfo.getMethods();
    final Set<String> signatures = new HashSet<String>();
    if (!methods.isEmpty()) {
      for (IMethodInfo method : methods) {
        if (isHidden(beanInfo, method)) {
          continue; // Don't expose hidden methods in the composer.
        }

        if (!mutualExclusiveStaticFilter(beanInfo, method)) {
          continue;
        }

        final String signature = TypeInfoUtil.getMethodSignature(method);
        if (!signatures.contains(signature)) {
          _children.add(new BeanTree(method, this));
          signatures.add(signature);
        }
      }
    }
  }

  private void addPropertyNodes(@NotNull ITypeInfo beanInfo) {
    List<? extends IPropertyInfo> properties = beanInfo instanceof IRelativeTypeInfo ? ((IRelativeTypeInfo) beanInfo).getProperties(_whosaskin) : beanInfo.getProperties();
    Set<String> propertyNames = new HashSet<String>();
    for (IPropertyInfo property : properties) {

      if (isHidden(beanInfo, property)) {
        // Don't expose hidden properties in the composer.
        continue;
      }

      if (!mutualExclusiveStaticFilter(beanInfo, property)) {
        continue;
      }

      String displayName = property.getDisplayName();
      if (!propertyNames.contains(displayName)) {
        _children.add(new BeanTree(property, this));
        propertyNames.add(displayName);
      }
    }

  }

  private void addInnerClassNodes(@NotNull ITypeInfo beanInfo) {
    if (beanInfo.getOwnersType() instanceof IMetaType) {
      IType ownersType = ((IMetaType) beanInfo.getOwnersType()).getType();
      if (ownersType instanceof IGosuClass) {
        for (Entry<CharSequence, ? extends IGosuClass> innerClass : ((IGosuClass) ownersType).getInnerClassesMap().entrySet()) {
          IGosuClass gosuClass = innerClass.getValue();
          if (!(gosuClass instanceof IBlockClass)) {
            boolean accessible = false;
            switch (gosuClass.getTypeInfo().getAccessibilityForType(_whosaskin)) {
              case PUBLIC:
                if (Modifier.isPublic(gosuClass.getModifiers())) {
                  accessible = true;
                }
                break;
              case PROTECTED:
                if (Modifier.isProtected(gosuClass.getModifiers()) || Modifier.isPublic(gosuClass.getModifiers())) {
                  accessible = true;
                }
                break;
              case INTERNAL:
                if (!Modifier.isPrivate(gosuClass.getModifiers())) {
                  accessible = true;
                }
                break;
              case PRIVATE:
                accessible = true;
                break;
            }
            if (accessible) {
              TypeInPackageType type = new TypeInPackageType(new TypeName(gosuClass));
              TypePropertyInfo descriptor = new TypePropertyInfo(ownersType.getTypeInfo(), type);
              _children.add(new BeanTree(descriptor, this));
            }
          }
        }
      } else if (ownersType instanceof IJavaType) {
        List<IJavaType> innerClasses = ((IJavaType) ownersType).getInnerClasses();
        for (IJavaType innerType : innerClasses) {
          //TODO-dp filter anonymous clases if returned ?
//          if(!innerType.isAnonymous()) {
          TypeInPackageType type = new TypeInPackageType(new TypeName(innerType));
          TypePropertyInfo descriptor = new TypePropertyInfo(ownersType.getTypeInfo(), type);
          _children.add(new BeanTree(descriptor, this));
//          }
        }
      }
    }
  }

  private boolean isHidden(@NotNull ITypeInfo beanInfo, @NotNull IAttributedFeatureInfo feature) {
    return
        feature.isHidden() ||
            !feature.isScriptable() ||
            feature.isDeprecated() ||
            isInternal(feature) ||
            (getFeatureFilter() != null && !getFeatureFilter().acceptFeature(beanInfo.getOwnersType(), feature));
  }

  private boolean mutualExclusiveStaticFilter(@NotNull ITypeInfo beanInfo, @NotNull IAttributedFeatureInfo descriptor) {
    if (beanInfo.getOwnersType() instanceof IMetaType || includeStaticMembers()) {
      return descriptor.isStatic();
    }
    return true;
  }

  private boolean isInternal(@NotNull IAttributedFeatureInfo feature) {
    return feature instanceof IMethodInfo && feature.getDisplayName().startsWith("@");
  }

  public BeanTree getChildAt(int iChildIndex) {
    return _children.get(iChildIndex);
  }

  public int getChildCount() {
    initializeChildren();
    return _children.size();
  }

  public BeanTree getParent() {
    return _parent;
  }

  public int compareTo(@NotNull BeanTree o) {
    if (_node == null) {
      return o._node == null ? 0 : -1;
    } else {
      if (o._node == null) {
        return 1;
      }
      return _node.compareTo(o._node);
    }
  }

  public List<BeanTree> getChildren() {
    return _children;
  }

  @Nullable
  private IFeatureFilter getFeatureFilter() {
    if (_filter != null) {
      return _filter;
    }
    return _parent != null ? _parent.getFeatureFilter() : null;
  }

  private boolean includeStaticMembers() {
    return _bIncludeStaticMembers;
  }

  public String toString() {
    return _node.toString();
  }

  public IType getWhosAsking() {
    return _whosaskin;
  }

}
