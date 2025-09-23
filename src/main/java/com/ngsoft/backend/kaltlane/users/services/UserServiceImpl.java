package com.ngsoft.backend.kaltlane.users.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ngsoft.backend.kaltlane.users.entites.User;
import com.ngsoft.backend.kaltlane.users.repositories.UserRepository;

/**
 * Implementation of the IUserService interface.
 */
@Service
public class UserServiceImpl implements IUserService {

    
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List<User>) this.userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        return this.userRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(@NonNull Long id) {
        return this.userRepository.findById(id);
    }

    @Override
    @Transactional
    public User save(User user) {
        return this.userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        this.userRepository.deleteById(id);
    }

    

   

}
