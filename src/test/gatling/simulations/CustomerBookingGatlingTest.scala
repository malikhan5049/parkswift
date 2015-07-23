import _root_.io.gatling.core.scenario.Simulation
import ch.qos.logback.classic.{Level, LoggerContext}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.slf4j.LoggerFactory

import scala.concurrent.duration._

/**
 * Performance test for the CustomerBooking entity.
 */
class CustomerBookingGatlingTest extends Simulation {

    val context: LoggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
    // Log all HTTP requests
    //context.getLogger("io.gatling.http").setLevel(Level.valueOf("TRACE"))
    // Log failed HTTP requests
    //context.getLogger("io.gatling.http").setLevel(Level.valueOf("DEBUG"))

    val baseURL = Option(System.getProperty("baseURL")) getOrElse """http://127.0.0.1:8080"""

    val httpConf = http
        .baseURL(baseURL)
        .inferHtmlResources()
        .acceptHeader("*/*")
        .acceptEncodingHeader("gzip, deflate")
        .acceptLanguageHeader("fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3")
        .connection("keep-alive")
        .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:33.0) Gecko/20100101 Firefox/33.0")

    val headers_http = Map(
        "Accept" -> """application/json"""
    )


    val headers_http_authentication = Map(
        "Content-Type" -> """application/x-www-form-urlencoded""",
        "Accept" -> """application/json"""
    )

    val headers_http_authenticated = Map(
        "Accept" -> """application/json""",
        "x-auth-token" -> "${x_auth_token}"
    )

    val scn = scenario("Test the CustomerBooking entity")
        .exec(http("First unauthenticated request")
        .get("/api/account")
        .headers(headers_http)
        .check(status.is(401)))
        .pause(10)
        .exec(http("Authentication")
        .post("/api/authenticate")
        .headers(headers_http_authentication)
        .formParam("username", "admin")
        .formParam("password", "admin")
        .check(jsonPath("$.token").saveAs("x_auth_token")))
        .pause(1)
        .exec(http("Authenticated request")
        .get("/api/account")
        .headers(headers_http_authenticated)
        .check(status.is(200)))
        .pause(10)
        .repeat(2) {
            exec(http("Get all customerBookings")
            .get("/api/customerBookings")
            .headers(headers_http_authenticated)
            .check(status.is(200)))
            .pause(10 seconds, 20 seconds)
            .exec(http("Create new customerBooking")
            .put("/api/customerBookings")
            .headers(headers_http_authenticated)
            .body(StringBody("""{"id":null, "bookingReferenceNumber":"SAMPLE_TEXT", "bookingDateTime":"2020-01-01T00:00:00.000Z", "numberOfSpacesBooked":"0", "totalAmount":null, "paymentRecursive":null, "paymentFrequency":"SAMPLE_TEXT", "numberOfPayments":"0", "firstPaymentAmount":null, "regularPaymentAmount":null, "lastPaymentAmount":null, "numberOfPaymentsMade":"0", "nextPaymentDateTime":"2020-01-01T00:00:00.000Z", "vehicleMake":"SAMPLE_TEXT", "vehicleModel":"SAMPLE_TEXT", "modelYear":"SAMPLE_TEXT", "licencePlateNumber":"SAMPLE_TEXT", "status":"SAMPLE_TEXT", "cancelationDateTime":"2020-01-01T00:00:00.000Z", "cancelledBy":null}""")).asJSON
            .check(status.is(201))
            .check(headerRegex("Location", "(.*)").saveAs("new_customerBooking_url")))
            .pause(10)
            .repeat(5) {
                exec(http("Get created customerBooking")
                .get("${new_customerBooking_url}")
                .headers(headers_http_authenticated))
                .pause(10)
            }
            .exec(http("Delete created customerBooking")
            .delete("${new_customerBooking_url}")
            .headers(headers_http_authenticated))
            .pause(10)
        }

    val users = scenario("Users").exec(scn)

    setUp(
        users.inject(rampUsers(100) over (1 minutes))
    ).protocols(httpConf)
}
