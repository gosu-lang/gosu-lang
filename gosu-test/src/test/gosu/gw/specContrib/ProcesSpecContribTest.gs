package gw.specContrib

uses gw.BaseVerifyErrantTest

class ProcesSpecContribTest extends BaseVerifyErrantTest {

    function testThemAll() {
      /*
        https://jira.guidewire.com/browse/PL-31650
      processErrantType(gw.specContrib.typeinference.SwitchTypeNarrowing)
      */

      processErrantType(gw.specContrib.typeinference.Errant_SwitchTypeNarrowing)
      processErrantType(gw.specContrib.typeinference.IfTypeNarrowing)
      processErrantType(gw.specContrib.statements.Errant_DuplicateCaseInSwitch)
      processErrantType(gw.specContrib.SubType1)
      processErrantType(gw.specContrib.generics.Errant_GenericMethodBounds)
      processErrantType(gw.specContrib.operators.Errant_GreaterThanOperator)
      processErrantType(gw.specContrib.operators.Errant_OperatorCombinations)
      processErrantType(gw.specContrib.operators.ShiftAssignmentOperatorTest)
      processErrantType(gw.specContrib.expressions.Errant_BitwiseOperations)
      processErrantType(gw.specContrib.expressions.Errant_MapKeyValueInitializers)
      processErrantType(gw.specContrib.types.Errant_VoidType)
      processErrantType(gw.specContrib.types.Errant_CompoundType)
      processErrantType(gw.specContrib.classes.property_Declarations.Errant_PropertyWithInconsistentModifiers)
      processErrantType(gw.specContrib.classes.property_Declarations.Errant_PropertyWithInconsistentTypes)
      processErrantType(gw.specContrib.classes.modifiers.Errant_ModifiersOnInterfaceMethod)
      processErrantType(gw.specContrib.annotations.Errant_AnnotationTargets1)
      processErrantType(gw.specContrib.annotations.Errant_AnnotationTargets2)
      //processErrantType(gw.specContrib.classes.Errant_ClassDeclaredInEnhancement)
      //processErrantType(gw.specContrib.classes.Errant_ClassNotNamedAfterFile)
      //processErrantType(gw.specContrib.classes.Errant_EnhancementDeclaredInClass)
      //processErrantType(gw.specContrib.classes.Errant_EnhancementNotNamedAfterFile)
      processErrantType(gw.specContrib.scopes.Errant_PropertyDeclaration)
      processErrantType(gw.specContrib.delegates.Errant_DelegateCompoundType)
      processErrantType(gw.specContrib.delegates.Errant_SelfReferencingDelegateType)
      processErrantType(gw.specContrib.operators.Errant_OperatorsPrimitiveAndObject)

  }

}