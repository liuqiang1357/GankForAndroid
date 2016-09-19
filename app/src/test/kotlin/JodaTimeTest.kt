import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.junit.Test

class JodaTimeTest {

    @Test
    fun test() {

        var dateTime = DateTime("2016-08-07", DateTimeZone.UTC)
        println(dateTime)
        println(dateTime.toDate())
        println(dateTime.toLocalDate())
        println(dateTime.toLocalTime())
        println(dateTime.toLocalDateTime())
        println(dateTime.toDateTimeISO())

        println("")

        dateTime = DateTime("2016-08-07T21:11:22", DateTimeZone.UTC)
        println(dateTime)
        println(dateTime.toDate())
        println(dateTime.toLocalDate())
        println(dateTime.toLocalTime())
        println(dateTime.toLocalDateTime())
        println(dateTime.toDateTimeISO())

        println("")

        dateTime = DateTime("2016-08-07T21:11:22Z", DateTimeZone.UTC)
        println(dateTime)
        println(dateTime.toDate())
        println(dateTime.toLocalDate())
        println(dateTime.toLocalTime())
        println(dateTime.toLocalDateTime())
        println(dateTime.toDateTimeISO())
    }
}
