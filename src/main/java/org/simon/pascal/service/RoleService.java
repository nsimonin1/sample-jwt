/**
 *
 */
package org.simon.pascal.service;

import java.util.List;
import java.util.Optional;

import org.simon.pascal.entity.Role;
import org.simon.pascal.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author simon.pascal.ngos
 *
 */
@Service
@Transactional
public class RoleService {

	private final RoleRepository roleRepository;

	public RoleService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	public Role enregistrer(Role role) {
		return roleRepository.save(role);
	}

	public Optional<Role> findRoleByLabel(String label) {
		return roleRepository.findOneByLabel(label);
	}

	public List<Role> getRoles() {
		return roleRepository.findAll();
	}

}
