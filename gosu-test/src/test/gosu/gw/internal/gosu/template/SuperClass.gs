package gw.internal.gosu.template

class SuperClass {

    public static var v : String = "static var"
    
    static property get p() : String {
        return "static property"
    }
    
    static function f() : String {
        return "static function"
    }
    
    static function a(x : String) : String {
        return "static function with arg " + x
    }
    
    private static function priv() : String {
        return "private!"
    }
    
    function nonStatic() : String {
        return "non-static!"
    }

}