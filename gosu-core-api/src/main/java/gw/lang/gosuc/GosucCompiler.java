/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.gosuc;

import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.exceptions.ParseWarning;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.ICompilable;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IFileSystemGosuClassRepository;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.module.IModule;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

public class GosucCompiler {
  public List<IType> compile( GosucProject project, Collection<? extends CharSequence> typeNames ) {
    final List<IType> types = new ArrayList<IType>();
    if( !typeNames.isEmpty() ) {
      if( typeNames.contains( "-all" ) ) {
        typeNames = project.getAllDefinedTypes();
      }
      for( CharSequence typeName : typeNames ) {
        System.out.println( "Compiling " + typeName + "..." );
        final IType type = TypeSystem.getByFullNameIfValid( typeName.toString() );
        if( type != null ) {
          if( compileType( type ) ) {
            types.add( type );
          }
        }
        else {
          System.out.println( " - can't be compiled, name is invalid" );
        }
      }
    }
    return types;
  }

  private boolean compileType( IType type ) {
    if( !(type instanceof ICompilable) || !((ICompilable)type).isCompilable() ) {
      return false;
    }
    if( !(type instanceof IGosuClass) ) {
      //## hack: need to check for IGosuClass for now, but other compilable types
      // need to use ParseResultsException or we need to define some other class
      // to unify how parse issues are reported.
      return false;
    }
    IModule module = type.getTypeLoader().getModule();
    if( module == TypeSystem.getJreModule() ) {
      // This is a Gosu library file e.g., an enhancement, we don't handle these now, ideally they'll come precompiled
      return false;
    }
    TypeSystem.pushModule( module );
    try {
      IGosuClass gsClass = (IGosuClass)type;
      boolean bValid = gsClass.isValid();
      final ParseResultsException parseException = gsClass.getParseResultsException();
      if( parseException != null ) {
        for( IParseIssue issue: parseException.getParseIssues() ) {
          System.out.println( (issue instanceof ParseWarning ? "Warning: " : "Error: ") + issue.getConsoleMessage() );
        }
      }

      if( bValid ) {
        // Compile to bytecode (.class files) (and also copies source file)
        makeClassFileForOut( (IGosuClass)type );
      }
      return true;
    }
    finally {
      TypeSystem.popModule( module );
    }
  }

  private File makeClassFileForOut(IGosuClass gsClass) {
    IModule module = TypeSystem.getCurrentModule();
    final File[] classFile = new File[1];
    IDirectory moduleOutputDirectory = module.getOutputPath();
    if( moduleOutputDirectory == null ) {
      throw new RuntimeException( "Can't make class file, no output path for module " + module.getName() );
    }

    final String outRelativePath = gsClass.getName().replace( '.', File.separatorChar ) + ".class";
    try {
      File child = new File( moduleOutputDirectory.getPath().getFileSystemPathString() );
      child.mkdirs();
      for( StringTokenizer tokenizer = new StringTokenizer( outRelativePath, File.separator + "/" ); tokenizer.hasMoreTokens(); ) {
        String token = tokenizer.nextToken();
        child = new File( child, token );
        if( !child.exists() ) {
          if( token.endsWith( ".class" ) ) {
            child.createNewFile();
          }
          else {
            child.mkdir();
          }
        }
      }
      createClassFile( child, gsClass );
      maybeCopySourceFile( child.getParentFile(), gsClass );
      classFile[0] = child;
    }
    catch( Exception e ) {
      System.out.println( e.getMessage() );
    }
    return classFile[0];
  }

  private void maybeCopySourceFile( File parent, IGosuClass gsClass ) {
    ISourceFileHandle sfh = gsClass.getSourceFileHandle();
    IFile srcFile = sfh.getFile();
    if( srcFile != null ) {
      File file = new File( srcFile.getPath().getFileSystemPathString() );
      if( file.isFile() ) {
        try {
          copyFile( file, new File( parent, file.getName() ) );
        }
        catch( IOException e ) {
          throw new RuntimeException( e );
        }
      }
    }
  }

  private void createClassFile( File outputFile, IGosuClass gosuClass ) throws IOException {
    if (hasDoNotVerifyAnnotation(gosuClass)) {
      return;
    }

    final byte[] bytes = TypeSystem.getGosuClassLoader().getBytes(gosuClass);
    OutputStream out = new FileOutputStream( outputFile );
    try {
      out.write( bytes );
    }
    finally {
      out.close();
    }
    for (IGosuClass innerClass : gosuClass.getInnerClasses()) {
      final String innerClassName = String.format("%s$%s.class", outputFile.getName().substring( 0, outputFile.getName().lastIndexOf( '.' ) ), innerClass.getRelativeName());
      File innerClassFile = new File( outputFile.getParent(), innerClassName );
      if( innerClassFile.isFile() ) {
        innerClassFile.createNewFile();
      }
      createClassFile( innerClassFile, innerClass );
    }
  }

  private boolean hasDoNotVerifyAnnotation(IGosuClass gsClass) {
    for (IAnnotationInfo ai : gsClass.getTypeInfo().getAnnotations()) {
      if (ai.getType().getRelativeName().equals("DoNotVerifyResource")) {
        return true;
      }
    }
    return false;
  }

  public static void copyFile(File sourceFile, File destFile) throws IOException {
    if (sourceFile.isDirectory()) {
      destFile.mkdirs();
      return;
    }

    if (!destFile.exists()) {
      destFile.getParentFile().mkdirs();
      destFile.createNewFile();
    }

    FileChannel source = null;
    FileChannel destination = null;
    try {
      source = new FileInputStream(sourceFile).getChannel();
      destination = new FileOutputStream(destFile).getChannel();
      destination.transferFrom(source, 0, source.size());
    }
    finally {
      if (source != null) {
        try {
          source.close();
        }
        catch (IOException e) {
          throw e;
        }
      }
      if (destination != null) {
        try {
          destination.close();
        }
        catch (IOException e) {
          throw e;
        }
      }
    }
  }

}

