/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.config.IService;
import gw.lang.reflect.IType;
import gw.lang.parser.exceptions.ParseIssue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public interface ICoercionManager extends IService
{
  /**
   * @param lhsType type to be coerced to
   * @param rhsType type to be coerced from
   *
   * @return true if a coercion exists from rhsType to lhsType, false otherwise
   */
  boolean canCoerce( IType lhsType, IType rhsType );

  /**
   * @param intrType    type to coerce to
   * @param value       the value to coerce (may not be null)
   *
   * @return the converted value
   */
  Object convertValue(Object value, IType intrType);

  /**
   * @param lhsType type to coerce to
   * @param rhsType type to coerce from
   *
   * @return true if the given coercion should generate a warning
   */
  boolean coercionRequiresWarningIfImplicit( IType lhsType, IType rhsType );

  /**
   * Verifies that the right hand type can be converted or coerced to the left hand type.  If bBiDirectional is true,
   * it will verify that either converts to another
   */
  IType verifyTypesComparable( IType lhsType, IType rhsType, boolean bBiDirectional ) throws ParseIssue;

  /**
   * Verifies that the right hand type can be converted or coerced to the left hand type.  If bBiDirectional is true,
   * it will verify that either converts to another.  The parser state will be used to report parse errors with.
   */
  IType verifyTypesComparable( IType lhsType, IType rhsType, boolean bBiDirectional, IFullParserState parserState ) throws ParseIssue;

  /**
   * Converts a null value to a value compatible with the specified primitive type.
   *
   * @param intrType The primitive type to convert to.
   *
   * @return A wrapped primitive value corresponding to null.
   */
  Object convertNullAsPrimitive( IType intrType, boolean isForBoxing );

  public ICoercer findCoercer( IType lhsType, IType rhsType, boolean runtime );
  
  /**
   * Determine and return a statically valid coercer from the rhsType to the lhsType.  Returns
   * null if no coercion is necessary.
   *
   * @param lhsType the type to coerce to
   * @param rhsType the type to coerce from
   */
  ICoercer resolveCoercerStatically( IType lhsType, IType rhsType );

  /**
   * Determine whether the specified type is either a primitive or a boxed primitive.
   * @param type the type to examine
   * @return true if the specified type is a primitive or a boxed primitive
   */
  public boolean isPrimitiveOrBoxed(IType type);

  /**
   * @return A Boolean for an arbitrary object.
   */
  public boolean makePrimitiveBooleanFrom( Object obj );

  public Boolean makeBooleanFrom( Object obj );

  public int makePrimitiveIntegerFrom( Object obj );

  public Integer makeIntegerFrom( Object obj );

  public long makePrimitiveLongFrom( Object obj );

  public Long makeLongFrom( Object obj );

  public float makePrimitiveFloatFrom( Object obj );

  public Float makeFloatFrom( Object obj );

  public double makePrimitiveDoubleFrom( Object obj );

  /**
   * Returns a Double for an arbitrary object. Uses a semi-intelligent
   * algorithm to create an appropriate Double instance.  If the Object
   * argument is a:
   * <pre>
   * null value - an appropriate value respecting this parsed element's nullAsZero
   *   setting e.g., either null or an 'empty' value.
   * Double - returns the argument as is.
   * Number - the Number's doubleValue().
   * String - Double.parseDouble( String )
   * Boolean - a pooled instance of either Double( 0 ) or Double( 1 )
   * Date - A Double for Date.getTime().
   * default - A parsed Double for the Object argument's toString() method.
   * </pre>
   *
   * @return A Double for an arbitrary object (may return a pooled instance).
   */
  public Double makeDoubleFrom( Object obj );

  public BigDecimal makeBigDecimalFrom( Object obj );

  public BigInteger makeBigIntegerFrom( Object obj );

  public String makeStringFrom( Object obj );

  /**
   * Returns a new Date instance representing the object.
   */
  public Date makeDateFrom( Object obj );

  /**
   * Produce a date from a string using standard DateFormat parsing.
   */
  public Date parseDateTime( String str ) throws java.text.ParseException;
  public boolean isDateTime( String str ) throws java.text.ParseException;

  public String formatDate( Date value, String strFormat );

  public String formatTime( Date value, String strFormat );

  public String formatNumber( Double value, String strFormat );

  public Number parseNumber( String strValue );

}
