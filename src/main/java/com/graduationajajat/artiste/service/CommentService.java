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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ExhibitionRepository exhibitionRepository;

    // 사용자 댓글 목록 조회
    public List<Comment> getCommentsByUserId(Long userId) {
        return commentRepository.findAllByUserId(userId);
    }

    // 전시회 댓글 목록 조회
    public List<CommentResponseDto> getCommentsByExhibitionId(Long exhibitionId) {
        List<Comment> commentList = commentRepository.findAllByExhibitionId(exhibitionId);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for(Comment comment : commentList) {
            CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                    .commentId(comment.getId())
                    .user(comment.getUser())
                    .commentContent(comment.getCommentContent())
                    .commentDate(comment.getCreatedDate()).build();
            commentResponseDtoList.add(commentResponseDto);

        }
        return commentResponseDtoList;
    }

    // 사용자 전시회 댓글 추가
    @Transactional
    public void createComment(User user, Long exhibitionId, String commentContent) {
        Optional<Exhibition> exhibition = exhibitionRepository.findById(exhibitionId);
        Comment comment = Comment.builder().user(user).exhibition(exhibition.get()).commentContent(commentContent).build();

        commentRepository.save(comment);

        exhibition.get().setCommentCount(exhibition.get().getCommentCount() + 1); // 댓글 수 증가
    }

    // 사용자 댓글 삭제
    @Transactional
    public void deleteComment(User user, Long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        Exhibition exhibition = comment.get().getExhibition();
        exhibition.setCommentCount(exhibition.getCommentCount() - 1); // 댓글 수 감소
        commentRepository.deleteByUserIdAndId(user.getId(), commentId);

    }

    // 관리자 - 회원 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        Exhibition exhibition = comment.get().getExhibition();
        exhibition.setCommentCount(exhibition.getCommentCount() - 1); // 댓글 수 감소
        commentRepository.deleteById(commentId);

    }

    // 관리자 - 회원 댓글 조회
    public List<Comment> getUserComments(Long userId) {
        return commentRepository.findAllByUserId(userId);
    }
}
