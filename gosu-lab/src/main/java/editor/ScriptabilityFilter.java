package editor;

import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.reflect.IType;
import gw.util.IFeatureFilter;

public class ScriptabilityFilter implements IFeatureFilter
{
  private IScriptabilityModifier _modifier;

  public ScriptabilityFilter( IScriptabilityModifier scriptabilityModifier )
  {
    _modifier = scriptabilityModifier;
  }

  public boolean acceptFeature( IType beanType, IFeatureInfo fi )
  {
    return !(fi instanceof IAttributedFeatureInfo) ||
           ((IAttributedFeatureInfo)fi).isVisible( _modifier );
  }
}