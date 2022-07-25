package com.graduationajajat.artiste.repository;

import com.graduationajajat.artiste.model.ExhibitionTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExhibitionTagRepository extends JpaRepository<ExhibitionTag, Long> {

    // 전시회 아이디로 조회
    List<ExhibitionTag> findAllByExhibitionId(Long exhibitionId);
}
