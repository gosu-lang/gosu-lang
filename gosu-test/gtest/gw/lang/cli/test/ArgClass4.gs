package gw.lang.cli.test

uses gw.lang.cli.*

abstract class ArgClass4
{
  @ShortName("t")
  @LongName("task")
  @Required
  public static var _taskClassName : String as TaskClassName

  @ShortName("p")
  @LongName("properties")
  @Required
  static var _propertyFilePath : String as PropertyFileName
}