/**
 *
 */
package org.simon.pascal.security.auth;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author simon.pascal.ngos
 *
 */

public class JwtUser implements UserDetails {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final Long id;
	private final String name;
	private final String password;
	private final String email;
	private final Collection<? extends GrantedAuthority> authorities;

	public JwtUser(Long id, String name, String email, String password,
			Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String getUsername() {
		return this.name;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	public String getEmail() {
		return email;
	}

	@JsonIgnore
	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (id == null ? 0 : id.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final JwtUser other = (JwtUser) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "JwtUser{" + "id=" + id + ", name='" + name + '\'' + ", password='" + password + '\'' + ", email='"
				+ email + '\'' + ", authorities=" + authorities + '}';
	}

}
