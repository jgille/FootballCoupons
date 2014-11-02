package org.jon.ivmark.footballcoupons.client;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.jon.ivmark.footballcoupons.api.GreetingDto;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;

public class FootballCouponsClient {

    private final CloseableHttpClient client;
    private final ObjectMapper objectMapper;
    private final String greetingUrl;

    public FootballCouponsClient(CloseableHttpClient client, ObjectMapper objectMapper, String serviceUrl) {
        this.client = client;
        this.objectMapper = objectMapper;
        this.greetingUrl = String.format("%s/greeting", serviceUrl);
    }

    public GreetingDto greet(String user) {
        try {
            return getGreeting(user);
        } catch (IOException e) {
            throw new FootballCouponsClientException("Failed to get greeting for " + user, e);
        }
    }

    private GreetingDto getGreeting(String user) throws IOException {
        HttpGet httpget = greetingRequest(user);
        GreetingDto greetingDto;
        try (CloseableHttpResponse response = client.execute(httpget)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                InputStream content = response.getEntity().getContent();
                greetingDto = objectMapper.readValue(content, GreetingDto.class);
            } else {
                EntityUtils.consumeQuietly(response.getEntity());
                throw new MetricsServiceClientException("Failed to greet user, got response code " + statusCode);
            }
        }
        return greetingDto;
    }

    private HttpGet greetingRequest(String user) {
        return new HttpGet(String.format("%s/%s", greetingUrl, user));
    }

}
