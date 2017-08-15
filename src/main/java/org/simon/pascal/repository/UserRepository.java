/**
 *
 */
package org.simon.pascal.repository;

import org.simon.pascal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author simon.pascal.ngos
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	/**
	 * Finds user by email
	 *
	 * @param email
	 *            to look for
	 * @return user by given email
	 */
	User findByEmail(String email);

	/**
	 * Finds user by name
	 *
	 * @param name
	 *            to look for
	 * @return user by given name
	 */
	User findByName(String name);
}
