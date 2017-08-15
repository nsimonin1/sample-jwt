/**
 *
 */
package org.simon.pascal.security.auth;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author simon.pascal.ngos
 *
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

	private UserDetailsService userDetailsService;
	private JwtUtil jwtTokenUtil;

	@Value("${auth.header}")
	private String tokenHeader;

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
	 * Injects JwtUtil instance
	 *
	 * @param jwtUtil
	 *            to inject
	 */
	@Autowired
	public void setJwtTokenUtil(JwtUtil jwtUtil) {
		this.jwtTokenUtil = jwtUtil;
	}

	/**
	 * Checks if JWT present and valid
	 *
	 * @param request
	 *            with JWT
	 * @param response
	 * @param chain
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		final String authToken = request.getHeader(this.tokenHeader);
		final String username = jwtTokenUtil.getUsernameFromToken(authToken);

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			final UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

			if (jwtTokenUtil.validateToken(authToken, userDetails)) {
				final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				logger.info("Authenticated user " + username + ", setting security context");
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		chain.doFilter(request, response);
	}

}
