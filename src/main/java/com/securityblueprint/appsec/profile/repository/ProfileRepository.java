package com.securityblueprint.appsec.profile.repository;

import com.securityblueprint.appsec.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByKeycloakSubject(String keycloakSubject);

    boolean existsByKeycloakSubject(String keycloakSubject);
}
