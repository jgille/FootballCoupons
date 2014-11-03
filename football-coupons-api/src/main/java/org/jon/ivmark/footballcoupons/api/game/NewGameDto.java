package org.jon.ivmark.footballcoupons.api.game;

import org.hibernate.validator.constraints.NotBlank;
import org.jon.ivmark.footballcoupons.api.Dto;

public class NewGameDto extends Dto {

    @NotBlank
    public String game_name;

}
