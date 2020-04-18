package gosujavactest.extensions.java.lang.String;

import java.math.BigDecimal;
import manifold.ext.api.Extension;
import manifold.ext.api.This;

@Extension
public class MyStringExt
{
  public static BigDecimal bd( @This String thiz ) {
    return new BigDecimal( thiz );
  }
}
