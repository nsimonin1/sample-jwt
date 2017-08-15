/**
 *
 */
package org.simon.pascal.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.simon.pascal.utils.DummyDataGenerato.getUsers;
import static org.simon.pascal.utils.JsonMapper.toJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.simon.pascal.entity.User;
import org.simon.pascal.security.auth.JwtAuthenticationRequest;
import org.simon.pascal.security.auth.JwtAuthenticationResponse;
import org.simon.pascal.security.auth.JwtUser;
import org.simon.pascal.security.auth.JwtUtil;
import org.simon.pascal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;

/**
 * @author simon.pascal.ngos
 *
 */
public class AuthControllerTest extends BaseControllerTest {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private JwtUtil jwtTokenUtil;

	@Mock
	private UserDetailsService userDetailsService;

	@Mock
	private UserService userService;

	@InjectMocks
	@Autowired
	private AuthController authController;

	private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"
			+ ".eyJzdWIiOiJyYW5kb20tbmFtZSIsInJvbGUiOiJVU0VSIiwiY3JlYXRlZCI6MX0"
			+ ".idLq2N5BJZiqkylavUVJTkGKiNlc_5xdFHISCoke3ss";

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		setUp(authController);
	}

	@Test(timeout = 1000)
	public void signUpTest() throws Exception {
		final User user = getUsers(1).get(0);
		final JwtUser jwtUser = new JwtUser(0L, user.getName(), user.getEmail(), user.getPassword(),
				Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getLabel())));

		final String password = passwordEncoder.encode(user.getPassword());
		final JwtAuthenticationRequest request = new JwtAuthenticationRequest(user.getName(), user.getEmail(),
				password);

		final JwtAuthenticationResponse expectedResponse = new JwtAuthenticationResponse(TOKEN);

		when(userService.save(any(User.class))).thenReturn(user);

		when(userDetailsService.loadUserByUsername(anyString())).thenReturn(jwtUser);

		when(jwtTokenUtil.generateToken(any(UserDetails.class))).thenReturn(TOKEN);

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(any(Authentication.class));

		final MvcResult result = mvc.perform(post(AuthController.SIGNUP_URL).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(toJson(request))).andReturn();

		final String content = result.getResponse().getContentAsString();
		final int status = result.getResponse().getStatus();

		verify(userService, times(1)).save(any(User.class));
		verify(userDetailsService, times(1)).loadUserByUsername(anyString());
		verify(jwtTokenUtil, times(1)).generateToken(any(UserDetails.class));
		verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));

		assertEquals("Expected HTTP status 200", 200, status);
		assertNotNull("Token shouldn't be NULL", content);
		assertTrue("Content shouldn't be empty", content.trim().length() > 0);
		assertEquals("Should return appropriate token", expectedResponse.toString(), content);
	}

	@Test(timeout = 10000)
	public void signInTest() throws Exception {
		final User user = getUsers(1).get(0);

		final String password = passwordEncoder.encode(user.getPassword());
		final JwtUser jwtUser = new JwtUser(0L, user.getName(), user.getEmail(), password,
				Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getLabel())));

		final JwtAuthenticationRequest request = new JwtAuthenticationRequest(user.getName(), user.getEmail(),
				user.getPassword());

		final JwtAuthenticationResponse expectedResponse = new JwtAuthenticationResponse(TOKEN);

		when(userDetailsService.loadUserByUsername(anyString())).thenReturn(jwtUser);
		when(jwtTokenUtil.generateToken(any(UserDetails.class))).thenReturn(TOKEN);
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(any(Authentication.class));

		final MvcResult result = mvc.perform(post(AuthController.SIGNIN_URL).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(toJson(request))).andReturn();

		final String content = result.getResponse().getContentAsString();
		final int status = result.getResponse().getStatus();

		verify(userDetailsService, times(1)).loadUserByUsername(anyString());
		verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));

		assertEquals("Expected HTTP status 200", 200, status);
		assertNotNull("Token shouldn't be NULL", content);
		assertTrue("Content shouldn't be empty", content.trim().length() > 0);
		assertEquals("Should return appropriate token", expectedResponse.toString(), content);
	}

	@Test(timeout = 10000)
	public void refreshTest() throws Exception {
		final JwtAuthenticationResponse expectedResponse = new JwtAuthenticationResponse(TOKEN);

		when(jwtTokenUtil.refreshToken(anyString())).thenReturn(TOKEN);

		final MvcResult result = mvc.perform(post(AuthController.REFRESH_TOKEN_URL)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(toJson(TOKEN)))
				.andReturn();

		final String content = result.getResponse().getContentAsString();
		final int status = result.getResponse().getStatus();

		verify(jwtTokenUtil, times(1)).refreshToken(anyString());

		assertEquals("Expected HTTP status 200", 200, status);
		assertNotNull("Token shouldn't be NULL", content);
		assertTrue("Content shouldn't be empty", content.trim().length() > 0);
		assertEquals("Should return appropriate token", expectedResponse.toString(), content);
	}

}
