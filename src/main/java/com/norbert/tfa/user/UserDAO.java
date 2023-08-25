package com.norbert.tfa.user;

public interface UserDAO {
    User findByEmail(String email);
    boolean isUserPresent(String email);
    void save(User user);
}
