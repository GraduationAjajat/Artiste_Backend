package com.graduationajajat.artiste.repository;

import com.graduationajajat.artiste.model.Qna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface QnaRepository extends JpaRepository<Qna, Long> {
}