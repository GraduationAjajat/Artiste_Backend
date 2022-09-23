package com.graduationajajat.artiste.repository;

import com.graduationajajat.artiste.model.Exhibition;
import com.graduationajajat.artiste.model.ExhibitionState;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {

    // 사용자의 전시회 조회 (승인 대기 여부 상관없이)
    List<Exhibition> findAllByUserId(Long userId, Sort sort);

    // 작가의의 전시회 조회 (승인 대기 여부 상관없이)
    List<Exhibition> findAllByUserIdAndIdNot(Long userId, Long artisteId, Sort sort);

    // 작가로 승인된 전시회 조회
    List<Exhibition> findAllByUserIdAndExhibitionState(Long userId, ExhibitionState exhibitionState, Sort sort);

    // 승인된 전시회 마감 시간 이전 조회
    List<Exhibition> findAllByExhibitionStateAndExhibitionEndDateAfter(
            ExhibitionState exhibitionState, LocalDateTime now, Sort sort
    );

    // 승인된 검색어 & 전시회 마감 시간 이전 조회
    List<Exhibition> findAllByExhibitionStateAndExhibitionNameContainsAndExhibitionEndDateAfter(
            ExhibitionState exhibitionState, String search, LocalDateTime now, Sort sort
    );

    // 전시회 아이디로 승인된 전시회 조회
    Optional<Exhibition> findByIdAndExhibitionState(Long exhibitionId, ExhibitionState approval);

    // 대기 중인 전시회 조회
    List<Exhibition> findAllByExhibitionState(ExhibitionState wait);

    // 사용자 전시회 개수 조회
    Long countByUserId(Long userId);
}