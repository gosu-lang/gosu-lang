/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.testgen;

import gw.spec.testgen.declcontexts.UnrelatedClassUsageContext;
import gw.spec.testgen.declcontexts.UnrelatedEnhancementUsageContext;
import gw.spec.testgen.declcontexts.PureGosuTypeUnrelatedClassUsageContext;
import gw.spec.testgen.declcontexts.JavaBackedGosuTypeUnrelatedClassUsageContext;
import gw.spec.testgen.declcontexts.gosuclass.ClassDeclEnhancementOfClassUsageContext;
import gw.spec.testgen.declcontexts.gosuclass.ClassDeclInnerClassOnSubclassUsageContext;
import gw.spec.testgen.declcontexts.gosuclass.ClassDeclInnerClassUsageContext;
import gw.spec.testgen.declcontexts.gosuclass.ClassDeclSameClassUsageContext;
import gw.spec.testgen.declcontexts.gosuclass.ClassDeclStaticInnerClassOnSubclassUsageContext;
import gw.spec.testgen.declcontexts.gosuclass.ClassDeclStaticInnerClassUsageContext;
import gw.spec.testgen.declcontexts.gosuclass.ClassDeclSubclassEnhancementUsageContext;
import gw.spec.testgen.declcontexts.gosuclass.ClassDeclSubclassUsageContext;
import gw.spec.testgen.declcontexts.gosuclass.ClassDeclViaSubclassSubclassUsageContext;
import gw.spec.testgen.declcontexts.gosuclass.ClassDeclViaSubclassUnrelatedClassUsageContext;
import gw.spec.testgen.declcontexts.gosuclass.ClassDeclarationContext;
import gw.spec.testgen.declcontexts.gosuclass.ClassDeclSubclassInOtherPackageUsageContext;
import gw.spec.testgen.declcontexts.gosuclass.PureGosuTypeDeclarationContext;
import gw.spec.testgen.declcontexts.gosuclass.JavaBackedGosuTypeDeclarationContext;
import gw.spec.testgen.declcontexts.gosuclassstaticinnerclass.StaticInnerClassDeclarationContext;
import gw.spec.testgen.declcontexts.gosuenhancement.EnhancementDeclEnhancedClassUsageContext;
import gw.spec.testgen.declcontexts.gosuenhancement.EnhancementDeclInnerClassUsageContext;
import gw.spec.testgen.declcontexts.gosuenhancement.EnhancementDeclOtherEnhancementUsageContext;
import gw.spec.testgen.declcontexts.gosuenhancement.EnhancementDeclSameEnhancementUsageContext;
import gw.spec.testgen.declcontexts.gosuenhancement.EnhancementDeclStaticInnerClassUsageContext;
import gw.spec.testgen.declcontexts.gosuenhancement.EnhancementDeclSubclassEnhancementUsageContext;
import gw.spec.testgen.declcontexts.gosuenhancement.EnhancementDeclSubclassUsageContext;
import gw.spec.testgen.declcontexts.gosuenhancement.EnhancementOfClassDeclarationContext;
import gw.spec.testgen.declcontexts.gosuinterface.GosuInterfaceDeclUnrelatedClassUsageContext;
import gw.spec.testgen.declcontexts.gosuinterface.GosuInterfaceDeclarationContext;
import gw.spec.testgen.declcontexts.gosuinterface.GosuInterfaceImplementorDeclUnrelatedClassUsageContext;
import gw.spec.testgen.declcontexts.gosuinterface.GosuInterfaceImplementorDeclarationContext;
import gw.spec.testgen.declcontexts.gosusubclass.SubclassDeclarationContext;
import gw.spec.testgen.declcontexts.javaclass.JavaClassDeclSubclassInnerClassUsageContext;
import gw.spec.testgen.declcontexts.javaclass.JavaClassDeclSubclassUsageContext;
import gw.spec.testgen.declcontexts.javaclass.JavaClassDeclUnrelatedClassUsageContext;
import gw.spec.testgen.declcontexts.javaclass.JavaClassDeclarationContext;
import gw.spec.testgen.declcontexts.javaclass.JavaClassDeclSubclassInOtherPackageUsageContext;
import gw.spec.testgen.declcontexts.javaenhancement.EnhancementOfJavaClassDeclSameEnhancementUsageContext;
import gw.spec.testgen.declcontexts.javaenhancement.EnhancementOfJavaClassDeclarationContext;
import gw.spec.testgen.declcontexts.javainterface.JavaInterfaceDeclarationContext;
import gw.spec.testgen.evaluationmethods.EvaluationMethod;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Oct 7, 2009
 * Time: 11:23:29 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class MemberTestGenerator {

  protected CodeGenerationContext initializeTests(TestGenerationContext testGenContext, String packageName) throws IOException {
    CodeGenerationContext codeGenContext = new CodeGenerationContext(packageName);

    // Set up the declaring contexts
    GosuClassFileGenerator unrelatedClassGenerator = codeGenContext.createGosuClassFileGenerator(testGenContext.constants().unrelatedClass());
    unrelatedClassGenerator.addUses(packageName + "." + testGenContext.constants().javaClass());

    GosuClassFileGenerator unrelatedEnhancementGenerator = codeGenContext.createGosuClassFileGenerator(testGenContext.constants().unrelatedEnhancement());
    unrelatedEnhancementGenerator.setEnhancement(true);
    unrelatedEnhancementGenerator.setSuperClass(testGenContext.constants().unrelatedClass());
    unrelatedClassGenerator.addUses(packageName + "." + testGenContext.constants().javaClass());

    GosuClassFileGenerator otherEnhancementGenerator = codeGenContext.createGosuClassFileGenerator(testGenContext.constants().gosuClassOtherEnhancement());
    otherEnhancementGenerator.setEnhancement(true);
    otherEnhancementGenerator.setSuperClass(testGenContext.constants().gosuClass());
    otherEnhancementGenerator.addUses(packageName + "." + testGenContext.constants().javaClass());

    GosuClassFileGenerator gosuSubclassInOtherPackageGenerator = codeGenContext.createGosuClassFileGenerator(testGenContext.constants().gosuClassSubclassInOtherPackage());
    gosuSubclassInOtherPackageGenerator.setSuperClass(testGenContext.constants().gosuClass());
//    gosuSubclassInOtherPackageGenerator.addUses(packageName + "." + testGenContext.constants().gosuClass());
    gosuSubclassInOtherPackageGenerator.addUses(packageName + ".*");
    gosuSubclassInOtherPackageGenerator.setPackage(packageName + ".other");

    GosuClassFileGenerator gosuSubclassEnhancementGenerator = codeGenContext.createGosuClassFileGenerator(testGenContext.constants().gosuClassSubclassEnhancement());
    gosuSubclassEnhancementGenerator.setEnhancement(true);
    gosuSubclassEnhancementGenerator.setSuperClass(testGenContext.constants().gosuClassSubclass());
    gosuSubclassEnhancementGenerator.addUses(packageName + "." + testGenContext.constants().javaClass());

    GosuClassFileGenerator javaSubclassGenerator = codeGenContext.createGosuClassFileGenerator(testGenContext.constants().javaClassSubclass());
    javaSubclassGenerator.setSuperClass(testGenContext.constants().javaClass());
    javaSubclassGenerator.addUses(packageName + "." + testGenContext.constants().javaClass());

    GosuClassFileGenerator javaSubclassInOtherPackageGenerator = codeGenContext.createGosuClassFileGenerator(testGenContext.constants().javaClassSubclassInOtherPackage());
    javaSubclassInOtherPackageGenerator.setSuperClass(testGenContext.constants().javaClass());
//    javaSubclassInOtherPackageGenerator.addUses(packageName + "." + testGenContext.constants().javaClass());
    javaSubclassInOtherPackageGenerator.addUses(packageName + ".*");
    javaSubclassInOtherPackageGenerator.setPackage(packageName + ".other");
                                                                                                            
    ClassDeclarationContext classDeclarationContext = new ClassDeclarationContext(codeGenContext, testGenContext);
    EnhancementOfClassDeclarationContext enhancementOfClassDeclarationContext = new EnhancementOfClassDeclarationContext(codeGenContext, testGenContext);
    SubclassDeclarationContext subclassDeclarationContext = new SubclassDeclarationContext(codeGenContext, testGenContext);
    StaticInnerClassDeclarationContext staticInnerClassDeclarationContext = new StaticInnerClassDeclarationContext(codeGenContext, testGenContext);
    JavaClassDeclarationContext javaClassDeclarationContext = new JavaClassDeclarationContext(codeGenContext, testGenContext);
    GosuInterfaceDeclarationContext gosuInterfaceDeclarationContext = new GosuInterfaceDeclarationContext(codeGenContext, testGenContext);
    GosuInterfaceImplementorDeclarationContext gosuInterfaceImplementorDeclarationContext = new GosuInterfaceImplementorDeclarationContext(codeGenContext, testGenContext);
    JavaInterfaceDeclarationContext javaInterfaceDeclarationContext = new JavaInterfaceDeclarationContext(codeGenContext, testGenContext);
    EnhancementOfJavaClassDeclarationContext enhancementOfJavaClassDeclarationContext = new EnhancementOfJavaClassDeclarationContext(codeGenContext, testGenContext);

    PureGosuTypeDeclarationContext pureGosuTypeCtx = new PureGosuTypeDeclarationContext(codeGenContext, testGenContext);
    JavaBackedGosuTypeDeclarationContext javaBackedGosuTypeCtx = new JavaBackedGosuTypeDeclarationContext(codeGenContext, testGenContext);

    // Now generate tests against them
    regenTest(codeGenContext, classDeclarationContext, new UnrelatedClassUsageContext(testGenContext));
    regenTest(codeGenContext, classDeclarationContext, new ClassDeclSameClassUsageContext(testGenContext));
    regenTest(codeGenContext, classDeclarationContext, new ClassDeclEnhancementOfClassUsageContext(testGenContext));
    regenTest(codeGenContext, classDeclarationContext, new ClassDeclInnerClassUsageContext(testGenContext));
    regenTest(codeGenContext, classDeclarationContext, new ClassDeclStaticInnerClassUsageContext(testGenContext));
    regenTest(codeGenContext, classDeclarationContext, new ClassDeclSubclassUsageContext(testGenContext));
    regenTest(codeGenContext, classDeclarationContext, new ClassDeclViaSubclassUnrelatedClassUsageContext(testGenContext));
    regenTest(codeGenContext, classDeclarationContext, new ClassDeclViaSubclassSubclassUsageContext(testGenContext));
    regenTest(codeGenContext, classDeclarationContext, new ClassDeclStaticInnerClassOnSubclassUsageContext(testGenContext));
    regenTest(codeGenContext, classDeclarationContext, new UnrelatedEnhancementUsageContext(testGenContext));
    regenTest(codeGenContext, classDeclarationContext, new ClassDeclSubclassEnhancementUsageContext(testGenContext));
    regenTest(codeGenContext, classDeclarationContext, new ClassDeclInnerClassOnSubclassUsageContext(testGenContext));
    regenTest(codeGenContext, classDeclarationContext, new ClassDeclSubclassInOtherPackageUsageContext(testGenContext), packageName + ".other." + testGenContext.constants().gosuClassSubclassInOtherPackage());

    regenTest(codeGenContext, enhancementOfClassDeclarationContext, new UnrelatedClassUsageContext(testGenContext));
    regenTest(codeGenContext, enhancementOfClassDeclarationContext, new EnhancementDeclEnhancedClassUsageContext(testGenContext));
    regenTest(codeGenContext, enhancementOfClassDeclarationContext, new EnhancementDeclSameEnhancementUsageContext(testGenContext));
    regenTest(codeGenContext, enhancementOfClassDeclarationContext, new EnhancementDeclInnerClassUsageContext(testGenContext));
    regenTest(codeGenContext, enhancementOfClassDeclarationContext, new EnhancementDeclStaticInnerClassUsageContext(testGenContext));
    regenTest(codeGenContext, enhancementOfClassDeclarationContext, new EnhancementDeclSubclassUsageContext(testGenContext));
    regenTest(codeGenContext, enhancementOfClassDeclarationContext, new ClassDeclViaSubclassUnrelatedClassUsageContext(testGenContext));
    regenTest(codeGenContext, enhancementOfClassDeclarationContext, new UnrelatedEnhancementUsageContext(testGenContext));
    regenTest(codeGenContext, enhancementOfClassDeclarationContext, new EnhancementDeclOtherEnhancementUsageContext(testGenContext));
    regenTest(codeGenContext, enhancementOfClassDeclarationContext, new EnhancementDeclSubclassEnhancementUsageContext(testGenContext));

    regenTest(codeGenContext, staticInnerClassDeclarationContext, new UnrelatedClassUsageContext(testGenContext));

    regenTest(codeGenContext, javaClassDeclarationContext, new JavaClassDeclUnrelatedClassUsageContext(testGenContext));
    regenTest(codeGenContext, javaClassDeclarationContext, new JavaClassDeclSubclassUsageContext(testGenContext));
    regenTest(codeGenContext, javaClassDeclarationContext, new JavaClassDeclSubclassInnerClassUsageContext(testGenContext));
    regenTest(codeGenContext, javaClassDeclarationContext, new JavaClassDeclSubclassInOtherPackageUsageContext(testGenContext), packageName + ".other." + testGenContext.constants().javaClassSubclassInOtherPackage());

    regenTest(codeGenContext, enhancementOfJavaClassDeclarationContext, new UnrelatedClassUsageContext(testGenContext));
    regenTest(codeGenContext, enhancementOfJavaClassDeclarationContext, new UnrelatedEnhancementUsageContext(testGenContext));
    regenTest(codeGenContext, enhancementOfJavaClassDeclarationContext, new EnhancementOfJavaClassDeclSameEnhancementUsageContext(testGenContext));

    regenTest(codeGenContext, gosuInterfaceDeclarationContext, new GosuInterfaceDeclUnrelatedClassUsageContext(testGenContext));
    regenTest(codeGenContext, gosuInterfaceImplementorDeclarationContext, new GosuInterfaceImplementorDeclUnrelatedClassUsageContext(testGenContext));

    regenTest(codeGenContext, javaInterfaceDeclarationContext, new GosuInterfaceDeclUnrelatedClassUsageContext(testGenContext));

    regenTest(codeGenContext, pureGosuTypeCtx, new PureGosuTypeUnrelatedClassUsageContext(testGenContext));

    regenTest(codeGenContext, javaBackedGosuTypeCtx, new JavaBackedGosuTypeUnrelatedClassUsageContext(testGenContext));

    return codeGenContext;
  }

  public void regenTest(CodeGenerationContext codeGenContext, DeclarationContext declarationContext, UsageContext usageContext, String... usesStatements) throws IOException {
    ClassFileGenerator cfg = codeGenContext.createGosuClassFileGenerator(determineClassName(declarationContext, usageContext));
    cfg.setSuperClass(getTestBaseClass());
    for (String usesStatement : usesStatements) {
      cfg.addUses(usesStatement);
    }
    codeGenContext.setCurrentTestGenerator(cfg);

    for (Member member : declarationContext.getMembers()) {
      if (usageContext.isApplicable(member)) {
        createTestUsage(usageContext, codeGenContext, member);
      }
    }
  }

  public void createTestUsage(UsageContext usageContext, CodeGenerationContext codeGenContext, Member member) {
    for (EvaluationMethod method : usageContext.getEvaluationMethods(member)) {
      method.getTestMethodStyle().generateTest(codeGenContext, usageContext, member, method);
    }
  }

  protected abstract String determineClassName(DeclarationContext declarationContext, UsageContext usageContext);

  protected abstract String getTestBaseClass();

}