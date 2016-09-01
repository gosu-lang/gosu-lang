package gw.specContrib.classes.property_Declarations.new_syntax.abc

class TargetModifierClass {
  @field:public
  property PublicField: int
  
  @get:protected
  property ProtectedGet: int
  
  @set:protected
  property ProtectedSet: int
  
  @accessors:protected
  property ProtectedAccessors: int
}