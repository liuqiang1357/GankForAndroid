import org.junit.Test
import rx.Observable
import rx.schedulers.Schedulers

class RxJavaTest {

    @Test
    fun test() {
        val sub = Observable.create<Any> { subscriber ->
            var i = 0
            while (true) {
                Thread.sleep(1000)
                subscriber.onNext(i++)
            }
        }
                .subscribeOn(Schedulers.io())
                .doOnNext { println("1:" + it + "," + Thread.currentThread().id) }
                .observeOn(Schedulers.io())
                .doOnNext { println("2:" + it + "," + Thread.currentThread().id) }
                .observeOn(Schedulers.io())
                .doOnNext { println("3:" + it + "," + Thread.currentThread().id) }
                .subscribe { o -> println("o:" + o + "," + Thread.currentThread().id) }

        Thread.sleep(3000)
        sub.unsubscribe()
        Thread.sleep(10000)
    }
}
