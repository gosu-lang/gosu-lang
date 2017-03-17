/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

public interface IAttributedFeatureInfo extends IAnnotatedFeatureInfo
{
  /**
   * True if this feature is exposed for scripting.
   */
  public boolean isScriptable();

  /**
   * Determine if this feature is visible given a visibility constraint.<p>
   * <p/>
   * NOTE: implementers of this method are responsible for handling the null
   * case, taken to mean that there are not scriptability constraints.
   */
  public boolean isVisible( IScriptabilityModifier constraint );

  /**
   * True if this feature is hidden. Hidden feature should not to be exposed to
   * earthlings.
   */
  public boolean isHidden();

  /**
   * True if this feature is static, meaning the feature is not specific to a
   * particular instance of the feature's containing type.
   */
  public boolean isStatic();

  default boolean isReified()
  {
    return false;
  }

  /**
   * True if this feature is private. Private features are only accessible from
   * within the type that owns them.
   */
  public boolean isPrivate();

  /**
   * True if this feature is internal. Internal features are accessible from
   * within the type that owns them and from other types in the same namespace.
   */
  public boolean isInternal();

  /**
   * True if this feature is protected. Protected features are accessible from
   * within the type that owns them and from derived containers.
   */
  public boolean isProtected();

  /**
   * True if this feature is public. Public features are not restricted.
   */
  public boolean isPublic();

  public boolean isAbstract();

  public boolean isFinal();

  public static class MODIFIER
  {
    public static int get( IAttributedFeatureInfo afi )
    {
      int iMod = 0;
      iMod = Modifier.setInternal( iMod, afi.isInternal() );
      iMod = Modifier.setPrivate( iMod, afi.isPrivate() );
      iMod = Modifier.setPublic( iMod, afi.isPublic() );
      iMod = Modifier.setHide( iMod, afi.isHidden() );
      iMod = Modifier.setProtected( iMod, afi.isProtected() );
      iMod = Modifier.setClassMember( iMod, true );
      iMod = Modifier.setStatic( iMod, afi.isStatic() );
      iMod = Modifier.setAbstract( iMod, afi.isAbstract() );
      iMod = Modifier.setFinal( iMod, afi.isFinal() );

      return iMod;      
    }
  }


}
