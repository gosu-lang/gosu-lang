/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.actions;

import com.intellij.ide.actions.CreateTemplateInPackageAction;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidatorEx;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiPackage;
import com.intellij.util.IncorrectOperationException;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.icons.GosuIcons;
import gw.plugin.ij.sdk.GosuSdkUtils;
import gw.plugin.ij.util.GosuBundle;
import gw.plugin.ij.util.GosuModuleUtil;
import org.jetbrains.annotations.NotNull;

public class CreateClassAction extends CreateTemplateInPackageAction<PsiFile> {
  private static final Logger LOG = Logger.getInstance( CreateClassAction.class );

  public CreateClassAction() {
    super( GosuBundle.message( "new.class.menu.action.text" ), GosuBundle.message( "new.class.menu.action.description" ), GosuIcons.CLASS, true );
  }

  protected void buildDialog( final Project project, final PsiDirectory directory, @NotNull com.intellij.ide.actions.CreateFileFromTemplateDialog.Builder builder ) {
    builder.setTitle( GosuBundle.message( "new.class.dlg.title" ) );
    builder.addKind(GosuBundle.message("type.Class"), GosuIcons.CLASS, GosuTemplatesFactory.GOSU_CLASS_TEMPLATE);
    builder.addKind( GosuBundle.message("type.Interface"), GosuIcons.INTERFACE, GosuTemplatesFactory.GOSU_INTERFACE_TEMPLATE );
    builder.addKind( GosuBundle.message("type.Enum"), GosuIcons.ENUM, GosuTemplatesFactory.GOSU_ENUM_TEMPLATE );
    builder.addKind( GosuBundle.message("type.Annotation"), GosuIcons.ANNOTATION, GosuTemplatesFactory.GOSU_ANNOTATION_TEMPLATE );

    builder.setValidator(new InputValidatorEx() {
      @Override
      public String getErrorText(String inputString) {
        if (inputString.length() > 0 && !JavaPsiFacade.getInstance(project).getNameHelper().isQualifiedName(inputString)) {
          return GosuBundle.message("new.class.dlg.InvalidGosuType");
        }
        return null;
      }

      @Override
      public boolean checkInput(String inputString) {
        return true;
      }

      @Override
      public boolean canClose(String inputString) {
        return !StringUtil.isEmptyOrSpaces(inputString) && getErrorText(inputString) == null;
      }
    });
  }

  @Override
  protected String getErrorTitle() {
    return GosuBundle.message( "error.new.class.dlg.title" );
  }

  @Override
  protected String getActionName( @NotNull PsiDirectory directory, String newName, String templateName ) {
    if( !checkPackageExists( directory ) ) {
      return GosuBundle.message( "error.new.artifact.nopackage" );
    }
    else {
      PsiPackage pkg = JavaDirectoryService.getInstance().getPackage( directory );
      return GosuBundle.message( "new.class.progress.text", pkg.getQualifiedName(), newName );
    }
  }

  protected final PsiFile doCreate( PsiDirectory dir, String className, String templateName ) throws IncorrectOperationException {
    AbstractCreateInPackageAction.checkDoesNotExistYet( dir, className );

    try {
      String fileName = className + GosuClassTypeLoader.GOSU_CLASS_FILE_EXT;
      return GosuTemplatesFactory.createFromTemplate( dir, className, fileName, templateName );
    }
    catch( IncorrectOperationException e ) {
      throw e;
    }
    catch( Exception e ) {
      LOG.error( e );
      return null;
    }
  }

  @Override
  protected boolean isAvailable( @NotNull DataContext dataContext ) {
    if( ActionUtil.isInConfigFolder( dataContext ) ) {
      return false;
    }

    boolean bAvailable = super.isAvailable( dataContext );
    if( !bAvailable ) {
      return false;
    }

    Project project = PlatformDataKeys.PROJECT.getData( dataContext );
    return GosuSdkUtils.isGosuSdkSet( project ) ||
           GosuSdkUtils.isGosuApiModuleInProject( project );
  }

  //  @Override
//  protected void doCheckCreate(PsiDirectory dir, String className, String templateName) throws IncorrectOperationException {
//
//    String fileName = className + "." + templateName.split("\\.")[1];
//    try {
//      JavaDirectoryService.getInstance().checkCreateClass(dir, className);
//    } catch (IncorrectOperationException e) {
//      if (e.getMessage().contains("File already exists")) {
//        throw new IncorrectOperationException(GosuBundle.message("error.new.artifact.java.conflict", fileName));
//      }
//      throw (IncorrectOperationException) e;
//    }
//    dir.checkCreateFile(fileName);
//
//    IModule module = getModule(dir);
//    if (!checkPackageExists(dir)) {
//      throw new IncorrectOperationException(GosuBundle.message("error.new.artifact.nopackage"));
//    }
//    String fullyQualifiedClassName = JavaDirectoryService.getInstance().getPackage(dir).getQualifiedName() + "." + className;
//    if (TypeSystem.getByFullNameIfValid(fullyQualifiedClassName, module) != null) {
//      throw new IncorrectOperationException(GosuBundle.message("error.new.artifact.gosu.conflict", fileName));
//    }
//  }

  @Override
  protected PsiElement getNavigationElement( @NotNull PsiFile createdElement ) {
    return createdElement;
  }

  @Override
  protected boolean checkPackageExists( @NotNull PsiDirectory dir ) {
    Boolean gosuActionsAvailable = dir.getUserData( GosuSdkUtils.NEW_GOSU_ACTIONS_AVAILABLE_KEY );
    if( gosuActionsAvailable != null && !gosuActionsAvailable.booleanValue() ) {
      return false;
    }

    PsiPackage pkg = JavaDirectoryService.getInstance().getPackage( dir );
    return pkg != null;
  }

  protected IModule getModule( @NotNull PsiDirectory dir ) {
    return GosuModuleUtil.findModuleForFile( dir.getVirtualFile(), dir.getProject() );
  }
}
