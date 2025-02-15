package com.cst323.eventsapp.models;

public class UserEntity {

    private Long id;
    private String userName;
    private String password;

    // Constructor, getters, and setters
    public UserEntity() {
    }

    public UserEntity(Long id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
    }

    // Getters and setters...
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String loginName) {
        this.userName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // public boolean isEnabled() {
    //     return enabled;
    // }

    // public void setEnabled(boolean enabled) {
    //     this.enabled = enabled;
    // }

    // public boolean isAccountNonExpired() {
    //     return accountNonExpired;
    // }

    // public void setAccountNonExpired(boolean accountNonExpired) {
    //     this.accountNonExpired = accountNonExpired;
    // }

    // public boolean isCredentialsNonExpired() {
    //     return credentialsNonExpired;
    // }

    // public void setCredentialsNonExpired(boolean credentialsNonExpired) {
    //     this.credentialsNonExpired = credentialsNonExpired;
    // }

    // public boolean isAccountNonLocked() {
    //     return accountNonLocked;
    // }

    // public void setAccountNonLocked(boolean accountNonLocked) {
    //     this.accountNonLocked = accountNonLocked;
    // }

    // public Set<String> getRoles() {
    //     return roles;
    // }

    // public void setRoles(Set<String> roles) {
    //     this.roles = roles;
    // }
}
