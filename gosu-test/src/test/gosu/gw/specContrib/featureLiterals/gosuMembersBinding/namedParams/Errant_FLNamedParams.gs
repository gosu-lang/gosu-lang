package gw.specContrib.featureLiterals.gosuMembersBinding.namedParams

class Errant_FLNamedParams {
  var gInstance : Errant_FLNamedParams

  function namedParameter(aa: int, bb: int, cc: int = 3, dd: int = 4) {}

  var namedErrantFL111 = Errant_FLNamedParams#namedParameter(int, int, int, int)
  var namedErrantFL112 = Errant_FLNamedParams#namedParameter(1, 2, 3, 4)
  var namedErrantFL113 = Errant_FLNamedParams#namedParameter(:aa = 1, :bb = 2, :cc = 3, :dd = 4)
  var namedErrantFL114 = Errant_FLNamedParams#namedParameter(1, :bb = 2, :cc = 3, :dd = 4)
  var namedErrantFL115 = Errant_FLNamedParams#namedParameter(1, 2, :cc = 3, :dd = 4)

  var namedFL111 = gInstance#namedParameter(int, int, int, int)
  var namedFL112 = gInstance#namedParameter(1, 2, 3, 4)
  var namedFL113 = gInstance#namedParameter(:aa = 1, :bb = 2, :cc = 3, :dd = 4)
  var namedFL114 = gInstance#namedParameter(1, :bb = 2, :cc = 3, :dd = 4)
  var namedFL115 = gInstance#namedParameter(1, 2, :cc = 3, :dd = 4)

  //Check default values
  var namedFL116 = gInstance#namedParameter(1, 2, 3)
  var namedFL117 = gInstance#namedParameter(1, 2, 3)
  var namedFL118 = gInstance#namedParameter(int, int, 3, 4)      //## issuekeys: 'NAMEDPARAMETER(INT, INT, INT, INT)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.FEATURELITERALS.FEATURELITERALSMAIN.GOSUFEATURELITERALS.NAMEDPARAMS.ERRANT_FLNAMEDPARAMS' CANNOT BE APPLIED TO '(GW.LANG.__PSI__.METATYPE<INT>, GW.LANG.__PSI__.METATYPE<INT>, INT, INT)'

  function invoker() {
    namedFL111.invoke(1,2,3,4)
    //IDE-1611 - OS Gosu Issue
    namedFL111.invoke(:aa = 1,:bb = 2,:cc = 3,:dd = 4)
    //IDE-1611 - OS Gosu Issue
    namedFL111.invoke(:dd = 1,:cc = 2,:bb = 3,:aa = 4)
    namedFL111.invoke(1,2,3)      //## issuekeys: 'INVOKE(INT, INT, INT, INT)' IN '' CANNOT BE APPLIED TO '(INT, INT, INT)'

    namedFL112.invoke()

    namedFL113.invoke()
    namedFL113.invoke(:dd = 1)      //## issuekeys: PARAMETER 'DD' DOES NOT EXIST

    namedFL114.invoke()
    namedFL115.invoke()
    namedFL116.invoke()
    namedFL117.invoke()
  }

}