package com.stock.tomorrowMarket.global.security;

import com.stock.tomorrowMarket.user.entity.Role;
import com.stock.tomorrowMarket.user.entity.Users;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Long usersId;
    private final String email;
    private final Role role;
    private final String password;
    private final boolean active;

    // DB 조회를 통한 생성 (UserDetailsService)
    public CustomUserDetails(Users user) {
        this.usersId = user.getUsersId();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.password = user.getPassword();
        this.active = "ACTIVE".equals(user.getStatus().name());
    }

    // JWT 파싱을 통한 무상태 객체 생성
    public CustomUserDetails(Long usersId, String email, String roleName) {
        this.usersId = usersId;
        this.email = email;
        this.role = Role.valueOf(roleName);
        this.password = "";
        this.active = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return active; }
}
