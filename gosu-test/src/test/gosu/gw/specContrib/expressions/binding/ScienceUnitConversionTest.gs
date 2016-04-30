package gw.specContrib.expressions.binding

uses gw.test.TestClass
uses gw.util.science.*
uses gw.util.science.UnitConstants#*
uses java.math.BigDecimal
uses gw.lang.reflect.TypeSystem
uses gw.lang.reflect.INamespaceType
uses gw.lang.reflect.gs.IGosuClass

class ScienceUnitConversionTest extends TestClass { 
  static final var SAMPLE_DENSITY: int = 10
  
  function testUnitConversions() {
    var namespace = TypeSystem.getNamespace( "gw.util.science" )
    var typeNames = namespace.getChildren( namespace )
    typeNames.where( \ tn -> tn.kind == TYPE )
             .map( \ tn -> TypeSystem.getByFullName( tn.name ) )
             .where( \ type -> type.Final && IUnit.Type.isAssignableFrom( type ) )
             .each( \ unitType -> _testUnitConversions( unitType as Type<IUnit> ) )
  }
  
  private function _testUnitConversions( type: Type<IUnit> ) {
    print( type.Type )
    var allUnits = getAllUnitsOfType( type.Type )
    for( unit in allUnits ) {
      var measure = unit.postfixBind( 1 ) as AbstractMeasure<IUnit, AbstractMeasure<IUnit, AbstractMeasure>>
      for( unit2 in allUnits ) {
        print( unit.UnitName + " to " + unit2.UnitName )
        assertEquals( measure, measure.to( unit2 ).to( unit ) ) 
      }
    }      
  }
 
  @SuppressWarnings( "all" ) 
  private function getAllUnitsOfType( unitType: Type<IUnit> ) : Collection<IUnit> {
    if( unitType.Type.Enum ) {
      var enumUnitType: Dynamic = unitType.Type
      return enumUnitType.AllValues
    }
    else if( AbstractBinaryUnit.Type.isAssignableFrom( unitType.Type ) ) {
      var leftUnitType = unitType.Type.Supertype.TypeParameters[0] as Type<IUnit>
      var rightUnitType = unitType.Type.Supertype.TypeParameters[1] as Type<IUnit>
      var prod = AbstractProductUnit.Type.isAssignableFrom( unitType )
      var allUnits = new ArrayList<IUnit>()
      for( lut in getAllUnitsOfType( leftUnitType ) ) {
        for( rut in getAllUnitsOfType( rightUnitType ) ) {
          try {
            if( prod ) {
              var prodUnit = (lut as Dynamic).multiply( rut )
              allUnits.add( prodUnit )
            }
            else {
              var quotUnit = (lut as Dynamic).divide( rut )
              allUnits.add( quotUnit )
            }
          }
          catch( e: Exception ) {
            throw new RuntimeException( "Error calling: " + (typeof lut).RelativeName + "#" + (prod ? "multiply( " : "divide( ") + (typeof rut).RelativeName + " )", e )
          }
        }
      }
      makeSample( allUnits )
      return allUnits
    }
    else {
      throw new IllegalStateException( "Unexpected Unit Type: " + unitType.Type.Name )
    }
  }
  
  private function makeSample( allUnits: List<IUnit> ) {
    var rand = new Random()
    while( allUnits.size() > SAMPLE_DENSITY ) {
      var i = rand.nextInt( allUnits.size() )
      allUnits.remove( i )
    }
  }
}