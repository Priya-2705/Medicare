package mypackage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mypackage.entities.Role;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long>{

}
