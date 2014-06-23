package gw.internal.gosu.compiler.sample.statement.classes

class ExtendsAbstractClassStaticVars extends AbstractClassStaticVars
{
  function getStaticVar() : String {
    return STRING_VAR
  }

  function getStaticVarWithStringConcatenation() : String {
    return "test" + STRING_VAR
  }

  function getStaticVarWithStringConcatenationWProp() : String {
    return StringProp + STRING_VAR
  }
}