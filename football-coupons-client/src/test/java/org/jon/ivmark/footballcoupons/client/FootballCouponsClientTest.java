package org.jon.ivmark.footballcoupons.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.jon.ivmark.footballcoupons.api.GreetingDto;
import io.dropwizard.jackson.Jackson;
import org.apache.http.impl.client.HttpClients;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class FootballCouponsClientTest {

    private static final int PORT = 8888;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(PORT);

    private FootballCouponsClient client;
    private ObjectMapper objectMapper;

    @Before
    public void init() {
        objectMapper = Jackson.newObjectMapper();
        client = new MetricsServiceClient(HttpClients.createDefault(), objectMapper,
                "http://localhost:" + PORT);
    }

    @Test
    public void successfulGreeting() throws JsonProcessingException {
        String username = "Johny";

        GreetingDto expectedGreeting = new GreetingDto();
        expectedGreeting.username = username;
        expectedGreeting.greeting = "Hi there!";

        String body = objectMapper.writeValueAsString(expectedGreeting);

        stubFor(get(urlEqualTo("/greeting/" + username))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(body)));

        GreetingDto greeting = client.greet(username);
        assertThat(greeting, notNullValue());
        assertThat(greeting.username, is(expectedGreeting.username));
        assertThat(greeting.greeting, is(expectedGreeting.greeting));
    }

    @Test(expected = FootballCouponsClientException.class)
    public void serverError() throws JsonProcessingException {
        String username = "Johny";
        stubFor(get(urlEqualTo("/greeting/" + username))
                .willReturn(aResponse()
                        .withStatus(500)));
        client.greet(username);
    }

    @Test(expected = FootballCouponsClientException.class)
    public void illegalJson() throws JsonProcessingException {
        String username = "Johny";
        String body = "<>";

        stubFor(get(urlEqualTo("/greeting/" + username))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(body)));

        client.greet(username);
    }

}