package gw.perf

uses gw.lang.reflect.TypeSystem
uses gw.lang.reflect.ITypeRef
uses org.junit.Ignore
uses org.junit.Test

class PerfTest {

  @Test
  @Ignore
  function testMe() {
    var stonesPrj =
      {"gw.perf.play.stones.Board",
       "gw.perf.play.stones.BoardFrame",
       "gw.perf.play.stones.BoardModel",
       "gw.perf.play.stones.ColorStoneRenderer",
       "gw.perf.play.stones.ImageStoneRenderer",
       "gw.perf.play.stones.IStoneRenderer",
       "gw.perf.play.stones.Main",
       "gw.perf.play.stones.RunGame",
       "gw.perf.play.stones.ScoreBoard",
       "gw.perf.play.stones.Stone",
       "gw.perf.play.stones.StoneColumn",
       "gw.perf.play.stones.StoneComponent",
       "gw.perf.play.stones.StoneDragHandler",
       "gw.perf.play.stones.StonesLayout",
       "gw.perf.play.stones.TimeBoard",
       "gw.perf.play.util.ModalEventQueue"}
     for( 0..100 ) {
       stonesPrj.each( \ name -> TypeSystem.getByFullName( name ).Valid )
       stonesPrj.each( \ name -> TypeSystem.refresh( TypeSystem.getByFullName( name ) as ITypeRef ) )
     }
  }

}