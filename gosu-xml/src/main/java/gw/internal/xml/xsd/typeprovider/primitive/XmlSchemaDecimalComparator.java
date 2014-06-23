/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.primitive;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * A comparator that compares xsd:decimal values.
 */
public class XmlSchemaDecimalComparator implements Comparator<String> {

  @Override
  public int compare(String o1, String o2) {
    return new BigDecimal( o1 ).compareTo( new BigDecimal( o2 ) );
  }

}
