package gw.plugin.ij.core;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.augment.PsiAugmentProvider;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IFeatureInfoDelegate;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.module.IModule;
//import gw.plugin.ij.lang.psi.impl.resolvers.PsiFeatureResolver;
import gw.plugin.ij.util.GosuModuleUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 */
public class GosuAugmentProvider extends PsiAugmentProvider {
  @Override
  @NotNull
  public <Psi extends PsiElement> List<Psi> getAugments( @NotNull PsiElement element, @NotNull Class<Psi> cls ) {
    if( !(element instanceof PsiClass) || !element.isValid() || !element.isPhysical() ) {
      return Collections.emptyList();
    }

    IModule moduleForPsiElement = GosuModuleUtil.findModuleForPsiElement( element );
    if( moduleForPsiElement == null ) {
      moduleForPsiElement = TypeSystem.getExecutionEnvironment( PluginLoaderUtil.getFrom( element.getProject() ) ).getGlobalModule();
    }
    if( moduleForPsiElement == null ) {
      return Collections.emptyList();
    }

    TypeSystem.pushModule( moduleForPsiElement );
    try {
      List<PsiElement> augFeatures = new ArrayList<PsiElement>();
      PsiClass psiClass = (PsiClass)element;
      String className = psiClass.getQualifiedName();
      if( className == null ) {
        return Collections.emptyList();
      }
      className = className.replace( '$', '.' );
      if( className.contains( "String" ) ) {
        System.out.println( "delete me" );
      }
      IType type = TypeSystem.getByFullNameIfValid( className );
      if( type == null ) {
        return Collections.emptyList();
      }
      if( PsiMethod.class.isAssignableFrom( cls ) ) {
        addMethods( element, augFeatures, type );
      }
      else if( PsiField.class.isAssignableFrom( cls ) ) {
        addFieldProperties( element, augFeatures, type );
      }
      return (List<Psi>)augFeatures;
    }
    finally {
      TypeSystem.popModule( moduleForPsiElement );
    }
  }

  private void addMethods( PsiElement element, List<PsiElement> augFeatures, IType type ) {
//    List<? extends IMethodInfo> methods = ((IRelativeTypeInfo)type.getTypeInfo()).getMethods( type );
//    for( IMethodInfo mi : methods ) {
//      if( getEnhancementFeature( mi ) != null ) {
//        PsiElement elem = PsiFeatureResolver.resolveMethodOrConstructor( mi, element );
//        if( elem instanceof PsiMethod ) {
//          augFeatures.add( elem );
//        }
//      }
//    }
//    List<? extends IPropertyInfo> properties = ((IRelativeTypeInfo)type.getTypeInfo()).getProperties( type );
//    for( IPropertyInfo pi : properties ) {
//      if( getEnhancementFeature( pi ) != null ) {
//        PsiElement elem = PsiFeatureResolver.resolveProperty( pi, element );
//        if( elem instanceof PsiMethod ) {
//          augFeatures.add( elem );
//        }
//      }
//    }
  }

  private void addFieldProperties( PsiElement element, List<PsiElement> augFeatures, IType type ) {
//    List<? extends IPropertyInfo> properties = ((IRelativeTypeInfo)type.getTypeInfo()).getProperties( type );
//    for( IPropertyInfo pi : properties ) {
//      if( getEnhancementFeature( pi ) != null ) {
//        PsiElement elem = PsiFeatureResolver.resolveProperty( pi, element );
//        if( elem instanceof PsiField ) {
//          augFeatures.add( elem );
//        }
//      }
//    }
  }

  private IFeatureInfo getEnhancementFeature( IFeatureInfo fi ) {
    IFeatureInfo delegateFi = null;
    if( isEnhancementType( fi.getContainer().getOwnersType() ) ) {
      return fi;
    }
    if( fi instanceof IFeatureInfoDelegate ) {
      delegateFi = ((IFeatureInfoDelegate)fi).getSource();
      if( !isEnhancementType( delegateFi.getOwnersType() ) ) {
        delegateFi = null;
      }
    }
    return delegateFi;
  }

  private boolean isEnhancementType( IType type ) {
    return type instanceof IGosuEnhancement;
  }
}
