package com.egg.homerepair.repository;

import com.egg.homerepair.entity.User;
import com.egg.homerepair.enums.Professions;
import com.egg.homerepair.enums.Roles;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, String> {

    User findByEmailIgnoreCase(String email);

    List<User> findByRoleAndAltaTrueOrderByRatingDesc(Roles role);

    List<User> findByRoleAndAltaTrueAndProfessionOrderByRatingDesc(
            Roles role,
            Professions profession);

    @Query("SELECT u FROM User u "
            + "WHERE u.role = :role AND u.alta = true "
            + "AND (LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) "
            + "OR LOWER(u.lastname) LIKE LOWER(CONCAT('%', :search, '%')) "
            + "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))) "
            + "ORDER BY u.rating DESC")
    List<User> searchActiveProviders(
            @Param("role") Roles role,
            @Param("search") String search);

    @Query("SELECT u FROM User u "
            + "WHERE u.role = :role AND u.alta = true AND u.profession = :profession "
            + "AND (LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) "
            + "OR LOWER(u.lastname) LIKE LOWER(CONCAT('%', :search, '%')) "
            + "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))) "
            + "ORDER BY u.rating DESC")
    List<User> searchActiveProvidersByProfession(
            @Param("role") Roles role,
            @Param("profession") Professions profession,
            @Param("search") String search);

    @Query("SELECT u FROM User u "
            + "WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) "
            + "OR LOWER(u.lastname) LIKE LOWER(CONCAT('%', :search, '%')) "
            + "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<User> searchAllUsers(@Param("search") String search);
}
