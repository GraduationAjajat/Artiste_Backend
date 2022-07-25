package com.graduationajajat.artiste.repository;

import com.graduationajajat.artiste.model.Art;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtRepository extends JpaRepository<Art, Long> {

    // 전시회 아이디로 조회
    List<Art> findAllByExhibitionId(Long exhibitionId);
}
