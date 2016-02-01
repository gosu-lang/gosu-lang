package match3

uses javax.swing.JPanel
uses javax.swing.BorderFactory
uses java.awt.Color
uses javax.swing.Timer
uses java.awt.event.ActionListener
uses java.awt.event.ActionEvent
uses java.awt.EventQueue
uses java.awt.image.BufferedImage
uses javax.swing.JLabel
uses java.awt.BorderLayout
uses javax.swing.JComponent
uses java.awt.geom.AffineTransform
uses java.awt.image.AffineTransformOp
uses match3.util.ModalEventQueue

/**
 */
class Board extends JPanel {
  var _model: BoardModel as Model
  var _renderers: List<IStoneRenderer>
  var _dragger: StoneDragHandler
  var _layout: StonesLayout


  construct() {
    initRenderers()
    _model = new BoardModel()
    _model.reset()
    setupLayout()
    _model.addLevelChangeListener( \-> levelChanged() )
    _model.addLevelTimeChangeListener( \-> timeChanged() )
  }

  private function timeChanged() {
    if( Model.LevelTime <= 0 ) {
      if( Parent == null ) {
        return
      }
      new MatchAnimator( Model.Stones.flatMap( \ elt -> elt ).toSet() ).start()
      var p = Parent as JComponent
      p.remove( this )
      p.add( new JLabel( "Time's Up", JLabel.CENTER ), BorderLayout.CENTER )
      invalidate()
      p.revalidate()
    }
  }

  private function levelChanged() {
    new MatchAnimator( Model.Stones.flatMap( \ elt -> elt ).toSet() ).start()
    Model.resetStones()
    for( c in Components ) {
      var stoneComp = c as StoneComponent
      stoneComp.Stone = Model.Stones[stoneComp.Stone.Row][stoneComp.Stone.Column]
    }
    new ReverseMatchAnimator( Model.Stones.flatMap( \ elt -> elt ).toSet() ).start()
    revalidate()
    repaint()
  }

  private function initRenderers() {
    _renderers = new ArrayList<IStoneRenderer>()

//    _renderers.add( new ColorStoneRenderer( new Color( 128, 0, 0 ) ) )
//    _renderers.add( new ColorStoneRenderer( new Color( 128, 128, 0 ) ) )
//    _renderers.add( new ColorStoneRenderer( new Color( 128, 0, 128 ) ) )
//    _renderers.add( new ColorStoneRenderer( new Color( 128, 128, 128 ) ) )
//    _renderers.add( new ColorStoneRenderer( new Color( 0, 128, 0 ) ) )
//    _renderers.add( new ColorStoneRenderer( new Color( 0, 0, 128 ) ) )

//    _renderers.add( new ImageStoneRenderer( "match3/images/1.png" ) )
//    _renderers.add( new ImageStoneRenderer( "match3/images/2.png" ) )
//    _renderers.add( new ImageStoneRenderer( "match3/images/3.png" ) )
//    _renderers.add( new ImageStoneRenderer( "match3/images/4.png" ) )
//    _renderers.add( new ImageStoneRenderer( "match3/images/5.png" ) )
//    _renderers.add( new ImageStoneRenderer( "match3/images/6.png" ) )

    _renderers.add( new ImageStoneRenderer( "match3/images/a.png" ) )
    _renderers.add( new ImageStoneRenderer( "match3/images/b.png" ) )
    _renderers.add( new ImageStoneRenderer( "match3/images/c.png" ) )
    _renderers.add( new ImageStoneRenderer( "match3/images/d.png" ) )
    _renderers.add( new ImageStoneRenderer( "match3/images/e.png" ) )
    _renderers.add( new ImageStoneRenderer( "match3/images/f.png" ) )

  }
  
  function getRenderer( stone: Stone ) : IStoneRenderer {
    return _renderers[stone.StoneType]
  }

  private function setupLayout() {
    Border = BorderFactory.createCompoundBorder(
      BorderFactory.createMatteBorder( 1, 0, 1, 0, new Color( 0, 0, 0, 128 ) ),
      BorderFactory.createCompoundBorder( 
        BorderFactory.createMatteBorder( 2, 0, 2, 0, new Color( 255, 255, 255, 128 ) ),
        BorderFactory.createMatteBorder( 1, 0, 1, 0, new Color( 0, 0, 0, 128 ) ) ) )
    _layout = new StonesLayout()
    Layout = _layout
    _dragger = new StoneDragHandler( this )
    for( row in _model.Stones ) {
      for( stone in row ) {
        createStoneComp( stone )
      }
    }
  }

  private function createStoneComp( stone: Stone ) : StoneComponent {
    var comp = new StoneComponent( stone, this )
    add( comp )
    comp.addMouseListener( _dragger )
    return comp
  }

  internal function horizontalDelta( comp: StoneComponent, iDelta: int ) {
    if( iDelta < 0 ) {
      if( comp.Stone.Column > 0 ) {
        // Swap to the left
        var right = comp.Stone
        var rComp = findComp( right )
        var left = _model.Stones[right.Row][right.Column-1]
        var lComp = findComp( left )
        animateHorzSwap( lComp, rComp )
        swap( lComp, rComp )
        if( !_model.TestMode ) {
          if( !processMatches( left, right ) ) {
            animateHorzSwap( lComp, rComp )
            swap( lComp, rComp )
          }
        }
      }
    }
    else {
      if( comp.Stone.Column < 7 ) {
        // Swap to the right
        var left = comp.Stone
        var lComp = findComp( left )
        var right = _model.Stones[left.Row][left.Column+1]
        var rComp = findComp( right )
        animateHorzSwap( lComp, rComp )
        swap( lComp, rComp )
        if( !_model.TestMode ) {
          if( !processMatches( left, right ) ) {
            animateHorzSwap( lComp, rComp )
            swap( lComp, rComp )
          }
        }
      }
    }
    revalidate()
  }

  internal function verticalDelta( comp: StoneComponent, iDelta: int ) {
    if( iDelta < 0 ) {
      if( comp.Stone.Row > 0 ) {
        // Swap to the top
        var bottom = comp.Stone
        var bComp = findComp( bottom )
        var top = _model.Stones[bottom.Row-1][bottom.Column]
        var tComp = findComp( top )
        animateVertSwap( tComp, bComp )
        swap( tComp, bComp )
        if( !_model.TestMode ) {
          if( !processMatches( top, bottom ) ) {
            animateVertSwap( tComp, bComp )
            swap( tComp, bComp )
          }
        }
      }
    }
    else {
      if( comp.Stone.Row < 7 ) {
        // Swap to the bottom
        var top = comp.Stone
        var tComp = findComp( top )
        var bottom = _model.Stones[top.Row+1][top.Column]
        var bComp = findComp( bottom )
        animateVertSwap( tComp, bComp )
        swap( tComp, bComp )
        if( !_model.TestMode ) {
          if( !processMatches( top, bottom ) ) {
            animateVertSwap( tComp, bComp )
            swap( tComp, bComp )
          }
        }
      }
    }
    revalidate()
  }
  
  function findComp( stone: Stone ) : StoneComponent {
    for( c in Components ) {
      var stoneComp = c as StoneComponent
      if( stoneComp.Stone == stone ) {
        return stoneComp
      }
    }
    return null
  }
  
  private function processMatches( stone1: Stone, stone2: Stone ) : boolean {
    var matches = _model.findMatches( stone1 )
    matches.addAll( _model.findMatches( stone2 ) )
    if( !matches.Empty ) {
      do {
        handleMatches( matches )
        matches = _model.findMatches()
      } while( !matches.Empty )
      if( Model.LevelTime >= BoardModel.MAX_TIME ) {
        EventQueue.invokeLater( \-> Model.advanceLevel() )
      }
      return true
    }
    return false
  }

  function handleMatches( allMatches: Set<Stone> ) {
    Layout = null
    for( grouping in groupAllMatches( allMatches ) ) {
      var animators = new ArrayList<DropAnimator>()
      new MatchAnimator( grouping ).start()
      grouping.each( \ elt -> remove( findStoneComp( elt ) ) )
      var columns = _model.removeMatchesAndReturnColumnsToFall( grouping )
      for( stoneCol in columns index i ) {
        var col = stoneCol.AboveStones
        var compCol = new StoneComponent[col.size()]
        var lastStone : StoneComponent
        var iNewCount = 0
        for( j in col.size()-1..0 ) {
          var stone = col[j]
          if( stone.New ) {
            iNewCount++
            compCol[j] = createStoneComp( stone )
            add( compCol[j] )
            compCol[j].setBounds( _layout.getColumnPos( this, stoneCol.Column ),
                                  _layout.getRowPos( this, -iNewCount ),
                                  getComponent( 2 ).Width,
                                  getComponent( 2 ).Height )
            stone.New = false
          }
          else {
            compCol[j] = findStoneComp( stone )
          }
          lastStone = compCol[j]
        }
        animators.add( new DropAnimator( compCol, iNewCount ) )
      }
      new GroupDropAnimator( animators ).start()
      Model.addToScore( grouping )
    }
    Layout = _layout
  }

  private function groupAllMatches(allMatches: Set <Stone> ) : List<Set<Stone>> {
    var sorted = new ArrayList<Set<Stone>>()
    
    while( !allMatches.Empty ) {
      var grouping = new HashSet<Stone>()
      addAdjacent( allMatches.first(), allMatches, grouping )
      sorted.add( grouping )
      allMatches.removeAll( grouping )
    }
    return sorted
  }
  
  private function addAdjacent( stone: Stone, allMatches: Set<Stone>, grouping: Set<Stone> )  {
    if( grouping.contains( stone ) ) {
      return
    }
    grouping.add( stone )
    for( csr in allMatches ) {
      if( (csr.Row == stone.Row &&
           (csr.Column == stone.Column-1 ||
            csr.Column == stone.Column+1 )) ||
          (csr.Column == stone.Column && 
           (csr.Row == stone.Row-1 ||
            csr.Row == stone.Row+1)) ) {
        addAdjacent( csr, allMatches, grouping )
      }
    }
  }

  private function findStoneComp( stone: Stone ) : StoneComponent {
    return Components.firstWhere( \ elt -> elt typeis StoneComponent && elt.Stone == stone ) as StoneComponent
  }

  private function animateHorzSwap( left: StoneComponent, right: StoneComponent ) {
    doWithNullLayout( \-> new SwapAnimator( HORZ, left, right ).start() )
  }

  private function animateVertSwap( top: StoneComponent, bottom: StoneComponent ) {
    doWithNullLayout( \-> new SwapAnimator( VERT, top, bottom ).start() )
  }

  private function doWithNullLayout( task() ) {
    var saveLayout = Layout
    Layout = null
    try {
      task()
    }
    finally {
      Layout = saveLayout
      doLayout()
    }
  }
  
  private function swap( lComp: StoneComponent, rComp: StoneComponent ) {
    var temp = lComp.Stone
    lComp.Stone = rComp.Stone
    rComp.Stone = temp
    _model.swap( lComp.Stone, rComp.Stone )
  }

  function repaintViaBuffer() {
    using( var g = Graphics ) {
      var bi = new BufferedImage( Width, Height, BufferedImage.TYPE_INT_ARGB )
      using( var buf = bi.createGraphics() ) {
        paint( buf )
      }
      g.drawImage( bi, 0, 0, null )
    }
  }
  
  class SwapAnimator implements ActionListener {
    enum Orientation { HORZ, VERT }
    var _orientation: Orientation
    var _comp1: StoneComponent
    var _comp2: StoneComponent
    var _timer: Timer
    var _iComp1Pos : int
    
    construct( orientation: Orientation, comp1: StoneComponent, comp2: StoneComponent ) {
      _orientation = orientation
      _comp1 = comp1
      _comp2 = comp2
      _timer = new Timer( 3, this )
      _iComp1Pos = orientation == HORZ ? _comp1.X : _comp1.Y
    }
    
    function start() {
      _timer.start()
      new ModalEventQueue( \-> _timer.Running, :disableMouse=true ).run()
    }
    
    override function actionPerformed( e: ActionEvent ) {
      if( _orientation == HORZ ) {
        if( _comp2.X > _iComp1Pos ) {
          _comp1.setLocation( _comp1.X+1, _comp1.Y )
          _comp2.setLocation( _comp2.X-1, _comp2.Y )
          repaintViaBuffer()
        }
        else {
          _timer.stop()
        }
      }
      else {
        if( _comp2.Y > _iComp1Pos ) {
          _comp1.setLocation( _comp1.X, _comp1.Y+1 )
          _comp2.setLocation( _comp2.X, _comp2.Y-1 )
          repaintViaBuffer()
        }
        else {
          _timer.stop()
        }
      }
    }
  }

  class MatchAnimator_Spin implements ActionListener {
    var _comps: List<StoneComponent>
    var _timer: Timer
    var _angle: double

    construct( stones: Set<Stone> ) {
      _comps = stones.map( \ stone -> findComp( stone ) )
      _timer = new Timer( 4, this )
      _timer.Repeats = true
      _angle = 1
    }

    function start() {
      _timer.start()
      new ModalEventQueue( \-> _angle < 2*3.14, :disableMouse=true )
              .run()
      _timer.stop()
    }

    override function actionPerformed( e: ActionEvent ) {
      _angle += 3.14/64
      for( c in _comps ) {
        spin( c )
      }
    }

    private function spin( c: JComponent ) {
      var cos = Math.cos( _angle )
      using( var g = c.Graphics ) {
        var bi = new BufferedImage( c.Width, c.Height, BufferedImage.TYPE_INT_ARGB )
        using( var gbi = bi.createGraphics() ) {
          // spin
          gbi.Color = c.Background
          gbi.fillRect( 0, 0, c.Width, c.Height )
          c.paint( gbi )
          var tx = AffineTransform.getScaleInstance( 1, cos >= 0 ? 1: -1 )
          if( cos < 0 ) {
            tx.translate( 0, -bi.getHeight( null ) )
          }
          tx.scale( 1, Math.abs( cos ) )
          var op = new AffineTransformOp( tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR )
          bi = op.filter( bi, null )
        }
        var bi2 = new BufferedImage( c.Width, c.Height, BufferedImage.TYPE_INT_ARGB )
        using( var gbi = bi2.createGraphics() ) {
          // fill background and blt spin bi
          gbi.Color = c.Background
          gbi.fillRect( 0, 0, c.Width, c.Height )
          if( cos >= 0 ) {
            gbi.drawImage( bi, 0, ((1d - cos) * c.Height) as int/2, null )
          }
          else {
            gbi.drawImage( bi, 0, ((1d - cos) * c.Height) as int/2 - c.Height, null )
          }
        }
        g.drawImage( bi2, 0, 0, null )
      }
    }
  }

  class MatchAnimator implements ActionListener {
    var _comps: List<StoneComponent>
    var _timer: Timer

    construct( stones: Set<Stone> ) {
      _comps = stones.map( \ stone -> findComp( stone ) )
      _timer = new Timer( 10, this )
      _timer.Repeats = true
    }

    function start() {
      _timer.start()
      new ModalEventQueue( \->
              _comps.allMatch( \ c -> c.Width > 0 ), :disableMouse=true )
              .run()
      _timer.stop()
    }

    override function actionPerformed( e: ActionEvent ) {
      for( c in _comps ) {
        c.setBounds( c.X+1, c.Y+1, c.Width-2, c.Height-2 )
      }
      repaintViaBuffer()
    }
  }

  class ReverseMatchAnimator implements ActionListener {
    var _comps: List<StoneComponent>
    var _timer: Timer

    construct( stones: Set<Stone> ) {
      _comps = stones.map( \ stone -> findComp( stone ) )
      _timer = new Timer( 10, this )
      _timer.Repeats = true
    }

    function start() {
      _timer.start()
      new ModalEventQueue( \-> _comps.allMatch( \ c -> c.Width < _layout.getWidth( outer ) ),
                           :disableMouse=true ).run()
      _timer.stop()
    }

    override function actionPerformed( e: ActionEvent ) {
      for( c in _comps ) {
        c.setBounds( c.X-1, c.Y-1, c.Width+2, c.Height+2 )
      }
      repaintViaBuffer()
    }
  }

  class GroupDropAnimator implements ActionListener {
    var _timer: Timer
    var _animators: List<DropAnimator>

    construct( animators: List<DropAnimator> ) {
      _timer = new Timer( 5, this )
      _timer.Repeats = true
      _animators = animators
      for( anim in _animators ) {
        anim.Owner = this
      }
    }

    function start() {
      _timer.start()
      new ModalEventQueue( \-> _animators.HasElements, :disableMouse=true ).run()
      _timer.stop()
    }

    function animDone( anim: DropAnimator ) {
      _animators.remove( anim )
    }

    override function actionPerformed( e: ActionEvent ) {
      for( anim in new ArrayList<DropAnimator>( _animators ) ) {
        anim.actionPerformed( e )
      }
      repaintViaBuffer()
    }
  }

  class DropAnimator implements ActionListener {
    var _comps: StoneComponent[]
    var _acc: float
    var _owner: GroupDropAnimator as Owner
    var _iSpaces: int
    var _iSpaceToFall: int
    var _iOrigLoc: int

    construct( stones: StoneComponent[], iSpaces: int ) {
      _comps = stones
      _iSpaces = iSpaces
      _acc = 1
      _iSpaceToFall = _iSpaces * _comps.last().Height
      _iOrigLoc = _comps.last().Y
    }

    override function actionPerformed( e: ActionEvent ) {
      for( i in _comps.length-1..0 ) {
        var c = _comps[i]
        var y = c.Y + _acc as int
        if( y - _iOrigLoc >= _iSpaceToFall ) {
          _owner.animDone( this )
          _layout.layoutComponentsUsingModel( outer, _comps )
          return
        }
        c.setLocation( c.X, y )
      }
      _acc *= 1.08
    }
  }
}