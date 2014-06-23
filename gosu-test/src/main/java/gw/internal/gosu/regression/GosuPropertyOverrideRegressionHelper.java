/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.regression;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Jan 19, 2010
 * Time: 2:12:14 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class GosuPropertyOverrideRegressionHelper {

  // Is methods with no setter

  public abstract boolean isFooPBoolean();

  public abstract Boolean isFooBoolean();

  public abstract String isFooString();

  public abstract int isFooPInt();


  // Is methods with setters

  public abstract boolean isFooPBooleanWithSetter();

  public abstract Boolean isFooBooleanWithSetter();

  public abstract String isFooStringWithSetter();

  public abstract int isFooPIntWithSetter();

  public abstract void setFooPBooleanWithSetter(boolean value);

  public abstract void setFooBooleanWithSetter(Boolean value);

  public abstract void setFooStringWithSetter(String value);

  public abstract void setFooPIntWithSetter(int value);

  // Get methods without setters

  public abstract boolean getBarPBoolean();

  public abstract Boolean getBarBoolean();

  public abstract String getBarString();

  public abstract int getBarPInt();

  // Get methods with setters

  public abstract boolean getBarPBooleanWithSetter();

  public abstract Boolean getBarBooleanWithSetter();

  public abstract String getBarStringWithSetter();

  public abstract int getBarPIntWithSetter();

  public abstract void setBarPBooleanWithSetter(boolean value);

  public abstract void setBarBooleanWithSetter(Boolean value);

  public abstract void setBarStringWithSetter(String value);

  public abstract void setBarPIntWithSetter(int value);

  // TODO - AHK - Setters without getters?
  
}
