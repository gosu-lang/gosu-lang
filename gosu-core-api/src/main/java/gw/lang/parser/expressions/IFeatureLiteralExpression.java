/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IType;

import java.util.List;

public interface IFeatureLiteralExpression extends ILiteralExpression, Cloneable
{
  /**
   * @return the feature associated with this literal
   */
  IFeatureInfo getFeature();

  /**
   * @return the root type of the entire literal chain (if one exists)
   */
  public IType getFinalRootType();

  /**
   * @return the type that the feature that this literal refers to is on
   */
  IType getRootType();

  /**
   * @return bound args of this feature, or null if there are no bound args
   */
  List<IExpression> getBoundArgs();

  /**
   * @return true if this literal is bound at its root
   */
  boolean isBound();

  /**
   * @return the final root expression of the feature literal
   */
  public IExpression getFinalRoot();

 }
