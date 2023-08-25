package com.norbert.tfa.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserDAO userDAO;

    public void save(User user){
        userDAO.save(user);
    }

    public User findByEmail(String email){
        return userDAO.findByEmail(email);
    }

    public boolean isUserPresent(String email){
        return userDAO.isUserPresent(email);
    }
}
