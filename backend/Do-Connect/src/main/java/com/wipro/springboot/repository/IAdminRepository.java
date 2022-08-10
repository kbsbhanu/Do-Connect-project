package com.wipro.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.wipro.springboot.entity.Admin;
import java.util.Optional;

@Repository
public interface IAdminRepository extends JpaRepository<Admin, Long> {

	Admin findFirstByEmail(String email);

	Optional<Admin> findByEmail(String email);
}
