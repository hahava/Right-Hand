package com.righthand.membership.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;

/**
 *  User Detail info
 *  Spring Security가 User정보 사용 및 session context에 저장하기 위한 클래스
 *
 */
public class MembershipInfo implements  UserDetails{

    private String username;
    private String password;
    private String name;
    private int userSeq;
    private SimpleDateFormat loginTime;

    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;
    private Collection<? extends GrantedAuthority> authorities;


    private List<String> authoritiesStr;
    private int authoritiesLevel;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() { return username; }

    @Override
    public boolean isAccountNonExpired() { return isAccountNonExpired; }

    @Override
    public boolean isAccountNonLocked() { return isAccountNonLocked; }

    @Override
    public boolean isCredentialsNonExpired() { return isCredentialsNonExpired; }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAccountNonExpired(boolean isAccountNonExpired) {
        this.isAccountNonExpired = isAccountNonExpired;
    }

    public void setAccountNonLocked(boolean isAccountNonLocked) {
        this.isAccountNonLocked = isAccountNonLocked;
    }

    public void setCredentialsNonExpired(boolean isCredentialsNonExpired) {
        this.isCredentialsNonExpired = isCredentialsNonExpired;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public SimpleDateFormat getLoginTime() { return loginTime; }
    public void setLoginTime(SimpleDateFormat loginTime) { this.loginTime = loginTime; }

    public int getUserSeq() {
        return userSeq;
    }
    public void setUserSeq(int userSeq) {
        this.userSeq = userSeq;
    }


    public List<String> getAuthoritiesStr() { return authoritiesStr; }
    public void setAuthoritiesStr(List<String> authoritiesStr) { this.authoritiesStr = authoritiesStr; }

    public int getAuthoritiesLevel() { return authoritiesLevel; }
    public void setAuthoritiesLevel(int authoritiesLevel) { this.authoritiesLevel = authoritiesLevel; }

}
