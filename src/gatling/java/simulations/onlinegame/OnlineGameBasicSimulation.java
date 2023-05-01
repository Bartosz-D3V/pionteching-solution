package simulations.onlinegame;

import static io.gatling.javaapi.core.CoreDsl.RawFileBody;
import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
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

public class OnlineGameBasicSimulation extends Simulation {
    private final HttpProtocolBuilder httpProtocol = ScenarioHelper.getCommonProtocolBuilder("/onlinegame");

    ScenarioBuilder scn = scenario("BasicSimulationWith10SmallSimRequest")
            .during(Duration.ofMinutes(5))
            .on(exec(http("request_1").post("/calculate").body(RawFileBody("onlinegame/small_request.json"))));

    {
        setUp(scn.injectOpen(atOnceUsers(10))
                        .throttle(
                                reachRps(500).in(Duration.ofSeconds(10)), holdFor(Duration.ofMinutes(1)),
                                reachRps(1000).in(Duration.ofSeconds(10)), holdFor(Duration.ofMinutes(2))))
                .protocols(httpProtocol);
    }
}
