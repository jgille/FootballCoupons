package org.jon.ivmark.footballcoupons.application.coupons.domain.aggregates;

import org.jon.ivmark.footballcoupons.application.coupons.domain.valueobjects.CouponId;
import org.jon.ivmark.footballcoupons.application.coupons.domain.valueobjects.MatchId;
import org.jon.ivmark.footballcoupons.application.coupons.domain.valueobjects.MatchResult;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MatchTest {

    @Test
    public void newMatch() {
        MatchId matchId = new MatchId(CouponId.randomCouponId(), 0);
        Match match = Match.newMatch(matchId);
        assertThat(match.getMatchId(), is(matchId));
        assertThat(match.getHomeTeam(), is(Match.DEFAULT_HOME_TEAM_NAME));
        assertThat(match.getAwayTeam(), is(Match.DEFAULT_AWAY_TEAM_NAME));
        assertThat(match.getMatchResult(), is(MatchResult.UNKNOWN));
    }
}
