package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CustomUser;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.time.Instant;

/**
 * Spring Data JPA repository for the {@link CustomUser} entity.
 */
@Repository
public interface CustomUserRepository extends JpaRepository<CustomUser, Long> {

    String USERS_BY_LOGIN_CACHE = "usersByLogin";

    String USERS_BY_EMAIL_CACHE = "usersByEmail";

    Optional<CustomUser> findOneByActivationKey(String activationKey);


    List<CustomUser> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);


    Optional<CustomUser> findOneByResetKey(String resetKey);

    Optional<CustomUser> findOneByEmailIgnoreCase(String email);

    Optional<CustomUser> findOneByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    Optional<CustomUser> findOneWithAuthoritiesById(Long id);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
    Optional<CustomUser> findOneWithAuthoritiesByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
    Optional<CustomUser> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    Page<CustomUser> findAllByLoginNot(Pageable pageable, String login);
}
