/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang;

import gw.config.IService;
import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.lang.init.GosuPathEntry;
import gw.lang.ir.IRClassCompiler;
import gw.lang.ir.IRTypeResolver;
import gw.lang.javadoc.IJavaDocFactory;
import gw.lang.parser.IConstructorInfoFactory;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IExpression;
import gw.lang.parser.IFullParserState;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.IParserPart;
import gw.lang.parser.IReducedDynamicFunctionSymbol;
import gw.lang.parser.IScope;
import gw.lang.parser.ISourceCodeTokenizer;
import gw.lang.parser.IStackProvider;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ITokenizerInstructor;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.expressions.IIdentifierExpression;
import gw.lang.parser.expressions.INullExpression;
import gw.lang.parser.template.ITemplateHost;
import gw.lang.parser.template.ITemplateObserver;
import gw.lang.parser.template.TemplateParseException;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IAnnotationInfoFactory;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IEntityAccess;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyAccessor;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.ITypeInfoFactory;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IEnhancementIndex;
import gw.lang.reflect.gs.IFileSystemGosuClassRepository;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.gs.ITemplateType;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.module.IClassPath;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.lang.reflect.module.IModule;
import gw.util.GosuExceptionUtil;
import gw.util.IFeatureFilter;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.List;

public interface IGosuShop extends IService
{
  ISymbolTable createSymbolTable();
  ISymbolTable createSymbolTable( boolean bDefineCommonSymbols );

  ITemplateHost createTemplateHost();

  IConstructorInfoFactory getConstructorInfoFactory();
  IAnnotationInfoFactory getAnnotationInfoFactory();
  IJavaDocFactory getJavaDocFactory();

  IPropertyInfo createLengthProperty(ITypeInfo typeInfo);

  IFunctionType createFunctionType( IMethodInfo mi );

  ISymbol createSymbol( CharSequence name, IType type, Object value );
  ISymbol createSymbol( CharSequence name, IType type, IStackProvider stackProvider );

  ITypeInfoFactory getTypeInfoFactory();

  IEntityAccess getDefaultEntityAccess();

  ITemplateHost createSimpleTemplateHost();

  ISourceCodeTokenizer createSourceCodeTokenizer( CharSequence code );
  ISourceCodeTokenizer createSourceCodeTokenizer( CharSequence code, boolean bTemplate );  
  ISourceCodeTokenizer createSourceCodeTokenizer( Reader reader );

  IScope createCommnoSymbolScope();

  IIdentifierExpression createIdentifierExpression();

  void generateTemplate( Reader readerTemplate, Writer writerOut, ISymbolTable symbolTable ) throws TemplateParseException;

  ITokenizerInstructor createTemplateTokenizerInstructor( ISourceCodeTokenizer tokenizer );

  ISymbolTable getGosuClassSymbolTable();

  ISymbol createDynamicFunctionSymbol( ISymbolTable symbolTable, String strMemberName, IFunctionType functionType, List<ISymbol> params, IExpression value );

  IEnhancementIndex createEnhancementIndex( GosuClassTypeLoader loader );

  IGosuClass createClass( String strNamespace, String strRelativeName, GosuClassTypeLoader loader, ISourceFileHandle sourceFile, ITypeUsesMap typeUsesMap );
  IGosuProgram createProgram( String strNamespace, String strRelativeName, GosuClassTypeLoader loader, ISourceFileHandle sourceFile, ITypeUsesMap typeUsesMap, ISymbolTable symTable );
  IGosuProgram createProgramForEval( String strNamespace, String strRelativeName, GosuClassTypeLoader loader, ISourceFileHandle sourceFile, ITypeUsesMap typeUsesMap, ISymbolTable symTable );
  IGosuEnhancement createEnhancement( String strNamespace, String strRelativeName, GosuClassTypeLoader loader, ISourceFileHandle sourceFile, ITypeUsesMap typeUsesMap );
  ITemplateType createTemplate( String strNamespace, String strRelativeName, GosuClassTypeLoader loader, ISourceFileHandle sourceFile, ITypeUsesMap typeUsesMap, ISymbolTable symTable );

  IFileSystemGosuClassRepository createFileSystemGosuClassRepository(IModule module, IDirectory[] files);
  IFileSystemGosuClassRepository createFileSystemGosuClassRepository(IModule module, IDirectory[] files, String[] extensions);

  ITypeUsesMap createTypeUsesMap( List<String> specialTypeUses );

  IPropertyInfo getPropertyInfo( IType classBean, String strProperty, IFeatureFilter filter, IParserPart parserBase, IScriptabilityModifier scriptabilityConstraint) throws ParseException;
  List<? extends IPropertyInfo> getProperties( ITypeInfo beanInfo, IType classSource );
  boolean isDescriptorHidden( IAttributedFeatureInfo pi );
  List<? extends IMethodInfo> getMethods( ITypeInfo beanInfo, IType ownersIntrinsicType );

  IFullParserState createStandardParserState( IParsedElement rootParsedElement, String scriptSrc, boolean b );

  RuntimeException createEvaluationException(String msg);

  IPropertyInfo createPropertyDelegate(IFeatureInfo container, IPropertyInfo prop);

  IModule createModule( IExecutionEnvironment execEnv, String strMemberName );

  IGosuClass getGosuClassFrom( IType fromType );

  INullExpression getNullExpressionInstance();

  GosuExceptionUtil.IForceThrower getForceThrower();

  IGosuClass getBlockToInterfaceConversionClass( IType typeToCoerceTo, IType enclosingType );

  IRTypeResolver getIRTypeResolver();

  IRClassCompiler getIRClassCompiler();

  IPropertyAccessor getLengthAccessor();

  GosuPathEntry createPathEntryFromModuleFile(IFile f);

  Method[] getDeclaredMethods( Class cls );

  boolean isAnnotationAllowedMultipleTimes( IFeatureInfo fi, IAnnotationInfo annotationInfo );

  IReducedDynamicFunctionSymbol createReducedDynamicFunctionSymbol(IDynamicFunctionSymbol symbol);

  IModule createGlobalModule(IExecutionEnvironment execEnv);

  IClassPath createClassPath(IModule module, boolean includeAllClasses);

  IType getPureGenericType(IType type);

  IJavaClassInfo createClassInfo(Class aClass, IModule module);

  IMetaType createMetaType(IType type, boolean literal);

  byte[] updateReloadClassesIndicator( List<String> changedTypes, String strScript );

  ITemplateObserver.ITemplateObserverManager makeTemplateObserverManager();

  void print( Object ret );

  String toString( Object val );
}
