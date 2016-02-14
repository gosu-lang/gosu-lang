//
// Gosu offers a few helpful tricks to deal with null in your code, 
// such as null-safe property access and method calls
//

// The '?.' operator does null checking for us
var name = getAPossiblyNullString()
var subStr = name?.substring( 2 )
print( subStr )


// Sometimes you want a default value if an expression is null. Gosu supports the "Elvis" operator '?:'
var myString = getAPossiblyNullString() ?: "default"
print( myString )

function getAPossiblyNullString() : String {
  return null
}