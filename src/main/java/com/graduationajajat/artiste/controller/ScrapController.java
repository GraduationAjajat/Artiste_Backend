package com.graduationajajat.artiste.controller;

import com.graduationajajat.artiste.dto.response.CommonResponseDto;
import com.graduationajajat.artiste.dto.response.ResponseDto;
import com.graduationajajat.artiste.model.Scrap;
import com.graduationajajat.artiste.model.User;
import com.graduationajajat.artiste.service.ScrapService;
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
@RequestMapping("/api/v1/scrap")
@Api(tags = {"Scrap API"})
public class ScrapController {

    private final UserService userService;
    private final ScrapService scrapService;

    // 사용자 찜 목록 조회
    @ApiOperation(value = "사용자 찜 목록 조회")
    @GetMapping("")
    public ResponseEntity<? extends ResponseDto> getScrapsByUserId() {
        User user = userService.getMyInfo();
        List<Scrap> scrapList = scrapService.getScrapsByUserId(user.getId());
        return ResponseEntity.ok().body(new CommonResponseDto<>(scrapList));
    }

    // 사용자 찜 추가
    @ApiOperation(value = "사용자 찜 추가")
    @PostMapping("/{exhibitionId}")
    public ResponseEntity createScrap(@PathVariable("exhibitionId") Long exhibitionId) {
        User user = userService.getMyInfo();
        scrapService.createScrap(user, exhibitionId);
        return new ResponseEntity(HttpStatus.OK);
    }

    // 사용자 찜 삭제
    @ApiOperation(value = "사용자 찜 삭제")
    @DeleteMapping("/unscrap/{exhibitionId}")
    public ResponseEntity deleteScrap(@PathVariable("exhibitionId") Long exhibitionId) {
        User user = userService.getMyInfo();
        scrapService.deleteScrap(user, exhibitionId);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 사용자 찜 여부 조회
    @ApiOperation(value = "사용자 찜 여부 조회")
    @GetMapping("/check/{exhibitionId}")
    public ResponseEntity<Boolean> checkUserScrap(@PathVariable Long exhibitionId) {
        User user = userService.getMyInfo();
        return ResponseEntity.ok(scrapService.checkUserScrap(user, exhibitionId));
    }

}
