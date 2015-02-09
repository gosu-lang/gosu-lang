package gw.test;

/*
 * Copyright 2014 Guidewire Software, Inc.
 */

import junit.framework.Test;

public interface IForwardingTestEnvironment {
  TestClass makeRemoteTestClassIDEExecutionWrapper(String typeName, String methodName, int totalNumMethods, TestExecutionManager executionManager, TestClass wrapped);
  Test makeRemoteTestClassWrapper(TestExecutionManager executionManager, String typeName, String... methodNames);
}
