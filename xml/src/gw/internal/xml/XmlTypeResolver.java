/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml;

import gw.lang.reflect.IType;
import gw.util.Pair;

import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.validation.Schema;

public interface XmlTypeResolver {

  Pair<IType,IType> resolveTypes( List<QName> elementStack );

  Schema getSchemaForValidation();

  String getValidationSchemasDescription();

}
