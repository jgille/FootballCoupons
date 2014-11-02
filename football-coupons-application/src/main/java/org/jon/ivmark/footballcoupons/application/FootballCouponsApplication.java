package org.jon.ivmark.footballcoupons.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcabi.manifests.Manifests;

import org.jon.ivmark.footballcoupons.application.configuration.FootballCouponsConfiguration;
import org.jon.ivmark.footballcoupons.application.coupons.converters.CouponDtoConverter;
import org.jon.ivmark.footballcoupons.application.coupons.infrastructure.InMemoryCouponRepository;
import org.jon.ivmark.footballcoupons.application.coupons.resources.CouponResource;
import org.jon.ivmark.footballcoupons.application.greeting.resources.GreetingResource;
import org.jon.ivmark.footballcoupons.application.greeting.services.GreetingService;
import org.jon.ivmark.footballcoupons.application.health.SimpleHealthCheck;
import org.jon.ivmark.footballcoupons.application.http.InfoServlet;
import org.jon.ivmark.footballcoupons.application.http.filter.CorrelationIdLoggingFilter;

import com.sun.jersey.api.container.filter.LoggingFilter;
import io.dropwizard.Application;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.jetty.setup.ServletEnvironment;

import java.util.Map;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static com.sun.jersey.api.core.ResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS;
import static com.sun.jersey.api.core.ResourceConfig.PROPERTY_CONTAINER_RESPONSE_FILTERS;

public class FootballCouponsApplication extends Application<FootballCouponsConfiguration> {

    public static void main(String[] args) throws Exception {
        new FootballCouponsApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<FootballCouponsConfiguration> configuration) {

    }

    @Override
    public void run(FootballCouponsConfiguration configuration, Environment environment) throws Exception {
        configureObjectMapper(environment.getObjectMapper());

        addCustomFilters(environment);

        JerseyEnvironment jersey = environment.jersey();
        configureWirelogging(configuration, jersey);

        GreetingService service = new GreetingService();
        jersey.register(new GreetingResource(service));


        jersey.register(new CouponResource(new CouponDtoConverter(), new InMemoryCouponRepository()));

        environment.admin().addServlet("info", new InfoServlet()).addMapping("/info");

        environment.healthChecks().register("simple", new SimpleHealthCheck());
    }

    @Override
    public String getName() {
        String serviceName = "FootballCoupons";
        return serviceName + getVersionFromManifest();
    }

    private void configureWirelogging(FootballCouponsConfiguration configuration, JerseyEnvironment jersey) {
        if (configuration.wirelogging) {
            Map<String, Object> properties = jersey.getResourceConfig().getProperties();
            properties.put(PROPERTY_CONTAINER_REQUEST_FILTERS, LoggingFilter.class.getName());
            properties.put(PROPERTY_CONTAINER_RESPONSE_FILTERS, LoggingFilter.class.getName());
        }
    }

    private void addCustomFilters(Environment environment) {
        ServletEnvironment servlets = environment.servlets();
        servlets.addFilter("logging-filter", new CorrelationIdLoggingFilter())
            .addMappingForUrlPatterns(null, false, "/*");
    }

    private void configureObjectMapper(ObjectMapper objectMapper) {
        objectMapper.disable(WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public String getVersionFromManifest() {
        try {
            return Manifests.read("Service-Version");
        } catch (Exception e) {
            return "N/A";
        }
    }
}
