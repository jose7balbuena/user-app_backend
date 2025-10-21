package com.ngsoft.backend.kaltlane.users.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ngsoft.backend.kaltlane.users.entites.Role;
import com.ngsoft.backend.kaltlane.users.entites.User;
import com.ngsoft.backend.kaltlane.users.models.IUser;
import com.ngsoft.backend.kaltlane.users.models.UserModel;
import com.ngsoft.backend.kaltlane.users.repositories.IRoleRepository;
import com.ngsoft.backend.kaltlane.users.repositories.UserRepository;

/**
 * Implementation of the IUserService interface.
 */
@Service
public class UserServiceImpl implements IUserService {

    private UserRepository userRepository;

    private IRoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, IRoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
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
        user.setRoles(getRoles(user));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return this.userRepository.save(user);
    }



    @Override
    @Transactional
    public Optional<User> update(Long id, UserModel user) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User userToUpdate = existingUser.get();
            userToUpdate.setEmail(user.getEmail());
            userToUpdate.setName(user.getName());
            userToUpdate.setLastname(user.getLastname());
            userToUpdate.setSecondlastname(user.getSecondlastname());
            userToUpdate.setUsername(user.getUsername());

            userToUpdate.setRoles(getRoles(user));
            return Optional.of(userRepository.save(userToUpdate));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        this.userRepository.deleteById(id);
    }


        private List<Role> getRoles(IUser user) {
        List<Role> roles = new ArrayList<>();
        Optional<Role> userRole = roleRepository.findByName("ROLE_USER"); // Fetch the role by name
        userRole.ifPresent(roles::add);// Add the role if it exists. It´s same as roles.add(userRole.get());

        if (user.isAdmin()) {
            Optional<Role> adminRole = roleRepository.findByName("ROLE_ADMIN");
            adminRole.ifPresent(roles::add); // Add the role if it exists. It´s same as roles.add(adminRole.get());

        }  
        return roles;
    }
}
