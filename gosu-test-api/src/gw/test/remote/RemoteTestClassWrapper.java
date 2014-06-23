/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.test.remote;

import gw.test.TestMetadata;
import gw.xml.simple.SimpleXmlNode;
import junit.framework.*;

import java.util.*;

import gw.util.GosuStringUtil;
import gw.util.StreamUtil;
import gw.test.TestExecutionManager;

public class RemoteTestClassWrapper extends TestSuite {
  private String _typeName;
  private int _numTestMethods;

  public RemoteTestClassWrapper(TestExecutionManager executionManager, String typeName, String... methodNames) {
    _typeName = typeName;
    _numTestMethods = 0;
    Set<String> filteredMethods = (methodNames == null ? null : new HashSet<String>(Arrays.asList(methodNames)));
    SimpleXmlNode testClassInfo = extractMethodsRemotely(executionManager, typeName);
    for (SimpleXmlNode methodInfo : testClassInfo.getChildren()) {
      String methodName = methodInfo.getAttributes().get("name");
      if (filteredMethods == null || filteredMethods.contains(methodName)) {
        _numTestMethods++;
        RemoteTestClass remoteTestClass = new RemoteTestClass(typeName, methodName, getNumTestMethods(), executionManager);
        List<TestMetadata> testMetadata = new ArrayList<TestMetadata>();
        for (SimpleXmlNode metadataNode : methodInfo.getChildren()) {
          testMetadata.add(TestMetadata.deserializeXml(metadataNode));
        }
        remoteTestClass.addMetadata(testMetadata);
        addTest(remoteTestClass);
      }
    }
  }

  @Override
  public String getName() {
    return _typeName;
  }

  public int getNumTestMethods() {
    return _numTestMethods;
  }

  public static SimpleXmlNode extractMethodsRemotely(TestExecutionManager executionManager, String typeName) {
    byte[] response = RemoteTestClass.makeRemoteRequest(((ForwardingTestEnvironment) executionManager.getEnvironment()).getRemoteURL(), typeName.replace('.', '/') + "/testInfo");
    String responseString = StreamUtil.toString(response);
    return SimpleXmlNode.parse(responseString);
  }
}
