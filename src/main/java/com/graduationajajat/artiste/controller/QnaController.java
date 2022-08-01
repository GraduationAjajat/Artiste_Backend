package com.graduationajajat.artiste.controller;

import com.graduationajajat.artiste.dto.response.CommonResponseDto;
import com.graduationajajat.artiste.dto.response.ResponseDto;
import com.graduationajajat.artiste.model.Qna;
import com.graduationajajat.artiste.model.User;
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
@RequestMapping("/api/v1/qna")
@Api(tags = {"QNA API"})
public class QnaController {

    private final UserService userService;
    private final QnaService qnaService;

    // 문의 사항 목록 조회 Controller
    @ApiOperation(value = "문의 사항 목록 조회")
    @GetMapping("")
    public ResponseEntity<? extends ResponseDto> getQnas() {
        List<Qna> qnaList  = qnaService.getQnas();
        return ResponseEntity.ok().body(new CommonResponseDto<>(qnaList));
    }

    // 사용자 문의 사항 추가 Controller
    @ApiOperation(value = "사용자 문의 사항 추가")
    @PostMapping("")
    public ResponseEntity createQna(@RequestBody String qnaContent) {
        User user = userService.getMyInfo();
        qnaService.createQna(user, qnaContent);
        return new ResponseEntity(HttpStatus.OK);
    }
}
