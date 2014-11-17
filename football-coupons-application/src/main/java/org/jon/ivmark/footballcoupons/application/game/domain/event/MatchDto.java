package org.jon.ivmark.footballcoupons.application.game.domain.event;

import org.jon.ivmark.footballcoupons.application.game.domain.valueobjects.MatchResult;

public class MatchDto {

    public int match_index;
    public  String home_team;
    public String away_team;
    public MatchResult matchResult;
}
