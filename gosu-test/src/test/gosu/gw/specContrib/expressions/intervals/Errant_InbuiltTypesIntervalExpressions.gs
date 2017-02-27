package gw.specContrib.expressions.intervals

class Errant_InbuiltTypesIntervalExpressions {

  //CharSequence
  var charSeq1 : CharSequence
  var charSeq2 : CharSequence
  var charSeqInterval = charSeq1..charSeq2

  //Dimensions
  var dim1 : SampleDimension_Integer = new SampleDimension_Integer(new Integer(7))
  var dim2 : SampleDimension_Integer = new SampleDimension_Integer(new Integer(5))
  var dimensionInterval1 = dim1..dim2

  var dimensionInterval2 = dim1..false      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'GW.SPECCONTRIB.EXPRESSIONS.INTERVALS.SAMPLEDIMENSION_INTEGER', 'BOOLEAN'
  var dimensionInterval3 = dim1..new Boolean(false)      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'GW.SPECCONTRIB.EXPRESSIONS.INTERVALS.SAMPLEDIMENSION_INTEGER', 'JAVA.LANG.BOOLEAN'
  var dimensionInterval4 = false..dim1      //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'BOOLEAN', 'GW.SPECCONTRIB.EXPRESSIONS.INTERVALS.SAMPLEDIMENSION_INTEGER'

  //Arrays
  var array1 : int[] = new int[]{}
  var array2 : int[] = new int[]{}
  var arrayInterval = array1..array2                  //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'INT[]', 'INT[]'

  var boolInterval1 = new Boolean(false)..new Boolean(true)            //## issuekeys: OPERATOR '..' CANNOT BE APPLIED TO 'JAVA.LANG.BOOLEAN', 'JAVA.LANG.BOOLEAN'

  function bar() {
    for(i in charSeqInterval) {}      //## issuekeys: FOREACH IS NOT APPLICABLE TO TYPE 'GW.LANG.REFLECT.INTERVAL.COMPARABLEINTERVAL<JAVA.LANG.STRING>'
    for(i in dimensionInterval1) {}      //## issuekeys: FOREACH IS NOT APPLICABLE TO TYPE 'GW.LANG.REFLECT.INTERVAL.COMPARABLEINTERVAL<GW.SPECCONTRIB.EXPRESSIONS.INTERVALS.SAMPLEDIMENSION_INTEGER>'
    for(i in arrayInterval) {}      //## issuekeys: FOREACH IS NOT APPLICABLE TO TYPE 'GW.LANG.REFLECT.INTERVAL.COMPARABLEINTERVAL<INT[]>'
  }
}