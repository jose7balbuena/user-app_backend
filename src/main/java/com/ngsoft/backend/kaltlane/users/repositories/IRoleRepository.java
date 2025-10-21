package com.ngsoft.backend.kaltlane.users.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.ngsoft.backend.kaltlane.users.entites.Role;

public interface IRoleRepository extends CrudRepository<Role, Long> {

    Optional<Role> findByName(String name);

}
