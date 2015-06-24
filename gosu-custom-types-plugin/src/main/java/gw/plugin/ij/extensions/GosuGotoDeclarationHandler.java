package gw.plugin.ij.extensions;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandlerBase;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiModifierListOwner;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.PsiMethodImpl;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
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
    int index = Integer.parseInt( psiAnnotation.getParameterList().getAttributes()[0].getValue().getText() );
    PsiFile sourceFile = facade.getRawFile();
    IModule module = GosuModuleUtil.findModuleForPsiElement( sourceFile );
    TypeSystem.pushModule( module );
    try
    {
      IType type = TypeSystem.getByFullNameIfValidNoJava( facade.getQualifiedName() );
      IFeatureInfo fi = null;
      ITypeInfo ti = type.getTypeInfo();
      if( psiAnnotation.getQualifiedName().equals( "PropertyGetInfoId" ) )
      {
        fi = ti instanceof IRelativeTypeInfo
             ? ((IRelativeTypeInfo)ti).getProperties( type ).get( index )
             : ti.getProperties().get( index );
      }
      else if( psiAnnotation.getQualifiedName().equals( "MethodInfoId" ) )
      {
        fi = ti instanceof IRelativeTypeInfo
             ? ((IRelativeTypeInfo)ti).getMethods( type ).get( index )
             : ti.getMethods().get( index );
      }
      else if( psiAnnotation.getQualifiedName().equals( "ConstructorInfoId" ) )
      {
        fi = ti instanceof IRelativeTypeInfo
             ? ((IRelativeTypeInfo)ti).getConstructors( type ).get( index )
             : ti.getConstructors().get( index );
      }
      else if( psiAnnotation.getQualifiedName().equals( "InnerClassInfoId" ) )
      {
        //## todo: somehow provide position/location info
        return null;
      }
      else
      {
        throw new IllegalStateException( "Unexpected annotation: " + psiAnnotation.getQualifiedName() );
      }
      int iOffset = fi.getOffset();
      if( iOffset >= 0 )
      {
        //PsiElement target = sourceFile.findElementAt( iOffset );

        int iLength = fi.getTextLength();
        return new FakeTargetElement( sourceFile, iOffset, iLength >= 0 ? iLength : 1, fi );
      }
    }
    finally
    {
      TypeSystem.popModule( module );
    }
    return facade;
  }

}
