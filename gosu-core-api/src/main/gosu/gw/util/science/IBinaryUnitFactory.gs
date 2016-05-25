package gw.util.science
uses gw.util.Rational

structure IBinaryUnitFactory {
  function get<A extends IUnit<Rational, IDimension, A>,
               B extends IUnit<Rational, IDimension, B>,
               D extends AbstractMeasure<U, D>,
               U extends AbstractBinaryUnit<A, B, D, U>>( lefUnit: A, rigthUnit: B, factor: Rational = null, name: String = null, symbol: String = null ) : U
}