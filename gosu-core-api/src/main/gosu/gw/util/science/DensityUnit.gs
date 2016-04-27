package gw.util.science
uses gw.util.Rational

final class DensityUnit extends AbstractQuotientUnit<MassUnit, VolumeUnit, Density, DensityUnit> {
  public static var BASE: DensityUnit = new( Kilogram, VolumeUnit.BASE )
  
  construct( massUnit: MassUnit, volumeUnit: VolumeUnit ) {
    super( massUnit, volumeUnit )
  }  

  property get MassUnit() : MassUnit {
    return LeftUnit 
  }
  property get VolumeUnit() : VolumeUnit {
    return RightUnit 
  }

  function multiply( t: VolumeUnit ) : MassUnit {
    return  MassUnit
  }
}
