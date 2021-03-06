/**
 *
 */
package org.simon.pascal.service;

import org.simon.pascal.entity.User;
import org.simon.pascal.repository.UserRepository;
import org.simon.pascal.security.auth.JwtUserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author simon.pascal.ngos
 *
 */
@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

	private UserRepository userRepository;

	/**
	 * Injects UserRepository instance
	 *
	 * @param userRepository
	 *            to inject
	 */
	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * Finds UserDetails by given username
	 *
	 * @param username
	 *            which is used to search user
	 * @return UserDetails
	 * @throws UsernameNotFoundException
	 *             if user with given name does not exists
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		final User user = userRepository.findByName(username);
		if (user == null) {
			throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
		}
		return JwtUserFactory.create(user);
	}

}
