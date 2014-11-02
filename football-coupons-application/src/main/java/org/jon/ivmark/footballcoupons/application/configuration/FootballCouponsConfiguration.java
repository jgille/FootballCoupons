package org.jon.ivmark.footballcoupons.application.configuration;

import io.dropwizard.Configuration;

import javax.validation.constraints.NotNull;

public class FootballCouponsConfiguration extends Configuration {
    public boolean wirelogging = false;

    @NotNull
    public GamesConfiguration games;
}
