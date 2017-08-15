/**
 *
 */
package org.simon.pascal.controller;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;

import org.simon.pascal.entity.Role;
import org.simon.pascal.entity.User;
import org.simon.pascal.exception.InvalidPasswordException;
import org.simon.pascal.exception.UserNotFoundException;
import org.simon.pascal.security.auth.JwtAuthenticationRequest;
import org.simon.pascal.security.auth.JwtAuthenticationResponse;
import org.simon.pascal.security.auth.JwtUser;
import org.simon.pascal.security.auth.JwtUtil;
import org.simon.pascal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author simon.pascal.ngos
 *
 */
@RestController
public class AuthController extends BaseController {
	@Value("${auth.header}")
	private String tokenHeader;

	public final static String SIGNUP_URL = "/api/auth/signup";
	public final static String SIGNIN_URL = "/api/auth/signin";
	public final static String REFRESH_TOKEN_URL = "/api/auth/token/refresh";

	private AuthenticationManager authenticationManager;
	private JwtUtil jwtUtil;
	private UserDetailsService userDetailsService;
	private UserService userService;

	/**
	 * Injects AuthenticationManager instance
	 *
	 * @param authenticationManager
	 *            to inject
	 */
	@Autowired
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	/**
	 * Injects JwtUtil instance
	 *
	 * @param jwtUtil
	 *            to inject
	 */
	@Autowired
	public void setJwtTokenUtil(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	/**
	 * Injects UserDetailsService instance
	 *
	 * @param userDetailsService
	 *            to inject
	 */
	@Autowired
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	/**
	 * Injects UserService instance
	 *
	 * @param userService
	 *            to inject
	 */
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Bean
	private PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Adds new user and returns authentication token
	 *
	 * @param authenticationRequest
	 *            request with username, email and password fields
	 * @return generated JWT
	 * @throws AuthenticationException
	 */
	@PostMapping(SIGNUP_URL)
	public ResponseEntity createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest)
			throws AuthenticationException {

		final String name = authenticationRequest.getUsername();
		final String email = authenticationRequest.getEmail();
		final String password = authenticationRequest.getPassword();
		LOG.info("[POST] CREATING TOKEN FOR User " + name);
		final Role role = new Role(1L, "USER");
		userService.save(new User(0L, name, email, password, role));
		JwtUser userDetails;

		try {
			userDetails = (JwtUser) userDetailsService.loadUserByUsername(name);
		} catch (final UsernameNotFoundException ex) {
			LOG.error(ex.getMessage());
			throw new UserNotFoundException();
		} catch (final Exception ex) {
			LOG.error(ex.getMessage());
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		}

		final Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(name, password));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		final String token = jwtUtil.generateToken(userDetails);
		return ResponseEntity.ok(new JwtAuthenticationResponse(token));
	}

	/**
	 * Returns authentication token for given user
	 *
	 * @param authenticationRequest
	 *            with username and password
	 * @return generated JWT
	 * @throws AuthenticationException
	 */
	@PostMapping(SIGNIN_URL)
	public ResponseEntity getAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest)
			throws AuthenticationException {

		final String name = authenticationRequest.getUsername();
		final String password = authenticationRequest.getPassword();
		LOG.info("[POST] GETTING TOKEN FOR User " + name);
		JwtUser userDetails;

		try {
			userDetails = (JwtUser) userDetailsService.loadUserByUsername(name);
		} catch (UsernameNotFoundException | NoResultException ex) {
			LOG.error(ex.getMessage());
			throw new UserNotFoundException();
		} catch (final Exception ex) {
			LOG.error(ex.getMessage());
			return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
		}

		if (!passwordEncoder().matches(password, userDetails.getPassword())) {
			throw new InvalidPasswordException();
		}

		final Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(name, password));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		final String token = jwtUtil.generateToken(userDetails);
		return ResponseEntity.ok(new JwtAuthenticationResponse(token));
	}

	/**
	 * Refreshes token
	 *
	 * @param request
	 *            with old JWT
	 * @return Refreshed JWT
	 */
	@PostMapping(REFRESH_TOKEN_URL)
	public ResponseEntity<JwtAuthenticationResponse> refreshAuthenticationToken(HttpServletRequest request) {
		final String token = request.getHeader(tokenHeader);
		LOG.info("[POST] REFRESHING TOKEN");
		final String refreshedToken = jwtUtil.refreshToken(token);
		return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
	}
}
