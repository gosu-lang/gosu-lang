package gw.internal.gosu.parser.gwPlatform;

import gw.config.CommonServices;
import gw.internal.gosu.parser.DefaultEntityAccess;
import gw.internal.gosu.parser.ExtendedProperty;
import gw.lang.GosuShop;
import gw.lang.parser.ILanguageLevel;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.exceptions.IncompatibleTypeException;
import gw.lang.parser.exceptions.ParseIssue;
import gw.lang.parser.resources.Res;
import gw.lang.reflect.IAnnotatedFeatureInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeVariableArrayType;
import gw.lang.reflect.ITypeVariableType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.ICompilableType;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.module.IModule;
import gw.util.IFeatureFilter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class GWEntityAccess extends DefaultEntityAccess
{

  private static final ITypeUsesMap PL_DEFAULT_TYPE_USES = GosuShop.createTypeUsesMap(Arrays.asList(
          "com.guidewire.pl.system.integration.plugins.gosu.EntityFactory",
          "gw.plugin.PluginRegistry",
          "productmodel.*",
          "entity.*",
          "typekey.*"
  )).lock();

  private final ILanguageLevel LANGUAGE_LEVEL = new ILanguageLevel() {
    @Override
    public boolean allowAllImplicitCoercions() {
      return false;
    }

    @Override
    public boolean isStandard() {
      return false;
    }

    @Override
    public boolean supportsNakedCatchStatements() {
      return false;
    }
  };

  private final IFeatureFilter QUERY_EXPRESSION_FEATURE_FILTER = new IFeatureFilter() {
    public boolean acceptFeature(IType beanType, IFeatureInfo fi) {
      if (!fi.getOwnersType().isAssignableFrom(beanType)) {
        return false;
      }
      if (!isEntityClass(beanType)) {
        return false;
      }
      IType type = TypeSystem.get(ExtendedProperty.class);
      return ((IAnnotatedFeatureInfo) fi).hasAnnotation(type);
    }
  };

  protected IType _keyType;
  protected IType _typekeyType;
  protected IType _beanType;
  protected IType _entityType;
  protected IType _typelistType;

  @Override
  protected void doInit() {
    _keyType = TypeSystem.getByFullNameIfValid("gw.pl.persistence.core.Key", TypeSystem.getGlobalModule());
    _beanType = TypeSystem.getByFullNameIfValid("gw.pl.persistence.core.Bean", TypeSystem.getGlobalModule());
    _typekeyType = TypeSystem.getByFullNameIfValid("gw.entity.TypeKey", TypeSystem.getGlobalModule());
    _entityType = TypeSystem.getByFullNameIfValid("gw.entity.IEntityType", TypeSystem.getGlobalModule());
    _typelistType = TypeSystem.getByFullNameIfValid("gw.entity.ITypeList", TypeSystem.getGlobalModule());
  }

  @Override
  public ITypeUsesMap getDefaultTypeUses() {
    return PL_DEFAULT_TYPE_USES;
  }

  public boolean isDomainInstance(Object value) {
    IType type = TypeSystem.get(value.getClass());
    return (_typekeyType != null && _typekeyType.isAssignableFrom(type)) ||
            (_keyType != null && _keyType.isAssignableFrom(type)) ||
            (_beanType != null && _beanType.isAssignableFrom(type));
  }

  @Override
  public boolean isEntityClass(IType type) {
    if (type.isArray()) {
      return isEntityClass(type.getComponentType());
    } else {
      String namespace = type.getNamespace();
      //TODO-dp simplify this
      return "entity".equals(namespace);
    }
  }

  @Override
  public boolean areBeansEqual(Object bean1, Object bean2) {
    if (bean1 == null && bean2 == null) {
      return true;
    }

    if (bean1 == null || bean2 == null) {
      return false;
    }

    return bean1.equals(bean2);
  }

  @Override
  public boolean verifyValueForType(IType type, Object value) throws RuntimeException {
    if (type instanceof ITypeVariableType || type instanceof ITypeVariableArrayType) {
      return true;
    } else {
      try {
        IType valueType = TypeSystem.getFromObject(value);
        CommonServices.getCoercionManager().verifyTypesComparable(type, valueType, false);
      } catch (ParseIssue pe) {
        throw new IncompatibleTypeException("Value of type: " + TypeSystem.getFromObject(value).getName() +
                " is not compatible with symbol type: " + type.getName());
      }
    }

    return true;
  }

  @Override
  public void addEnhancementMethods(IType typeToEnhance, Collection methodsToAddTo) {
    IModule module = TypeSystem.getCurrentModule();
    for (IModule dep : module.getModuleTraversalList()) {
      GosuClassTypeLoader loader = GosuClassTypeLoader.getDefaultClassLoader(dep);
      if (loader != null) {
        loader.getEnhancementIndex().addEnhancementMethods(typeToEnhance, methodsToAddTo);
      }
    }
  }

  @Override
  public void addEnhancementProperties(IType typeToEnhance, Map propertyInfosToAddTo, boolean caseSensitive) {
    IModule module = TypeSystem.getCurrentModule();
    for (IModule dep : module.getModuleTraversalList()) {
      GosuClassTypeLoader loader = GosuClassTypeLoader.getDefaultClassLoader(dep);
      if (loader != null) {
        loader.getEnhancementIndex().addEnhancementProperties(typeToEnhance, propertyInfosToAddTo, caseSensitive);
      }
    }
  }

  @Override
  public IFeatureFilter getQueryExpressionFeatureFilter() {
    return QUERY_EXPRESSION_FEATURE_FILTER;
  }

  @Override
  public ILanguageLevel getLanguageLevel() {
    return LANGUAGE_LEVEL;
  }

  @Override
  public boolean areUsesStatementsAllowedInStatementLists(ICompilableType gosuClass) {
    ISourceFileHandle sourceFileHandle = gosuClass.getSourceFileHandle();
    if (sourceFileHandle != null) {
      String fileName = sourceFileHandle.getFileName();
      return fileName != null && (fileName.endsWith(GosuClassTypeLoader.GOSU_RULE_EXT) || fileName.endsWith(GosuClassTypeLoader.GOSU_RULE_SET_EXT));
    }
    return false;
  }

  @Override
  public IType getKeyType() {
    return _keyType;
  }

  @Override
  public IPropertyInfo getEntityIdProperty(IType rootType) {
    return rootType.getTypeInfo().getProperty("ID");
  }

  @Override
  public boolean shouldAddWarning(IType type, IParseIssue warning) {
    if (type.getName().startsWith("pcf.") &&
            (Res.MSG_UNUSED_VARIABLE.equals(warning.getMessageKey()) ||
                    Res.MSG_STATEMENT_ON_SAME_LINE.equals(warning.getMessageKey()))) {
      return false;
    } else {
      return true;
    }
  }

}
