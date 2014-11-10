package org.jon.ivmark.footballcoupons.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcabi.manifests.Manifests;
import com.stormpath.sdk.impl.jwt.signer.DefaultJwtSigner;
import com.sun.jersey.api.container.filter.LoggingFilter;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.oauth.OAuthProvider;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.jetty.setup.ServletEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.jon.ivmark.footballcoupons.application.auth.jwt.JwtAuthenticator;
import org.jon.ivmark.footballcoupons.application.auth.jwt.JwtService;
import org.jon.ivmark.footballcoupons.application.auth.resources.LoginResource;
import org.jon.ivmark.footballcoupons.application.configuration.FootballCouponsConfiguration;
import org.jon.ivmark.footballcoupons.application.game.converters.CouponDtoConverter;
import org.jon.ivmark.footballcoupons.application.game.domain.GameRepository;
import org.jon.ivmark.footballcoupons.application.game.domain.GameService;
import org.jon.ivmark.footballcoupons.application.game.domain.event.EventLog;
import org.jon.ivmark.footballcoupons.application.game.domain.event.GameEvent;
import org.jon.ivmark.footballcoupons.application.game.infrastructure.CachingEventBasedGameRepository;
import org.jon.ivmark.footballcoupons.application.game.infrastructure.InMemoryGameRepository;
import org.jon.ivmark.footballcoupons.application.game.infrastructure.event.FileBasedEventLog;
import org.jon.ivmark.footballcoupons.application.game.resources.CouponResource;
import org.jon.ivmark.footballcoupons.application.game.resources.GameResource;
import org.jon.ivmark.footballcoupons.application.game.service.GameServiceImpl;
import org.jon.ivmark.footballcoupons.application.health.SimpleHealthCheck;
import org.jon.ivmark.footballcoupons.application.http.InfoServlet;
import org.jon.ivmark.footballcoupons.application.http.filter.CorrelationIdLoggingFilter;

import java.io.File;
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
    public void initialize(Bootstrap<FootballCouponsConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets/", "/"));
    }

    @Override
    public void run(FootballCouponsConfiguration configuration, Environment environment) throws Exception {
        configureObjectMapper(environment.getObjectMapper());

        addCustomFilters(environment);

        JerseyEnvironment jersey = environment.jersey();

        jersey.setUrlPattern("/api/*");

        setupAuth(configuration, environment);

        configureWirelogging(configuration, jersey);

        EventLog<GameEvent> eventLog = new FileBasedEventLog<>(new File(configuration.games.dataDir, "events"),
                                                               environment.getObjectMapper());
        InMemoryGameRepository cache = new InMemoryGameRepository();
        GameRepository gameRepository = new CachingEventBasedGameRepository(cache, eventLog);
        GameService gameService = new GameServiceImpl(gameRepository);

        jersey.register(new GameResource(gameService));
        jersey.register(new CouponResource(new CouponDtoConverter(), gameService));

        environment.admin().addServlet("info", new InfoServlet()).addMapping("/info");

        environment.healthChecks().register("simple", new SimpleHealthCheck());
    }

    private void setupAuth(FootballCouponsConfiguration configuration, Environment environment) {
        DefaultJwtSigner signer = new DefaultJwtSigner(configuration.jwtSecret);
        ObjectMapper objectMapper = environment.getObjectMapper();

        JwtAuthenticator jwtAuthenticator = new JwtAuthenticator(signer, objectMapper);
        environment.jersey().register(new OAuthProvider<>(jwtAuthenticator, "realm"));

        // TODO: Setup properly
        com.stormpath.sdk.application.Application stormpathApplication = null;
        JwtService jwtService = new JwtService(signer, objectMapper);
        environment.jersey().register(new LoginResource(stormpathApplication, jwtService));
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
