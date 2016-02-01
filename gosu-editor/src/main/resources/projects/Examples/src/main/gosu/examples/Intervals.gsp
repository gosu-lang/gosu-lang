//
// Interval expressions using the '..' operator express an interval or range of values
// between two endpoints.
//

// Interval from 0 through 5
for( i in 0..5 ) {
  print( i ) // Prints 0-5
}
print("---")


// Interval from 0 to 5 (exclusive)
for( i in 0..|5 ) {
  print( i ) // Prints 0-4
}
print("---")


// Interval from 0 (exclusive) to 5 (exclusive)
for( i in 0|..|5 ) {
  print( i ) // Prints 1-4
}
print("---")


// Date interval over two week periods
var now = new Date()
var later = now.addWeeks( 12 )
for( date in (now..later).unit( WEEKS ).step( 2 ) ) {
  print( date )
}  
print("---")


// Interval expression
var from = 0
var to = 5
var interval = from..to // intervals are expressions
for( i in interval ) {
  print( i )
}
