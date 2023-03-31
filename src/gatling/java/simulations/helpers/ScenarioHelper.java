package simulations.helpers;

import static io.gatling.javaapi.http.HttpDsl.http;

import io.gatling.javaapi.http.HttpProtocolBuilder;

public class ScenarioHelper {
  public static HttpProtocolBuilder getCommonProtocolBuilder(String path) {
    return http.baseUrl("http://localhost:8080/".concat(path))
        .acceptHeader("*/*")
        .contentTypeHeader("application/json")
        .acceptLanguageHeader("en-US,en;q=0.5")
        .acceptEncodingHeader("gzip, deflate, br")
        .userAgentHeader("Gatling");
  }
}
