package com.liftnet.liftnet_backend.postulante.repository;

import com.liftnet.liftnet_backend.postulante.entity.PostulanteProfile;
import com.liftnet.liftnet_backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PostulanteRepository extends JpaRepository<PostulanteProfile, UUID> {

    Optional<PostulanteProfile> findByUser(User user);
}
