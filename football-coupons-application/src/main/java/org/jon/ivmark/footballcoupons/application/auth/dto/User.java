package org.jon.ivmark.footballcoupons.application.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    private final String username;
    private final boolean admin;

    public User(@JsonProperty("username") String username,
                @JsonProperty("is_admin_user") boolean admin) {
        this.username = username;
        this.admin = admin;
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    @JsonProperty("is_admin_user")
    public boolean isAdmin() {
        return admin;
    }
}
