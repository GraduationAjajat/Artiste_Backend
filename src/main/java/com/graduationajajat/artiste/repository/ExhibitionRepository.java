package com.graduationajajat.artiste.repository;

import com.graduationajajat.artiste.model.Exhibition;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {

    List<Exhibition> findAllByUserId(Long id, Sort sort);

    List<Exhibition> findAllByEndDateBefore(LocalDateTime now, Sort sort);

    List<Exhibition> findAllByExhibitionNameContainsAndEndDateBefore(String search, LocalDateTime now, Sort sort);
}