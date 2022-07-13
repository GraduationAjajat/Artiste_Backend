package com.graduationajajat.artiste.repository;

import com.graduationajajat.artiste.model.Exhibition;
import com.graduationajajat.artiste.model.ExhibitionTag;
import com.graduationajajat.artiste.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExhibitionTagRepository extends JpaRepository<ExhibitionTag, Long> {

    List<Tag> findByExhibitionId(Long exhibitionId);
}
