package com.egg.MiMaridoTeLoHace.Repositories;

import com.egg.MiMaridoTeLoHace.Entities.User;
import com.egg.MiMaridoTeLoHace.Enums.Professions;
import com.egg.MiMaridoTeLoHace.Enums.Roles;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, String> {

    User findByEmail(String email);

    default User searchByEmail(String email) {
        return findByEmail(email);
    }

    List<User> findByRole(Roles role);

    @Query("SELECT u FROM User u WHERE u.role = 'PROVIDER' AND u.alta = true ORDER BY u.rating DESC")
    List<User> AllProviderAlta();

    @Query("SELECT u FROM User u WHERE u.role = 'PROVIDER' AND u.alta = true AND u.profession = :profession ORDER BY u.rating DESC")
    List<User> searchByProfessionAlta(@Param("profession") Professions profession);

    @Query("SELECT u FROM User u WHERE u.role = 'PROVIDER' AND u.alta = true "
            + "AND (LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) "
            + "OR LOWER(u.lastname) LIKE LOWER(CONCAT('%', :search, '%')) "
            + "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))) "
            + "ORDER BY u.rating DESC")
    List<User> searchByAllAltaFiltro(@Param("search") String search);

    @Query("SELECT u FROM User u WHERE u.role = 'PROVIDER' AND u.alta = true AND u.profession = :profession "
            + "AND (LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) "
            + "OR LOWER(u.lastname) LIKE LOWER(CONCAT('%', :search, '%')) "
            + "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))) "
            + "ORDER BY u.rating DESC")
    List<User> searchByAllProfessionAltaFiltro(
            @Param("profession") Professions profession,
            @Param("search") String search);

    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) "
            + "OR LOWER(u.lastname) LIKE LOWER(CONCAT('%', :search, '%')) "
            + "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<User> searchEngine(@Param("search") String search);
}
