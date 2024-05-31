/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.javadoc;

import gw.lang.javadoc.IClassDocNode;
import gw.lang.javadoc.IConstructorNode;
import gw.lang.javadoc.IMethodNode;
import gw.lang.javadoc.IVarNode;
import gw.lang.javadoc.IParamNode;
import gw.lang.PublishInGosu;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassConstructor;
import gw.lang.reflect.java.IJavaMethodDescriptor;
import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.IJavaClassTypeVariable;
import gw.lang.reflect.java.IJavaClassGenericArrayType;
import gw.lang.reflect.java.IJavaClassWildcardType;
import gw.lang.reflect.java.IJavaClassParameterizedType;
import gw.util.concurrent.LocklessLazyVar;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.XMLConstants;
import java.io.InputStream;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Collections;

/**
 */
class ClassDocNode extends BaseFeatureNode implements IClassDocNode, IDocNodeWithDescription {
  private static final String DOC_EXTENSION = ".gti";
  private static final QName QNAME_CLASS = new QName( XMLConstants.NULL_NS_URI, "class" );
  private static final QName QNAME_DESC = new QName( XMLConstants.NULL_NS_URI, "desc" );
  private static final QName QNAME_METHOD = new QName( XMLConstants.NULL_NS_URI, "method" );
  private static final QName QNAME_VAR = new QName( XMLConstants.NULL_NS_URI, "var" );
  private static final QName QNAME_RETURN = new QName( XMLConstants.NULL_NS_URI, "return" );
  private static final QName QNAME_PARAM = new QName( XMLConstants.NULL_NS_URI, "param" );
  private static final QName QNAME_EX = new QName( XMLConstants.NULL_NS_URI, "ex" );
  private static final QName QNAME_CONS = new QName( XMLConstants.NULL_NS_URI, "cons" );
  private static final QName QNAME_DEPRECATED = new QName( XMLConstants.NULL_NS_URI, "deprecated" );

  private static final Map<QName,NodeHandler> _nodeHandlers = new HashMap<QName, NodeHandler>();

  private List<IMethodNode> _methods = Collections.emptyList();
  private List<IConstructorNode> _constructors = Collections.emptyList();
  private List<IVarNode> _vars = Collections.emptyList();
  private static final LocklessLazyVar<XMLInputFactory> XML_INPUT_FACTORY_FACTORY = new LocklessLazyVar<XMLInputFactory>()
  {
    @Override
    protected XMLInputFactory init()
    {
      XMLInputFactory xif = XMLInputFactory.newFactory();
      xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
      xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
      return xif;
    }
  };

  static {
    _nodeHandlers.put( QNAME_CLASS, new NodeHandler() {
      @Override
      public Object handle( Object parent, XMLStreamReader parser ) {
        if ( parent != null ) {
          throw new RuntimeException( "Expected null parent" );
        }
        return new ClassDocNode();
      }
    } );
    _nodeHandlers.put( QNAME_DESC, new NodeHandler() {
      @Override
      public Object handle( Object parent, XMLStreamReader parser ) throws XMLStreamException {
        ( (IDocNodeWithDescription) parent ).setDescription( parser.getElementText() );
        return null; // shouldn't have any children
      }
    } );
    _nodeHandlers.put( QNAME_METHOD, new NodeHandler() {
      @Override
      public Object handle( Object parent, XMLStreamReader parser ) throws XMLStreamException {
        MethodNode methodNode = new MethodNode();
        final String methodName = parser.getAttributeValue( null, "name" );
        if ( methodName == null ) {
          throw new RuntimeException( "method's name is null" );
        }
        methodNode.setName( methodName );
        ( (ClassDocNode) parent ).addMethod( methodNode );
        return methodNode;
      }
    } );
    _nodeHandlers.put( QNAME_VAR, new NodeHandler() {
      @Override
      public Object handle( Object parent, XMLStreamReader parser ) throws XMLStreamException {
        VarNode varNode = new VarNode();
        final String varName = parser.getAttributeValue( null, "name" );
        if ( varName == null ) {
          throw new RuntimeException( "var's name is null" );
        }
        varNode.setName( varName );
        ( (ClassDocNode) parent ).addVar( varNode );
        return varNode;
      }
    } );
    _nodeHandlers.put( QNAME_CONS, new NodeHandler() {
      @Override
      public Object handle( Object parent, XMLStreamReader parser ) throws XMLStreamException {
        ConstructorNode constructorNode = new ConstructorNode();
        ( (ClassDocNode) parent ).addConstructor( constructorNode );
        return constructorNode;
      }
    } );
    _nodeHandlers.put( QNAME_RETURN, new NodeHandler() {
      @Override
      public Object handle( Object parent, XMLStreamReader parser ) throws XMLStreamException {
        ( (MethodNode) parent ).setReturnDescription( parser.getElementText() );
        return null; // shouldn't have any children
      }
    } );
    _nodeHandlers.put( QNAME_PARAM, new NodeHandler() {
      @Override
      public Object handle( Object parent, XMLStreamReader parser ) throws XMLStreamException {
        ParamNode paramNode = new ParamNode();
        paramNode.setName( parser.getAttributeValue( null, "name" ) );
        paramNode.setType( parser.getAttributeValue( null, "type" ) );
        paramNode.setDescription( parser.getElementText() );
        ( (IDocNodeWithParams) parent ).addParam( paramNode );
        return null; // shouldn't have any children
      }
    } );
    _nodeHandlers.put( QNAME_EX, new NodeHandler() {
      @Override
      public Object handle( Object parent, XMLStreamReader parser ) throws XMLStreamException {
        ExceptionNode exNode = new ExceptionNode();
        exNode.setType( parser.getAttributeValue( null, "type" ) );
        exNode.setDescription( parser.getElementText() );
        ( (IDocNodeWithParams) parent ).addException( exNode );
        return null; // shouldn't have any children
      }
    } );
    _nodeHandlers.put( QNAME_DEPRECATED, new NodeHandler() {
      @Override
      public Object handle( Object parent, XMLStreamReader parser ) throws XMLStreamException {
        ( (BaseFeatureNode) parent ).setDeprecated( parser.getElementText() );
        return null; // shouldn't have any children
      }
    } );
  }

  @Override
  public List<IMethodNode> getMethods() {
    return _methods;
  }

  @Override
  public void addMethod( IMethodNode method ) {
    if ( _methods == Collections.EMPTY_LIST ) {
      _methods = new ArrayList<IMethodNode>( 1 );
    }
    _methods.add( method );
  }

  @Override
  public IMethodNode getMethod(IJavaMethodDescriptor name) {
    List<IMethodNode> methods = getMethods();
    for (IMethodNode method : methods) {
      if (method.getName().equals(name.getName()) && compareParams(name.getMethod().getGenericParameterTypes(), method.getParams())) {
        return method;
      }
    }
    return null;
  }

  @Override
  public IMethodNode getMethod(String name, Type[] parameterTypes) {
    List<IMethodNode> methods = getMethods();
    for (IMethodNode method : methods) {
      if (method.getName().equals(name) && compareParams(parameterTypes, method.getParams())) {
        return method;
      }
    }
    return null;
  }

  @Override
  public List<IConstructorNode> getConstructors() {
    return _constructors;
  }

  @Override
  public void addConstructor( IConstructorNode constructor ) {
    if ( _constructors == Collections.EMPTY_LIST ) {
      _constructors = new ArrayList<IConstructorNode>( 1 );
    }
    _constructors.add( constructor );
  }

  @Override
  public List<IVarNode> getVars() {
    return _vars;
  }

  @Override
  public void addVar( IVarNode var ) {
    if ( _vars == Collections.EMPTY_LIST ) {
      _vars = new ArrayList<IVarNode>( 1 );
    }
    _vars.add( var );
  }

  @Override
  public IVarNode getVar( String name ) {
    List<IVarNode> vars = getVars();
    for ( IVarNode var : vars ) {
      final String varName = var.getName(); // extract variable to debug TH-only NPE
      if ( varName.equals( name ) ) {
        return var;
      }
    }
    return null;
  }

  public static ClassDocNode get(Class<?> type) {
    if( !type.getName().startsWith( "gw.lang" ) &&
        !type.isAnnotationPresent( PublishInGosu.class ) )
    {
      return null;
    }
    String resourceName = type.getName().replaceAll("\\.", "/") + DOC_EXTENSION;
    if (type.getClassLoader() == null) {
      return null;
    }
    InputStream stream = type.getClassLoader().getResourceAsStream( resourceName );
    if ( stream == null ) {
      return null;
    }
    try {
      XMLStreamReader parser = XML_INPUT_FACTORY_FACTORY.get().createXMLStreamReader( stream );

      Object classNode = null; // will end up being last END_ELEMENT we receive
      LinkedList<Object> nodeStack = new LinkedList<Object>();
      Object parent = null;

      while ( parser.hasNext() ) {
        int event = parser.next();
        switch ( event ) {
          case XMLStreamConstants.START_ELEMENT: {
            QName elementName = parser.getName();
            NodeHandler nodeHandler = _nodeHandlers.get( elementName );
            Object newParent = nodeHandler.handle( parent, parser );
            if ( newParent != null ) {
              nodeStack.add( parent );
              parent = newParent;
            }
            break;
          }
          case XMLStreamConstants.END_ELEMENT: {
            classNode = parent;
            parent = nodeStack.removeLast();
            break;
          }
        }
      }
      parser.close();
      return (ClassDocNode) classNode;
    }
    catch ( Exception ex ) {
      throw new RuntimeException( "Could not parse " + resourceName, ex );
    }
  }

  @Override
  public IConstructorNode getConstructor(IJavaClassConstructor ctor) {
    for (IConstructorNode constructorNode : getConstructors()) {
      if (compareParams(ctor.getParameterTypes(), constructorNode.getParams())) {
        return constructorNode;
      }
    }
    return null;
  }

  private boolean compareParams(Type[] parameterTypes, List<IParamNode> params) {
    if (parameterTypes.length != params.size()) {
      return false;
    }
    for (int i = 0; i < parameterTypes.length; i++) {
      Type parameterType = parameterTypes[i];
      String parameterTypeName = getParameterTypeName(parameterType);
      if (!areTypeNamesEqual(parameterTypeName, params.get(i).getType())) {
        return false;
      }
    }

    return true;
  }

  private boolean compareParams(IJavaClassType[] parameterTypes, List<IParamNode> params) {
    if (parameterTypes.length != params.size()) {
      return false;
    }
    for (int i = 0; i < parameterTypes.length; i++) {
      IJavaClassType parameterType = parameterTypes[i];
      String parameterTypeName = getParameterTypeName(parameterType);
      if (!areTypeNamesEqual(parameterTypeName, params.get(i).getType())) {
        return false;
      }
    }

    return true;
  }

  // Some versions of qdox use $ for inner class names, some use . instead.  I'd
  // rather we just didn't care about the difference
  private boolean areTypeNamesEqual(String name1, String name2) {
    return name1.replace('$', '.').equals(name2.replace('$', '.'));
  }

  private String getParameterTypeName(Type parameterType) {
    String parameterTypeName;
    if (parameterType instanceof Class) {
      Class parameterT = (Class) parameterType;
      parameterTypeName = parameterT.getName();
    } else if (parameterType instanceof TypeVariable) {
      TypeVariable parameterT = (TypeVariable) parameterType;
      parameterTypeName = parameterT.getName();
    } else if (parameterType instanceof ParameterizedType) {
      ParameterizedType parameterT = (ParameterizedType) parameterType;
      parameterTypeName = getParameterTypeName(parameterT.getRawType());
    } else if (parameterType instanceof WildcardType) {
      assert false : "Do not expect WilcardType as a parameter type";
      parameterTypeName = null;
    } else if (parameterType instanceof GenericArrayType) {
      GenericArrayType parameterT = (GenericArrayType) parameterType;
      parameterTypeName = getParameterTypeName(parameterT.getGenericComponentType());
    } else {
      throw new RuntimeException("Don't know the type of " + parameterType);
    }
    return parameterTypeName;
  }

  private String getParameterTypeName(IJavaClassType parameterType) {
    String parameterTypeName;
    if (parameterType instanceof IJavaClassInfo) {
      parameterTypeName = parameterType.getName();
    } else if (parameterType instanceof IJavaClassTypeVariable) {
      parameterTypeName = parameterType.getName();
    } else if (parameterType instanceof IJavaClassParameterizedType) {
      IJavaClassParameterizedType parameterT = (IJavaClassParameterizedType) parameterType;
      parameterTypeName = getParameterTypeName(parameterT.getConcreteType());
    } else if (parameterType instanceof IJavaClassWildcardType) {
      assert false : "Do not expect WilcardType as a parameter type";
      parameterTypeName = null;
    } else if (parameterType instanceof IJavaClassGenericArrayType) {
      IJavaClassGenericArrayType parameterT = (IJavaClassGenericArrayType) parameterType;
      parameterTypeName = getParameterTypeName(parameterT.getGenericComponentType());
    } else {
      throw new RuntimeException("Don't know the type of " + parameterType);
    }
    return parameterTypeName;
  }

  private interface NodeHandler {

    Object handle( Object parent, XMLStreamReader parser ) throws XMLStreamException;
  }

}
