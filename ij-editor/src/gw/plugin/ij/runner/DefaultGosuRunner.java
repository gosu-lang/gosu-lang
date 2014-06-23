/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.runner;

import com.google.common.base.Strings;
import com.intellij.execution.CantRunException;
import com.intellij.execution.configurations.JavaParameters;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.PsiElementProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import gw.internal.gosu.parser.MetaType;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.expressions.IEvalExpression;
import gw.lang.parser.statements.IEvalStatement;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.lang.psi.api.expressions.IGosuExpression;
import gw.plugin.ij.lang.psi.impl.GosuScratchpadFileImpl;
import gw.plugin.ij.util.GosuBundle;
import gw.plugin.ij.util.GosuModuleUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DefaultGosuRunner extends GosuProgramRunner {
  private JavaParameters _params;

  @Override
  public boolean isValidModule( @NotNull Module module ) {
    return true; // revisit: LibrariesUtil.hasGosuSdk( module );
  }

  @Override
  public boolean ensureRunnerConfigured( @Nullable Module module, String confName, final Project project ) {
    if(confName.equals("Gosu Scratchpad.gsp") && hasCustomTypeInScratchpad( project )) {
      Messages.showMessageDialog(project, GosuBundle.message("scratchpad.debug.process"),
                                 GosuBundle.message("scratchpad.name"), Messages.getErrorIcon());
      return false;
    }
    return true;
  }


  private static boolean hasCustomTypeInScratchpad( Project project ) {
    PsiElement psiElement = GosuScratchpadFileImpl.instance(project).getFirstChild();
    PsiElementProcessor.CollectElements processor = new PsiElementProcessor.CollectElements();
    PsiTreeUtil.processElements(psiElement, processor);
    boolean ret = false;
    for(PsiElement elem : processor.toArray()) {
      if( hasCustomType( elem ) ) {
        ret = true;
        break;
      }
    }
    return ret;
  }

  private static boolean hasCustomType( PsiElement element ) {
    if( element instanceof IGosuExpression ) {
      IParsedElement parsedElement = ((IGosuExpression)element).getParsedElement();
      if( parsedElement == null ) {
        return false;
      }
      IType type = parsedElement.getReturnType();
      if(type instanceof MetaType) {
        type = ((MetaType) type).getType();
      }
      if(type.isArray()) {
        type = type.getComponentType();
      }
      String packageName = type.getClass().getPackage().getName();
      if( !packageName.startsWith( "gw.internal.gosu" ) &&
          !packageName.startsWith( "gw.lang" ))
      {
        return true;
      }
    }
    return false;
  }

  public void configureCommandLine( Sdk gosuSdk, @NotNull JavaParameters params, @Nullable Module module, boolean tests, VirtualFile programFile, @NotNull GosuProgramRunConfiguration configuration ) throws CantRunException {
    _params = params;

    setToolsJar( params );

    params.getVMParametersList().addParametersString( configuration._vmParams );

    final String fqn = configuration.getFqn();
    if( canExecuteFromBytecode( module, fqn ) ) {
      params.setMainClass( fqn );
    }
    else {
      params.setMainClass( "gw.lang.Gosu" );
    }

    IModule gosuModule = module == null ? GosuModuleUtil.getGlobalModule( configuration.getProject() ) : GosuModuleUtil.getModule( module );
    GosuClasspathBuilder classpathBuilder = new GosuClasspathBuilder( gosuSdk, gosuModule, params );
    classpathBuilder.fillClasspath();

    if( !params.getClassPath().getPathsString().contains( "gosu-core-api" ) ) {
      addClasspathFromRootModel( module, tests, params );
    }

    addProgramClasspath( classpathBuilder );

    params.getProgramParametersList().add( FileUtil.toSystemDependentName( configuration._strProgramPath ) );
    params.getProgramParametersList().addParametersString( configuration._strProgramParams );
  }

  private boolean canExecuteFromBytecode( Module module, String fqn ) {
    if( Strings.isNullOrEmpty( fqn ) ) {
      return false;
    }

    IType type = TypeSystem.getByFullNameIfValid( fqn, GosuModuleUtil.getModule( module ) );
    if( type instanceof IGosuProgram ) {
      IGosuProgram gsClass = (IGosuProgram)type;
      if( gsClass.getClassStatement().getContainedParsedElementsByType( IEvalExpression.class, null ) ||
          gsClass.getClassStatement().getContainedParsedElementsByType( IEvalStatement.class, null ) ) {
        // A program with an eval statement can't be precompiled, therefore it can't be executed directly from bytecode on disk
        return false;
      }
    }
    return true;
  }

  private void addProgramClasspath( @NotNull GosuClasspathBuilder classpathBuilder ) {
    _params.getProgramParametersList().add( "-classpath" );
    _params.getProgramParametersList().add( classpathBuilder.getProgramClasspath() );
  }
}
