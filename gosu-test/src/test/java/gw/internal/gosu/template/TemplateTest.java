/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.template;

import gw.internal.gosu.parser.TypeLoaderAccess;
import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.template.TemplateParseException;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ITemplateType;
import gw.test.TestClass;
import gw.util.GosuExceptionUtil;
import gw.util.GosuTestUtil;
import junit.framework.Assert;


public class TemplateTest extends TestClass
{
  public void testRenderNoArgTemplateAsString() throws ParseResultsException {
    Assert.assertEquals("No Arg Template", GosuTestUtil.eval("return gw.internal.gosu.template.NoArg.renderToString()"));
  }
  
  public void testRenderNoArgTemplateToWriter() throws ParseResultsException {
    Assert.assertEquals("No Arg Template", GosuTestUtil.eval("var w = new java.io.StringWriter(); gw.internal.gosu.template.NoArg.render(w); return w.toString()"));
  }
  
  public void testEvaluateStringLiteralInTemplate() throws ParseResultsException {
    Assert.assertEquals("evaluated string", GosuTestUtil.eval("return gw.internal.gosu.template.NoArgEval.renderToString()"));
  }
  
  public void testEvaluateCurlyStyleStringLiteralInTemplate() throws ParseResultsException {
    Assert.assertEquals("evaluated string", GosuTestUtil.eval("return gw.internal.gosu.template.NoArgEvalCurly.renderToString()"));
  }

  public void testEvaluateControlFlowInTemplate() throws ParseResultsException {
    Assert.assertEquals("pass", GosuTestUtil.eval("return gw.internal.gosu.template.NoArgCtrl.renderToString()"));
  }
  
  public void testBadTemplateTypeIsNotValid() {
    Assert.assertFalse( TypeLoaderAccess.instance().getByFullNameIfValid("gw.internal.gosu.template.Errant_NoArgBad").isValid());
  }
  
  public void testCantPassArgsToNoArgTemplate() {
    try {
      GosuTestUtil.eval("return gw.internal.gosu.template.NoArg.renderToString(\"foo\")");
      Assert.fail();
    } catch (RuntimeException e) {
      Assert.assertTrue(e.getCause() instanceof ParseResultsException);
      // pass
    }
    try {
      GosuTestUtil.eval("var w = new java.io.StringWriter(); gw.internal.gosu.template.NoArg.render(w, \"foo\"); return w.toString()");
      Assert.fail();
    } catch (RuntimeException e) {
      Assert.assertTrue(e.getCause() instanceof ParseResultsException);
      // pass
    }
  }


  public void testExpressionsNotFused() throws ParseResultsException {
    Assert.assertEquals("Prevent this from compiling as x(x), where the two expressions are fused hello hello", GosuTestUtil.eval("return gw.internal.gosu.template.SeparateExpr.renderToString()"));
  }

  public void testRenderOneArgTemplateAsStringTwice() throws ParseResultsException {
    Assert.assertEquals("One Arg Template: bar1One Arg Template: bar2", GosuTestUtil.eval("return gw.internal.gosu.template.OneArg.renderToString(\"bar1\") + gw.internal.gosu.template.OneArg.renderToString(\"bar2\")"));
  }

  public void testRenderOneArgTemplateToWriter() throws ParseResultsException {
    Assert.assertEquals("One Arg Template: foo", GosuTestUtil.eval("var w = new java.io.StringWriter(); gw.internal.gosu.template.OneArg.render(w, \"foo\"); return w.toString()"));
  }

  public void testRenderOneArgTemplateWithRelativeArgTypeAndUsesStatement() throws ParseResultsException {
    Object val = GosuTestUtil.eval( "return gw.internal.gosu.template.OneArgWithUses.renderToString({\"foo\" -> 1, \"bar\" -> 2})" );
    Assert.assertEquals( "One Arg Template With Uses: 2", val );
  }

  public void testCurlyStyleEvalsWithNewlinesBetween() throws ParseResultsException {
    ITemplateType template = (ITemplateType)TypeSystem.getByFullName("gw.internal.gosu.template.CurliesWithNewlines");
    template.getTemplateGenerator().verify( GosuParserFactory.createParser(""));
  }

  public void testEscapingCurlyStyleExpression() {
    Assert.assertEquals("${foo}", GosuTestUtil.eval("return gw.internal.gosu.template.EscapedCurly.renderToString()"));
  }

  public void testEscapingPercentStyleExpression() {
    Assert.assertEquals("<%=foo%>", GosuTestUtil.eval("return gw.internal.gosu.template.EscapedPercent.renderToString()"));
  }

  public void testEscapingosulet() {
    Assert.assertEquals("<%foo%>", GosuTestUtil.eval("return gw.internal.gosu.template.EscapedScriptlet.renderToString()"));
  }

  public void testMustPassArgToOneArgTemplate() {
    try {
      GosuTestUtil.eval("return gw.internal.gosu.template.OneArg.renderToString()");
      Assert.fail();
    } catch (RuntimeException e) {
      Assert.assertTrue(e.getCause() instanceof ParseResultsException);
      // pass
    }
    try {
      GosuTestUtil.eval("var w = new java.io.StringWriter(); gw.internal.gosu.template.OneArg.render(w); return w.toString()");
      Assert.fail();
    } catch (RuntimeException e) {
      Assert.assertTrue(e.getCause() instanceof ParseResultsException );
      // pass
    }
  }
  
  public void testCantPassTwoArgsToOneArgTemplate() {
    try {
      GosuTestUtil.eval("return gw.internal.gosu.template.OneArg.renderToString(\"foo\", \"bar\")");
      Assert.fail();
    } catch (RuntimeException e) {
      Assert.assertTrue(e.getCause() instanceof ParseResultsException);
      // pass
    }
    try {
      GosuTestUtil.eval("var w = new java.io.StringWriter(); gw.internal.gosu.template.OneArg.render(w, \"foo\", \"bar\"); return w.toString()");
      Assert.fail();
    } catch (RuntimeException e) {
      Assert.assertTrue(e.getCause() instanceof ParseResultsException);
      // pass
    }
  }

  public void testBadTemplateThrowsTemplateParseException() {
    try {
      GosuTestUtil.eval("return gw.internal.gosu.template.Errant_NoArgBad.renderToString()");
      Assert.fail("No exception thrown.");
    } catch (Throwable e) {
      //Assert.assertTrue( e.getMessage().endsWith( "Errant_NoArgBad" ) );
    }
  }
  
  public void testTemplateCanAccessStaticVarOnSuperclass() throws ParseResultsException {
    Assert.assertEquals("static var", GosuTestUtil.eval("return gw.internal.gosu.template.ExtendsVarAccess.renderToString()"));
  }
  
  public void testTemplateCanAccessStaticPropertyOnSuperclass() throws ParseResultsException {
    Assert.assertEquals("static property", GosuTestUtil.eval("return gw.internal.gosu.template.ExtendsPropertyAccess.renderToString()"));
  }
  
  public void testTemplateCanAccessStaticFunctionOnSuperclass() throws ParseResultsException {
    Assert.assertEquals("static function", GosuTestUtil.eval("return gw.internal.gosu.template.ExtendsNoArgFunctionAccess.renderToString()"));
  }
  
  public void testTemplateCanAccessStaticFunctionWithArgumentsOnSuperclass() throws ParseResultsException {
    Assert.assertEquals("static function with arg foo", GosuTestUtil.eval("return gw.internal.gosu.template.ExtendsFunctionWithArgAccess.renderToString()"));
  }
  
  public void testTemplateCanNotAccessPrivateStaticFunctionOnSuperclass() {
    try {
      GosuTestUtil.eval("return gw.internal.gosu.template.Errant_ExtendsPrivateFunctionAccess.renderToString()");
      Assert.fail("No exception thrown.");
    } catch (Throwable e) {
      //Assert.assertTrue( e.getMessage().endsWith( "Errant_ExtendsPrivateFunctionAccess" ) );
    }
  }
  
  public void testTemplateCanNotAccessNonStaticFunctionOnSuperclass() {
    try {
      GosuTestUtil.eval("return gw.internal.gosu.template.Errant_ExtendsNonStaticFunctionAccess.renderToString()");
      Assert.fail("No exception thrown.");
    } catch (Throwable e) {
      Assert.assertNotNull(GosuExceptionUtil.findException( TemplateParseException.class, e));
    }
  }

  public void testTemplateCanPassThroughWriterInRenderToString()
  {
    assertEquals( "foobar",
                  GosuTestUtil.eval( "return gw.internal.gosu.template.PassesThroughWriter.renderToString()" ) );
  }

  public void testTemplateCanPassThroughWriterInRender()
  {
    assertEquals( "foobar",
                  GosuTestUtil.eval( "var sw = new java.io.StringWriter()\n" +
                                     "gw.internal.gosu.template.PassesThroughWriter.render(sw)\n" +
                                     "return sw.toString()" ) );
  }

  public void testTemplateCanHaveWriterParameter()
  {
    assertEquals( "foobar",
                  GosuTestUtil.eval( "return gw.internal.gosu.template.WriterArg.renderToString('foobar')" ) );
  }

  public void testTemplatesAreEnhanceable() throws ParseResultsException
  {
    Assert.assertEquals( "Great Enhancement Justice!", GosuTestUtil.eval( "return gw.internal.gosu.template.EnhancedTemplate.passThroughToRender()" ) );
  }

  public String getExceptionChain( Throwable e ) {
    String res = e.getClass().getSimpleName();
    if( e.getCause() != null && e.getCause() != e )
    {
      res += " -> " + getExceptionChain( e.getCause() );
    }
    return res;
  }
}
