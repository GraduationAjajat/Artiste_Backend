package com.graduationajajat.artiste.controller;

import com.graduationajajat.artiste.dto.request.AnswerDto;
import com.graduationajajat.artiste.dto.response.*;
import com.graduationajajat.artiste.model.Comment;
import com.graduationajajat.artiste.service.CommentService;
import com.graduationajajat.artiste.service.ExhibitionService;
import com.graduationajajat.artiste.service.QnaService;
import com.graduationajajat.artiste.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/v1/admin")
@Api(tags = {"Admin API"})
public class AdminController {

    private final ExhibitionService exhibitionService;
    private final QnaService qnaService;
    private final UserService userService;
    private final CommentService commentService;

    // 대기 중인 전시회 목록 조회 Controller
    @ApiOperation(value = "대기 중인 전시회 목록 조회")
    @GetMapping("/waiting")
    public ResponseEntity<? extends ResponseDto> getWaitingExhibitionList() {
        List<WaitingExhibitionResponseDto> waitingExhibitionResponseDtoList = exhibitionService.getWaitingExhibitions();
        return ResponseEntity.ok().body(new CommonResponseDto<>(waitingExhibitionResponseDtoList));
    }

    // 대기 중인 전시회 승인 Controller
    @ApiOperation(value = "대기 중인 전시회 승인")
    @PutMapping("/waiting/{exhibitionId}")
    public ResponseEntity approveExhibitionList(@PathVariable("exhibitionId") Long exhibitionId) {
        exhibitionService.approveExhibitions(exhibitionId);
        return new ResponseEntity(HttpStatus.OK);
    }

    // 문의 사항 답변 Controller
    @ApiOperation(value = "문의 사항 답변")
    @PutMapping("/qna")
    public ResponseEntity answerQna(@RequestBody AnswerDto qnaDto) {
        qnaService.answerQna(qnaDto.getQnaId(), qnaDto.getQnaAnswer());
        return new ResponseEntity(HttpStatus.OK);
    }

    // 회원 관리 목록 조회 Controller
    @ApiOperation(value = "회원 관리 목록 조회")
    @GetMapping("/user")
    public ResponseEntity<? extends ResponseDto> getUserList() {
        List<UserResponseDto> userResponseDtoList = userService.getUsers();
        return ResponseEntity.ok().body(new CommonResponseDto<>(userResponseDtoList));
    }

    // 회원 댓글 조회 Controller
    @ApiOperation(value = "회원 댓글 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<? extends ResponseDto> getUserCommentList(@PathVariable("userId") Long userId) {

        List<Comment> commentList = commentService.getUserComments(userId);

        return ResponseEntity.ok().body(new CommonResponseDto<>(commentList));
    }

    // 회원 댓글 삭제 Controller
    @ApiOperation(value = "회원 댓글 삭제")
    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(@PathVariable("commentId") Long commentId) {

        commentService.deleteComment(commentId);

        return new ResponseEntity(HttpStatus.OK);
    }

}
