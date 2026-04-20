package com.liftnet.liftnet_backend.empresa.repository;

import com.liftnet.liftnet_backend.empresa.entity.EmpresaProfile;
import com.liftnet.liftnet_backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmpresaRepository extends JpaRepository<EmpresaProfile, UUID> {

    Optional<EmpresaProfile> findByUser(User user);
}
