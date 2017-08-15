/**
 *
 */
package org.simon.pascal.security.auth;

import java.io.Serializable;

/**
 * @author simon.pascal.ngos
 *
 */
public class JwtAuthenticationRequest implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String username;
	private String email;
	private String password;

	public JwtAuthenticationRequest() {
		super();
	}

	public JwtAuthenticationRequest(String username, String email, String password) {
		this.setUsername(username);
		this.setEmail(email);
		this.setPassword(password);
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
