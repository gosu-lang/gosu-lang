package gw.data

class TestGosuClass {
    static final var VALUE: String = "cause" + " I " + "want to"

    @gw.lang.Deprecated("cause I want to")
    function test1() {}

    @gw.lang.Deprecated("cause" + " I " + "want to")
    function test2() {}

    @gw.lang.Deprecated(VALUE)
    function test3() {}

    @gw.lang.Deprecated(VALUE + " really bad")
    function test4() {}

    @gw.lang.Deprecated(JavaClass.VALUE)
    function test5() {}

    @JavaIntAnnotation(1+2)
    function test6() {}

    @JavaEnumAnnotation(JavaEnum.TWO)
    function test7() {}
}