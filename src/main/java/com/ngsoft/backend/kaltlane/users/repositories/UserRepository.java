package com.ngsoft.backend.kaltlane.users.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import com.ngsoft.backend.kaltlane.users.entites.User;

/**
 * Repository interface for User entities.
 * Provides CRUD operations for User entities.
 */
public interface UserRepository extends CrudRepository<User, Long> {

    Page<User> findAll(Pageable pageable);

}
