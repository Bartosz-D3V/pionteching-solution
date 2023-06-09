package simulations.atmservice;

import static io.gatling.javaapi.core.CoreDsl.RawFileBody;
import static io.gatling.javaapi.core.CoreDsl.constantConcurrentUsers;
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.holdFor;
import static io.gatling.javaapi.core.CoreDsl.reachRps;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import java.time.Duration;
import simulations.helpers.ScenarioHelper;

public class AtmServiceBasicSimulation extends Simulation {
    private final HttpProtocolBuilder httpProtocol = ScenarioHelper.getCommonProtocolBuilder("/atms");

    ScenarioBuilder scn = scenario("BasicSimulationWith10SmallSimRequest")
            .during(Duration.ofMinutes(3))
            .on(exec(http("request_1").post("/calculateOrder").body(RawFileBody("atmservice/small_request.json"))));

    {
        setUp(scn.injectClosed(constantConcurrentUsers(10).during(Duration.ofMinutes(2))))
                .throttle(
                        reachRps(500).in(Duration.ofSeconds(10)),
                        holdFor(Duration.ofMinutes(1)),
                        reachRps(1000).in(Duration.ofSeconds(10)),
                        holdFor(Duration.ofMinutes(2)))
                .protocols(httpProtocol);
    }
}
