package ru.alex0d.realestateagency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alex0d.realestateagency.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
