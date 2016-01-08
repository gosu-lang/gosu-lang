package gw.specification.genericTypesAndMethods.varianceOfTypeParametersTest

uses gw.BaseVerifyErrantTest

class VarianceOfTypeParametersTest extends BaseVerifyErrantTest{
  function testErrant_VarianceOfTypeParametersTest() {
    processErrantType(Errant_VarianceOfTypeParametersTest)
  }

  function testErrant_DeclarationSiteVariance() {
    processErrantType(Errant_DeclarationSiteVariance)
  }

  function testErrant_DeclarationSiteVariance_InferVariance() {
    processErrantType(Errant_DeclarationSiteVariance_InferVariance)
  }

  function testErrant_Enh_In_In() {
    processErrantType(Errant_Enh_In_In)
  }

  function testErrant_Enh_In_Out() {
    processErrantType(Errant_Enh_In_Out)
  }

  function testErrant_Enh_Out_In() {
    processErrantType(Errant_Enh_Out_In)
  }

  function testErrant_Enh_Out_Out() {
    processErrantType(Errant_Enh_Out_Out)
  }

  function testErrant_GenericStructures() {
    processErrantType(Errant_GenericStructures)
  }

  function testErrant_StrictGenericInterfaces() {
    processErrantType(Errant_StrictGenericInterfaces)
  }

}


