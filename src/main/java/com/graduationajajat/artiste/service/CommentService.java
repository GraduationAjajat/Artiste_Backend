package com.graduationajajat.artiste.service;

import com.graduationajajat.artiste.dto.response.CommentResponseDto;
import com.graduationajajat.artiste.model.Comment;
import com.graduationajajat.artiste.model.Exhibition;
import com.graduationajajat.artiste.model.User;
import com.graduationajajat.artiste.repository.CommentRepository;
import com.graduationajajat.artiste.repository.ExhibitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ExhibitionRepository exhibitionRepository;

    // 사용자 댓글 목록 조회
    @Transactional
    public List<Comment> getCommentsByUserId(Long userId) {

        return commentRepository.findAllByUserId(userId);
    }

    // 전시회 댓글 목록 조회
    @Transactional
    public List<CommentResponseDto> getCommentsByExhibitionId(Long exhibitionId) {
        List<Comment> commentList = commentRepository.findAllByExhibitionId(exhibitionId);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for(Comment comment : commentList) {
            CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                    .commentId(comment.getId())
                    .user(comment.getUser())
                    .commentContent(comment.getCommentContent()).build();
            commentResponseDtoList.add(commentResponseDto);

        }
        return commentResponseDtoList;
    }

    // 사용자 전시회 댓글 추가
    @Transactional
    public void createComment(User user, Long exhibitionId, String commentContent) {
        Exhibition exhibition = exhibitionRepository.findById(exhibitionId).get();
        Comment comment = Comment.builder().user(user).exhibition(exhibition).commentContent(commentContent).build();

        exhibition.setCommentCount(exhibition.getCommentCount() + 1); // 댓글 수 증가

        commentRepository.save(comment);
    }

    // 사용자 댓글 삭제
    @Transactional
    public void deleteComment(User user, Long commentId) {
        commentRepository.deleteByUserIdAndExhibitionId(user.getId(), commentId);
        Exhibition exhibition = exhibitionRepository.findById(commentId).get();
        exhibition.setCommentCount(exhibition.getCommentCount() - 1); // 댓글 수 감소

    }

}
