/**
 *
 */
package org.simon.pascal.controller;

import java.util.List;
import java.util.Optional;

import org.simon.pascal.entity.Role;
import org.simon.pascal.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author simon.pascal.ngos
 *
 */
@RestController
public class RoleController extends BaseController {
	public final static String CREATE_ROLE = "/api/roles";
	public final static String ALL_ROLES = "/api/roles";
	@Autowired
	private RoleService roleService;

	@PostMapping(CREATE_ROLE)
	public ResponseEntity<Role> createAuthenticationToken(@RequestBody String label) throws AuthenticationException {
		LOG.info("[POST] CREATING ROLE " + label);

		final Optional<Role> findRole = roleService.findRoleByLabel(label);

		if (findRole.isPresent()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).header("ROLE", "Ce role existe deja").body(null);
		}

		final Role role = new Role(label);
		roleService.enregistrer(role);

		return ResponseEntity.ok(role);

	}

	@GetMapping(ALL_ROLES)
	public ResponseEntity<List<Role>> getAll() {
		return ResponseEntity.ok(roleService.getRoles());
	}
}
