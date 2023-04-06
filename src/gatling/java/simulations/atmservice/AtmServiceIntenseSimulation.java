package simulations.atmservice;

import static io.gatling.javaapi.core.CoreDsl.RawFileBody;
import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import java.time.Duration;
import simulations.helpers.ScenarioHelper;

public class AtmServiceIntenseSimulation extends Simulation {
	private final HttpProtocolBuilder httpProtocol = ScenarioHelper.getCommonProtocolBuilder("/atms");

	ScenarioBuilder scn = scenario("BasicSimulationWith10SmallSimRequest")
			.during(Duration.ofMinutes(5))
			.on(exec(http("request_1")
					.post("calculateOrder")
					.body(RawFileBody("atmservice/big_request.json"))));
	{
		setUp(scn.injectOpen(atOnceUsers(20))).protocols(httpProtocol);
	}
}
