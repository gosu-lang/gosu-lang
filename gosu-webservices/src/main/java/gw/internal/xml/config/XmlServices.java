/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.config;

import gw.config.ServiceKernel;
import gw.internal.xml.IMarshaller;
import gw.internal.xml.ws.server.IWsiWebService;
import gw.internal.xml.ws.server.WsiUtilities;
import gw.internal.xml.ws.server.marshal.MarshalContext;
import gw.internal.xml.ws.server.marshal.UnmarshalContext;
import gw.lang.parser.IParsedElement;
import gw.lang.reflect.IType;
import gw.util.GosuLoggerFactory;
import gw.util.ILogger;
import gw.xml.XmlElement;

import java.util.HashMap;
import java.util.Map;

public class XmlServices extends ServiceKernel {
  private static String _marshallerClassName = "gw.internal.xml.Marshaller";
  private static XmlServices _instance;

  private static Map<Category,ILogger> _loggers;

  static {
    _loggers = new HashMap<Category,ILogger>(Category.values().length);
    for (Category category : Category.values()) {
      ILogger logger = GosuLoggerFactory.getLogger("XML." + category.name());
      _loggers.put(category, logger);
    }
  }

  /* these categories need to be defined in PLLoggerCategory as well */
  public enum Category { Loading, Runtime, XmlMarshal, XmlUnMarshal, Request }

  private XmlServices() {
  }

  public static XmlServices getInstance() {
    if (_instance == null) {
      _instance = new XmlServices();
    }
    return _instance;
  }

  protected void defineServices() {
    _instance = this;
    try {
      defineService( IMarshaller.class, (IMarshaller)Class.forName(_marshallerClassName).newInstance() );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
    catch( NoClassDefFoundError e)  {
      e.printStackTrace();
      throw e;
    }
  }

  @Override
  protected void redefineServices() {
    // don't really want to do this here via another class
    // only have a couple of services, and the calling system can use
    // redefineService(Class<? extends T> service, Q newProvider)
  }


  public static void redefineService(Class serviceInterface, String className) {
    if (IMarshaller.class.equals(serviceInterface)) {
      _marshallerClassName = className;
      if (_instance != null) {
        final IMarshaller service = _instance.getService(IMarshaller.class);
        if (service != null && !service.getClass().getName().equals(className)) {
          _instance.resetKernel();
        }
      }
    }
  }

  /** This will get the logger for the specific type
   *
   * @param category the category needed
   * @return the logger
   */
  public static ILogger getLogger( Category category) {
    return _loggers.get(category);
  }

  /** This will get the logger for any custom category, it will also initialize it to
   * a console appender at warning level if not otherwise configured.
   *
   * @param category the category needed
   * @return the logger
   */
  public static ILogger getLogger(String category) {
    return GosuLoggerFactory.getLogger(category);
  }

  /** this will check that the supplied type is valid for a webservice, if it is not valid it will format a exception on the
   * parsedElement.
   *
   * @param parsedElement the element to attach warnings and errors to.
   * @param name the parameter name or "return"
   * @param type the type of the parameter or return value
   * @param seenNamespaces a map of namespaces already seen, a caller should treat this as opaque and ignore
   */
  public static void checkWsiWebServiceType(IParsedElement parsedElement, String name, IType type, Map<String, Object> seenNamespaces) {
    getInstance().getService(IMarshaller.class).checkType(parsedElement, name, type, seenNamespaces);
  }

  /** this will create the target namespace for this type
   *
   * @param type to create a target namespace for
   * @return the uri for this type
   */
  public static String createTargetNamespace( IType type ) {
    @SuppressWarnings( {"deprecation"} )
    IWsiWebService clsAnnot = (IWsiWebService)type.getTypeInfo().getAnnotationsOfType( WsiUtilities.WSI_WEB_SERVICE_ANNOTATION_TYPE.get() ).get( 0 ).getInstance();
    if ( clsAnnot.getTargetNamespace() == null ) {
      return getInstance().getService( IMarshaller.class ).createTargetNamespace( null, type );
    }
    else {
      return clsAnnot.getTargetNamespace();
    }
  }

  /** this will create the target namespace for this relative path
   *
   * @param path to create a target namespace for
   * @return the uri for this type
   */
  public static String createTargetNamespace( String path ) {
    return getInstance().getService( IMarshaller.class ).createTargetNamespace( null, path );
  }

  /** This will create elements for the type in the target.
   *
   * @param parameterElement where to write the value
   * @param type the expected type of the object (e.g., may be an interface)
   * @param obj the actual object
   * @param context the context for this marshalling
   */
  public static void marshal(XmlElement parameterElement, IType type, Object obj, MarshalContext context) {
    getInstance().getService( IMarshaller.class ).marshal(parameterElement, type, obj, context);
  }

  /** This will create an object from the xml element.  There are several marshalling
   * attributes that will affect this behavior.
   *
   * @param type the type desired
   * @param parameterElement the element to unmarshal
   * @param context the context for this unmarshalling
   * @return the create and populated object
   * @throws Exception on error
   */
  public static Object unmarshal(IType type, XmlElement parameterElement, UnmarshalContext context) {
    return getInstance().getService( IMarshaller.class ).unmarshal(type, parameterElement, context);
  }
}
