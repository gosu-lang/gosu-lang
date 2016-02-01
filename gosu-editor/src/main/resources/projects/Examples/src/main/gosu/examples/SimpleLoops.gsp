//
// Gosu supports the standard loop variants: for, while, do ... while etc.
//

// simple iteration
var list = {"one", "two", "three"} // Creates a List<String>
for( num in list ) {
  print( num )
}
print( "---" )


// 'index' usage
var list2 = {"one", "two", "three"}
for( num in list2 index i ) {
  print( "${i} : ${num}" ) // i is an int, and num is still of type String
}
print( "---" )


// access to the 'iterator'
var list3 = {"one", "two", "three"}
for( num in list iterator iter ) {
  iter.remove() // direct access to iterator
}
print(list)
