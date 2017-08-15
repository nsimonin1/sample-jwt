package org.simon.pascal.security.auth;

import java.util.ArrayList;
import java.util.List;

import org.simon.pascal.entity.Role;
import org.simon.pascal.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 *
 * @author simon.pascal.ngos
 *
 */
public class JwtUserFactory {
	private JwtUserFactory() {
	}

	public static JwtUser create(User user) {
		return new JwtUser(user.getId(), user.getName(), user.getEmail(), user.getPassword(),
				mapToGrantedAuthorities(user.getRole()));
	}

	private static List<GrantedAuthority> mapToGrantedAuthorities(Role role) {
		final List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(role.getLabel()));
		return authorities;
	}
}
