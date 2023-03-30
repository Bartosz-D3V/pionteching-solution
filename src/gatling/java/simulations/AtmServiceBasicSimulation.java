package simulations;

import static io.gatling.javaapi.core.CoreDsl.RawFileBody;
import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import java.time.Duration;

public class AtmServiceBasicSimulation extends Simulation {
  HttpProtocolBuilder httpProtocol =
      http.baseUrl("http://localhost:8080/")
          .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // 6
          .doNotTrackHeader("1")
          .acceptLanguageHeader("en-US,en;q=0.5")
          .acceptEncodingHeader("gzip, deflate")
          .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0");

  ScenarioBuilder scn =
      scenario("BasicSimulationWith10SimRequest")
          .during(Duration.ofMinutes(5))
          .on(
              exec(
                  http("request_1")
                      .post("atms/calculateOrder")
                      .body(RawFileBody("small_request.json"))));

  {
    setUp(scn.injectOpen(atOnceUsers(10))).protocols(httpProtocol);
  }
}
