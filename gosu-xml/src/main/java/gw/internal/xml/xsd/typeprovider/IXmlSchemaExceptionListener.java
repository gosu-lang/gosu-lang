/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider;

import gw.fs.IFile;

public interface IXmlSchemaExceptionListener {

  void exceptionOccurred( String namespace, IFile resourceFile, Throwable t );

}
