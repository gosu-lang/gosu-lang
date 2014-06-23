/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.intellij.codeInsight.AutoPopupController;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.completion.JavaCompletionUtil;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.PsiFile;
import com.intellij.ui.LayeredIcon;
import com.intellij.ui.RowIcon;
import gw.internal.gosu.parser.IGosuTemplateInternal;
import gw.internal.gosu.parser.TypeLord;
import gw.lang.reflect.IBlockType;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.gs.IGosuVarPropertyInfo;
import gw.lang.reflect.java.IJavaFieldPropertyInfo;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.icons.GosuIcons;
import gw.plugin.ij.lang.psi.custom.CustomGosuClass;
import gw.plugin.ij.lang.psi.impl.CustomPsiClassCache;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.Iterables.transform;

public class GosuFeatureCallLookupElement extends LookupElement {
  private final IFeatureInfo _featureInfo;

  public GosuFeatureCallLookupElement(IFeatureInfo featureInfo) {
    _featureInfo = featureInfo;
  }

  @NotNull
  @Override
  public String getLookupString() {
    return _featureInfo instanceof IMethodInfo ? _featureInfo.getDisplayName() : _featureInfo.getName();
  }

  @NotNull
  private RowIcon getIcon() {
    Icon icon = null;
    IFeatureInfo fi = _featureInfo;
    IModule mod = TypeSystem.getCurrentModule();
    IType ownersType;
    if( mod == null ) {
      TypeSystem.pushGlobalModule();
    }
    try {
      ownersType = fi.getOwnersType();
      if( fi instanceof IPropertyInfo ) {
        if( fi instanceof IJavaFieldPropertyInfo || fi instanceof IGosuVarPropertyInfo ) {
          icon = GosuIcons.FIELD;
        }
        else {
          icon = GosuIcons.PROPERTY;
        }
      }
      else if( fi instanceof ITypeInfo ) {
  //## Warning: following call to getPsiClass() is SSSSSSSSSSSSSSLLLLLLLLLLLLLLLOOOOOOOOOOOOWWWWWWWWWWWWWWWW
        if( !(ownersType instanceof IGosuClass || ownersType instanceof IJavaType || ownersType instanceof IBlockType) ) {
          CustomGosuClass psiClass = CustomPsiClassCache.instance().getPsiClass( ownersType );
          if( psiClass != null ) {
            icon = psiClass.getIcon( 0 );
          }
        }
        if( icon == null ) {
          icon = ownersType.isInterface()
                 ? GosuIcons.INTERFACE
                 : ownersType instanceof IGosuTemplateInternal
                   ? GosuIcons.TEMPLATE
                   : ownersType instanceof IGosuProgram
                     ? GosuIcons.PROGRAM
                     : ownersType instanceof IGosuEnhancement
                       ? GosuIcons.ENHANCEMENT
                       : GosuIcons.CLASS;
        }
      }
      else {
        icon = GosuIcons.METHOD;
      }
      RowIcon rowIcon = new RowIcon(2);
      LayeredIcon icon2 = new LayeredIcon(2);
      icon2.setIcon(icon, 0);
      if ( ownersType instanceof IGosuEnhancement) {
        icon2.setIcon(GosuIcons.ENH, 1);
      }
      rowIcon.setIcon(icon2, 0);
      return rowIcon;
    }
    finally {
      if( mod == null ) {
        TypeSystem.popGlobalModule();
      }
    }
  }

  @Override
  public void renderElement(@NotNull LookupElementPresentation presentation) {
    String typeText = null;
    if( _featureInfo instanceof IMethodInfo ) {
      typeText = ((IMethodInfo)_featureInfo).getReturnType().getRelativeName();
    }
    else if( _featureInfo instanceof IPropertyInfo ) {
      typeText = ((IPropertyInfo)_featureInfo).getFeatureType().getRelativeName();
    }
    else if( _featureInfo instanceof IConstructorInfo ) {
      typeText = _featureInfo.getOwnersType().getRelativeName();
    }
    else if( _featureInfo instanceof ITypeInfo ) {
      typeText = getFqn( (ITypeInfo)_featureInfo );
    }
    presentation.setTypeText(typeText);
    presentation.setItemText(getDisplayString());
    presentation.setIcon(getIcon());
  }

  private String getFqn( ITypeInfo ti ) {
    IModule mod = TypeSystem.getCurrentModule();
    if( mod == null ) {
      TypeSystem.pushGlobalModule();
    }
    try {
      return TypeLord.getPureGenericType( ti.getOwnersType() ).getName();
    }
    finally {
      if( mod == null ) {
        TypeSystem.popGlobalModule();
      }
    }
  }

  public String getDisplayString() {
    if (_featureInfo instanceof IMethodInfo) {
      final List<IParameterInfo> params = Arrays.asList(((IMethodInfo) _featureInfo).getParameters());
      final String strParams = Joiner.on(", ").join(transform(params, new Function<IParameterInfo, String>() {
        @NotNull
        @Override
        public String apply(@NotNull IParameterInfo info) {
          return info.getDisplayName() + " " + info.getFeatureType().getRelativeName();
        }
      }));
      return String.format("%s(%s)", _featureInfo.getDisplayName(), strParams);
    } else {
      return _featureInfo.getDisplayName();
    }
  }

  @Override
  public void handleInsert(@NotNull InsertionContext context) {
    if (_featureInfo instanceof IMethodInfo) {
      final Document document = context.getDocument();
      final PsiFile file = context.getFile();

      //final LookupElement[] allItems = context.getElements();
      final boolean overloadsMatter = true; //allItems.length == 1 && getUserData( FORCE_SHOW_SIGNATURE_ATTR ) == null;
      final boolean hasParams = ((IMethodInfo) _featureInfo).getParameters().length > 0;
      JavaCompletionUtil.insertParentheses(context, this, overloadsMatter, hasParams);
      AutoPopupController.getInstance(file.getProject()).autoPopupParameterInfo(context.getEditor(), null);

/*
    final int startOffset = context.getStartOffset();
    final OffsetKey refStart = context.trackOffset(startOffset, true);
    if (shouldInsertTypeParameters(context)) {
      qualifyMethodCall(file, startOffset, document);
      insertExplicitTypeParameters(context, refStart);
    }
    else if (myCanImportStatic || getAttribute(FORCE_QUALIFY) != null) {
      context.commitDocument();
      if (myCanImportStatic && myShouldImportStatic) {
        final PsiReferenceExpression ref = PsiTreeUtil.findElementOfClassAtOffset( file, startOffset, PsiReferenceExpression.class, false );
        if (ref != null) {
          ref.bindToElementViaStaticImport(myContainingClass);
        }
        return;
      }

      qualifyMethodCall(file, startOffset, document);
    }

    final PsiType type = method.getReturnType();
    if (context.getCompletionChar() == '!' && type != null && PsiType.BOOLEAN.isAssignableFrom(type)) {
      context.commitDocument();
      final int offset = context.getOffset(refStart);
      final PsiMethodCallExpression methodCall = PsiTreeUtil.findElementOfClassAtOffset(file, offset, PsiMethodCallExpression.class, false);
      if (methodCall != null) {
        FeatureUsageTracker.getInstance().triggerFeatureUsed( CodeCompletionFeatures.EXCLAMATION_FINISH);
        document.insertString(methodCall.getTextRange().getStartOffset(), "!");
      }
    }
*/
    } else {
      super.handleInsert(context);
    }
  }
}
