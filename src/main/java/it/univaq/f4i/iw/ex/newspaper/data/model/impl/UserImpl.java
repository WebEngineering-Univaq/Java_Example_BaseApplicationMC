package it.univaq.f4i.iw.ex.newspaper.data.model.impl;

import it.univaq.f4i.iw.ex.newspaper.data.model.User;
import it.univaq.f4i.iw.framework.data.DataItemImpl;
import java.util.ArrayList;
import java.util.List;

public class UserImpl extends DataItemImpl<Integer> implements User {

    private String username;
    private String password;
    private final List<String> roles;

    public UserImpl() {
        super();
        username = "";
        password = "";
        roles = new ArrayList<>();
    }

    /**
     * @return the username
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public List<String> getRoles() {
        return roles;
    }

    @Override
    public void setRoles(List<String> roles) {
        this.roles.clear();
        this.roles.addAll(roles);
    }

}
