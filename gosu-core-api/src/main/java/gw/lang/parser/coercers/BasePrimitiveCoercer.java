/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

import gw.lang.parser.IResolvingCoercer;
import gw.lang.parser.ICoercer;
import gw.lang.reflect.IType;
import gw.lang.reflect.MethodScorer;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.TypeSystemShutdownListener;
import gw.lang.reflect.java.JavaTypes;
import gw.config.CommonServices;
import gw.util.concurrent.LockingLazyVar;

public class BasePrimitiveCoercer extends StandardCoercer implements IResolvingCoercer
{
  public static final LockingLazyVar<BasePrimitiveCoercer> DoublePCoercer = new LockingLazyVar<BasePrimitiveCoercer>() {
    protected BasePrimitiveCoercer init() {
      return new BasePrimitiveCoercer(DoubleCoercer.instance(), JavaTypes.pDOUBLE(), JavaTypes.DOUBLE());
    }
  };

  public static final LockingLazyVar<BasePrimitiveCoercer> FloatPCoercer = new LockingLazyVar<BasePrimitiveCoercer>() {
    protected BasePrimitiveCoercer init() {
      return new BasePrimitiveCoercer(FloatCoercer.instance(), JavaTypes.pFLOAT(), JavaTypes.FLOAT());
    }
  };

  public static final LockingLazyVar<BasePrimitiveCoercer> BooleanPCoercer = new LockingLazyVar<BasePrimitiveCoercer>() {
    protected BasePrimitiveCoercer init() {
      return new BasePrimitiveCoercer(BooleanCoercer.instance(), JavaTypes.pBOOLEAN(), JavaTypes.BOOLEAN());
    }
  };

  public static final LockingLazyVar<BasePrimitiveCoercer> BytePCoercer = new LockingLazyVar<BasePrimitiveCoercer>() {
    protected BasePrimitiveCoercer init() {
      return new BasePrimitiveCoercer(ByteCoercer.instance(), JavaTypes.pBYTE(), JavaTypes.BYTE());
    }
  };

  public static final LockingLazyVar<BasePrimitiveCoercer> ShortPCoercer = new LockingLazyVar<BasePrimitiveCoercer>() {
    protected BasePrimitiveCoercer init() {
      return new BasePrimitiveCoercer(ShortCoercer.instance(), JavaTypes.pSHORT(), JavaTypes.SHORT());
    }
  };

  public static final LockingLazyVar<BasePrimitiveCoercer> CharPCoercer = new LockingLazyVar<BasePrimitiveCoercer>() {
    protected BasePrimitiveCoercer init() {
      return new BasePrimitiveCoercer(CharCoercer.instance(), JavaTypes.pCHAR(), JavaTypes.CHARACTER());
    }
  };

  public static final LockingLazyVar<BasePrimitiveCoercer> IntPCoercer = new LockingLazyVar<BasePrimitiveCoercer>() {
    protected BasePrimitiveCoercer init() {
      return new BasePrimitiveCoercer(IntCoercer.instance(), JavaTypes.pINT(), JavaTypes.INTEGER());
    }
  };

  public static final LockingLazyVar<BasePrimitiveCoercer> LongPCoercer = new LockingLazyVar<BasePrimitiveCoercer>() {
    protected BasePrimitiveCoercer init() {
      return new BasePrimitiveCoercer(LongCoercer.instance(), JavaTypes.pLONG(), JavaTypes.LONG());
    }
  };

  static {
    TypeSystem.addShutdownListener(new TypeSystemShutdownListener() {
      public void shutdown() {
        DoublePCoercer.clear();
        FloatPCoercer.clear();
        BooleanPCoercer.clear();
        BytePCoercer.clear();
        ShortPCoercer.clear();
        CharPCoercer.clear();
        IntPCoercer.clear();
        LongPCoercer.clear();
      }
    });
  }

  //The non-primitive coercer
  private final ICoercer _nonPrimitiveCoercer;
  private final IType _primitiveType;
  private final IType _nonPrimitveType;

  public BasePrimitiveCoercer( ICoercer nonPrimitiveCoercer, IType primitiveType, IType nonPrimitiveType )
  {
    _nonPrimitiveCoercer = nonPrimitiveCoercer;
    _primitiveType = primitiveType;
    _nonPrimitveType = nonPrimitiveType;
  }

  public final Object coerceValue( IType typeToCoerceTo, Object value )
  {
    if( value == null )
    {
      return CommonServices.getCoercionManager().convertNullAsPrimitive( _primitiveType, false );
    }
    else
    {
      return _nonPrimitiveCoercer.coerceValue(typeToCoerceTo, value);
    }
  }

  @Override
  public boolean handlesNull()
  {
    return true;
  }

  public IType resolveType( IType target, IType source )
  {
    return target.isPrimitive() ? _primitiveType : _nonPrimitveType;
  }

  @Override
  public int getPriority( IType to, IType from )
  {
    //!! Note this "priority" is scored as follows: 0 is perfect, higher numbers are worse
    //!! This is to support method scoring which is zero-preferenced.
    return getPriorityOf( to, from );
  }
  public static int getPriorityOf( IType to, IType from )
  {
    if( to == from ) {
      return 0;                       // score = 0
    }
    if( JavaTypes.OBJECT().equals( to ) ) {
      return MethodScorer.BOXED_COERCION_SCORE + 1;   // score = 11 (must be consistent with MethodScorer)
    }

    int infoLoss = losesInformation( from, to );
    boolean bSameFamily = isInSameFamily( from, to );

    int iScore = 1;
    if( infoLoss > 1 ) {
      // Errant incompatible primitive types are treated identical to errant explicit coercible types because, same thing
      iScore += MethodScorer.PRIMITIVE_COERCION_SCORE; // score = 25
      if( !bSameFamily ) {
        iScore += 1;                                   // score = 26
      }
    }
    else if( bSameFamily ) {
      if( from == JavaTypes.pCHAR() || from == JavaTypes.CHARACTER() ) {
        from = JavaTypes.pSHORT(); // char same distance to int as short
      }
      iScore += distance( from, to ); // score = (2..4)
    }
    else {
      iScore += infoLoss;             // score = (1..2)
      iScore += 4;                    // score = (5..6)
    }
    return iScore;
  }

  private static int distance( IType from, IType to ) {
    int iDistance = getIndex( to ) - getIndex( from );
    return iDistance >= 0 ? iDistance : 5;
  }

  public static int losesInformation( IType from, IType to ) {
    int[][] tab =
    {                                        //TO
      //FROM       boolean  char    byte    short   int     long    float   double
      /*boolean*/  {0,      0,      0,      0,      0,      0,      0,      0 },
      /*char   */  {2,      0,      2,      2,      0,      0,      0,      1 },
      /*byte   */  {2,      0,      0,      0,      0,      0,      0,      1 },
      /*short  */  {2,      2,      2,      0,      0,      0,      0,      1 },
      /*int    */  {2,      2,      2,      2,      0,      0,      0,      1 },
      /*long   */  {2,      2,      2,      2,      2,      0,      0,      1 },
      /*float  */  {2,      2,      2,      2,      2,      2,      0,      0 },
      /*double */  {2,      2,      2,      2,      2,      2,      1,      0 },
    };
    final int i = getIndex(from);
    final int j = getIndex(to);
    if( i == -1 || j == -1 )
    {
      return 0;
    }
    return tab[i][j];
  }

  private static int getIndex(IType type) {
    if( type == JavaTypes.pBOOLEAN() || type == JavaTypes.BOOLEAN() )
    {
      return 0;
    }
    else if( type == JavaTypes.pCHAR() || type == JavaTypes.CHARACTER() )
    {
      return 1;
    }
    else if( type == JavaTypes.pBYTE() || type == JavaTypes.BYTE() )
    {
      return 2;
    }
    else if( type == JavaTypes.pSHORT() || type == JavaTypes.SHORT() )
    {
      return 3;
    }
    else if( type == JavaTypes.pINT() || type == JavaTypes.INTEGER() )
    {
      return 4;
    }
    else if( type == JavaTypes.pLONG() || type == JavaTypes.LONG() )
    {
      return 5;
    }
    else if( type == JavaTypes.pFLOAT() || type == JavaTypes.FLOAT() )
    {
      return 6;
    }
    else if( type == JavaTypes.pDOUBLE() || type == JavaTypes.DOUBLE() )
    {
      return 7;
    }
    return -1;
  }

  private static boolean isInSameFamily( IType t1, IType t2 ) {
    int indexT1 = getIndex( t1 );
    int indexT2 = getIndex( t2 );
    return indexT1 == indexT2 ||
           indexT1 > 0 && indexT1 < 6 && indexT2 > 0 && indexT2 < 6 ||
           indexT1 > 5 && indexT2 > 5;
  }
}
