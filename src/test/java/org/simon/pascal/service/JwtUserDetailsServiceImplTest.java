/**
 *
 */
package org.simon.pascal.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.simon.pascal.utils.DummyDataGenerato.getUsers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.simon.pascal.BaseTest;
import org.simon.pascal.entity.User;
import org.simon.pascal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author simon.pascal.ngos
 *
 */
public class JwtUserDetailsServiceImplTest extends BaseTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	@Autowired
	private UserDetailsService jwtUserDetailsService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test(timeout = 3000)
	public void loadUserByUsernameTest() {
		final User user = getUsers(1).get(0);
		when(userRepository.findByName(anyString())).thenReturn(user);
		final UserDetails fetchedUserDetails = jwtUserDetailsService.loadUserByUsername("random name");

		verify(userRepository, times(1)).findByName(anyString());
		assertNotNull("Fetched user details shouldn't be NULL", fetchedUserDetails);
		assertEquals("Should return appropriate username", user.getName(), fetchedUserDetails.getUsername());
		assertEquals("Should return appropriate password", user.getPassword(), fetchedUserDetails.getPassword());
	}

	@Test(timeout = 3000, expected = UsernameNotFoundException.class)
	public void loadUserWhichNotExistsTest() {
		when(userRepository.findByName(anyString())).thenThrow(UsernameNotFoundException.class);
		jwtUserDetailsService.loadUserByUsername("random name");
		verify(userRepository, times(1)).findByName(anyString());
		verifyNoMoreInteractions();
	}

}
