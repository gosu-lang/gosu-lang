package editor.util;

import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaType;
import gw.util.GosuStringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 */
public class JavadocAccess
{
  private static JavadocAccess g_instance;

  private static final String[] J2SE_PACKAGE_PREFIXES = {"java.", "javax."};
  private static final String J2SE_URL_PREFIX = "http://java.sun.com/j2se/1.5.0/docs/api/";

  private static final Map<String, String> JAVADOC_URL_BY_PREFIXES = new HashMap<>();

  static
  {
    // Map package prefixes to a javadoc url, for now only providing j2se support.
    // TODO: map other javadocs by allowing customers to register prefixes with a url
    for( String J2SE_PACKAGE_PREFIX : J2SE_PACKAGE_PREFIXES )
    {
      JAVADOC_URL_BY_PREFIXES.put( J2SE_PACKAGE_PREFIX, J2SE_URL_PREFIX );
    }
  }

  private JavadocAccess()
  {
  }

  public static synchronized JavadocAccess instance()
  {
    if( g_instance == null )
    {
      g_instance = new JavadocAccess();
    }

    return g_instance;
  }

  public String getJavadocHelp( IFeatureInfo featureInfo )
  {
    if( featureInfo instanceof ITypeInfo )
    {
      return getTypeHelp( featureInfo.getOwnersType() );
    }
    else if( featureInfo instanceof IPropertyInfo )
    {
      return getMemberHelp( (IPropertyInfo)featureInfo );
    }
    else if( featureInfo instanceof IMethodInfo )
    {
      return getMemberHelp( (IMethodInfo)featureInfo );
    }
    else
    {
      return null;
    }
  }

  private String getTypeHelp( IType type )
  {
    if( isTypeHandled( type ) )
    {
      return buildHtmlForClass( type.getName() );
    }
    else if( type instanceof IJavaType )
    {
      return getGenericNoJavadocContent( type.getTypeInfo() );
    }
    else
    {
      return null;
    }
  }

  private String getMemberHelp( IPropertyInfo propertyInfo )
  {
    IType type = propertyInfo.getOwnersType();
    if( isTypeHandled( type ) )
    {
      return buildHtmlForProperty( (IJavaType)type, propertyInfo );
    }
    else if( type instanceof IJavaType )
    {
      return getGenericNoJavadocContent( propertyInfo );
    }
    else
    {
      return null;
    }
  }

  private String getMemberHelp( IMethodInfo methodInfo )
  {
    IType type = methodInfo.getOwnersType();
    if( isTypeHandled( type ) )
    {
      return buildHtmlForMethod( (IJavaType)type, methodInfo );
    }
    else if( type instanceof IJavaType )
    {
      return getGenericNoJavadocContent( methodInfo );
    }
    else
    {
      return null;
    }
  }

  private boolean isTypeHandled( IType type )
  {
    if( type instanceof IJavaType )
    {
      String javadocURLPrefix = getJavadocURLPrefix( type );
      return javadocURLPrefix != null;
    }
    else
    {
      return false;
    }
  }

  private String getJavadocURLPrefix( IType type )
  {
    String typeName = type.getName();
    int i = typeName.indexOf( '.' );
    return JAVADOC_URL_BY_PREFIXES.get( typeName.substring( 0, i + 1 ) );
  }

  private String getGenericNoJavadocContent( IFeatureInfo featureInfo )
  {
    return "No javadoc found for " + featureInfo.getDisplayName();
  }

  private String buildHtmlForClass( String fqn )
  {
    return buildHtml( J2SE_URL_PREFIX + "index.html?" + fqn.replace( '.', '/' ) + ".html", fqn );
  }

  private String buildHtmlForProperty( IJavaType type, IPropertyInfo propertyInfo )
  {
    String propertyName = propertyInfo.getName();
    String strClassName = type.getName();
    strClassName = GosuStringUtil.replaceChars( strClassName, '.', '/' );
    return buildHtml( getJavadocURLPrefix( type ) + strClassName + ".html#" + propertyName, propertyName );
  }

  private String buildHtmlForMethod( IJavaType type, IMethodInfo methodInfo )
  {
    String strClassName = type.getName();
    strClassName = GosuStringUtil.replaceChars( strClassName, '.', '/' );
    StringBuilder strUrl = new StringBuilder( getJavadocURLPrefix( type ) );
    strUrl.append( strClassName ).append( ".html#" );
    appendMethodSignatureLink( strUrl, methodInfo );
    return buildHtml( strUrl.toString(), getMethodSignatureHtml( methodInfo ) );
  }

  private void appendMethodSignatureLink( StringBuilder strUrl, IMethodInfo methodInfo )
  {
    strUrl.append( methodInfo.getDisplayName() ).append( '(' );
    IParameterInfo[] parameterInfos = methodInfo.getParameters();
    for( int i = 0; i < parameterInfos.length; i++ )
    {
      IParameterInfo parameterInfo = parameterInfos[i];
      strUrl.append( i > 0 ? ",%20" : "" ).append( parameterInfo.getFeatureType() );
    }
    strUrl.append( ')' );
  }

  private String getMethodSignatureHtml( IMethodInfo methodInfo )
  {
    StringBuilder strSignature = new StringBuilder( methodInfo.getDisplayName() + "(" );
    IParameterInfo[] parameterInfos = methodInfo.getParameters();
    for( int i = 0; i < parameterInfos.length; i++ )
    {
      IParameterInfo parameterInfo = parameterInfos[i];
      strSignature.append( i > 0 ? ", " : "" ).append( TypeSystem.getUnqualifiedClassName( parameterInfo.getFeatureType() ) );
    }
    strSignature.append( ')' );
    return strSignature.toString();
  }

  private String buildHtml( String strUrl, String strText )
  {
    return "<html><b>" + "Go to online JavaDoc" + " </b><a href=\"" + strUrl + "\">" + strText + "</a></html>";
  }
}
