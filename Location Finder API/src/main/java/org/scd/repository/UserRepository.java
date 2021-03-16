package org.scd.repository;

import org.scd.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * User Repository
 */
public interface UserRepository extends MongoRepository<User, String> {
    /**
     * Find user by email
     *
     * @param email - unique email address
     * @return
     */
    User findByEmail(final String email);

    User save(User user);
}