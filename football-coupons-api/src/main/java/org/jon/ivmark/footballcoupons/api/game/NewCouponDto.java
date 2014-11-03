package org.jon.ivmark.footballcoupons.api.game;

import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.DateTime;
import org.jon.ivmark.footballcoupons.api.Dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class NewCouponDto extends Dto {

    @NotBlank
    public String coupon_name;

    @NotNull
    public DateTime coupon_must_be_submitted_before;

    @Min(value = 1)
    @Max(value = 20)
    public int number_of_matches;

}
