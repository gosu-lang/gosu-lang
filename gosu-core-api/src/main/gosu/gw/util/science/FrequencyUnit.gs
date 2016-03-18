package gw.util.science

uses java.math.BigDecimal

final class FrequencyUnit extends AbstractQuotientUnit<AngleUnit, TimeUnit, Frequency, FrequencyUnit> {
  public static var BASE: FrequencyUnit = new( Radian, Second )
  public static var Hertz: FrequencyUnit = new( Turn, Second )
  public static var RPM: FrequencyUnit = new( Turn, Minute )

  construct( angleUnit: AngleUnit, timeUnit: TimeUnit ) {
    super( angleUnit, timeUnit )
  }
  
  property get AngleUnit() : AngleUnit {
    return LeftUnit 
  }
  property get TimeUnit() : TimeUnit {
    return RightUnit 
  } 
}
