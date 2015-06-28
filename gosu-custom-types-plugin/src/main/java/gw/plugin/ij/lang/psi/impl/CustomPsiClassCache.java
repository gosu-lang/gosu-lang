/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.PsiManagerImpl;
import com.intellij.psi.impl.PsiModificationTrackerImpl;
import gw.lang.parser.IHasInnerClass;
import gw.lang.reflect.AbstractTypeSystemListener;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IDefaultTypeLoader;
import gw.lang.reflect.IFileBasedType;
import gw.lang.reflect.IHasParameterInfos;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.MethodList;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.RefreshRequest;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.custom.JavaFacadePsiClass;
import gw.plugin.ij.util.FileUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CustomPsiClassCache extends AbstractTypeSystemListener
{
  private static final CustomPsiClassCache INSTANCE = new CustomPsiClassCache();

  @NotNull
  public static CustomPsiClassCache instance()
  {
    return INSTANCE;
  }

  private final ConcurrentHashMap<String, JavaFacadePsiClass> _psi2Class = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<IModule, Map<String, JavaFacadePsiClass>> _type2Class = new ConcurrentHashMap<>();

  private CustomPsiClassCache()
  {
    TypeSystem.addTypeLoaderListenerAsWeakRef( this );
  }

  public JavaFacadePsiClass getPsiClass( @NotNull IType type )
  {
    if( !(type instanceof IFileBasedType) )
    {
      return null;
    }
    List<VirtualFile> typeResourceFiles = FileUtil.getTypeResourceFiles( type );
    if( typeResourceFiles.isEmpty() )
    {
      return null;
    }

    IModule module = type.getTypeLoader().getModule();
    String name = type.getName();
    Map<String, JavaFacadePsiClass> map = _type2Class.get( module );
    if( map == null )
    {
      map = new ConcurrentHashMap<>();
      _type2Class.put( module, map );
    }
    JavaFacadePsiClass psiFacadeClass = map.get( name );
    if( psiFacadeClass == null || !psiFacadeClass.isValid() )
    {
      PsiClass delegate = createPsiClass( type );
      psiFacadeClass = new JavaFacadePsiClass( delegate, (IFileBasedType)type );
      map.put( name, psiFacadeClass );
      _psi2Class.put( ((IFileBasedType)type).getSourceFiles()[0].getPath().getPathString(), psiFacadeClass );
    }
    return psiFacadeClass;
  }

  @NotNull
  private PsiClass createPsiClass( @NotNull IType type )
  {
    if( !(type instanceof IFileBasedType) )
    {
      throw new RuntimeException( "Only file-based types can have custom PsiClasses: " + type.getClass().getName() );
    }

    PsiManager manager = PsiManagerImpl.getInstance( (Project)type.getTypeLoader().getModule().getExecutionEnvironment().getProject().getNativeProject() );
    String source = generateSource( type );
    final PsiJavaFile aFile = createDummyJavaFile( type, manager, source );
    final PsiClass[] classes = aFile.getClasses();
    return classes[0];
//    PsiJavaParserFacadeImpl psiJavaParserFacade = new PsiJavaParserFacadeImpl( manager );
//    String source = generateSource( type );
//    return psiJavaParserFacade.createClassFromText( source, null );
  }

  protected PsiJavaFile createDummyJavaFile( IType type, PsiManager manager, @NonNls final String text ) {
    final FileType fileType = JavaFileType.INSTANCE;
    return (PsiJavaFile)PsiFileFactory.getInstance( manager.getProject() ).createFileFromText( type.getName() + '.'  + JavaFileType.INSTANCE.getDefaultExtension(), fileType, text);
  }

  private String generateSource( IType type )
  {
    StringBuilder sb = new StringBuilder()
      .append( "package " ).append( type.getNamespace() ).append( "\n\n" );
    generateClass( type, sb );
    return sb.toString();
  }

  private void generateClass( IType type, StringBuilder sb )
  {
    if( Modifier.isPublic( type.getModifiers() ) )
    {
      sb.append( "public " );
    }
    if( Modifier.isStatic( type.getModifiers() ) )
    {
      sb.append( "static " );
    }
    sb.append( "class " ).append( type.getRelativeName() ).append( " " ) ;
    IType supertype = type.getSupertype();
    if( supertype != null && supertype != JavaTypes.OBJECT() )
    {
      sb.append( "extends " ).append( supertype.getName() ).append( " " );
    }
    IType[] interfaces = type.getInterfaces();
    if( interfaces != null && interfaces.length > 0 )
    {
      sb.append( "implements " );
      for( int i = 0; i < interfaces.length; i++ )
      {
        if( i != 0 )
        {
          sb.append( ", " );
        }
        sb.append( interfaces[i].getName() );
      }
    }
    sb.append( " {\n" );
    generateConstructors( type, sb );
    generateMethods( type, sb );
    generateProperties( type, sb );
    generateInnerClasses( type, sb );
    sb.append( "}\n\n" );

  }

  private void generateInnerClasses( IType type, StringBuilder sb )
  {
    if( type instanceof IHasInnerClass )
    {
      int i = 0;
      for( IType innerClass : ((IHasInnerClass)type).getInnerClasses() )
      {
        ITypeInfo ti = innerClass.getTypeInfo();
        sb.append( "  @InnerClassInfoId(" ).append( i++ ).append( ", \"" ).append( type.getName() ).append( "\", " ).append( ti.getOffset() ).append( ", " ).append( ti.getTextLength() ).append( ")\n" );
        generateClass( innerClass, sb );
      }
    }
  }

  private void generateConstructors( IType type, StringBuilder sb )
  {
    ITypeInfo ti = type.getTypeInfo();
    List<? extends IConstructorInfo> constructors;
    if( ti instanceof IRelativeTypeInfo )
    {
      constructors = ((IRelativeTypeInfo)ti).getConstructors( type );
    }
    else
    {
      constructors = ti.getConstructors();
    }
    int i = 0;
    for( IConstructorInfo ci : constructors )
    {
      sb.append( "  @ConstructorInfoId(" ).append( i++ ).append( ", \"" ).append( ci.getName() ).append( "\", " ).append( ci.getOffset() ).append( ", " ).append( ci.getTextLength() ).append( ")\n" )
      .append( "  " );
      generateModifiers( sb, ci );
      sb.append( " " );
      sb.append( type.getRelativeName() );
      sb.append( "(" );
      generateParameters( sb, ci );
      sb.append( ") {}\n" );
    }
  }

  private void generateMethods( IType type, StringBuilder sb )
  {
    ITypeInfo ti = type.getTypeInfo();
    MethodList methods;
    if( ti instanceof IRelativeTypeInfo )
    {
      methods = ((IRelativeTypeInfo)ti).getMethods( type );
    }
    else
    {
      methods = ti.getMethods();
    }
    int i = 0;
    for( IMethodInfo mi : methods )
    {
      if( mi.getDisplayName().charAt( 0 ) == '@' )
      {
        i++;
        continue;
      }
      sb.append( "  @MethodInfoId(" ).append( i++ ).append( ", \"" ).append( mi.getName() ).append( "\", " ).append( mi.getOffset() ).append( ", " ).append( mi.getTextLength() ).append( ")\n" )
      .append( "  " );
      generateModifiers( sb, mi );
      generateReturnType( sb, mi );
      sb.append( " " );
      sb.append( mi.getDisplayName() );
      sb.append( "(" );
      generateParameters( sb, mi );
      sb.append( ")" );
      generateMethodImplStub( sb, mi );
    }
  }

  private void generateProperties( IType type, StringBuilder sb )
  {
    ITypeInfo ti = type.getTypeInfo();
    List<? extends IPropertyInfo> properties;
    if( ti instanceof IRelativeTypeInfo )
    {
      properties = ((IRelativeTypeInfo)ti).getProperties( type );
    }
    else
    {
      properties = ti.getProperties();
    }
    int i = 0;
    for( IPropertyInfo pi : properties )
    {
      if( pi.isStatic() )
      {
        generatePropertyAsField( sb, i, pi );
      }
      else
      {
        generateInstanceProperty( sb, i, pi );
      }
      i++;
    }
  }

  private void generateInstanceProperty( StringBuilder sb, int i, IPropertyInfo pi )
  {
    if( pi.isReadable() )
    {
      sb.append( "  @PropertyGetInfoId(" ).append( i ).append( ", \"" ).append( pi.getName() ).append( "\", " ).append( pi.getOffset() ).append( ", " ).append( pi.getTextLength() ).append( ")\n" )
        .append( "  " );
      generateModifiers( sb, pi );
      sb.append( pi.getFeatureType().getName() );
      sb.append( " " );
      sb.append( "get" ).append( pi.getDisplayName() );
      sb.append( "() {throw new RuntimeException();}\n" );
    }
    if( pi.isWritable( pi.getOwnersType() ) )
    {
      sb.append( "  @PropertySetInfoId(" ).append( i ).append( ", \"" ).append( pi.getName() ).append( "\", " ).append( pi.getOffset() ).append( ", " ).append( pi.getTextLength() ).append( ")\n" )
        .append( "  " );
      generateModifiers( sb, pi );
      sb.append( pi.getFeatureType().getName() );
      sb.append( " " );
      sb.append( "set" ).append( pi.getDisplayName() );
      sb.append( "( " ).append( pi.getFeatureType().getName() ).append( " value ) {}\n" );
    }
  }

  private void generatePropertyAsField( StringBuilder sb, int i, IPropertyInfo pi )
  {
    if( pi.isReadable() )
    {
      sb.append( "  @PropertyFieldInfoId(" ).append( i ).append( ", \"" ).append( pi.getName() ).append( "\", " ).append( pi.getOffset() ).append( ", " ).append( pi.getTextLength() ).append( ")\n" )
        .append( "  " );
      generateFieldModifiers( sb, pi );
      sb.append( pi.getFeatureType().getName() ).append( " " ).append( pi.getDisplayName() ).append( ";\n" );
    }
  }

  private void generateMethodImplStub( StringBuilder sb, IMethodInfo mi )
  {
    if( mi.isAbstract() )
    {
      sb.append( ";\n" );
    }
    else
    {
      sb.append( " {throw new RuntimeException();}\n" );
    }
  }

  private void generateReturnType( StringBuilder sb, IMethodInfo mi )
  {
    sb.append( mi.getReturnType().getName() );
  }

  private void generateParameters( StringBuilder sb, IHasParameterInfos mi )
  {
    IParameterInfo[] parameters = mi.getParameters();
    for( int i = 0; i < parameters.length; i++ )
    {
      IParameterInfo pi = parameters[i];
      if( i != 0 )
      {
        sb.append( "," );
      }
      sb.append( " " ).append( pi.getFeatureType() ).append( " " ).append( pi.getName() );
      if( i == parameters.length-1 )
      {
        sb.append( " " );
      }
    }
  }

  private void generateModifiers( StringBuilder sb, IAttributedFeatureInfo fi )
  {
    if( fi.isStatic() )
    {
      sb.append( "static " );
    }
    else if( fi.isAbstract() )
    {
      sb.append( "abstract " );
    }
    if( fi.isFinal() )
    {
      sb.append( "final " );
    }
    if( fi.isPrivate() )
    {
      sb.append( "private " );
    }
    else if( fi.isProtected() )
    {
      sb.append( "protected " );
    }
    else if( !fi.isInternal() )
    {
      sb.append( "public " );
    }
  }

  private void generateFieldModifiers( StringBuilder sb, IPropertyInfo pi )
  {
    if( pi.isStatic() )
    {
      sb.append( "static " );
    }
    if( !pi.isWritable() )
    {
      sb.append( "final " );
    }
    if( pi.isPrivate() )
    {
      sb.append( "private " );
    }
    else if( pi.isProtected() )
    {
      sb.append( "protected " );
    }
    else if( !pi.isInternal() )
    {
      sb.append( "public " );
    }
  }

  public Collection<? extends String> getAllClassNames()
  {
//    long t1 = System.nanoTime();

    Set<String> classes = new HashSet<>();
    for( ITypeLoader loader : TypeSystem.getAllTypeLoaders() )
    {
      if( !(loader instanceof GosuClassTypeLoader || loader instanceof IDefaultTypeLoader) )
      {
        IModule module = loader.getModule();
        TypeSystem.pushModule( module );
        try
        {
          for( CharSequence cs : loader.getAllTypeNames() )
          {
            String s = cs.toString();
            int i = s.lastIndexOf( '.' );
            if( i > 0 )
            {
              s = s.substring( i + 1 );
            }
            classes.add( s );
          }
        }
        finally
        {
          TypeSystem.popModule( module );
        }
      }
    }

//    System.out.println((System.nanoTime() - t1)*1e-6);
    return classes;
  }

  @NotNull
  public Collection<PsiClass> getByShortName( String shortName )
  {
    Set<PsiClass> classes = new HashSet<>();
    String prefix = "." + shortName;
    for( ITypeLoader loader : TypeSystem.getAllTypeLoaders() )
    {
      if( loader.showTypeNamesInIDE() && !(loader instanceof GosuClassTypeLoader || loader instanceof IDefaultTypeLoader) )
      {
        IModule module = loader.getModule();
        TypeSystem.pushModule( module );
        try
        {
          for( CharSequence cs : loader.getAllTypeNames() )
          {
            String typeName = cs.toString();
            if( typeName.endsWith( prefix ) )
            {
              IType type = TypeSystem.getByFullNameIfValid( typeName, module );
              if( type instanceof IFileBasedType )
              {
                PsiClass psiClass = getPsiClass( type );
                if( psiClass != null )
                {
                  classes.add( psiClass );
                }
              }
            }
          }
        }
        finally
        {
          TypeSystem.popModule( module );
        }
      }
    }
    return classes;
  }

  @Override
  public void refreshedTypes( RefreshRequest request )
  {
    Map<String, JavaFacadePsiClass> map = _type2Class.get( request.module );
    if( map != null )
    {
      for( String type : request.types )
      {
        map.remove( type );
      }
    }
    if( request.file != null )
    {
      String pathString = request.file.getPath().getPathString();
      JavaFacadePsiClass removedFacade = _psi2Class.remove( pathString );
      if( removedFacade != null )
      {
        ((PsiModificationTrackerImpl) removedFacade.getManager().getModificationTracker()).incCounter();
      }
    }
  }

  @Override
  public void refreshed()
  {
    _psi2Class.clear();
    _type2Class.clear();
  }
}
