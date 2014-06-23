/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.test.remote;

import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.xml.simple.SimpleXmlNode;
import junit.framework.AssertionFailedError;

import java.util.ArrayList;
import java.util.List;

public class RemoteTestResult {

  public static final String SUCCESS_ATTRIBUTE = "success";
  public static final String ROOT_ELEMENT = "RemoteTestResult";

  private boolean _success = true;
  private ExceptionInfo _exceptionInfo;

  public static class ExceptionInfo {
    private String _className;
    private String _message;
    private StackTraceElement[] _stackTrace;
    private ExceptionInfo _cause;

    public static final String ELEMENT_NAME = "ExceptionInfo";
    public static final String CLASS_NAME_ATTRIBUTE = "className";
    public static final String MESSAGE_ATTRIBUTE = "message";

    public static final String STACK_TRACE_ELEMENT_NAME = "StackTrace";
    public static final String STACK_TRACE_ELEMENT_ELEMENT_NAME = "StackTraceElement";
    public static final String STACK_TRACE_ELEMENT_DECLARING_CLASS_ATTRIBUTE = "declaringClass";
    public static final String STACK_TRACE_ELEMENT_METHOD_NAME_ATTRIBUTE = "methodName";
    public static final String STACK_TRACE_ELEMENT_FILE_NAME_ATTRIBUTE = "fileName";
    public static final String STACK_TRACE_ELEMENT_LINE_NUMBER_ATTRIBUTE = "lineNumber";

    public static final String CAUSE_ELEMENT_NAME = "Cause";

    public static ExceptionInfo fromXml(SimpleXmlNode xmlNode) {
      String className = xmlNode.getAttributes().get(CLASS_NAME_ATTRIBUTE);
      String message = xmlNode.getAttributes().get(MESSAGE_ATTRIBUTE);

      SimpleXmlNode stackNode = xmlNode.getChildren().get(0);
      // TODO - Assert it's the right thing
      List<StackTraceElement> stes = new ArrayList<StackTraceElement>();
      for (SimpleXmlNode steNode : stackNode.getChildren()) {
        stes.add(new StackTraceElement(
            steNode.getAttributes().get(STACK_TRACE_ELEMENT_DECLARING_CLASS_ATTRIBUTE),
            steNode.getAttributes().get(STACK_TRACE_ELEMENT_METHOD_NAME_ATTRIBUTE),
            steNode.getAttributes().get(STACK_TRACE_ELEMENT_FILE_NAME_ATTRIBUTE),
            Integer.valueOf(steNode.getAttributes().get(STACK_TRACE_ELEMENT_LINE_NUMBER_ATTRIBUTE))
        ));
      }

      ExceptionInfo cause = null;
      if (xmlNode.getChildren().size() > 1) {
        SimpleXmlNode causeNode = xmlNode.getChildren().get(1);
        cause = fromXml(causeNode.getChildren().get(0));
      }

      return new ExceptionInfo(className, message, stes.toArray(new StackTraceElement[stes.size()]), cause);
    }

    public static ExceptionInfo fromThrowable(Throwable t) {
      ExceptionInfo causeExceptionInfo = null;
      Throwable cause = t.getCause();
      if (cause != null && cause != t) {
        causeExceptionInfo = fromThrowable(cause);
      }
      IType type = TypeSystem.getFromObject( t );
      if ( type == null ) {
        t.printStackTrace();
        throw new IllegalStateException( "Unable to determine type from throwable: " + t );
      }
      return new ExceptionInfo( type.getName(), t.getMessage(), t.getStackTrace(), causeExceptionInfo);
    }

    private ExceptionInfo(String className, String message, StackTraceElement[] stackTrace, ExceptionInfo cause) {
      _className = className;
      _message = message;
      _stackTrace = stackTrace;
      _cause = cause;
    }

    public String getClassName() {
      return _className;
    }

    public String getMessage() {
      return _message;
    }

    public StackTraceElement[] getStackTrace() {
      return _stackTrace;
    }

    public ExceptionInfo getCause() {
      return _cause;
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    public Throwable toThrowable() {
      if (_className.equals(AssertionFailedError.class.getName())) {
        return new RemoteAssertionFailedError(_message, _className, _stackTrace);
      } else {
        return new RemoteTestException(_message, _cause == null ? null : _cause.toThrowable(), _className, _stackTrace);
      }
    }

    public SimpleXmlNode toXml() {
      SimpleXmlNode root = new SimpleXmlNode(ELEMENT_NAME);
      root.getAttributes().put(CLASS_NAME_ATTRIBUTE, _className);
      root.getAttributes().put(MESSAGE_ATTRIBUTE, _message);

      SimpleXmlNode stack = new SimpleXmlNode(STACK_TRACE_ELEMENT_NAME);
      root.getChildren().add(stack);
      for (StackTraceElement ste : _stackTrace) {
        SimpleXmlNode stackTraceElement = new SimpleXmlNode(STACK_TRACE_ELEMENT_ELEMENT_NAME);
        stackTraceElement.getAttributes().put(STACK_TRACE_ELEMENT_DECLARING_CLASS_ATTRIBUTE, ste.getClassName());
        stackTraceElement.getAttributes().put(STACK_TRACE_ELEMENT_METHOD_NAME_ATTRIBUTE, ste.getMethodName());
        stackTraceElement.getAttributes().put(STACK_TRACE_ELEMENT_FILE_NAME_ATTRIBUTE, ste.getFileName());
        stackTraceElement.getAttributes().put(STACK_TRACE_ELEMENT_LINE_NUMBER_ATTRIBUTE, "" + ste.getLineNumber());
        stack.getChildren().add(stackTraceElement);
      }

      if (_cause != null) {
        SimpleXmlNode cause = new SimpleXmlNode(CAUSE_ELEMENT_NAME);
        cause.getChildren().add(_cause.toXml());
        root.getChildren().add(cause);
      }

      return root;
    }
  }

  public String toXML() {
    SimpleXmlNode root = new SimpleXmlNode(ROOT_ELEMENT);
    root.getAttributes().put(SUCCESS_ATTRIBUTE, Boolean.toString(_success));
    if (_exceptionInfo != null) {
      root.getChildren().add(_exceptionInfo.toXml());
    }

    return root.toXmlString();
  }

  public boolean successful() {
    return _success;
  }

  public void setException(Throwable t) {
    _success = false;
    _exceptionInfo = ExceptionInfo.fromThrowable(t);
  }

  // Exposed for testing purposes only
  ExceptionInfo getExceptionInfo() {
    return _exceptionInfo;
  }

  public Throwable recreateException() {
    return _exceptionInfo.toThrowable();
  }

  public static RemoteTestResult fromXML(String xml) {
    RemoteTestResult result = new RemoteTestResult();

    // TODO - Assert that the root has the right name
    // TODO - Assert that if it's not successful, there must be a stack trace
    SimpleXmlNode resultNode = SimpleXmlNode.parse(xml);
    result._success = Boolean.valueOf(resultNode.getAttributes().get(SUCCESS_ATTRIBUTE));
    if (resultNode.getChildren().size() > 0) {
      // TODO - AHK - Assert it's the right node
      result._exceptionInfo = ExceptionInfo.fromXml(resultNode.getChildren().get(0));
    }

    return result;
  }
}
