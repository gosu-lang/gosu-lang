package editor.shipit;

import editor.FileTree;
import editor.NodeKind;
import editor.MessageTree;
import editor.MessagesPanel;
import editor.settings.CompilerSettings;
import editor.util.IProgressCallback;
import gw.lang.javac.IJavaParser;
import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.IFileRepositoryBasedType;
import gw.lang.parser.IHasInnerClass;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeRef;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;

import gw.lang.reflect.java.IJavaType;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

/**
 */
public class Compiler
{
  private MessageTree _warnings;
  private int _iWarnings = 0;
  private MessageTree _errors;
  private int _iErrors = 0;
  private MessageTree _failures;
  private int _iFailures = 0;

  public boolean compileTree( FileTree tree, ICompileConsumer consumer, IProgressCallback progress, MessagesPanel messagesPanel )
  {
    if( progress.isAbort() )
    {
      return false;
    }

    if( tree.isFile() )
    {
      IType type = tree.getType();
      progress.incrementProgress( type != null ? type.getName() : "" );
      if( type instanceof IFileRepositoryBasedType )
      {
        if( !compile( type, consumer, messagesPanel ) )
        {
          return false;
        }
      }
    }
    else if( tree.isDirectory() )
    {
      for( FileTree file: tree.getChildren() )
      {
        if( !compileTree( file, consumer, progress, messagesPanel ) )
        {
          return false;
        }
      }
    }
    return true;
  }

  public int getWarnings()
  {
    return _iWarnings;
  }

  public int getErrors()
  {
    return _iErrors;
  }

  public int getFailures()
  {
    return _iFailures;
  }

  private void addWarnings( ParseResultsException parseException )
  {
    _iWarnings += parseException.getParseWarnings().size();
  }

  private void addErrors( ParseResultsException parseException )
  {
    _iErrors += parseException.getParseExceptions().size();
  }

  private void addFailure()
  {
    _iFailures++;
  }

  private void updateMessageTree( MessagesPanel messages )
  {
    EventQueue.invokeLater( () -> {
      if( _iWarnings > 0 )
      {
        if( _warnings == null )
        {
          _warnings = new MessageTree( "", NodeKind.Info, MessageTree.empty() );
          messages.appendToTop( _warnings );
        }
        _warnings.setText( "Warnings: " + _iWarnings );
      }

      if( _iErrors > 0 )
      {
        if( _errors == null )
        {
          _errors = new MessageTree( "", NodeKind.Info, MessageTree.empty() );
          messages.appendToTop( _errors );
        }
        _errors.setText( "Errors: " + _iErrors );
      }

      if( _iFailures > 0 )
      {
        if( _failures == null )
        {
          _failures = new MessageTree( "", NodeKind.Info, MessageTree.empty() );
          messages.appendToTop( _failures );
        }
        _failures.setText( "Failures: " + _iFailures );
      }
    } );
  }

  public boolean compile( IType type, ICompileConsumer consumer, MessagesPanel messages )
  {
    //## todo: provide a plugin interface for type compiler

    if( type instanceof IGosuClass )
    {
      return compileGosu( (IGosuClass)type, consumer, messages );
    }
    if( type instanceof IJavaType )
    {
      return compileJava( (IJavaType)type, consumer, messages );
    }

    return true;
  }

  private boolean compileGosu( IGosuClass gsClass, ICompileConsumer consumer, MessagesPanel messages )
  {
    if( isExcluded( gsClass ) )
    {
      return true;
    }

    //
    // Parse
    //
    parseImpl( gsClass );
    //noinspection ThrowableResultOfMethodCallIgnored
    ParseResultsException parseException = gsClass.getParseResultsException();

    if( parseException != null && !parseException.hasOnlyParseWarnings() )
    {
      addWarnings( parseException );
      addErrors( parseException );
      EventQueue.invokeLater( () -> {
        updateMessageTree( messages );
        MessageTree typeNode = messages.addTypeMessage( gsClass.getName(), null, MessageTree.empty() );
        parseException.getParseWarnings().forEach( warning -> messages.addWarningMessage( makeIssueMessage( warning, NodeKind.Warning ), typeNode, MessageTree.makeIssueMessage( warning ) ) );
        parseException.getParseExceptions().forEach( error -> messages.addErrorMessage( makeIssueMessage( error, NodeKind.Error ), typeNode, MessageTree.makeIssueMessage( error ) ) );
      } );

      // Parse Errors, return now
      return consumer.accept( new CompiledClass( gsClass, null ) );
    }

    //
    // Compile
    //
    try
    {
      if( parseException != null && parseException.getParseIssues().size() > 0 )
      {
        addWarnings( parseException );
        EventQueue.invokeLater( () -> {
          updateMessageTree( messages );
          MessageTree typeNode = messages.addTypeMessage( gsClass.getName(), null, MessageTree.empty() );
          parseException.getParseWarnings().forEach( warning -> messages.addWarningMessage( makeIssueMessage( warning, NodeKind.Warning ), typeNode, MessageTree.makeIssueMessage( warning ) ) );
        } );
      }

      return compileClass( gsClass, consumer );
    }
    catch( Exception e )
    {
      //
      // Failure
      //
      addFailure();
      EventQueue.invokeLater( () -> {
        updateMessageTree( messages );
        MessageTree typeNode = messages.addTypeMessage( gsClass.getName(), null, MessageTree.empty() );
        messages.addFailureMessage( e.getMessage(), typeNode, MessageTree.empty() );
        e.printStackTrace();
      } );
      return consumer.accept( new CompiledClass( gsClass, null ) );
    }
  }

  private boolean compileJava( IJavaType javaType, ICompileConsumer consumer, MessagesPanel messages )
  {
    if( isExcluded( javaType ) )
    {
      return true;
    }

    IJavaParser javaParser = GosuParserFactory.getInterface( IJavaParser.class );
    DiagnosticCollector<JavaFileObject> errorHandler = new DiagnosticCollector<>();
    javaParser.compile( javaType.getName(), Collections.singleton( "-Xlint:unchecked" ), errorHandler );
    boolean errant = errorHandler.getDiagnostics().stream().anyMatch( e -> e.getKind() == Diagnostic.Kind.ERROR );
    if( errant )
    {
      errorHandler.getDiagnostics().forEach(
        e ->
        {
          switch( e.getKind() )
          {
            case ERROR:
              _iErrors++;
              break;
            case WARNING:
            case MANDATORY_WARNING:
              _iWarnings++;
              break;
          }
        } );
      EventQueue.invokeLater( () -> {
        updateMessageTree( messages );
        MessageTree typeNode = messages.addTypeMessage( javaType.getName(), null, MessageTree.empty() );
        errorHandler.getDiagnostics().forEach(
          e ->
          {
            switch( e.getKind() )
            {
              case ERROR:
                messages.addErrorMessage( makeIssueMessage( e, NodeKind.Error ), typeNode, MessageTree.makeIssueMessage( e, javaType ) );
                break;
              case WARNING:
              case MANDATORY_WARNING:
                messages.addWarningMessage( makeIssueMessage( e, NodeKind.Warning ), typeNode, MessageTree.makeIssueMessage( e, javaType ) );
                break;
            }
          } );
      } );

      // Parse Errors, return now
      return consumer.accept( new CompiledClass( javaType, null ) );
    }

    //
    // Compile
    //
    try
    {
      if( errorHandler.getDiagnostics().stream().anyMatch( e -> e.getKind() == Diagnostic.Kind.WARNING || e.getKind() == Diagnostic.Kind.MANDATORY_WARNING ) )
      {
        errorHandler.getDiagnostics().forEach(
          e ->
          {
            switch( e.getKind() )
            {
              case WARNING:
              case MANDATORY_WARNING:
                _iWarnings++;
                break;
            }
          } );
        EventQueue.invokeLater( () -> {
          updateMessageTree( messages );
          MessageTree typeNode = messages.addTypeMessage( javaType.getName(), null, MessageTree.empty() );
          errorHandler.getDiagnostics().forEach(
            e ->
            {
              switch( e.getKind() )
              {
                case WARNING:
                case MANDATORY_WARNING:
                  messages.addWarningMessage( makeIssueMessage( e, NodeKind.Warning ), typeNode, MessageTree.makeIssueMessage( e, javaType ) );
                  break;
              }
            } );
        } );
      }

      return compileClass( javaType, consumer );
    }
    catch( Exception e )
    {
      //
      // Failure
      //
      addFailure();
      EventQueue.invokeLater( () -> {
        updateMessageTree( messages );
        MessageTree typeNode = messages.addTypeMessage( javaType.getName(), null, MessageTree.empty() );
        messages.addFailureMessage( e.getMessage(), typeNode, MessageTree.empty() );
        e.printStackTrace();
      } );
      return consumer.accept( new CompiledClass( javaType, null ) );
    }
  }

  private boolean isExcluded( IType type )
  {
    //## todo: expose "Excluded" in Lab so users can exclude stuff

    if( type.getRelativeName().startsWith( "Errant_" ) )
    {
      return true;
    }

    if( type instanceof IGosuClass )
    {
      IGosuClass gsClass = (IGosuClass)type;
      if( gsClass.getSource().contains( "@DoNotVerifyResource" ) )
      {
        return true;
      }
    }

    return false;
  }

  private String makeIssueMessage( IParseIssue issue, NodeKind kind )
  {
    String msg = kind == NodeKind.Warning ? "Warning: " : "Error: ";
    msg += "(" + issue.getLine() + ", " + issue.getColumn() + ") " + issue.getPlainMessage();
    return msg;
  }

  private String makeIssueMessage( Diagnostic<? extends JavaFileObject> issue, NodeKind kind )
  {
    String msg = kind == NodeKind.Error ? "Error: " : "Warning: ";
    msg += "(" + issue.getLineNumber() + ", " + issue.getColumnNumber() + ") " + issue.getMessage( Locale.getDefault() );
    return msg;
  }

  private boolean compileClass( IFileRepositoryBasedType type, ICompileConsumer consumer )
  {
    if( !type.isCompilable() )
    {
      // For instance, a JavaSourceUnresolvedClass is not ocmpilable;
      // this happens if the Java source file doesn't have a class in it matching the name of the file
      return true;
    }

    byte[] bytes = type.compile();
    if( !consumer.accept( new CompiledClass( type, bytes ) ) )
    {
      return false;
    }
    makeClassFile( type, bytes );
    if( type instanceof IHasInnerClass )
    {
      for( IType innerClass : ((IHasInnerClass)type).getInnerClasses() )
      {
        if( !compileClass( (IFileRepositoryBasedType)innerClass, consumer ) )
        {
          return false;
        }
      }
    }
    return true;
  }

  private void makeClassFile( IFileRepositoryBasedType type, byte[] bytes )
  {
    if( !CompilerSettings.isStaticCompile() )
    {
      return;
    }

    File outputDir = CompilerSettings.getCompilerOutputDir();
    if( !outputDir.exists() )
    {
      if( !outputDir.mkdirs() || !outputDir.isDirectory() )
      {
        return;
      }
    }

    String javaName = type.getJavaName();
    javaName = javaName.replace( '.', File.separatorChar ) + ".class";
    File classFile = new File( outputDir, javaName );
    //noinspection ResultOfMethodCallIgnored
    classFile.getParentFile().mkdirs();
    try( FileOutputStream writer = new FileOutputStream( classFile ) )
    {
      writer.write( bytes );
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
  }

  protected void parseImpl( IGosuClass gsClass )
  {
    TypeSystem.lock();
    try
    {
      if( gsClass != null )
      {
        TypeSystem.refresh( (ITypeRef)gsClass );
        gsClass.setCreateEditorParser( false );
        gsClass.isValid(); // force compilation
      }
    }
    finally
    {
      TypeSystem.unlock();
    }
  }
}

