package gw.lang.spec_old.generics
uses java.util.Queue
uses java.util.concurrent.ConcurrentLinkedQueue
uses java.util.concurrent.atomic.AtomicBoolean
uses java.util.concurrent.FutureTask
uses java.lang.Thread
uses java.util.concurrent.ExecutionException
uses java.lang.Error
uses java.lang.InterruptedException
uses java.util.concurrent.Future
uses java.util.concurrent.PriorityBlockingQueue
uses java.lang.Iterable
uses java.util.Iterator
uses gw.test.TestClass

// FIXME: (PL-22163) does not verify with non-open source Gosu
@gw.testharness.DoNotVerifyResource
class VictorTetstTest extends TestClass {

  static abstract class Option<E> implements Iterable<E> {
    private construct() { }
  
    property get IsNone() : boolean { return this typeis NoneType }
  
    property get IsSome() : boolean { return this typeis SomeType }
  
    abstract property get Some() : E
  
    override function iterator() : Iterator<E> { return (IsSome ? {Some} : ({} as Iterable<E>)).iterator() }
  
    function transform<T>(f(e : E) : T) : Option<T> { return IsSome ? some(f(Some)) : none<T>() }

    function orSome(e : E) : E { return IsSome ? Some : e }
  
    function orSome(p() : E) : E { return IsSome ? Some : p() }
  
    function orElse(e : Option<E>) : Option<E> { return IsSome ? this : e }
  
    function orElse(p() : Option<E>) : Option<E> { return IsSome ? this : p() }
  
    override function equals(that : Object) : boolean {
      return that typeis Option and ((IsNone and that.IsNone) or (IsSome and that.IsSome and Some == that.Some))
    }
  
    override function hashCode() : int { return IsSome ? Some.hashCode() : 0 }
  
    static function none<T>() : Option<T> { return new NoneType<T>() }
  
    static function some<T>(t : T) : Option<T> { return new SomeType<T>() { :_t = t } }
  
    static function iff<T>(b : boolean, t : T) : Option<T> { return b ? some(t) : none<T>() }
  
    static function iff<T>(b : boolean, p() : T) : Option<T> { return b ? some(p()) : none<T>() }
  
    static function nonNull<T>(t : T) : Option<T> { return iff(t <> null, t) }
  
    static function IsNone<T>() : block(t : Option<T>) : boolean { return \ t : Option<T> -> t.IsNone }
  
    static function IsSome<T>() : block(t : Option<T>) : boolean { return \ t : Option<T> -> t.IsSome }
  
    static function Some<T>() : block(t : Option<T>) : T { return \ t : Option<T> -> t.Some }

    // FIXME: PL-23787, had to rename function to make code to compile
    static function transform2<A, B>() : block(f : block(a : A) : B) : block(a : Option<A>) : Option<B> {
      return \ f : block(a : A) : B -> \ a : Option<A> -> a.transform(f)
    }
  
    private static class NoneType<T> extends Option<T> {
      override property get Some() : T { throw new Error("Some of None") }
    }
  
    private static class SomeType<T> extends Option<T> {
      var _t : T as readonly Some
    }
  }

  static abstract class P1<A> {
    private construct() { }
  
    abstract property get One() : A
  
    function map<B>(f(a : A) : B) : P1<B> { return p(\ -> f(One)) }
  
    abstract function memo() : P1<A>
  
    abstract function unmemo() : P1<A>
  
    override function equals(that : Object) : boolean { return that typeis P1 and One == that.One }
  
    override function hashCode() : int { return One.hashCode() } // deal with null
  
    static function p<X>(x : X) : P1<X> { return new ConstantP1<X>() { :_one = x } }
  
    static function p<X>(p() : X) : P1<X> { return new LazyP1<X>() { :_one = p } }
  
    static function One<X>() : block(x : P1<X>) : X { return \ x : P1<X> -> x.One }

    // FIXME: PL-23787, had to rename function to make code to compile
    static function map2<X, Y>() : block(f : block(x : X) : Y) : block(x : P1<X>) : P1<Y> {
      return \ f : block(x : X) : Y -> \ x : P1<X> -> x.map(f)
    }
  
    static function curry<X, Y>(f(x : X) : Y) : block(x : X) : P1<Y> { return \ x -> P1.p(f(x)) }
  
    private static class ConstantP1<T> extends P1<T> {
      var _one : T as readonly One
     override function memo() : P1<T> { return this }
      override function unmemo() : P1<T> { return this }
    }
  
    private static class LazyP1<LPT> extends P1<LPT> {
      var _one() : LPT
      override property get One() : LPT { return _one() }
      override function memo() : P1<LPT> { return new MemoP1<LPT>() { :_underlying = _one } }
      override function unmemo() : P1<LPT> { return this }
    }
  
    private static class MemoP1<T> extends P1<T> {
      var _underlying() : T
      var _one : java.lang.ref.SoftReference<Option<T>>
      override property get One() : T {
        var result = _one == null ? null : _one.get()
        if (result == null) {
          using (_underlying as IMonitorLock) {
            result = Option.some(_underlying())
            _one = new java.lang.ref.SoftReference<Option<T>>(result)
          }
        }
        return result.Some
      }
      override function memo() : P1<T> { return this }
      override function unmemo() : P1<T> { return p(_underlying) }
    }
  }

  abstract static class QueueStrategy {
    private construct() { }
  
    abstract function queue<A>() : Queue<A>
  
    static function linked() : QueueStrategy { return new LinkedQueueStrategy() }
  
    static function priority() : QueueStrategy { return new PriorityQueueStrategy() }
  
    private static class LinkedQueueStrategy extends QueueStrategy {
      override function queue<T>() : Queue<T> { return new ConcurrentLinkedQueue<T>() }
    }
  
    private static class PriorityQueueStrategy extends QueueStrategy {
      override function queue<T>() : Queue<T> { return new PriorityBlockingQueue<T>() }
    }
  }

  static class Unit {
    private construct() { }
  
    static var _unit : Unit as Unit = new Unit()
  }

  static class QueueActor<A> { 
    var _s : Strategy<Unit> 
    var _qs : QueueStrategy 
    var _f(a : A) : P1<Unit> 

    var suspended = new AtomicBoolean(true) 
    var mbox : Queue<A> 
    var act : Actor<Unit> 
    var selfish : Actor<A> 

    private construct(s : Strategy<Unit>, qs : QueueStrategy, f(a : A) : P1<Unit>) { 
      _s = s 
      _qs = qs 
      _f = f 
      mbox = _qs.queue<A>() 
      act = Actor.of(s, \ u : Unit -> {  
        var a = mbox.poll()     
        if (a <> null) { 
          _f(a) 
          return act.act(u) 
        } else {  
          suspended.set(true) 
          return work() 
        } 
      }) 
      selfish = Actor.of(s, \ a : A -> { 
        act(a) 
        return P1.p(Unit.Unit) 
      }) 
    } 

    private function work() : P1<Unit> { 
      var mt = not mbox.HasElements 
      return mt ? P1.p(Unit.Unit) : suspended.compareAndSet(not mt, false) ? act.act(Unit.Unit) : P1.p(Unit.Unit) 
    } 

    static function ofLinked<Q>(s : Strategy<Unit>, e(q : Q) : P1<Unit>) : QueueActor<Q> { 
      return of(s, QueueStrategy.linked(), e) 
    } 

    static function of<Q>(s : Strategy<Unit>, qs : QueueStrategy, e(q : Q) : P1<Unit>) : QueueActor<Q> { 
      return new QueueActor<Q>(s, qs, e) 
    } 

    static function of<Q>(s : Strategy<Unit>, e(q : Q) : P1<Unit>) : QueueActor<Q> { return ofLinked(s, e) } 

    function act(a : A) : P1<Unit> { return mbox.offer(a) ? work() : selfish.act(a) } 
  }  

  static class Actor<X> { 
    var _s : Strategy<Unit> 
    var _f(a : X) : P1<Unit> 
   
    private construct() { } 
   
    function act(a : X) : P1<Unit> { return _f(a) } 
   
    static function of<T>(s : Strategy<Unit>, f(a : T) : P1<Unit>) : Actor<T> { 
      return new Actor<T>() { :_s = s, :_f = \ a -> s.par(f(a)) } 
    } 
  } 

  static class Strategy<A> { 
    var _f(p : P1<A>) : P1<A> 
   
    private construct() { } 
   
    function par(p : P1<A>) : P1<A> { return _f(p) } 
   
    protected static function obtain<T>(f : Future<T>) : T { 
      try { 
        return f.get() 
      } catch (ie : InterruptedException) { 
        Thread.currentThread().interrupt() 
        throw new Error(ie) 
      } catch (ee : ExecutionException) { 
        throw new Error(ee) 
      } 
    } 

    static function simpleThread<T>() : Strategy<T> { 
      return 
        new Strategy<T>() 
        { :_f = \ p:P1<T> ->
          P1.p( \ -> 
                { 
                  var f = new FutureTask<T>(\ -> p.One) 
                  new Thread(f).start() 
                  return obtain(f) 
                } ) 
        } 
    }
  } 

  function testQueueActor() { 
    var my_actor = QueueActor.of(Strategy.simpleThread<Unit>(), \ n : String -> { 
      Thread.sleep(1000) 
      print("Done with ${n}") 
      return P1.p(Unit.Unit) 
    }) 
    var dones : List<P1<Unit>> = {} 
    for (i in 1..5) { 
      dones.add(my_actor.act(i as String)) 
    } 
    dones.map(\ p -> p.One)
    print("I think I'm done") 
  } 

}
