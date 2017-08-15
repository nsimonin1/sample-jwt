/**
 *
 */
package org.simon.pascal.service;

import java.util.List;

import org.simon.pascal.entity.User;
import org.springframework.stereotype.Service;

/**
 * @author simon.pascal.ngos
 *
 */
@Service
public interface UserService {
	User save(User user);

	void delete(Long id);

	List<User> findAll();

	User findById(Long id);

	User findByEmail(String email);

	User findByName(String name);
}
