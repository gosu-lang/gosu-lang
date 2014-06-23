/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.util.concurrent.LockingLazyVar;

import java.util.List;

public abstract class BaseHandBuiltTypeInfo extends BaseJavaTypeInfo implements IUnloadable, IRelativeTypeInfo, IExplicitTypeInfo
{
  private final FeatureManager _fm;
  private final LockingLazyVar<List<? extends IEventInfo>> _eventsCache;

  public BaseHandBuiltTypeInfo(Class javaClass) {
    super(javaClass);
    _fm = new FeatureManager(this, false);
    _eventsCache = new LockingLazyVar<List<? extends IEventInfo>>() {
      protected List<? extends IEventInfo> init() {
        return loadEvents();
      }
    };
  }

  protected abstract List<? extends IEventInfo> loadEvents();

  public List<? extends IPropertyInfo> getProperties()
  {
    return getProperties(null);
  }

  public IPropertyInfo getProperty( CharSequence property )
  {
    return getProperty(null, property);
  }

  public MethodList getMethods()
  {
    return getMethods(null);
  }

  public List<? extends IConstructorInfo> getConstructors()
  {
    return getConstructors(null);
  }

  public void unload() {
    _fm.clear();
    _eventsCache.clear();
  }

  public Accessibility getAccessibilityForType(IType whosaskin) {
    return FeatureManager.getAccessibilityForClass( getOwnersType(), whosaskin);
  }

  public IConstructorInfo getConstructor(IType whosAskin, IType[] params) {
    return _fm.getConstructor(getAccessibilityForType(whosAskin), params);
  }

  public List<? extends IConstructorInfo> getConstructors(IType whosaskin) {
    //noinspection unchecked
    return _fm.getConstructors(getAccessibilityForType(whosaskin));
  }

  public IMethodInfo getMethod(IType whosaskin, CharSequence methodName, IType... params) {
    return _fm.getMethod(getAccessibilityForType(whosaskin), methodName, params);
  }

  public MethodList getMethods(IType whosaskin) {
    //noinspection unchecked
    return _fm.getMethods(getAccessibilityForType(whosaskin));
  }

  public List<? extends IPropertyInfo> getProperties(IType whosaskin) {
    //noinspection unchecked
    return _fm.getProperties(getAccessibilityForType(whosaskin));
  }

  public IPropertyInfo getProperty(IType whosaskin, CharSequence propName) {
    return _fm.getProperty(getAccessibilityForType(whosaskin), propName);
  }

  public IEventInfo getEvent(CharSequence event) {
    for (IEventInfo eventInfo : getEvents()) {
      if (eventInfo.getName().equals(event)) {
        return eventInfo;
      }
    }
    return null;
  }

  public List<? extends IEventInfo> getEvents() {
    return _eventsCache.get();
  }
  
}
