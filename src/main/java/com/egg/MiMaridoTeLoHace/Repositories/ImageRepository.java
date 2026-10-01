package com.egg.MiMaridoTeLoHace.Repositories;

import com.egg.MiMaridoTeLoHace.Entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {
}
