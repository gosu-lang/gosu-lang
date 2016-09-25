package editor.shipit;

import editor.FileTree;
import editor.IMessageTreeNode;
import editor.MessageKind;
import editor.MessageTree;
import editor.MessagesPanel;
import editor.RunMe;
import editor.util.IProgressCallback;
import editor.util.SettleModalEventQueue;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeRef;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;

import javax.swing.tree.TreeModel;
import java.awt.*;

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
      if( type instanceof IGosuClass )
      {
        if( !compile( (IGosuClass)type, consumer, messagesPanel ) )
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

  private void addWarnings( ParseResultsException parseException, MessagesPanel messages )
  {
    if( _warnings == null )
    {
      _warnings = new MessageTree( "", MessageKind.Info, IMessageTreeNode.empty() );
      messages.appendToTop( _warnings );
    }
    _iWarnings += parseException.getParseWarnings().size();
    _warnings.setMessage( "Warnings: " + _iWarnings );
  }

  private void addErrors( ParseResultsException parseException, MessagesPanel messages )
  {
    if( _errors == null )
    {
      _errors = new MessageTree( "", MessageKind.Info, IMessageTreeNode.empty() );
      messages.appendToTop( _errors );
    }
    _iErrors += parseException.getParseExceptions().size();
    _errors.setMessage( "Errors: " + _iErrors );
  }

  private void addFailure( MessagesPanel messages )
  {
    if( _failures == null )
    {
      _failures = new MessageTree( "", MessageKind.Info, IMessageTreeNode.empty() );
      messages.appendToTop( _failures );
    }
    _iFailures++;
    _failures.setMessage( "Failures: " + _iFailures );
  }

  public boolean compile( IGosuClass gsClass, ICompileConsumer consumer, MessagesPanel messages )
  {
    // Parse
    parseImpl( gsClass );
    //noinspection ThrowableResultOfMethodCallIgnored
    ParseResultsException parseException = gsClass.getParseResultsException();

    if( parseException != null && !parseException.hasOnlyParseWarnings() )
    {
      EventQueue.invokeLater( () -> {
        addWarnings( parseException, messages );
        addErrors( parseException, messages );
        MessageTree typeNode = messages.addTypeMessage( gsClass.getName(), null, IMessageTreeNode.empty() );
        parseException.getParseWarnings().forEach( warning -> messages.addWarningMessage( makeIssueMessage( warning, MessageKind.Warning ), typeNode, new IssueMessageCallback( warning ) ) );
        parseException.getParseExceptions().forEach( error -> messages.addErrorMessage( makeIssueMessage( error, MessageKind.Error ), typeNode, new IssueMessageCallback( error ) ) );
      } );

      return consumer.accept( new CompiledClass( gsClass, null, parseException ) );
    }

    // Compile
    try
    {
      if( parseException != null && parseException.getParseIssues().size() > 0 )
      {
        EventQueue.invokeLater( () -> {
                                  addWarnings( parseException, messages );
                                  MessageTree typeNode = messages.addTypeMessage( gsClass.getName(), null, IMessageTreeNode.empty() );
                                  parseException.getParseWarnings().forEach( warning -> messages.addWarningMessage( makeIssueMessage( warning, MessageKind.Warning ), typeNode, new IssueMessageCallback( warning ) ) );
                                } );
      }

      return compileClass( gsClass, consumer );
    }
    catch( Exception e )
    {
      EventQueue.invokeLater( () -> {
        addFailure( messages );
        MessageTree typeNode = messages.addTypeMessage( gsClass.getName(), null, IMessageTreeNode.empty() );
        messages.addFailureMessage( e.getMessage(), typeNode, IMessageTreeNode.empty() );
        e.printStackTrace();
      } );
      return consumer.accept( new CompiledClass( gsClass, null, e ) );
    }
  }

  private String makeIssueMessage( IParseIssue issue, MessageKind kind )
  {
    String msg = kind == MessageKind.Warning ? "Warning: " : "Error: ";
    msg += "(" + issue.getLine() + ", " + issue.getColumn() + ") " + issue.getPlainMessage();
    return msg;
  }

  private boolean compileClass( IGosuClass gsClass, ICompileConsumer consumer )
  {
    byte[] bytes = TypeSystem.getGosuClassLoader().getBytes( gsClass );
    if( !consumer.accept( new CompiledClass( gsClass, bytes, gsClass.getParseResultsException() ) ) )
    {
      return false;
    }
    for( IGosuClass innerClass : gsClass.getInnerClasses() )
    {
      if( !compileClass( innerClass, consumer ) )
      {
        return false;
      }
    }
    return true;
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

  private class IssueMessageCallback implements IMessageTreeNode
  {
    private final IParseIssue _issue;

    public IssueMessageCallback( IParseIssue issue )
    {
      _issue = issue;
    }

    @Override
    public boolean hasTarget()
    {
      return true;
    }

    @Override
    public void jumpToTarget()
    {
      IGosuClass gsClass = _issue.getSource().getGosuClass();
      gsClass = getOuterMostEnclosingClass( gsClass );

      RunMe.getEditorFrame().getGosuPanel().openType( gsClass.getName(), true );
      EventQueue.invokeLater( ()->
        RunMe.getEditorFrame().getGosuPanel().getCurrentEditor().gotoLine( _issue.getLine(), _issue.getColumn() ) );
    }

    private IGosuClass getOuterMostEnclosingClass( IGosuClass innerClass )
    {
      IGosuClass outerMost = innerClass;
      while( outerMost != null && outerMost.getEnclosingType() != null )
      {
        outerMost = (IGosuClass)outerMost.getEnclosingType();
      }
      return outerMost;
    }

  }
}

