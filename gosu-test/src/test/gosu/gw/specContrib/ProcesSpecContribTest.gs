package gw.specContrib

uses gw.BaseVerifyErrantTest


class ProcesSpecContribTest extends BaseVerifyErrantTest {
    function testThemAll() {
      processErrantType(gw.specContrib.typeinference.SwitchTypeNarrowing)
      processErrantType(gw.specContrib.typeinference.Errant_SwitchTypeNarrowing)
      processErrantType(gw.specContrib.typeinference.IfTypeNarrowing)
      processErrantType(gw.specContrib.statements.Errant_DuplicateCaseInSwitch)
      processErrantType(gw.specContrib.SubType1)
      processErrantType(gw.specContrib.generics.Errant_GenericMethodBounds)
      processErrantType(gw.specContrib.operators.Errant_GreaterThanOperator)
      processErrantType(gw.specContrib.operators.Errant_OperatorCombinations)
      processErrantType(gw.specContrib.operators.ShiftAssignmentOperatorTest)
      processErrantType(gw.specContrib.expressions.Errant_MapKeyValueInitializers)
      processErrantType(gw.specContrib.types.Errant_VoidType)
      processErrantType(gw.specContrib.classes.property_Declarations.Errant_PropertyWithInconsistentModifiers)
      processErrantType(gw.specContrib.classes.property_Declarations.Errant_PropertyWithInconsistentTypes)
      processErrantType(gw.specContrib.classes.modifiers.Errant_ModifiersOnInterfaceMethod)
      processErrantType(gw.specContrib.classes.method_Scoring.Errant_PrimitiveMethodScoring)
      processErrantType(gw.specContrib.classes.method_Scoring.Errant_CollectionMethodScoring)
      processErrantType(gw.specContrib.annotations.Errant_AnnotationTargets1)
      processErrantType(gw.specContrib.annotations.Errant_AnnotationTargets2)

  }
}