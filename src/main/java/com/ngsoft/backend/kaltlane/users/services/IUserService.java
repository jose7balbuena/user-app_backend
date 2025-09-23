package com.ngsoft.backend.kaltlane.users.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import com.ngsoft.backend.kaltlane.users.entites.User;

/**
 * User service interface.
 */
public interface IUserService {
    /**
     * Retrieves all users.
     * @return a list of all users
     */
    List<User> findAll();
    /**
     * Retrieves a paginated list of users.
     * @param pageable pagination information
     * @return a paginated list of users
     */
    Page<User> findAll(Pageable pageable);
    /**
     * Finds a user by their ID.
     * @param id the ID of the user
     * @return an Optional containing the user if found, or empty if not found
     */
    Optional<User> findById(@NonNull Long id);
    /**
     * Saves a user.
     * @param user the user to save
     * @return the saved user
     */
    User save(User user);
    /**
     * Deletes a user by their ID.
     * @param id the ID of the user to delete
     */
    void deleteById(Long id);
    
}
