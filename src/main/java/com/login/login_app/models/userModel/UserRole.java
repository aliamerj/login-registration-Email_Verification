package com.login.login_app.models.userModel;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.login.login_app.models.userModel.UserPermission.*;

@AllArgsConstructor
@Getter
public enum UserRole {
    USER(Sets.newHashSet(GET_USER_BY_ID)),
    ADMIN(Sets.newHashSet(BLOCK_USER,GET_ALL_USERS,GET_USER_BY_ID,DELETE_USER));
    private final Set<UserPermission> permissions ;

    // attach permission with role
    @Bean
    public Set<SimpleGrantedAuthority> gratedAuthorities(){
        var permission = getPermissions().stream().map(permissions -> new SimpleGrantedAuthority(permissions.getPermission()))
                .collect(Collectors.toSet());
        permission.add(new SimpleGrantedAuthority("ROLE_ "+ this.name()));
        return permission;

    }

}

