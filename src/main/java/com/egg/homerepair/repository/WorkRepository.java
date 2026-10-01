package com.egg.homerepair.repository;

import com.egg.homerepair.entity.User;
import com.egg.homerepair.entity.Work;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkRepository extends JpaRepository<Work, String> {

    List<Work> findByProvider(User provider);

    List<Work> findByCustomer(User customer);
}
