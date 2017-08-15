/**
 *
 */
package org.simon.pascal.security.auth;

import java.io.Serializable;

/**
 * @author simon.pascal.ngos
 *
 */
public class JwtAuthenticationResponse implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final String token;

	public JwtAuthenticationResponse(String token) {
		this.token = token;
	}

	public String getToken() {
		return this.token;
	}

	@Override
	public String toString() {
		return String.format("{\"token\":\"%s\"}", this.token);
	}
}
