package com.graduationajajat.artiste.repository;

import com.graduationajajat.artiste.model.Exhibition;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {

    // 작가로 조회
    List<Exhibition> findAllByUserId(Long id, Sort sort);

    // 전시회 마감 시간 이전 조회
    List<Exhibition> findAllByEndDateAfter(LocalDateTime now, Sort sort);

    // 검색어 & 전시회 마감 시간 이전 조회
    List<Exhibition> findAllByExhibitionNameContainsAndEndDateAfter(String search, LocalDateTime now, Sort sort);
}