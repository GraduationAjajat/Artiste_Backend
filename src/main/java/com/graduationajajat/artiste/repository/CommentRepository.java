package com.graduationajajat.artiste.repository;

import com.graduationajajat.artiste.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 사용자 댓글 목록 조회
    List<Comment> findAllByUserId(Long userId);

    // 전시회 댓글 목록 조회
    List<Comment> findAllByExhibitionId(Long exhibitionId);

    // 사용자 댓글 삭제
    void deleteByUserIdAndId(Long userId, Long commentId);

    // 사용자 댓글 개수 조회
    Long countByUserId(Long userId);

}