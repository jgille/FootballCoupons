package org.jon.ivmark.footballcoupons.application.game.domain.aggregates;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jon.ivmark.footballcoupons.application.game.domain.valueobjects.MatchId;
import org.jon.ivmark.footballcoupons.application.game.domain.valueobjects.MatchResult;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

public class Match {

    static final String DEFAULT_HOME_TEAM_NAME = "Hemmalag";
    static final String DEFAULT_AWAY_TEAM_NAME = "Bortalag";

    private final MatchId matchId;
    private final String homeTeam;
    private final String awayTeam;
    private final MatchResult matchResult;

    public Match(MatchId matchId, String homeTeam, String awayTeam, MatchResult matchResult) {
        this.matchId = matchId;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.matchResult = matchResult;
    }

    public MatchId getMatchId() {
        return matchId;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public MatchResult getMatchResult() {
        return matchResult;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, SHORT_PREFIX_STYLE);
    }

    public static Match newMatch(MatchId matchId) {
        return new Match(matchId, DEFAULT_HOME_TEAM_NAME, DEFAULT_AWAY_TEAM_NAME, MatchResult.UNKNOWN);
    }
}
