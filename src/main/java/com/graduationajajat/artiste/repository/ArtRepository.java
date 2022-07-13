package com.graduationajajat.artiste.repository;

import com.graduationajajat.artiste.model.Art;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtRepository extends JpaRepository<Art, Long> {

    List<Art> findAllByExhibitionId(Long exhibitionId);
}
