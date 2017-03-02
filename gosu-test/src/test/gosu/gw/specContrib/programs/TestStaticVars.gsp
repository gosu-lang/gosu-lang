static var staticVar: String = "8"

function foo() : String {
  return staticVar
}

return staticVar + foo()