package gw.specContrib.enhancements.enhWithTypeParams

enhancement Errant_EnhMultipleExtends2<S, U, T extends S & MyInterface2> : Map<Map<Integer, T>, String> {      //## issuekeys: TYPE PARAMETER CANNOT BE FOLLOWED BY OTHER BOUNDS

}