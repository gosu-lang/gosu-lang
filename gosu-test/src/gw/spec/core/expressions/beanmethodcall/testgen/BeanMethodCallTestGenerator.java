/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.core.expressions.beanmethodcall.testgen;

import gw.spec.testgen.CodeGenerationContext;
import gw.spec.testgen.DeclarationContext;
import gw.spec.testgen.MemberTestGenerator;
import gw.spec.testgen.UsageContext;

import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Oct 7, 2009
 * Time: 11:23:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class BeanMethodCallTestGenerator extends MemberTestGenerator {

  public static void main(String[] args) throws IOException {
    CodeGenerationContext codeGenContext = new BeanMethodCallTestGenerator().initializeTests(BeanMethodCallTestGenerationContext.INSTANCE, "gw.spec.core.expressions.beanmethodcall.generated");

    File rootDir = new File("C:\\eng\\diamond\\pl2\\active\\core\\platform\\gosu-test\\src\\");
    codeGenContext.writeAllClassFiles(rootDir);
  }

  protected String determineClassName(DeclarationContext declarationContext, UsageContext usageContext) {
    return "BeanMethodCall_" + declarationContext.testClassPrefix() + "From" + usageContext.testClassSuffix() + "Test";
  }

  @Override
  protected String getTestBaseClass() {
    return "BeanMethodCallTestBase";
  }

}