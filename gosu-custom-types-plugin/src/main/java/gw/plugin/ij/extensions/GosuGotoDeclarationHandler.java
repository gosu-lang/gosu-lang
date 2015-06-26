package gw.plugin.ij.extensions;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandlerBase;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiModifierListOwner;
import com.intellij.psi.PsiReference;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.custom.JavaFacadePsiClass;
import gw.plugin.ij.util.GosuModuleUtil;
import org.jetbrains.annotations.Nullable;

/**
 */
public class GosuGotoDeclarationHandler extends GotoDeclarationHandlerBase
{
  @Nullable
  @Override
  public PsiElement getGotoDeclarationTarget( @Nullable PsiElement sourceElement, Editor editor )
  {
    PsiElement parent = sourceElement.getParent();
    if( parent != null )
    {
      PsiReference ref = parent.getReference();
      if( ref != null )
      {
        PsiElement resolve = ref.resolve();
        if( resolve != null )
        {
          PsiFile file = resolve.getContainingFile();
          if( file != null )
          {
            JavaFacadePsiClass facade = file.getUserData( JavaFacadePsiClass.KEY_JAVAFACADE );
            if( facade != null )
            {
              PsiAnnotation[] annotations = ((PsiModifierListOwner)resolve).getModifierList().getAnnotations();
              if( annotations != null && annotations.length > 0 )
              {
                return findTargetFeature( annotations[0], facade );
              }
            }
          }
        }
      }
    }
    return null;
  }

  private PsiElement findTargetFeature( PsiAnnotation psiAnnotation, JavaFacadePsiClass facade )
  {
    // int index = Integer.parseInt( psiAnnotation.getParameterList().getAttributes()[0].getValue().getText() );
    String name = psiAnnotation.getParameterList().getAttributes()[1].getValue().getText();
    int iOffset = Integer.parseInt( psiAnnotation.getParameterList().getAttributes()[2].getValue().getText() );
    int iLength = Integer.parseInt( psiAnnotation.getParameterList().getAttributes()[3].getValue().getText() );

    PsiFile sourceFile = facade.getRawFile();
    IModule module = GosuModuleUtil.findModuleForPsiElement( sourceFile );
    TypeSystem.pushModule( module );
    try
    {
      if( iOffset >= 0 )
      {
        //PsiElement target = sourceFile.findElementAt( iOffset );
        return new FakeTargetElement( sourceFile, iOffset, iLength >= 0 ? iLength : 1, name );
      }
    }
    finally
    {
      TypeSystem.popModule( module );
    }
    return facade;
  }

}
