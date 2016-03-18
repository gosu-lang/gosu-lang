package gw.util.science

uses java.math.BigDecimal
uses java.math.RoundingMode
uses java.math.MathContext

final class Volume extends AbstractMeasure<VolumeUnit, Volume> {
  /** 
   * @param value Volume in specified units
   * @param unit Volume unit, default is Meter³
   * @param displayUnit Unit in which to display this volume
   */
  construct( value : BigDecimal, unit: VolumeUnit, displayUnit: VolumeUnit ) {
    super( value, unit, displayUnit, VolumeUnit.BASE )
  }
  construct( value : BigDecimal, unit: VolumeUnit ) {
    this( value, unit, unit )
  }

  function divide( t: Length ) : Area {
    return new Area( toNumber() / t.toNumber(), new( Meter ), new( t.Unit ) )
  }
  
  function divide( area: Area ) : Length {
    return new Length( toNumber() / area.toNumber(), Meter, area.Unit.WidthUnit )
  }
  
  function multiply( density: Density ) : Mass {
    return new Mass( toNumber() * density.toNumber(), Kilogram, density.Unit.MassUnit )
  }
  
}
