package gw.specification.types.typeConversion.conversionbetweenPrimitiveTypes

uses gw.BaseVerifyErrantTest

class ConversionBetweenPrimitiveTest extends BaseVerifyErrantTest {
  function testErrant_ConversionBetweenPrimitiveTest() {
    processErrantType(Errant_ConversionBetweenPrimitiveTest)
  }

  function testConversions() {
    var t : boolean = true
    var c : char = 'a'
    var b : byte = 1
    var s : short = 1
    var i : int = 1
    var l : long = 1
    var f : float = 1.0f
    var d : double = 1.0


    var v0 : boolean = t
    assertEquals(v0, t)
    var v1 : char = t as char
    assertEquals(v1, t as char)
    var v3 : byte = t as byte
    assertEquals(v3, t as byte)
    var v5 : short = t as short
    assertEquals(v5, t as short)
    var v7 : int = t as int
    assertEquals(v7, t as int)
    var v9 : long = t as long
    assertEquals(v9, t as long)
    var v11 : float = t as float
    assertEquals(v11, t as float, 0.01f)
    var v13 : double = t as double
    assertEquals(v13, t as double, 0.01)
    var v15 : boolean = c as boolean
    assertEquals(v15, c as boolean)
    var v17 : char = c
    assertEquals(v17, c)
    var v18 : byte = c as byte
    assertEquals(v18, c as byte)
    var v20 : short = c as short
    assertEquals(v20, c as short)
    var v22 : int = c
    assertEquals(v22, c)
    var v23 : long = c
    assertEquals(v23, c)
    var v24 : float = c
    assertEquals(v24, c, 0.01f)
    var v25 : double = c
    assertEquals(v25, c, 0.01)
    var v26 : boolean = b as boolean
    assertEquals(v26, b as boolean)
    var v28 : char = b as char
    assertEquals(v28, b as char)
    var v30 : byte = b
    assertEquals(v30, b)
    var v31 : short = b
    assertEquals(v31, b)
    var v32 : int = b
    assertEquals(v32, b)
    var v33 : long = b
    assertEquals(v33, b)
    var v34 : float = b
    assertEquals(v34, b, 0.01f)
    var v35 : double = b
    assertEquals(v35, b, 0.01)
    var v36 : boolean = s as boolean
    assertEquals(v36, s as boolean)
    var v38 : char = s as char
    assertEquals(v38, s as char)
    var v40 : byte = s as byte
    assertEquals(v40, s as byte)
    var v42 : short = s
    assertEquals(v42, s)
    var v43 : int = s
    assertEquals(v43, s)
    var v44 : long = s
    assertEquals(v44, s)
    var v45 : float = s
    assertEquals(v45, s, 0.01f)
    var v46 : double = s
    assertEquals(v46, s, 0.01)
    var v47 : boolean = i as boolean
    assertEquals(v47, i as boolean)
    var v49 : char = i as char
    assertEquals(v49, i as char)
    var v51 : byte = i as byte
    assertEquals(v51, i as byte)
    var v53 : short = i as short
    assertEquals(v53, i as short)
    var v55 : int = i
    assertEquals(v55, i)
    var v56 : long = i
    assertEquals(v56, i)
    var v57 : float = 1000111222
    assertEquals(v57, 1.00011123E9f, 0.01f )  // precision loss
    var v58 : double = 1000111222
    assertEquals(v58, 1000111222, 0.01)
    var v59 : boolean = l as boolean
    assertEquals(v59, l as boolean)
    var v61 : char = l as char
    assertEquals(v61, l as char)
    var v63 : byte = l as byte
    assertEquals(v63, l as byte)
    var v65 : short = l as short
    assertEquals(v65, l as short)
    var v67 : int = l as int
    assertEquals(v67, l as int)
    var v69 : long = l
    assertEquals(v69, l)
    var v70 : float = 1000111222L
    assertEquals(v70, 1.00011123E9f, 0.01f )  // precision loss
    var v71 : double = 1000000000000111222L
    assertEquals(v71, 1.00000000000011123E18, 0.01 )  // precision loss
    var v72 : boolean = f as boolean
    assertEquals(v72, f as boolean)
    var v74 : char = f as char
    assertEquals(v74, f as char)
    var v76 : byte = f as byte
    assertEquals(v76, f as byte)
    var v78 : short = f as short
    assertEquals(v78, f as short)
    var v80 : int = f as int
    assertEquals(v80, f as int)
    var v82 : long = f as long
    assertEquals(v82, f as long)
    var v84 : float = f
    assertEquals(v84, f, 0.01f)
    var v85 : double = f
    assertEquals(v85, f, 0.01)
    var v86 : boolean = d as boolean
    assertEquals(v86, d as boolean)
    var v88 : char = d as char
    assertEquals(v88, d as char)
    var v90 : byte = d as byte
    assertEquals(v90, d as byte)
    var v92 : short = d as short
    assertEquals(v92, d as short)
    var v94 : int = d as int
    assertEquals(v94, d as int)
    var v96 : long = d as long
    assertEquals(v96, d as long)
    var v98 : float = d as float
    assertEquals(v98, d as float, 0.01f)
    var v100 : double = d
    assertEquals(v100, d, 0.01)
 }
}