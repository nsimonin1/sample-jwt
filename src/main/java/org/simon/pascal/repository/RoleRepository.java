/**
 *
 */
package org.simon.pascal.repository;

import java.util.Optional;

import org.simon.pascal.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author simon.pascal.ngos
 *
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findOneByLabel(String label);

}
