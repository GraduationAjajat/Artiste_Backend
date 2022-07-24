package com.graduationajajat.artiste.controller;

import com.graduationajajat.artiste.dto.CommentDto;
import com.graduationajajat.artiste.dto.CommonResponseDto;
import com.graduationajajat.artiste.dto.ResponseDto;
import com.graduationajajat.artiste.model.Comment;
import com.graduationajajat.artiste.model.Exhibition;
import com.graduationajajat.artiste.model.User;
import com.graduationajajat.artiste.service.CommentService;
import com.graduationajajat.artiste.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/v1/comment")
@Api(tags = {"Comment API"})
public class CommentController {
    private final UserService userService;
    private final CommentService commentService;

    // 사용자 댓글 목록 조회
    @ApiOperation(value = "사용자 댓글 목록 조회")
    @GetMapping("")
    public ResponseEntity<? extends ResponseDto> getCommentsByUserId() {
        User user = userService.getMyInfo();
        List<Comment> commentList = commentService.getCommentsByUserId(user.getId());
        List<Exhibition> exhibitionList = new ArrayList<>();
        for (Comment comment : commentList)
            exhibitionList.add(comment.getExhibition());
        return ResponseEntity.ok().body(new CommonResponseDto<>(exhibitionList));
    }

    // 전시회 댓글 목록 조회
    @ApiOperation(value = "전시회 댓글 목록 조회")
    @GetMapping("/{exhibitionId}")
    public ResponseEntity<? extends ResponseDto> getCommentsByExhibitoinId(@PathVariable("exhibitionId") Long exhibitionId) {
        List<Comment> commentList = commentService.getCommentsByExhibitionId(exhibitionId);
        return ResponseEntity.ok().body(new CommonResponseDto<>(commentList));
    }

    // 사용자 댓글 추가
    @ApiOperation(value = "사용자 댓글 추가")
    @PostMapping("/{exhibitionId}")
    public ResponseEntity createComment(@Valid @RequestBody CommentDto commentDto) {
        User user = userService.getMyInfo();
        commentService.createComment(user, commentDto.getExhibitionId(), commentDto.getContent());
        return new ResponseEntity(HttpStatus.OK);
    }

    // 사용자 댓글 삭제
    @ApiOperation(value = "사용자 댓글 삭제")
    @DeleteMapping("/{exhibitionId}")
    public ResponseEntity deleteComment(@PathVariable("exhibitionId") Long exhibitionId) {
        User user = userService.getMyInfo();
        commentService.deleteComment(user, exhibitionId);

        return new ResponseEntity(HttpStatus.OK);
    }

}
