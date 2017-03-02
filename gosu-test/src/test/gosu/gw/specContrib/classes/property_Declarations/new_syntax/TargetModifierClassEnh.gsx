package gw.specContrib.classes.property_Declarations.new_syntax
uses gw.specContrib.classes.property_Declarations.new_syntax.abc.TargetModifierClass

enhancement TargetModifierClassEnh : TargetModifierClass
{
  @receiver:MyParamAnno( 1 )
  property get EnhProp(): String
  {
    return "hi"
  }
  @receiver:MyParamAnno( 1 )
  property set EnhProp( @MyParamAnno(2) v: String)
  {
  }
}