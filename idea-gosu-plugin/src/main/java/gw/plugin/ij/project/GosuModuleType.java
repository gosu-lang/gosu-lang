/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.project;

import com.intellij.CommonBundle;
import com.intellij.ide.util.projectWizard.JavaModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.ProjectJdkForModuleStep;
import com.intellij.ide.util.projectWizard.ProjectWizardStepFactory;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.module.ModuleTypeManager;
import com.intellij.openapi.projectRoots.JavaSdk;
import com.intellij.openapi.projectRoots.JavaSdkVersion;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.impl.ProjectJdkImpl;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.ui.Messages;
import com.intellij.pom.java.LanguageLevel;
import gw.plugin.ij.icons.GosuIcons;
import gw.plugin.ij.sdk.GosuSdkType;
import gw.plugin.ij.sdk.GosuSdkUtils;
import gw.plugin.ij.util.GosuBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class GosuModuleType extends JavaModuleType {
  public static final String MODULE_TYPE_ID = "GOSU_MODULE";

  public GosuModuleType() {
    super( MODULE_TYPE_ID );
  }

  @NotNull
  public static GosuModuleType getInstance() {
    return (GosuModuleType)ModuleTypeManager.getInstance().findByID( MODULE_TYPE_ID );
  }

  @NotNull
  public GosuModuleBuilder createModuleBuilder() {
    return new GosuModuleBuilder();
  }

  @Override
  public ModuleWizardStep[] createWizardSteps( @NotNull final WizardContext wizardContext,
                                               final JavaModuleBuilder moduleBuilder,
                                               ModulesProvider modulesProvider ) {
    List<ModuleWizardStep> steps = new ArrayList<>();
    ProjectWizardStepFactory factory = ProjectWizardStepFactory.getInstance();
    steps.add( factory.createSourcePathsStep( wizardContext, moduleBuilder, null, "reference.dialogs.new.project.fromScratch.source" ) );

    if( GosuSdkUtils.getDefaultGosuSdk() == null ) {
      // There is no Gosu SDK yet.
      // Add a step for the user to select a JDK from which we'll create the Default Gosu SDK and automatically set the project's SDK to it
      steps.add(
        new ProjectJdkForModuleStep( wizardContext, JavaSdk.getInstance() )
        {
          public void updateDataModel() {
            Sdk sdk = getJdk();
            if( sdk != null ) {
              Sdk gosuSdk = GosuSdkUtils.createDefaultGosuSDK( sdk );
              wizardContext.setProjectJdk( gosuSdk );
            }
          }
        } );
    }
    else {
      // There is at least a default Gosu SDK, provide a step for the user to select the Gosu SDK for the project
      steps.add( new GosuSDKForModuleStep( wizardContext ) );
    }

    //steps.add(new GosuModuleWizardStep(moduleBuilder, wizardContext));
    return steps.toArray( new ModuleWizardStep[steps.size()] );
  }

  public String getName() {
    return GosuBundle.message( "gosu.module.type.name" );
  }

  public String getDescription() {
    return GosuBundle.message( "gosu.module.type.description" );
  }

  @Nullable
  public Icon getBigIcon() {
    return GosuIcons.G_24;
  }

  @Nullable
  public Icon getNodeIcon( boolean isOpened ) {
    return GosuIcons.G_16;
  }

  private class GosuSDKForModuleStep extends ProjectJdkForModuleStep {
    GosuSDKForModuleStep( WizardContext wizardContext ) {
      super( wizardContext, GosuSdkType.getInstance() );
    }

    @Override
    public void updateDataModel() {

    }

    @Override
    public boolean validate() {
      final Sdk sdk = getJdk();
      if( sdk != null ) {
        if( sdk.getSdkType() instanceof GosuSdkType ) {
          JavaSdkVersion version = JavaSdk.getInstance().getVersion( sdk );
          //noinspection ConstantConditions
          return version.getMaxLanguageLevel().isAtLeast( LanguageLevel.JDK_1_5 );
        }
      }
      Messages.showErrorDialog( GosuBundle.message( "error.no.gosu.sdk" ), CommonBundle.getErrorTitle() );
      return false;
    }
  }
}
