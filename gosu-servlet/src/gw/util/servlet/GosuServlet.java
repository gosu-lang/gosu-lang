/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util.servlet;

import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ITemplateType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.Gosu;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

@SuppressWarnings("serial")
public class GosuServlet extends HttpServlet
{
  private static final String ENCODING = "UTF-8";
  private HashMap<ITemplateType, IMethodInfo> _renderMethodsMap = new HashMap<ITemplateType, IMethodInfo>();
  private boolean _bInit;

  @Override
  protected void service( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException {
    initGosu( req );
    resp.setContentType( "text/html; charset=" + ENCODING );
    String uri = req.getRequestURI();
    String strType = getTypeNameFromUri( uri, req.getContextPath() );
    ITemplateType templateType = (ITemplateType)TypeSystem.getByFullNameIfValid( strType );
    if( templateType == null ) {
      resp.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR, strType + " not found" );
      return;
    }
    if( !templateType.isValid() ) {
      resp.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR, getInvalidTemplateMessage( templateType ) );
      return;
    }
    ServletParams params = new ServletParams( req, resp, req.getSession(), req.getSession().getServletContext() );
    renderTemplate( templateType, resp.getWriter(), params );
    resp.setStatus( HttpServletResponse.SC_OK );
  }

  private void renderTemplate( ITemplateType templateType, PrintWriter writer, ServletParams params ) {
    IMethodInfo renderMethod = _renderMethodsMap.get(templateType);
    if( renderMethod == null ) {
      renderMethod = templateType.getTypeInfo().getMethod( "render", JavaTypes.getJreType(Writer.class), JavaTypes.getJreType( ServletParams.class ) );
      _renderMethodsMap.put(templateType, renderMethod);
    }
    renderMethod.getCallHandler().handleCall( null, writer, params );
  }

  private String getInvalidTemplateMessage( ITemplateType templateType ) {
    StringBuilder sb = new StringBuilder( "Template: " + templateType.getName() + " has errors:\n" );
    //noinspection ThrowableResultOfMethodCallIgnored
    sb.append( templateType.getTemplateGenerator().getProgram().getGosuProgram().getParseResultsException().getFeedback() );
    return sb.toString();
  }

  private String getTypeNameFromUri( String uri, String context ) {
    String strType = uri.replaceFirst( context, "" ).replace( '/', '.' );
    if( strType.startsWith( "." ) ) {
      strType = strType.substring( 1 );
    }
    strType = strType.substring( 0, strType.lastIndexOf( '.' ) );
    return strType;
  }


  private void initGosu( HttpServletRequest req ) {
    if( !_bInit ) {
      synchronized( GosuServlet.class ) {
        if( !_bInit ) {
          Properties props = System.getProperties();
          for ( String prop : props.stringPropertyNames() ) {
            if ( prop.startsWith("gw.") && prop.endsWith(".env") ) {
              _bInit = true;
              return;
            }
          }

          String strServletDir = req.getSession().getServletContext().getRealPath( "/" );
          File servletDir = new File( strServletDir );
          List<File> classpath = new ArrayList<File>();
          classpath.add( servletDir );
          addWebInfPaths( new File( servletDir, "WEB-INF" ), classpath );
          Gosu.init( classpath );
          _bInit = true;
        }
      }
    }
  }

  public void addWebInfPaths( File webInf, List<File> classpath ) {
    if( !webInf.isDirectory() ) {
      return;
    }
    File classes = new File( webInf, "classes" );
    classpath.add( classes );
    File lib = new File( webInf, "lib" );
    if( lib.isDirectory() ) {
      addJarsToClassPath( classpath, lib );
    }
  }

  private static void addJarsToClassPath( final List<File> classpath, File lib ) {
    //noinspection ResultOfMethodCallIgnored
    lib.listFiles(
        new FilenameFilter() {
          public boolean accept( File dir, String name ) {
            String lname = name.toLowerCase();
            if( lname.endsWith( ".jar" ) || lname.endsWith( ".zip" ) ) {
              classpath.add( new File( dir, name ) );
            }
            return false;
          }
      } );
  }

}
