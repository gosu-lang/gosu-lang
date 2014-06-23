/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.simple;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.InputStream;
import java.io.StringReader;

public class SimpleXmlParser {

  private static ThreadLocal<SAXParser> _saxParser = new ThreadLocal<SAXParser>();

  // Disable construction of new instances
  private SimpleXmlParser() { }

  public static SimpleXmlNode parseFile(File file) {
    SimpleXmlNodeHandler nodeHandler = new SimpleXmlNodeHandler();

    try {
      makeSAXParser().parse( file, nodeHandler );
      return nodeHandler.getRoot();
    } catch (Exception e) {
      throw maybeWrapException( e );
    }
  }

  public static SimpleXmlNode parseInputStream(InputStream stream) {
    SimpleXmlNodeHandler nodeHandler = new SimpleXmlNodeHandler();

    try {
      makeSAXParser().parse( stream, nodeHandler );
      return nodeHandler.getRoot();
    } catch (Exception e) {
      throw maybeWrapException( e );
    }
  }

  public static SimpleXmlNode parseString(String s) {
    SimpleXmlNodeHandler nodeHandler = new SimpleXmlNodeHandler();

    try {
      makeSAXParser().parse( new InputSource( new StringReader( s ) ), nodeHandler);
      return nodeHandler.getRoot();
    } catch (Exception e) {
      throw maybeWrapException( e );
    }
  }

  private static RuntimeException maybeWrapException(Exception e) {
    if (e instanceof RuntimeException) {
      return (RuntimeException) e;
    } else {
      return new RuntimeException( e );
    }
  }

  /*
   * Builds and returns the SAXParser
   */
  private static SAXParser makeSAXParser() throws ParserConfigurationException, SAXException {
    SAXParser parser = _saxParser.get();
    if (parser != null) {
      try {
        parser.reset();
      } catch (UnsupportedOperationException uoe) {
        parser = null;
      }
    }
    if (parser == null) {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      parser = factory.newSAXParser();
      _saxParser.set( parser );
    }
    return parser;
  }

}