/**
 *
 */
package org.simon.pascal.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.simon.pascal.utils.DummyDataGenerato.getUsers;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.simon.pascal.BaseTest;
import org.simon.pascal.entity.User;
import org.simon.pascal.exception.UserNotFoundException;
import org.simon.pascal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author simon.pascal.ngos
 *
 */
public class UserServiceImplTest extends BaseTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	@Autowired
	private UserService userService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test(timeout = 3000)
	public void findByIdTest() {
		final User user = getUsers(1).get(0);
		when(userRepository.exists(anyLong())).thenReturn(true);
		when(userRepository.findOne(anyLong())).thenReturn(user);
		final User fetchedUser = userService.findById(1L);

		verify(userRepository, times(1)).findOne(anyLong());
		assertNotNull("Fetched user shouldn't be NULL", fetchedUser);
		assertEquals("Should return appropriate user", user, fetchedUser);
	}

	@Test(timeout = 3000, expected = UserNotFoundException.class)
	public void findByIdNotExistsTest() {
		final User user = getUsers(1).get(0);
		when(userRepository.exists(anyLong())).thenReturn(false);
		when(userRepository.findOne(anyLong())).thenReturn(user);
		userService.findById(1L);
		verify(userRepository, times(1)).findOne(anyLong());
	}

	@Test(timeout = 5000)
	public void findByNameTest() {
		final User user = getUsers(1).get(0);
		when(userRepository.findByName(anyString())).thenReturn(user);
		final User fetchedUser = userService.findByName("Random name");

		verify(userRepository, times(1)).findByName(anyString());
		assertNotNull("Fetched user shouldn't be NULL", fetchedUser);
		assertEquals("Should return appropriate user", user, fetchedUser);
	}

	@Test(timeout = 5000)
	public void findByEmailTest() {
		final User user = getUsers(1).get(0);
		when(userRepository.findByEmail(anyString())).thenReturn(user);
		final User fetchedUser = userService.findByEmail("random@email.com");

		verify(userRepository, times(1)).findByEmail(anyString());
		assertNotNull("Fetched user shouldn't be NULL", fetchedUser);
		assertEquals("Should return appropriate user", user, fetchedUser);
	}

	@Test(timeout = 10000)
	public void findAllTest() {
		final List<User> users = getUsers(10);
		when(userRepository.findAll()).thenReturn(users);
		final List<User> fetchedUsers = userService.findAll();

		verify(userRepository, times(1)).findAll();
		assertNotNull("Fetched users shouldn't be NULL", fetchedUsers);
		assertEquals("Fetched users should have appropriate size", fetchedUsers.size(), users.size());

		fetchedUsers.forEach(user -> {
			assertNotNull("Any of the fetched users shouldn't be NULL", user);
			assertTrue("Fetched users should contain each user", users.contains(user));
		});
	}

	@Test(timeout = 5000)
	public void addUserTest() {
		final User user = getUsers(1).get(0);
		when(userRepository.save(any(User.class))).thenReturn(user);
		final User fetchedUser = userService.save(user);

		verify(userRepository, times(1)).save(any(User.class));
		assertNotNull("Fetched user shouldn't be NULL", fetchedUser);
		assertEquals("Should return appropriate user", user, fetchedUser);
	}

	@Test(timeout = 5000)
	public void updateUserTest() {
		final User user = getUsers(1).get(0);
		when(userRepository.save(any(User.class))).thenReturn(user);
		when(userRepository.exists(anyLong())).thenReturn(true);
		user.setName("Changed name");
		user.setEmail("changed@email.com");
		final User fetchedUser = userService.save(user);

		verify(userRepository, times(1)).exists(anyLong());
		verify(userRepository, times(1)).save(any(User.class));
		assertNotNull("Fetched user shouldn't be NULL", fetchedUser);
		assertEquals("Should return appropriate user", user, fetchedUser);
	}

	@Test(timeout = 3000)
	public void deleteUserTest() {
		when(userRepository.exists(anyLong())).thenReturn(true);
		userService.delete(1L);
		verify(userRepository, times(1)).exists(anyLong());
		verify(userRepository, times(1)).delete(anyLong());
	}

	@Test(timeout = 3000, expected = UserNotFoundException.class)
	public void deleteUserWhichDoesNotExistTest() {
		when(userRepository.exists(anyLong())).thenReturn(false);
		userService.delete(1L);
		verify(userRepository, times(1)).exists(anyLong());
	}

}
