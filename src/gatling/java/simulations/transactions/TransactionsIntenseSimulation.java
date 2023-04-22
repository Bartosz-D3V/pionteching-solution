package simulations.transactions;

import static io.gatling.javaapi.core.CoreDsl.RawFileBody;
import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.jsonPath;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import java.time.Duration;
import simulations.helpers.ScenarioHelper;

public class TransactionsIntenseSimulation extends Simulation {
    private final HttpProtocolBuilder httpProtocol = ScenarioHelper.getCommonProtocolBuilder("/transactions");
    // > mean response time                                  1284 (OK=1284   KO=-     )
    //            > std deviation                                        384 (OK=384    KO=-     )
    //            > response time 50th percentile                       1253 (OK=1253   KO=-     )
    //            > response time 75th percentile                       1404 (OK=1404   KO=-     )
    //            > response time 95th percentile                       1649 (OK=1649   KO=-     )
    //            > response time 99th percentile                       2027 (OK=2027   KO=-     )

    ScenarioBuilder scn = scenario("IntenseSimulationWith10BigSimRequest")
            .during(Duration.ofMinutes(3))
            .on(exec(http("request_1")
                    .post("/report")
                    .body(RawFileBody("transactions/big_request.json"))
                    // All transactions sum to 0 to ease testing
                    .check(jsonPath("$[*].balance").ofInt().is(0))));

    {
        setUp(scn.injectOpen(atOnceUsers(10))).protocols(httpProtocol);
    }
}
