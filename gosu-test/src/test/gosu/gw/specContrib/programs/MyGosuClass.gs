package gw.specContrib.programs

class MyGosuClass {
  function run() : String {
    var res = MyProgram.execute() as String
    res += MyProgram.executeWithArgs( {"fu"} )
    return res
  }
}