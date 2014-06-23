package gw.internal.gosu.parser.composition

uses java.lang.Class

interface IGenMethod
{
  function getRemoteObject<T>(arg : Class<T>) : T
}
