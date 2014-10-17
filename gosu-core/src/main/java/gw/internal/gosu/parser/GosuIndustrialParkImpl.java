/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.BaseService;
import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.internal.gosu.coercer.FunctionToInterfaceClassGenerator;
import gw.internal.gosu.ir.builders.SimpleCompiler;
import gw.internal.gosu.ir.transform.util.IRTypeResolverAPIWrapper;
import gw.internal.gosu.javadoc.JavaDocFactoryImpl;
import gw.internal.gosu.module.GlobalModule;
import gw.internal.gosu.module.Module;
import gw.internal.gosu.parser.expressions.Identifier;
import gw.internal.gosu.parser.expressions.NullExpression;
import gw.internal.gosu.runtime.GosuRuntimeMethods;
import gw.internal.gosu.template.GosuTemplateType;
import gw.internal.gosu.template.SimpleTemplateHost;
import gw.internal.gosu.template.TemplateGenerator;
import gw.internal.gosu.template.TemplateTokenizerInstructor;
import gw.lang.IGosuShop;
import gw.lang.annotation.UsageModifier;
import gw.lang.annotation.UsageTarget;
import gw.lang.init.GosuPathEntry;
import gw.lang.init.ModuleFileUtil;
import gw.lang.ir.IRClassCompiler;
import gw.lang.ir.IRTypeResolver;
import gw.lang.javadoc.IJavaDocFactory;
import gw.lang.parser.EvaluationException;
import gw.lang.parser.IConstructorInfoFactory;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IExpression;
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
import gw.lang.parser.StandardSymbolTable;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.expressions.IIdentifierExpression;
import gw.lang.parser.expressions.INullExpression;
import gw.lang.parser.template.ITemplateHost;
import gw.lang.parser.template.TemplateParseException;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IAnnotationInfoFactory;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IEntityAccess;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IMethodInfoFactory;
import gw.lang.reflect.IPropertyAccessor;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IPropertyInfoFactory;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.ITypeInfoFactory;
import gw.lang.reflect.ITypeRef;
import gw.lang.reflect.PropertyInfoDelegate;
import gw.lang.reflect.TypeSystem;
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

import java.beans.IntrospectionException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.List;

/**
 */
public class GosuIndustrialParkImpl extends BaseService implements IGosuShop
{
  private IPropertyInfoFactory _propertyInfoFactory = new PropertyInfoFactoryImpl();
  private IMethodInfoFactory _methodInfoFactory = new MethodInfoFactoryImpl();
  private IAnnotationInfoFactory _annotationInfoFactory = AnnotationInfoFactoryImpl.instance();
  private IConstructorInfoFactory _constructorInfoFactory = new ConstructorInfoFactoryImpl();
  private IJavaDocFactory _javaDocFactory = new JavaDocFactoryImpl();
  private ITypeInfoFactory _typeInfoFactory = new TypeInfoFactoryImpl();

  public ISymbolTable createSymbolTable()
  {
    return new StandardSymbolTable();
  }

  public ISymbolTable createSymbolTable( boolean bDefineCommonSymbols )
  {
    return new StandardSymbolTable( bDefineCommonSymbols );
  }

  public ITemplateHost createTemplateHost()
  {
    return new SimpleTemplateHost();
  }

  public IConstructorInfoFactory getConstructorInfoFactory()
  {
    return _constructorInfoFactory;
  }

  public IAnnotationInfoFactory getAnnotationInfoFactory()
  {
    return _annotationInfoFactory;
  }

  public IPropertyInfo createLengthProperty(ITypeInfo typeInfo)
  {
    try
    {
      return new LengthProperty(typeInfo);
    }
    catch( IntrospectionException e )
    {
      throw new RuntimeException( e );
    }
  }

  @Override
  public INullExpression getNullExpressionInstance() {
    return NullExpression.instance();
  }

  @Override
  public GosuExceptionUtil.IForceThrower getForceThrower()
  {
    return ForceThrowerGenerator.create();
  }
  
  public IFunctionType createFunctionType( IMethodInfo mi )
  {
    return new FunctionType( mi );
  }

  public ISymbol createSymbol( CharSequence name, IType type, Object value )
  {
    return new Symbol( name.toString(), type, value );
  }
  public ISymbol createSymbol( CharSequence name, IType type, IStackProvider stackProvider )
  {
    return new Symbol( name.toString(), type, stackProvider );
  }

  public ISymbol createDynamicFunctionSymbol( ISymbolTable symbolTable, String strMemberName, IFunctionType functionType, List<ISymbol> params, IExpression value )
  {
    return new DynamicFunctionSymbol( symbolTable, strMemberName, functionType, params, value );
  }

  public IEnhancementIndex createEnhancementIndex( GosuClassTypeLoader loader )
  {
    return new EnhancementIndex( loader );
  }

  public IGosuClass createClass( String strNamespace, String strRelativeName, GosuClassTypeLoader loader, ISourceFileHandle sourceFile, ITypeUsesMap typeUsesMap )
  {
    final ITypeRef ref = TypeSystem.getOrCreateTypeReference(new GosuClass(strNamespace, strRelativeName, loader, sourceFile, typeUsesMap));
    return (IGosuClass) ref;
  }
  public IGosuProgram createProgram( String strNamespace, String strRelativeName, GosuClassTypeLoader loader, ISourceFileHandle sourceFile, ITypeUsesMap typeUsesMap, ISymbolTable symTable )
  {
    GosuProgram program = new GosuProgram(strNamespace, strRelativeName, loader, sourceFile, typeUsesMap, symTable);
    return (IGosuProgram)TypeSystem.getOrCreateTypeReference(program);
  }

  @Override
  public IGosuProgram createProgramForEval( String strNamespace, String strRelativeName, GosuClassTypeLoader loader, ISourceFileHandle sourceFile, ITypeUsesMap typeUsesMap, ISymbolTable symTable )
  {
    IGosuProgramInternal gosuProgram = (IGosuProgramInternal)createProgram( strNamespace, strRelativeName, loader, sourceFile, typeUsesMap, symTable );
    gosuProgram.setAnonymous( true );
    return gosuProgram;
  }

  public IGosuEnhancement createEnhancement( String strNamespace, String strRelativeName, GosuClassTypeLoader loader, ISourceFileHandle sourceFile, ITypeUsesMap typeUsesMap )
  {
    return (IGosuEnhancement)TypeSystem.getOrCreateTypeReference( new GosuEnhancement( strNamespace, strRelativeName, loader, sourceFile, typeUsesMap ) );
  }
  public ITemplateType createTemplate( String strNamespace, String strRelativeName, GosuClassTypeLoader loader, ISourceFileHandle sourceFile, ITypeUsesMap typeUsesMap, ISymbolTable symTable )
  {
    return (ITemplateType)TypeSystem.getOrCreateTypeReference( new GosuTemplateType( strNamespace, strRelativeName, loader, sourceFile, typeUsesMap, symTable ) );
  }

  public IFileSystemGosuClassRepository createFileSystemGosuClassRepository(IModule module, IDirectory[] files)
  {
    return createFileSystemGosuClassRepository(module, files, GosuClassTypeLoader.ALL_EXTS);
  }
  public IFileSystemGosuClassRepository createFileSystemGosuClassRepository(IModule module, IDirectory[] files, String[] extensions)
  {
    IFileSystemGosuClassRepository repository = new FileSystemGosuClassRepository(module);
    repository.setSourcePath(files);
    return repository;
  }

  public ITypeUsesMap createTypeUsesMap( List<String> specialTypeUses )
  {
    return new TypeUsesMap( specialTypeUses );
  }

  public IPropertyInfo getPropertyInfo( IType classBean, String strProperty, IFeatureFilter filter, IParserPart parser, IScriptabilityModifier scriptabilityConstraint) throws ParseException
  {
    return BeanAccess.getPropertyInfo( classBean, strProperty, filter, (ParserBase)parser, scriptabilityConstraint );
  }
  public List<? extends IPropertyInfo> getProperties( ITypeInfo beanInfo, IType classSource )
  {
    return BeanAccess.getProperties( beanInfo, classSource );
  }
  public boolean isDescriptorHidden( IAttributedFeatureInfo pi )
  {
    return BeanAccess.isDescriptorHidden( pi );
  }
  public List<? extends IMethodInfo> getMethods( ITypeInfo beanInfo, IType ownersIntrinsicType )
  {
    return BeanAccess.getMethods( beanInfo, ownersIntrinsicType );
  }

  public StandardParserState createStandardParserState( IParsedElement rootParsedElement, String scriptSrc, boolean b )
  {
    return new StandardParserState( rootParsedElement, scriptSrc, b );
  }

  public EvaluationException createEvaluationException(String msg) {
    return new EvaluationException( msg );
  }

  public IModule createModule( IExecutionEnvironment execEnv, String strMemberName )
  {
    return new Module( execEnv, strMemberName);
  }

  @Override
  public IGosuClass getGosuClassFrom( IType fromType ) {
    return IGosuClassInternal.Util.getGosuClassFrom( fromType );
  }

  @Override
  public IModule createGlobalModule(IExecutionEnvironment execEnv) {
    return new GlobalModule( execEnv, IExecutionEnvironment.GLOBAL_MODULE_NAME);
  }

  @Override
  public IClassPath createClassPath(IModule module, boolean includeAllClasses) {
    return new ClassPath(module, includeAllClasses ? ClassPath.ALLOW_ALL_FILTER : ClassPath.ONLY_API_CLASSES);
  }

  @Override
  public IType getPureGenericType(IType type) {
    return TypeLord.getPureGenericType(type);
  }

  public IJavaClassInfo createClassInfo(Class aClass, IModule module) {
    return new ClassJavaClassInfo(aClass, module);
  }

  @Override
  public IMetaType createMetaType(IType type, boolean literal) {
    return new MetaType(type, literal);
  }

  public IPropertyInfo createPropertyDelegate(IFeatureInfo container, IPropertyInfo prop) {
    return new PropertyInfoDelegate(container, prop);
  }

  public IJavaDocFactory getJavaDocFactory()
  {
    return _javaDocFactory;
  }

  public ITypeInfoFactory getTypeInfoFactory()
  {
    return _typeInfoFactory;
  }

  public IEntityAccess getDefaultEntityAccess()
  {
    return DefaultEntityAccess.instance();
  }

  public ITemplateHost createSimpleTemplateHost()
  {
    return new SimpleTemplateHost();
  }

  public ISourceCodeTokenizer createSourceCodeTokenizer( CharSequence code )
  {
    return new SourceCodeTokenizer( code );
  }
  
  public ISourceCodeTokenizer createSourceCodeTokenizer( CharSequence code, boolean bTemplate )
  {
    SourceCodeTokenizer sourceCodeTokenizer = new SourceCodeTokenizer( code );
    if(bTemplate) {
      sourceCodeTokenizer.setInstructor(new TemplateTokenizerInstructor(sourceCodeTokenizer));
    }
    return sourceCodeTokenizer;
  }

  public ISourceCodeTokenizer createSourceCodeTokenizer( Reader reader )
  {
    return new SourceCodeTokenizer( reader );
  }

  public IScope createCommnoSymbolScope()
  {
    return CommonSymbolsScope.make();
  }

  public IIdentifierExpression createIdentifierExpression()
  {
    return new Identifier();
  }

  public void generateTemplate( Reader readerTemplate, Writer writerOut, ISymbolTable symbolTable ) throws TemplateParseException
  {
    TemplateGenerator.generateTemplate( readerTemplate, writerOut, symbolTable );
  }

  @Override
  public ITokenizerInstructor createTemplateTokenizerInstructor( ISourceCodeTokenizer tokenizer )
  {
    return new TemplateTokenizerInstructor( tokenizer );
  }

  public ISymbolTable getGosuClassSymbolTable()
  {
    return TypeSystem.getCompiledGosuClassSymbolTable();
  }

  @Override
  public IGosuClass getBlockToInterfaceConversionClass( IType typeToCoerceTo, IType enclosingType ) {
    return FunctionToInterfaceClassGenerator.getBlockToInterfaceConversionClass( typeToCoerceTo, enclosingType );
  }

  @Override
  public IRTypeResolver getIRTypeResolver() {
    return IRTypeResolverAPIWrapper.INSTANCE;
  }

  @Override
  public IRClassCompiler getIRClassCompiler() {
    return SimpleCompiler.INSTANCE;
  }

  @Override
  public IPropertyAccessor getLengthAccessor()
  {
    return LengthAccessor.INSTANCE;
  }

  @Override
  public GosuPathEntry createPathEntryFromModuleFile(IFile f) {
    return ModuleFileUtil.createPathEntryForModuleFile(f);
  }

  @Override
  public Method[] getDeclaredMethods( Class cls )
  {
    return NewIntrospector.getDeclaredMethods( cls );
  }

  @Override
  public boolean isAnnotationAllowedMultipleTimes( IFeatureInfo fi, IAnnotationInfo annotationInfo )
  {
    return UsageModifier.Many == UsageModifier.getUsageModifier( UsageTarget.getForFeature( fi ), annotationInfo.getType() );
  }

  @Override
  public IReducedDynamicFunctionSymbol createReducedDynamicFunctionSymbol(IDynamicFunctionSymbol symbol) {
    return symbol.createReducedSymbol();
  }

  public byte[] updateReloadClassesIndicator( List<String> changedTypes, String strScript )
  {
    return ReloadClassesIndicatorCompiler.updateReloadClassesIndicator( changedTypes, strScript );
  }

  @Override
  public void print( Object ret ) {
    GosuRuntimeMethods.print( ret );
  }

  @Override
  public String toString( Object val ) {
    return GosuRuntimeMethods.toString( val );
  }
}
