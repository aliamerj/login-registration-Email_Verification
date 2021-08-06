package com.login.login_app.models.userModel;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserPermission {
    GET_ALL_USERS("user : show"),
    GET_USER_BY_ID("user : getById"),
    DELETE_USER("user : delete"),
    BLOCK_USER("user : block");
    private final String permission;
}
