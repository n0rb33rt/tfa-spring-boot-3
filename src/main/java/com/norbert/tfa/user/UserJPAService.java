package com.norbert.tfa.user;


import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserJPAService implements UserDAO{
    private final UserRepository userRepository;

    @Override
    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User is not found with email " + email ));
    }

    @Override
    public boolean isUserPresent(String email){
        return userRepository.existsByEmail(email);
    }

    @Override
    public void save(User user){
        userRepository.save(user);
    }
}
