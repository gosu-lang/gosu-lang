/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.config;

import gw.fs.IFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class Registry
{
  /**
   * The xml tag for indicating if deprecated methods/properties s/b allowed.
   */
  public static final String TAG_ALLOW_DEPRECATED_REFERENCES = "allow-deprecated-references";

  /**
   * The xml tag for indicating if Gosu query expressions can reference entities.
   */
  public static final String TAG_ALLOW_ENTITY_QUERIES = "allow-entity-queries";

  /**
   * The xml tag for indicating what class to use to initialize Gosu's Common Services with.
   */
  private static final String COMMON_SERVICE_INIT = "common-service-init";

  private static final String TAG_SERIALIZATION = "serialization";

  private static final String TAG_TYPELOADERS = "typeloaders";

  private static final String TAG_TYPELOADER = "typeloader";

  private static final String TAG_CLASSPATH = "classpath";

  public static final String TAG_CLASS = "class";

  public static final String TAG_SRC = "src";

  public static final String TAG_ENTRY = "entry";

  /**
   * The singleton instance of this class.
   */
  private static Registry g_instance;

  /**
   * A list of init listeners.
   */
  private static List g_listeners = new ArrayList();

  /**
   * Indicates if deprecated methods/properties s/b allowed.
   */
  private boolean _bAllowDeprecatedReferences;

  /**
   * Indicates if Gosu query expressions can reference entities.
   */
  private boolean _bAllowEntityQueires;

  /**
   * XMLNode specifications for additional typeloaders that will be created for
   * this runtime environment
   */
  private List<TypeLoaderSpec> _additionalTypeLoaderSpecs = Collections.emptyList();

  /**
   * Classpath entries from the registry
   */
  private List<String> _classpathEntries = Collections.emptyList();

  /**
   * The entity access class.
   */
  private String _commonServiceInit;

  /**
   * The URL of the active registry
   */
  private URL _registerUrl;

  public Registry(String kernelInitClass) {
    _commonServiceInit = kernelInitClass;
  }


  static
  {
    initDefaults();
  }

  /**

  /**
   * Initialize with defaults.
   * Guidewire applications should NEVER call this.
   */
  public static void initDefaults()
  {
    try
    {
      Registry.setLocation( Registry.class.getResource( "/gw/config/default.xml" ) );
    }
    catch( Throwable t )
    {
      t.printStackTrace();
    }
  }

  public static void setLocation(String kernelInitClass) {
    g_instance = new Registry( kernelInitClass );
  }

  /**
   * Sets the location of the registry file. Initializes singleton registry.
   *
   * @param registryURL
   */
  public static void setLocation( URL registryURL )
  {
    setLocation( registryURL, DefaultCommonsServiceKernelInit.class.getName() );
  }
  public static void setLocation( URL registryURL, String strCommonServicesInitDefault )
  {
    g_instance = new Registry( registryURL, strCommonServicesInitDefault );
    fireLocationChanged();
  }

  public synchronized static void addLocationListener( ChangeListener l )
  {
    //noinspection unchecked
    g_listeners.add( l );
  }

  public synchronized static void removeLocationListener( ChangeListener l )
  {
    //noinspection unchecked
    g_listeners.add( l );
  }

  private static void fireLocationChanged()
  {
    if( g_listeners.isEmpty() )
    {
      return;
    }

    ChangeEvent e = new ChangeEvent( g_instance );
    synchronized( Registry.class )
    {
      for( int i = 0; i < g_listeners.size(); i++ )
      {
        ((ChangeListener)g_listeners.get( i )).stateChanged( e );
      }
    }
  }

  private Registry( URL registrURL, String strCommonServicesInitDefault )
  {
    try
    {
      _registerUrl = registrURL;
      Document doc = getDocument( registrURL );

      //override kernel init if present
      Node commonService = getTag( doc, COMMON_SERVICE_INIT );
      String kernelInit = null;
      if( commonService != null )
      {
        kernelInit = commonService.getAttributes().item( 0 ).getNodeValue();
      }
      kernelInit = kernelInit == null || kernelInit.isEmpty() ? strCommonServicesInitDefault : kernelInit;
      if( kernelInit != null )
      {
        setCommonServiceInit( kernelInit );
      }

      String allowDeprecatedRefs = getTag( doc, TAG_ALLOW_DEPRECATED_REFERENCES ) == null ? null :
                                   getTag( doc, TAG_ALLOW_DEPRECATED_REFERENCES ).getTextContent();
      if( allowDeprecatedRefs != null )
      {
        setAllowDeprecatedReferences( Boolean.valueOf( allowDeprecatedRefs ) );
      }

      String allowEntityQueries = getTag( doc, TAG_ALLOW_ENTITY_QUERIES ) == null ? null :
                                  getTag( doc, TAG_ALLOW_ENTITY_QUERIES ).getTextContent();
      if( allowEntityQueries != null )
      {
        setAllowEntityQueires( Boolean.valueOf( allowEntityQueries ) );
      }

      Node typeLoaders = getTag( doc, TAG_TYPELOADERS );
      if( typeLoaders != null )
      {
        List<TypeLoaderSpec> additionalTypeLoaders = new ArrayList<TypeLoaderSpec>();
        NodeList childNodes = typeLoaders.getChildNodes();
        int i = 0;
        while( i < childNodes.getLength() )
        {
          Node xmlNode = childNodes.item( i );
          if( xmlNode.getNodeName().equals( TAG_TYPELOADER ) )
          {
            additionalTypeLoaders.add( new TypeLoaderSpec(xmlNode, _registerUrl) );
          }
          i++;
        }
        _additionalTypeLoaderSpecs = Collections.unmodifiableList( additionalTypeLoaders );
      }

      Node classpathEntries = getTag( doc, TAG_CLASSPATH );
      if( classpathEntries != null )
      {
        List<String> entries = new ArrayList<String>();
        NodeList childNodes = classpathEntries.getChildNodes();
        int i = 0;
        while( i < childNodes.getLength() )
        {
          Node xmlNode = childNodes.item( i );
          if( xmlNode.getNodeName().equals( TAG_ENTRY ) )
          {
            entries.add( xmlNode.getTextContent() );
          }
          i++;
        }
        _classpathEntries = Collections.unmodifiableList( entries );
      }
    }
    catch( Exception e )
    {
      e.printStackTrace();
      throw new RuntimeException( e.getMessage(), e );
    }
  }

  private Document getDocument( URL registrURL ) throws IOException, ParserConfigurationException, SAXException
  {
    InputStream inputStream = registrURL.openStream();;
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    Document doc = db.parse( new InputSource( inputStream ) );
    inputStream.close();
    return doc;
  }

  private Node getTag( Document doc, String tagName )
  {
    NodeList list = doc.getElementsByTagName( tagName );
    return list == null ? null : list.item( 0 );
  }

  /**
   * Get the singleton instance.
   *
   * @return The one and only Registry.
   */
  public static Registry instance()
  {
    return g_instance;
  }

  /**
   * @return
   */
  public String getCommonServiceInit()
  {
    return _commonServiceInit;
  }

  /**
   * @param commonServiceInit
   */
  public void setCommonServiceInit( String commonServiceInit )
  {
    _commonServiceInit = commonServiceInit;
  }

  public List<TypeLoaderSpec> getAdditionalTypeLoaders()
  {
    return _additionalTypeLoaderSpecs;
  }

  public List<String> getClasspathEntries()
  {
    return _classpathEntries;
  }

  /**
   *
   * @return
   */
  public boolean isAllowDeprecatedReferences()
  {
    return _bAllowDeprecatedReferences;
  }

  public void setAllowDeprecatedReferences( boolean bAllowDeprecatedReferences )
  {
    _bAllowDeprecatedReferences = bAllowDeprecatedReferences;
  }

  /**
   *
   * @return
   */
  public boolean isAllowEntityQueires()
  {
    return _bAllowEntityQueires;
  }

  public void setAllowEntityQueires( boolean bAllowEntityQueires )
  {
    _bAllowEntityQueires = bAllowEntityQueires;
  }

  public void updateClasspath( List<String> classpath )
  {
    if( new HashSet( classpath ).equals( new HashSet( getClasspathEntries() ) ) )
    {
      return;
    }
    Document doc;
    try
    {
      doc = getDocument( _registerUrl );

      Node classpathEntries = getTag( doc, TAG_CLASSPATH );
      if( classpathEntries == null )
      {
        Node ser = getTag( doc, TAG_SERIALIZATION );
        classpathEntries = doc.createElement( TAG_CLASSPATH );
        ser.appendChild( classpathEntries );
      }
      else
      {
        while( classpathEntries.hasChildNodes() )
        {
          classpathEntries.removeChild( classpathEntries.getLastChild() );
        }
      }
      List<String> classpathList = new ArrayList<String>();
      for( String strPath : classpath )
      {
        Element entry = doc.createElement( TAG_ENTRY );
        entry.setTextContent( strPath );
        classpathEntries.appendChild( entry );
        classpathList.add( strPath );
      }
      instance()._classpathEntries = classpathList;
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }

  private boolean canWrite( File file )
  {
    if( file.isFile() && !file.canWrite() )
    {
      return file.setWritable( true );
    }
    return true;
  }

//  private boolean _canWrite( File file )
//  {
//    if( file.isFile() && !file.canWrite() )
//    {
//      if( JOptionPane.showConfirmDialog( null, file.getName() + " is not writable.  Do you want to make the file writable?", "Gosu",
//                                              JOptionPane.YES_NO_OPTION ) == JOptionPane.YES_OPTION )
//      {
//        if( !file.setWritable( true ) )
//        {
//          JOptionPane.showMessageDialog( null, "Failed to make " + file.getName() + " writable.", "Gosu", JOptionPane.ERROR_MESSAGE );
//          return false;
//        }
//        return true;
//      }
//      else
//      {
//        return false;
//      }
//    }
//    return true;
//  }

}
