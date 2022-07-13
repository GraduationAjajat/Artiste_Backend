package com.graduationajajat.artiste.repository;

import com.graduationajajat.artiste.model.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    // 사용자 찜 목록 조회
    List<Scrap> findAllByUserId(Long userId);

    // 사용자 찜 삭제
    void deleteByUserIdAndExhibitionId(Long id, Long exhibitionId);

    // 사용자 찜 여부
    boolean existsByUserIdAndExhibitionId(Long id, Long exhibitionId);
}
