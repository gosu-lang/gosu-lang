/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import gw.internal.gosu.parser.AsmClassJavaClassInfo;
import gw.internal.gosu.parser.TypeUsesMap;
import gw.internal.gosu.parser.java.IJavaASTNode;
import gw.internal.gosu.parser.java.JavaASTConstants;
import gw.internal.gosu.parser.java.JavaLexer;
import gw.internal.gosu.parser.java.JavaParser;
import gw.internal.gosu.parser.java.LeafASTNode;
import gw.internal.gosu.parser.java.SourceTypeFormatException;
import gw.internal.gosu.parser.java.TreeBuilder;
import gw.internal.gosu.parser.java.TypeASTNode;
import gw.lang.GosuShop;
import gw.lang.SimplePropertyProcessing;
import gw.lang.javadoc.IClassDocNode;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.EnumValuePlaceholder;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IDefaultTypeLoader;
import gw.lang.reflect.IEnumValue;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.reflect.IType;
import gw.lang.reflect.ImplicitPropertyUtil;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.java.AbstractJavaClassInfo;
import gw.lang.reflect.java.ErrorJavaClassInfo;
import gw.lang.reflect.java.IJavaClassConstructor;
import gw.lang.reflect.java.IJavaClassField;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.IJavaClassTypeVariable;
import gw.lang.reflect.java.IJavaMethodDescriptor;
import gw.lang.reflect.java.IJavaPropertyDescriptor;
import gw.lang.reflect.java.ITypeInfoResolver;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.IModule;
import gw.internal.ext.org.antlr.runtime.ANTLRStringStream;
import gw.internal.ext.org.antlr.runtime.CharStream;
import gw.internal.ext.org.antlr.runtime.RecognitionException;
import gw.internal.ext.org.antlr.runtime.TokenRewriteStream;
import gw.util.GosuObjectUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class JavaSourceType extends AbstractJavaClassInfo implements IJavaClassType, ITypeInfoResolver {
  public static final int IGNORE_NONE = 0;
  public static final int IGNORE_INTERFACES = 1;
  public static final int IGNORE_SUPERCLASS = 2;

  private static Map<String, Class> PRIMITIVES = new HashMap<String, Class>();

  static {
    PRIMITIVES.put(int.class.getName(), int.class);
    PRIMITIVES.put(byte.class.getName(), byte.class);
    PRIMITIVES.put(char.class.getName(), char.class);
    PRIMITIVES.put(short.class.getName(), short.class);
    PRIMITIVES.put(long.class.getName(), long.class);
    PRIMITIVES.put(float.class.getName(), float.class);
    PRIMITIVES.put(double.class.getName(), double.class);
    PRIMITIVES.put(boolean.class.getName(), boolean.class);
    PRIMITIVES.put(void.class.getName(), void.class);
  }

  private static final Object CACHE_MISS = new Object() { public String toString() {return "cache miss";}};

  protected IModule _gosuModule;
  protected IJavaASTNode _typeNode;
  protected IJavaASTNode _bodyNode;
  protected int _typeNodeIndex;
  protected String _fullyQualifiedName;
  protected String _namespace;
  protected String _simpleName;
  protected List<String> _importList;
  protected JavaSourceType _enclosingClass;
  protected IJavaClassField[] _fields;
  protected IJavaClassMethod[] _methods;
  protected JavaSourceModifierList _modifiersList;
  protected IJavaClassInfo[] _innerClasses;
  protected IJavaClassConstructor[] _constructors;
  protected IJavaClassInfo[] _interfaces;
  protected IJavaClassType _genericSuperClass;
  protected IJavaClassTypeVariable[] _typeParameters;
  protected IJavaClassType[] _genericInterfaces;
  private IJavaClassField[] _allFields;
  private Object[] _enumConstants;
  private IJavaPropertyDescriptor[] _propertyDescriptors;
  private IJavaClassInfo _superClass;
  private IJavaMethodDescriptor[] _methodDescriptors;
  protected Map<String, Object> _cache;
  private ISourceFileHandle _fileHandle;
  private List<String> _staticImportList;

  public static IJavaClassInfo createTopLevel(ISourceFileHandle fileHandle, IModule gosuModule) {
    CharStream cs = new ANTLRStringStream(fileHandle.getSource().getSource());
    JavaLexer lexer = new JavaLexer(cs);
    TokenRewriteStream tokens = new TokenRewriteStream(lexer);
    JavaParser parser = new JavaParser(tokens);
    TreeBuilder treeBuilder = new TreeBuilder();
    parser.setTreeBuilder(treeBuilder);
    try {
      parser.compilationUnit();
    } catch (RecognitionException e) {
      // ignore parse issues
    }
    IJavaASTNode tree = treeBuilder.getTree();

    JavaSourceType result = null;
    if (tree.getChildOfType(JavaASTConstants.normalClassDeclaration) != null) {
      result = new JavaSourceClass(fileHandle, tree, gosuModule);
    } else if (tree.getChildOfType(JavaASTConstants.normalInterfaceDeclaration) != null) {
      result = new JavaSourceInterface(fileHandle, tree, gosuModule);
    } else if (tree.getChildOfType(JavaASTConstants.enumDeclaration) != null) {
      result = new JavaSourceEnum(fileHandle, tree, gosuModule);
    } else if (tree.getChildOfType(JavaASTConstants.annotationTypeDeclaration) != null) {
      result = new JavaSourceAnnotation(fileHandle, tree, gosuModule);
    }
    if (result != null && result.isValid()) {
      return result;
    } else {
      return new JavaSourceUnresolvedClass(fileHandle, gosuModule);
    }
  }

  private static JavaSourceType createInner(IJavaASTNode tree, JavaSourceType containingClass) {
    if (tree.isOfType(JavaASTConstants.normalClassDeclaration)) {
      return new JavaSourceClass(tree, containingClass);
    } else if (tree.isOfType(JavaASTConstants.normalInterfaceDeclaration)) {
      return new JavaSourceInterface(tree, containingClass);
    } else if (tree.isOfType(JavaASTConstants.enumDeclaration)) {
      return new JavaSourceEnum(tree, containingClass);
    } else if (tree.isOfType(JavaASTConstants.annotationTypeDeclaration)) {
      return new JavaSourceAnnotation(tree, containingClass);
    } else {
      throw new RuntimeException("unsupported node type");
    }
  }

  /**
   * For top level classes.
   */
  protected JavaSourceType(ISourceFileHandle fileHandle, IJavaASTNode node, String declarationType, int typeNodeType, String bodyType, IModule gosuModule) {
    _fileHandle = fileHandle;
    _namespace = fileHandle.getNamespace();
    _simpleName = fileHandle.getRelativeName();
    _gosuModule = gosuModule;
    makeImportList(node);
    _typeNode = node.getChildOfTypes(declarationType);
    _typeNodeIndex = _typeNode.getChildOfTypesIndex( typeNodeType );
    _bodyNode = _typeNode.getChildOfTypes(bodyType);
    _cache = new HashMap<String, Object>();
  }

  /**
   * For inner classes.
   */
  protected JavaSourceType(IJavaASTNode node, JavaSourceType enclosingClass, int typeNodeType, String bodyType) {
    _typeNode = node;
    _enclosingClass = enclosingClass;
    _gosuModule = enclosingClass.getModule();
    _bodyNode = _typeNode.getChildOfType(bodyType);
    _typeNodeIndex = _typeNode.getChildOfTypesIndex(typeNodeType);
    _cache = new HashMap<String, Object>();
    _namespace = enclosingClass.getNamespace();
    _simpleName = computeSimpleName();
  }

  private String computeSimpleName() {
    try {
      if (_typeNodeIndex != -1) {
        IJavaASTNode child = _typeNode.getChild(_typeNodeIndex + 1);
        if (checkNode(child, JavaParser.IDENTIFIER)) {
          return child.getText();
        }
      }
    } catch(IndexOutOfBoundsException e) {
      // no node for classname field
    }
    throw new SourceTypeFormatException("no class name");
  }

  private static IJavaClassType resolveParameterizedArrayType(ITypeInfoResolver typeResolver, TypeASTNode typeASTNode, String typeName) {
    if (!typeName.endsWith("[]")) {
      return resolveParameterizedType(typeResolver, typeASTNode, typeName);
    }
    IJavaClassType type = resolveParameterizedArrayType(typeResolver, typeASTNode, typeName.substring(0, typeName.length() - 2));
    return new JavaSourceArrayType(type);
  }

  public static IJavaClassType createType(ITypeInfoResolver typeResolver, IJavaASTNode typeNode) {
    if (typeNode instanceof TypeASTNode) {
      TypeASTNode typeASTNode = (TypeASTNode) typeNode;
      String typeName = typeASTNode.getTypeName();
      if (typeASTNode.isParameterizedArrayType()) {
        return resolveParameterizedArrayType(typeResolver, typeASTNode, typeName);
      } else if (typeASTNode.isParameterized()) {
        return resolveParameterizedType(typeResolver, typeASTNode, typeName);
      } else {
        return createType(typeResolver, typeName, IGNORE_NONE);
      }
    } else if (isTypeArgument(typeNode)) {
      if (isWildcardType(typeNode)) {
        IJavaASTNode childOfType = typeNode.getChildOfType(JavaASTConstants.type);
        if (childOfType == null) {
          return new JavaWildcardType(NULL_TYPE);
        } else {
          if (isSuper(typeNode)) {
            return new JavaWildcardType(JavaTypes.OBJECT().getBackingClassInfo());
          } else {
            return new JavaWildcardType(createType(typeResolver, childOfType));
          }
        }
      } else {
        return createType(typeResolver, typeNode.getChildOfType(JavaASTConstants.type));
      }
    } else { // 'void' and the like go through here
      return createType(typeResolver, typeNode.getText(), IGNORE_NONE);
    }
  }

  private static boolean isSuper(IJavaASTNode typeNode) {
    return typeNode.getChildOfType(JavaParser.SUPER) != null;
  }

  private static boolean isTypeArgument(IJavaASTNode typeNode) {
    return typeNode.isOfType(JavaASTConstants.typeArgument);
  }

  private static boolean isWildcardType(IJavaASTNode typeNode) {
    return typeNode.getChildOfType(JavaParser.QUES) != null;
  }

  private static IJavaClassType resolveParameterizedType(ITypeInfoResolver typeResolver, TypeASTNode typeNode, String typeName) {
    List<IJavaASTNode> typeArgumentNodes = typeNode.getTypeArguments();
    IJavaClassType[] typeParameters = new IJavaClassType[typeArgumentNodes.size()];
    IJavaClassType concreteType = createType(typeResolver, typeName, IGNORE_NONE);
    if (concreteType == null || concreteType instanceof ErrorJavaClassInfo) {
      return ERROR_TYPE;
    }
    IJavaClassTypeVariable[] parameters = ((IJavaClassInfo) concreteType).getTypeParameters();
    if (parameters.length != typeParameters.length) {
      throw new RuntimeException("Type parameters screwup.");
    }
    for (int i = 0; i < typeParameters.length; i++) {
      typeParameters[i] = createType(typeResolver, typeArgumentNodes.get(i));
      if (typeParameters[i] instanceof JavaWildcardType && ((JavaWildcardType)typeParameters[i]).getUpperBound() == NULL_TYPE) {
        ((JavaWildcardType)typeParameters[i]).setBound(parameters[i].getBounds()[0]);
      }
    }
    return new JavaParameterizedType(typeParameters, concreteType);
  }

  public static IJavaClassType createType(ITypeInfoResolver typeResolver, String typeName, int ignoreFlags) {
    if (typeName.endsWith("[]")) {
      String typeNameNoArray = typeName.substring(0, typeName.length() - 2);
      IJavaClassType componentType = createType(typeResolver, typeNameNoArray, ignoreFlags);
      if (componentType instanceof IJavaClassInfo) {
        return new JavaArrayClassInfo((IJavaClassInfo) componentType);
      } else {
        return new JavaSourceArrayType(componentType);
      }
    } else {
      IJavaClassType type = typeResolver.resolveType(typeName, ignoreFlags);
      return type == null ? ERROR_TYPE : type;
    }
  }

  protected void makeImportList(IJavaASTNode node) {
    List<IJavaASTNode> importDecls = node.getChildrenOfTypes(JavaASTConstants.importDeclaration);
    List<String> importList = new ArrayList<String>(importDecls.size());
    List<String> staticImportList = new ArrayList<String>(importDecls.size());
    importList.add("java.lang.*");
    for (IJavaASTNode importDecl : importDecls) {
      StringBuilder importText = new StringBuilder();
      List<IJavaASTNode> children = importDecl.getChildren();
      boolean bStatic = false;
      for (int i = 1; i < children.size(); i++) {
        IJavaASTNode leaf = children.get(i);
        String token = leaf.getText();
        if ("static".equals(token)) {
          bStatic = true;
          continue;
        }
        importText.append(token);
      }
      if (bStatic) {
        int iDotStar = importText.lastIndexOf(".*");
        if (iDotStar > 0) {
          importText.delete(iDotStar, importText.length());
        }
        staticImportList.add( importText.toString() );
      }
      importList.add(importText.toString());
    }
    Collections.sort(importList, new Comparator<String>() {
      public int compare(String s1, String s2) {
        if (s1.endsWith("*")) {
          return +1;
        }
        if (s2.endsWith("*")) {
          return -1;
        }
        return 0;
      }
    });
    _importList = importList;
    _staticImportList = staticImportList;
  }

  public IModule getModule() {
    return _gosuModule;
  }

  @Override
  public String getNameSignature() {
    return GosuShop.toSignature(getName());
  }

  @Override
  public String getRelativeName() {
    return getName().substring(getNamespace().length() + 1);
  }

  @Override
  public String getDisplayName() {
    return getSimpleName();
  }

  @Override
  public String getSimpleName() {
    return _simpleName;
  }

  @Override
  public boolean isArray() {
    return false;
  }

  private boolean checkNode(IJavaASTNode node, int tokenType) {
    return node != null && node.isLeaf() && ((LeafASTNode) node).getTokenType() == tokenType;
  }

  public String getName() {
    if (_fullyQualifiedName == null) {
      if (_enclosingClass == null) {
        _fullyQualifiedName = _namespace + "." + getSimpleName();
      } else {
        _fullyQualifiedName = _enclosingClass.getName() + "." + getSimpleName();
      }
    }
    return _fullyQualifiedName;
  }

  @Override
  public IJavaClassInfo[] getInterfaces() {
    if (_interfaces == null) {
      TypeSystem.lock();
      try {
        if (_interfaces == null) {
          IJavaASTNode typeList = _typeNode.getChildOfType(JavaASTConstants.typeList);
          if (typeList == null) {
            _interfaces = IJavaClassType.EMPTY_ARRAY;
            return _interfaces;
          }
          List<IJavaASTNode> children = typeList.getChildren();
          IJavaClassInfo[] interfaces = new IJavaClassInfo[children.size()];
          for (int i = 0; i < interfaces.length; i++) {
            String typeName = ((TypeASTNode) children.get(i)).getTypeName();
            IJavaClassInfo classInfo = (IJavaClassInfo) createType(this, typeName, IGNORE_SUPERCLASS | IGNORE_INTERFACES);
            interfaces[i] = classInfo;
          }
          _interfaces = interfaces;
        }
      } finally {
        TypeSystem.unlock();
      }
    }
    return _interfaces;
  }

  @Override
  public IJavaClassType[] getGenericInterfaces() {
    TypeSystem.lock();
    try {
      if (_genericInterfaces == null) {
        IJavaASTNode typeList = _typeNode.getChildOfType(JavaASTConstants.typeList);
        if (typeList == null) {
          _genericInterfaces = IJavaClassType.EMPTY_ARRAY;
          return _genericInterfaces;
        }
        List<IJavaASTNode> children = typeList.getChildren();
        IJavaClassType[] genericInterfaces = new IJavaClassType[children.size()];
        for (int i = 0; i < genericInterfaces.length; i++) {
          genericInterfaces[i] = createType(this, children.get(i));
        }
        _genericInterfaces = genericInterfaces;
      }
      return _genericInterfaces;
    } finally {
      TypeSystem.unlock();
    }
  }

  @Override
  public IJavaClassInfo getSuperclass() {
    if (_superClass == null) {
      if (isInterface()) {
        _superClass = NULL_TYPE;
      } else if (isEnum()) {
        _superClass = JavaTypes.ENUM().getBackingClassInfo();
      } else {
        IJavaASTNode superTypeNode = _typeNode.getChildOfType(JavaASTConstants.type);
        _superClass = superTypeNode != null
          ? (IJavaClassInfo) createType(this, ((TypeASTNode) superTypeNode).getTypeName(), IGNORE_SUPERCLASS | IGNORE_INTERFACES)
          : JavaTypes.OBJECT().getBackingClassInfo();
      }
      if (hasCyclicInheritance(_superClass)) {
        _superClass = NULL_TYPE;
      }
    }
    return _superClass == NULL_TYPE ? null : _superClass;
  }

  private boolean hasCyclicInheritance(IJavaClassInfo superClass) {
    Set<IJavaClassInfo> visited = new HashSet<IJavaClassInfo>();
    visited.add(this);
    while (superClass != null && !visited.contains(superClass)) {
      visited.add(superClass);
      superClass = superClass.getSuperclass();
    }
    return superClass != null;
  }

  @Override
  public IJavaClassType getGenericSuperclass() {
    if (_genericSuperClass == null) {
      if (isInterface()) {
        _genericSuperClass = NULL_TYPE;
      } else if (isEnum()) {
        _genericSuperClass = JavaTypes.ENUM().getBackingClassInfo();
      } else {
        IJavaASTNode superTypeNode = _typeNode.getChildOfType(JavaASTConstants.type);
        _genericSuperClass = superTypeNode != null ? createType(this, superTypeNode) : NULL_TYPE;
      }
    }
    return _genericSuperClass == NULL_TYPE ? null : _genericSuperClass;
  }

  public void initMethodsAndConstructors() {
    List<IJavaASTNode> methodNodes = _bodyNode.getChildrenOfTypes(getMethodDeclNodeType());
    List<IJavaClassMethod> methods = new ArrayList<IJavaClassMethod>();
    List<IJavaClassConstructor> constructors = new ArrayList<IJavaClassConstructor>();
    for (int i = 0; i < methodNodes.size(); i++) {
      JavaSourceMethod method = JavaSourceMethod.create(methodNodes.get(i), this);
      if (method != null) {
        if (method.isConstructor()) {
          constructors.add((IJavaClassConstructor) method);
        } else {
          methods.add(method);
        }
      }
    }
    if (isEnum()) {
      methods.add(new SyntheticJavaMethod(this, this, this, "valueOf",
        Modifier.STATIC | Modifier.PUBLIC, new IJavaClassInfo[]{
        TypeSystem.getJavaClassInfo(String.class)
      }, new IJavaClassInfo[0]));
      methods.add(new SyntheticJavaMethod(this, this.getArrayType(), this.getArrayType(), "values",
        Modifier.STATIC | Modifier.PUBLIC, new IJavaClassInfo[0], new IJavaClassInfo[0]));
    }
    if (isClass() && constructors.size() == 0) {
      constructors.add(new JavaSourceDefaultConstructor(this));
    }
    _methods = methods.toArray(new IJavaClassMethod[methods.size()]);
    _constructors = constructors.toArray(new IJavaClassConstructor[constructors.size()]);
  }

  private String getMethodDeclNodeType() {
    if (isClass() ||isEnum()) {
      return JavaASTConstants.methodDeclaration;
    } else if (isInterface()) {
      return JavaASTConstants.interfaceMethodDeclaration;
    } else if (isAnnotation()) {
      return JavaASTConstants.annotationMethodDeclaration;
    } else {
      throw new RuntimeException("What the heck is this thing.");
    }
  }

  public IJavaClassMethod[] getDeclaredMethods() {
    if (_methods == null) {
      initMethodsAndConstructors();
    }
    return _methods;
  }

  public IJavaClassConstructor[] getDeclaredConstructors() {
    if (_constructors == null) {
      initMethodsAndConstructors();
    }
    return _constructors;
  }

  public IJavaClassConstructor getConstructor( IJavaClassInfo... paramTypes ) throws NoSuchMethodException {
    outer:
    for (IJavaClassConstructor ctor : getDeclaredConstructors()) {
      IJavaClassInfo[] methodParamTypes = ctor.getParameterTypes();
      if (paramTypes.length != methodParamTypes.length) {
        continue;
      }
      for (int i = 0; i < paramTypes.length; i++) {
        if (!paramTypes[i].equals(methodParamTypes[i])) {
          continue outer;
        }
      }
      return ctor;
    }
    throw new NoSuchMethodException();
  }

  public IJavaClassField[] getDeclaredFields() {
    if (_fields == null) {
      List<IJavaASTNode> fieldNodes = _bodyNode.getChildrenOfTypes(
        JavaASTConstants.fieldDeclaration,
        JavaASTConstants.interfaceFieldDeclaration,
        JavaASTConstants.enumConstant
      );
      _fields = new JavaSourceField[fieldNodes.size()];
      for (int i = 0; i < _fields.length; i++) {
        _fields[i] = JavaSourceField.create(fieldNodes.get(i), this);
      }
    }
    return _fields;
  }

  public IJavaClassField[] getFields() {
    if (_allFields == null) {
      List<IJavaClassField> fields = new ArrayList<IJavaClassField>();
      IJavaClassField[] declaredFields = getDeclaredFields();
      for (int i = 0; i < declaredFields.length; i++) {
        IJavaClassField field = declaredFields[i];
        if (Modifier.isPublic(field.getModifiers())) {
          fields.add(field);
        }
      }
      IJavaClassInfo superclass = getSuperclass();
      if (superclass != null) {
        fields.addAll(Arrays.asList(superclass.getFields()));
      }
      _allFields = fields.toArray(new IJavaClassField[fields.size()]);
    }
    return _allFields;
  }

  @Override
  public Object[] getEnumConstants() {
    if (_enumConstants == null) {
      List<IEnumValue> enums = new ArrayList<IEnumValue>();
      IJavaClassField[] fields = getFields();
      for (IJavaClassField field : fields) {
        if (field.isEnumConstant()) {
          enums.add(new EnumValuePlaceholder(field.getName()));
        }
      }
      _enumConstants = enums.toArray(new IEnumValue[enums.size()]);
    }
    return _enumConstants;
  }

  @Override
  public IJavaPropertyDescriptor[] getPropertyDescriptors() {
    if (_propertyDescriptors == null) {
      _propertyDescriptors = initPropertyDescriptors();
    }
    return _propertyDescriptors;
  }

  protected IJavaPropertyDescriptor[] initPropertyDescriptors() {
    Map<String, IJavaClassMethod> getters = new HashMap<String, IJavaClassMethod>();
    Map<String, IJavaClassMethod> setters = new HashMap<String, IJavaClassMethod>();
    List<IJavaClassMethod> methods = new ArrayList<IJavaClassMethod>();
    methods.addAll(Arrays.asList(getDeclaredMethods()));

    boolean simplePropertyProcessing = getModifierList().isAnnotationPresent(SimplePropertyProcessing.class);

    for (IJavaClassMethod method : methods) {
      ImplicitPropertyUtil.ImplicitPropertyInfo info = JavaSourceUtil.getImplicitProperty(method, simplePropertyProcessing);
      if (info != null) {
        if (info.isGetter() && !getters.containsKey(info.getName())) {
          getters.put(info.getName(), method);
        } else if (info.isSetter() && !setters.containsKey(info.getName())) {
          setters.put(info.getName(), method);
        }
      }
    }

    List<IJavaPropertyDescriptor> propertyDescriptors = new ArrayList<IJavaPropertyDescriptor>();
    for (Map.Entry<String, IJavaClassMethod> entry : getters.entrySet()) {
      String propName = entry.getKey();
      IJavaClassMethod setter = setters.get(propName);
      IJavaClassMethod getter = entry.getValue();
      IJavaClassType getterType = getter == null ? null : getter.getGenericReturnType();
      if (setter != null) {
        setters.remove(propName);
        if (getterType != null &&
          !setter.getGenericParameterTypes()[0].equals(getterType) &&
          !GosuObjectUtil.equals(setter.getGenericParameterTypes()[0].getConcreteType(), getterType)) {
          setter = null;
        }
      }
      if (getterType != null) {
        if( setter == null ) {
          setter = AsmClassJavaClassInfo.maybeFindSetterInSuper( getter, getSuperclass() );
        }
        propertyDescriptors.add(new JavaSourcePropertyDescriptor(
          propName, (IJavaClassInfo) getterType.getConcreteType(), getter, setter));
      }
      else {
        setter = setters.get(propName);
        if( setter != null ) {
          getter = AsmClassJavaClassInfo.maybeFindGetterInSuper( setter, getSuperclass() );
          if( getter != null ) {
            setters.remove( propName );
            propertyDescriptors.add( new JavaSourcePropertyDescriptor(
              propName, (IJavaClassInfo)getterType.getConcreteType(), getter, setter ) );
          }
        }
      }
    }
    for (Map.Entry<String, IJavaClassMethod> entry : setters.entrySet()) {
      String propName = entry.getKey();
      IJavaClassMethod setter = entry.getValue();
      IJavaClassType setterType = setter.getGenericReturnType();
      propertyDescriptors.add(new JavaSourcePropertyDescriptor(propName, (IJavaClassInfo) setterType.getConcreteType(), null, setter));
    }
    return propertyDescriptors.toArray(new IJavaPropertyDescriptor[propertyDescriptors.size()]);
  }

  @Override
  public IType getJavaType() {
    return TypeSystem.get(this);
  }

  public IJavaClassTypeVariable[] getTypeParameters() {
    if (_typeParameters == null) {
      IJavaASTNode typeParamsNode = _typeNode.getChildOfType(JavaASTConstants.typeParameters);
      if (typeParamsNode != null) {
        List<IJavaASTNode> typeParamNodes = typeParamsNode.getChildrenOfTypes(JavaASTConstants.typeParameter);
        _typeParameters = new IJavaClassTypeVariable[typeParamNodes.size()];
        for (int i = 0; i < _typeParameters.length; i++) {
          _typeParameters[i] = JavaSourceTypeVariable.create(this, typeParamNodes.get(i));
        }
      } else {
        _typeParameters = JavaSourceTypeVariable.EMPTY;
      }
    }
    return _typeParameters;
  }

  @Override
  public IClassDocNode createClassDocNode() {
    return null;
  }

  @Override
  public boolean hasCustomBeanInfo() {
    return false;
  }

  @Override
  public boolean isVisibleViaFeatureDescriptor(IScriptabilityModifier constraint) {
    return true;
  }

  @Override
  public boolean isHiddenViaFeatureDescriptor() {
    return false;
  }

  @Override
  public IJavaClassInfo getComponentType() {
    return null;
  }

  @Override
  public int getModifiers() {
    return getModifierList().getModifiers();
  }

  public IModifierList getModifierList() {
    if (_modifiersList == null) {
      _modifiersList = new JavaSourceModifierList(this, _typeNode.getChildOfType(JavaASTConstants.modifiers));
    }
    return _modifiersList;
  }

  public IType getEnclosingType() {
    return _enclosingClass == null ? null : TypeSystem.get(_enclosingClass);
  }

  public String getNamespace() {
    return _namespace;
  }

  @Override
  public IJavaClassInfo getArrayType() {
    return new JavaArrayClassInfo(this);
  }

  @Override
  public IJavaClassInfo[] getDeclaredClasses() {
    if (_innerClasses == null) {
      List<IJavaASTNode> innerNodes = _bodyNode.getChildrenOfTypes(
        JavaASTConstants.normalClassDeclaration,
        JavaASTConstants.normalInterfaceDeclaration,
        JavaASTConstants.annotationTypeDeclaration,
        JavaASTConstants.enumDeclaration
      );
      List<JavaSourceType> innerClasses = new ArrayList<JavaSourceType>(innerNodes.size());
      //noinspection ForLoopReplaceableByForEach
      for (int i = 0; i < innerNodes.size(); i++) {
        JavaSourceType inner = null;
        try {
          inner = JavaSourceType.createInner(innerNodes.get(i), this);
        } catch (SourceTypeFormatException e) {
          // malformed class
        }
        if (inner != null && inner.isValid()) {
          innerClasses.add(inner);
        }
      }
      _innerClasses = innerClasses.toArray(new JavaSourceType[innerClasses.size()]);
    }
    return _innerClasses;
  }

  @Override
  public Class getBackingClass() {
    return null;
  }

  public IJavaClassInfo getInnerClass(String relativeName) {
    for (IJavaClassInfo innerClass : getDeclaredClasses()) {
      if (innerClass.getSimpleName().equals(relativeName)) {
        return innerClass;
      }
    }
    return null;
  }

  private boolean isValid() {
    return _typeNode != null && _bodyNode != null;
  }

  @Override
  public IType getActualType(TypeVarToTypeMap typeMap) {
    return getActualType(typeMap, false);
  }

  @Override
  public IType getActualType(TypeVarToTypeMap typeMap, boolean bKeepTypeVars) {
    return TypeSystem.getActualType(getJavaType(), typeMap, bKeepTypeVars);
  }

  @Override
  public IJavaClassType getConcreteType() {
    return this;
  }

  public boolean isClass() {
    return this instanceof JavaSourceClass;
  }

  public boolean isInterface() {
    return this instanceof JavaSourceInterface;
  }

  @Override
  public Object newInstance() throws InstantiationException, IllegalAccessException {
    return null;
  }

  @Override
  public IJavaClassMethod getMethod(String methodName, IJavaClassInfo... paramTypes) throws NoSuchMethodException {
    outer:
    for (IJavaClassMethod method : getDeclaredMethods()) {
      if (!method.getName().equals(methodName)) {
        continue;
      }
      IJavaClassInfo[] methodParamTypes = method.getParameterTypes();
      if (paramTypes.length != methodParamTypes.length) {
        continue;
      }
      for (int i = 0; i < paramTypes.length; i++) {
        if (!paramTypes[i].equals(methodParamTypes[i])) {
          continue outer;
        }
      }
      return method;
    }
    throw new NoSuchMethodException();
  }

  public IJavaClassMethod getDeclaredMethod(String methodName, IJavaClassInfo... paramTypes) throws NoSuchMethodException {
    return getMethod(methodName, paramTypes);
  }

  @Override
  public IJavaMethodDescriptor[] getMethodDescriptors() {
    if (_methodDescriptors == null) {
      IJavaClassMethod[] declaredMethods = getDeclaredMethods();
      _methodDescriptors = new IJavaMethodDescriptor[declaredMethods.length];
      for (int i = 0; i < declaredMethods.length; i++) {
        _methodDescriptors[i] = new JavaSourceMethodDescriptor(declaredMethods[i]);
      }
    }
    return _methodDescriptors;
  }

  public boolean isEnum() {
    return this instanceof JavaSourceEnum;
  }

  @Override
  public boolean isPrimitive() {
    return false;
  }

  public boolean isAnnotation() {
    return this instanceof JavaSourceAnnotation;
  }

  public boolean isPublic() {
    return getModifierList().hasModifier(Modifier.PUBLIC);
  }

  public boolean isProtected() {
    return getModifierList().hasModifier(Modifier.PROTECTED);
  }

  public boolean isPrivate() {
    return getModifierList().hasModifier(Modifier.PRIVATE);
  }

  public boolean isInternal() {
    return getModifierList().hasModifier(Modifier.INTERNAL) ||
      (!getModifierList().hasModifier(Modifier.PUBLIC) &&
        !getModifierList().hasModifier(Modifier.PROTECTED) &&
        !getModifierList().hasModifier(Modifier.PRIVATE));
  }

  public IJavaClassType resolveType(String relativeName, int ignoreFlags) {
    return resolveType(relativeName, this, ignoreFlags);
  }

  public IJavaClassType resolveType(String relativeName, IJavaClassInfo whosAskin, int ignoreFlags) {
    Object cachedOrCyclicType = getCachedDetectCyclicType(relativeName);
    if( cachedOrCyclicType != null ) {
      return cachedOrCyclicType == CACHE_MISS ? null : (IJavaClassType)cachedOrCyclicType;
    }
    IJavaClassType type = null;
    try {

      // Primitives
      if (PRIMITIVES.containsKey(relativeName)) {
        return type = JavaTypes.getJreType(PRIMITIVES.get(relativeName)).getBackingClassInfo();
      }

      // Qualified or Semi-qualified Type e.g., Outer.Inner
      int iDot = relativeName.indexOf(".");
      if (iDot == 0) {
        return type = null;
      } else if (iDot >= 0) {
        return type = resolveQualifiedOrSemiQualifiedType(relativeName, whosAskin, iDot, ignoreFlags);
      }

      // Identity
      if (relativeName.equals(getSimpleName())) {
        return type = this;
      }

      // Direct Inner Classes (no recursion)
      IJavaClassType innerClass = JavaSourceUtil.resolveInnerClass(this, relativeName, whosAskin);
      if (innerClass != null) {
        return type = innerClass;
      }

      // Imported Class
      IJavaClassType importedType = resolveImport(relativeName);
      if (importedType != null) {
        return type = importedType;
      }

      // type variable
      for (IJavaClassTypeVariable typeParameter : getTypeParameters()) {
        if (relativeName.equals(typeParameter.getName())) {
          return type = typeParameter;
        }
      }

      // Enclosing Class
      IJavaClassInfo enclosingClass = getEnclosingClass();
      if (enclosingClass != null) {
        IJavaClassType outerClsCtx = enclosingClass.resolveType(relativeName, whosAskin, IGNORE_NONE);
        if (outerClsCtx != null) {
          return type = outerClsCtx;
        }
      }

      // Class in Same Package
      IJavaClassType neighborClass = resolveClassInSamePackage(relativeName);
      if (neighborClass != null) {
        return type = neighborClass;
      }

      // Super Ancestry
      if ((ignoreFlags & IGNORE_SUPERCLASS) == 0) {
        IJavaClassInfo superClass = getSuperclass();
        if (superClass != null) {
          IJavaClassType outerClsCtx = superClass.resolveType(relativeName, whosAskin, ignoreFlags);
          if (outerClsCtx != null) {
            return type = outerClsCtx;
          }
        }
      }

      // Interface Ancestry
      if ((ignoreFlags & IGNORE_INTERFACES) == 0) {
        for (IJavaClassInfo ifaceType : getInterfaces()) {
          IJavaClassType innerClassCtx = ifaceType.resolveType(relativeName, whosAskin, ignoreFlags);
          if (innerClassCtx != null) {
            return type = innerClassCtx;
          }
        }
      }

      // Not found
      return type = null;
    }
    finally {
      if( type == null ) {
        _cache.put( relativeName, CACHE_MISS );
      }
      else {
        _cache.put( relativeName, type );
      }
    }
  }

  private Object getCachedDetectCyclicType( String relativeName ) {
    // Short-circuit type cycles
    Object type = _cache.get( relativeName );
    if( type != null ) {
      return type;
    }
//    _cache.put( relativeName, CACHE_MISS );
    return null;
  }

  private IJavaClassType resolveQualifiedOrSemiQualifiedType(String typeName, IJavaClassInfo whosAskin, int iDot, int ignoreFlags) {
    // typeName is either:
    // 1) A fully qualified name e.g., com.abc.Foo
    // 2) A semi-qualified inner class name e.g., Foo.Inner
    // 3) A fully qualified inner class name e.g., com.abc.Foo.Inner
    String rootName = typeName.substring( 0, iDot );
    // First try to resolve a relative root type e.g., the Foo in Foo.Inner (for case #2)
    IJavaClassType rootType = resolveType( rootName, whosAskin, ignoreFlags);
    if (rootType == null) {
      // Now try to resolve a root qualified type, with or without an inner class (for cases #1 and #3)
      rootType = resolveRootQualifiedType(typeName);
      if (rootType != null) {
        if (rootType.getName().replace('$', '.').equals(typeName)) {
          // Case #1, no inner class
          return rootType;
        }
      } else {
        return null;
      }
    }
    // Case #2 or #3
    return resolveTrailingInnerClass(rootType, typeName, whosAskin);
  }

  private IJavaClassType resolveRootQualifiedType(String qname) {
    while (true) {
      IJavaClassType rootType = JavaSourceUtil.getClassInfo(qname, _gosuModule);
      if (rootType != null) {
        return rootType;
      }
      int iLastDot = qname.lastIndexOf('.');
      if (iLastDot > 0) {
        qname = qname.substring(0, iLastDot);
      } else {
        return null;
      }
    }
  }

  private IJavaClassType resolveTrailingInnerClass(IJavaClassType rootType, String fullNameIncludingRoot, IJavaClassInfo whosAskin) {
    String rootName;
    if (fullNameIncludingRoot.startsWith(rootType.getName())) {
      rootName = rootType.getName();
    } else {
      rootName = rootType.getSimpleName().replace('$', '.');
    }
    String innerSuffix = fullNameIncludingRoot.substring(rootName.length() + 1);
    String[] innerNames = innerSuffix.split("\\.");
    for (String innerName : innerNames) {
      IJavaClassType innerClass = JavaSourceUtil.resolveInnerClass((IJavaClassInfo) rootType.getConcreteType(), innerName, whosAskin);
      if (innerClass == null) {
        return null;
      }
      rootType = innerClass;
    }
    return rootType;
  }

  private IJavaClassType resolveClassInSamePackage(String relativeName) {
    String packageName = getNamespace();
    if (packageName.length() > 0) {
      relativeName = packageName + '.' + relativeName;
    }
    return JavaSourceUtil.getClassInfo(relativeName, _gosuModule);
  }

  public IJavaClassType resolveImport(String relativeName) {
    if( _importList == null ) {
      return null;
    }
    for (String importText : _importList) {
      int iStar = importText.lastIndexOf("*");
      if (iStar > 0) {
        IJavaClassType type = JavaSourceUtil.getClassInfo(importText.substring(0, iStar) + relativeName, _gosuModule);
        if (type != null) {
          return type;
        }
      } else if (importText.endsWith('.' + relativeName)) {
        IJavaClassType type = JavaSourceUtil.getClassInfo(importText, _gosuModule);
        if (type != null) {
          return type;
        }
      }
    }
    return null;
  }

  @Override
  public IJavaClassInfo getEnclosingClass() {
    return _enclosingClass;
  }

  public String toString() {
    return getName();
  }

  @Override
  public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
    return getModifierList().isAnnotationPresent(annotationClass);
  }

  @Override
  public IAnnotationInfo getAnnotation(Class annotationClass) {
    return getModifierList().getAnnotation(annotationClass);
  }

  @Override
  public IAnnotationInfo[] getDeclaredAnnotations() {
    return getModifierList().getAnnotations();
  }

  public TypeUsesMap getTypeUsesMap() {
    TypeUsesMap typeUsesMap = new TypeUsesMap();
    typeUsesMap.addToTypeUses( getNamespace() + ".*" );
    if (_importList != null) {
      for (String s : _importList) {
        typeUsesMap.addToTypeUses(s);
      }
    }
    return typeUsesMap;
  }

  public List<String> getImportList() {
    return _importList;
  }

  public List<String> getStaticImports() {
    return _staticImportList;
  }

  @Override
  public ISourceFileHandle getSourceFileHandle() {
    if (_fileHandle == null) {
      IDefaultTypeLoader loader = _enclosingClass.getModule().getTypeLoaders(IDefaultTypeLoader.class).get(0);
      _fileHandle = loader.getSouceFileHandle(getName());
    }
    return _fileHandle;
  }
}
