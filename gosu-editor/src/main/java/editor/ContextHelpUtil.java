package editor;

import editor.util.JavadocAccess;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IFunctionSymbol;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.expressions.IBeanMethodCallExpression;
import gw.lang.parser.expressions.IFieldAccessExpression;
import gw.lang.parser.expressions.IMethodCallExpression;
import gw.lang.parser.expressions.INewExpression;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.lang.parser.statements.IFunctionStatement;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.IJavaBasePropertyInfo;
import gw.util.GosuStringUtil;

public class ContextHelpUtil
{
  public static final String ERROR_IN_EXPR = "Error in curent expression";
  public static final String NO_DOCUMENTATION = "No documentation available";

  public static String getContextHelp( IParseTree deepestParseTree )
  {
    if( deepestParseTree == null )
    {
      return null;
    }
    if( (deepestParseTree.getParsedElement() instanceof ITypeLiteralExpression) &&
        (deepestParseTree.getParsedElement().getParent() instanceof INewExpression) )
    {
      deepestParseTree = deepestParseTree.getParsedElement().getParent().getLocation();
    }

    IFeatureInfo featureInfo;
    String description = null;
    IParsedElement deepestParsedElement = deepestParseTree.getParsedElement();
    if( deepestParsedElement.hasParseExceptions() )
    {
      description = ERROR_IN_EXPR;
    }
    else
    {
      featureInfo = getCorrespondingFeatureInfo( deepestParsedElement );
      if( featureInfo != null )
      {
        if( featureInfo.getOwnersType() instanceof IGosuClass )
        {
          description = getContextHelp( featureInfo, deepestParsedElement );
        }
        else
        {
          description = JavadocAccess.instance().getJavadocHelp( featureInfo );
        }
      }
    }

    if( GosuStringUtil.isEmpty( description ) )
    {
      description = NO_DOCUMENTATION;
    }
    return description;
  }

  public static String getContextHelp( IFeatureInfo featureInfo )
  {
    String description;
    if( featureInfo != null )
    {
      description = featureInfo.getDescription();
    }
    else
    {
      description = null;
    }
    StringBuilder descriptionString = new StringBuilder( description == null ? "" : description );
    if( featureInfo instanceof IMethodInfo )
    {
      boolean addedHeader = false;
      IMethodInfo methodInfo = (IMethodInfo)featureInfo;
      // Builds the Parameters section for a method.  The parameters are output as definition list
      for( IParameterInfo parameterInfo : methodInfo.getParameters() )
      {
        String parameterDescription = parameterInfo.getDescription();
        if( !GosuStringUtil.isEmpty( parameterDescription ) )
        {
          if( !addedHeader )
          {
            descriptionString.append( "<H3>" ).append( "parameters" ).append( "</H3>\n<dl>\n" );
            addedHeader = true;
          }
          descriptionString.append( "<dt>" ).append( parameterInfo.getName() ).append( "</dt>\n<dd>" ).append( parameterDescription ).append( "</dd>\n" );
        }
      }
      if( addedHeader )
      {
        descriptionString.append( "</dl>\n" );
      }
      String returnDescription = methodInfo.getReturnDescription();
      // Builds the Returns section of the page for entries that have them..
      if( !GosuStringUtil.isEmpty( returnDescription ) )
      {
        descriptionString.append( "<H3>" ).append( "returns" ).append( "</H3>\n" ).append( returnDescription ).append( "\n" );
      }
    }
    if( featureInfo instanceof IJavaBasePropertyInfo )
    {
      String returnDescription = ((IJavaBasePropertyInfo)featureInfo).getReturnDescription();
      // Builds the Returns section of the page for entries that have them..
      if( !GosuStringUtil.isEmpty( returnDescription ) )
      {
        descriptionString.append( "<H3>" ).append( "Returns" ).append( "</H3>\n" ).append( returnDescription ).append( "\n" );
      }
    }
    if( featureInfo instanceof IPropertyInfo )
    {
      if( ((IPropertyInfo)featureInfo).isReadable() && ((IPropertyInfo)featureInfo).isWritable() )
      {
        descriptionString.append( "<H4>Readable/Writable</H4>" );
      }
      else
      {
        if( ((IPropertyInfo)featureInfo).isReadable() )
        {
          descriptionString.append( "<H4>Readable</H4>" );
        }
        if( ((IPropertyInfo)featureInfo).isWritable() )
        {
          descriptionString.append( "<H4>Writable</H4>" );
        }
      }
    }
    description = descriptionString.toString();
    if( GosuStringUtil.isEmpty( description ) )
    {
      description = JavadocAccess.instance().getJavadocHelp( featureInfo );
      if( description == null )
      {
        description = NO_DOCUMENTATION;
      }
    }
    if( featureInfo instanceof IAttributedFeatureInfo && ((IAttributedFeatureInfo)featureInfo).isDeprecated() )
    {
      description = "<b" + "deprecated" + "</b>" + ((IAttributedFeatureInfo)featureInfo).getDeprecatedReason() + "<br><br>" + description;
    }

    return description;
  }

  private static String getContextHelp( IFeatureInfo featureInfo, IParsedElement deepestParsedElement )
  {
    assert deepestParsedElement != null;
    return getContextHelp( featureInfo );
  }

  private static IFeatureInfo getCorrespondingFeatureInfo( IParsedElement deepestParsedElement )
  {
    IFeatureInfo featureInfo = null;
    if( deepestParsedElement instanceof ITypeLiteralExpression )
    {
      ITypeLiteralExpression typeLiteral = (ITypeLiteralExpression)deepestParsedElement;
      IType type = typeLiteral.getType().getType();
      featureInfo = type.getTypeInfo();
    }
    else if( deepestParsedElement instanceof IFieldAccessExpression )
    {
      IFieldAccessExpression ma = (IFieldAccessExpression)deepestParsedElement;
      if( ma.getMemberExpression() != null || ma.getMemberName() == null )
      {
        return null;
      }
      try
      {
        featureInfo = TypeSystem.getPropertyInfo( ma.getRootType(), ma.getMemberName(), null, null, null );
      }
      catch( ParseException e )
      {
        featureInfo = null;
      }
    }
    else if( deepestParsedElement instanceof IBeanMethodCallExpression )
    {
      IBeanMethodCallExpression bmc = (IBeanMethodCallExpression)deepestParsedElement;
      featureInfo = bmc.getMethodDescriptor();
    }
    else if( deepestParsedElement instanceof IMethodCallExpression )
    {
      IMethodCallExpression mc = (IMethodCallExpression)deepestParsedElement;
      IFunctionSymbol functionSymbol = mc.getFunctionSymbol();
      if( functionSymbol instanceof IDynamicFunctionSymbol )
      {
        IDynamicFunctionSymbol dynamicFunctionSymbol = (IDynamicFunctionSymbol)functionSymbol;
        IType containingType = dynamicFunctionSymbol.getScriptPart().getContainingType();
        featureInfo = containingType.getTypeInfo().getMethod( dynamicFunctionSymbol.getDisplayName(), dynamicFunctionSymbol.getArgTypes() );
      }
      else
      {
        featureInfo = functionSymbol.getType().getTypeInfo();
      }
    }
    else if( deepestParsedElement instanceof INewExpression )
    {
      INewExpression newExpression = (INewExpression)deepestParsedElement;
      featureInfo = newExpression.getConstructor();
    }
    else if( deepestParsedElement instanceof IFunctionStatement )
    {
      IFunctionStatement functionStatement = (IFunctionStatement)deepestParsedElement;
      IDynamicFunctionSymbol symbol = functionStatement.getDynamicFunctionSymbol();
      if( symbol != null )
      {
        featureInfo = symbol.getMethodOrConstructorInfo();
      }
    }
    return featureInfo;
  }
}
