package gw.internal.xml.ws.server;

import gw.lang.parser.IExpression;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.IUsageSiteValidator;
import gw.lang.parser.expressions.IBeanMethodCallExpression;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.lang.parser.resources.Res;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;

/**
* Created by IntelliJ IDEA.
* User: dandrews
* Date: 11/4/13
* Time: 2:17 PM
* To change this template use File | Settings | File Templates.
*/ // method call validator - validates call sites for publish(IType) to ensure only types annotated with
// @WsiWebService are published, for user convenience
public class ServerAnnotationVerifier implements IUsageSiteValidator
{

  public void validate( IParsedElement pe ) {
    if ( pe instanceof IBeanMethodCallExpression) { // should always be true
      IBeanMethodCallExpression expr = (IBeanMethodCallExpression) pe;
      if ( expr.getArgs() != null ) {
        IExpression arg = expr.getArgs()[0];
        if ( arg instanceof ITypeLiteralExpression) {
          ITypeLiteralExpression typeLiteral = (ITypeLiteralExpression) arg;
          IType type = typeLiteral.getType().getType();
          if ( ! type.getTypeInfo().hasAnnotation( TypeSystem.getByFullName("gw.xml.ws.annotation.WsiWebService") ) ) {
            expr.addParseException( Res.MSG_ANY, "Type argument must have @WsiWebService annotation" );
          }
        }
      }
    }
  }
}
