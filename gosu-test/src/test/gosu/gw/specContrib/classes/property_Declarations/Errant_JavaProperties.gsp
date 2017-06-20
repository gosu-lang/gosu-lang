var mj = new MyJava()

mj.CamelCase = "CamelCase"
print( mj.CamelCase )
print( mj.getCamelCase() )
print( mj.isCamelCase() )  //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD
mj.setCamelCase( "CamelCase2" )


mj.Underscore = "underscore"
print( mj.Underscore )
print( mj.getUnderscore() )
print( mj.isUnderscore() )  //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD
mj.setUnderscore( "underscore2" )
print( mj.get_underscore() )
mj.set_underscore( "underscore3" )


mj.alllower = "alllower"  //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND, MSG_NO_PROPERTY_DESCRIPTOR_FOUND
print( mj.Alllower )  //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
print( mj.getAlllower() )  //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD
print( mj.isAlllower() )  //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD
mj.setAlllower( "alllower2" )  //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD, MSG_NO_SUCH_FUNCTION
print( mj.getalllower() )
mj.setalllower( "alllower3" )


print( mj.Writeonly )  //## issuekeys: MSG_CLASS_PROPERTY_NOT_READABLE
mj.Writeonly = "hot"
mj.setWriteonly( "hot2" )


print( mj.Readonly )
print( mj.getReadonly() )
mj.Readonly = "no"   //## issuekeys: MSG_CLASS_PROPERTY_NOT_WRITABLE


mj.Insane = true
print( mj.Insane )
print( mj.getInsane() )
print( mj.isInsane() )
mj.setInsane( true )


mj.Crazy = true
print( mj.Crazy )
print( mj.getCrazy() )
print( mj.isCrazy() )
mj.setCrazy( true )

