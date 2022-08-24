package com.graduationajajat.artiste.controller;

import com.graduationajajat.artiste.dto.request.ExhibitionDto;
import com.graduationajajat.artiste.dto.response.ExhibitionDetailResponseDto;
import com.graduationajajat.artiste.dto.response.ExhibitionResponseDto;
import com.graduationajajat.artiste.model.ExhibitionTagName;
import com.graduationajajat.artiste.model.User;
import com.graduationajajat.artiste.service.ExhibitionService;
import com.graduationajajat.artiste.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/v1/exhibition")
@Api(tags = {"Exhibition API"})
public class ExhibitionController {

    private final UserService userService;
    private final ExhibitionService exhibitionService;

    // 전시회 등록 Controller
    @ApiOperation(value = "전시회 등록")
    @PostMapping("")
    public ResponseEntity<Object> createExhibition(@Valid @RequestBody ExhibitionDto exhibitionDto) {
        User user = userService.getMyInfo();
        return ResponseEntity.ok(exhibitionService.createExhibition(user, exhibitionDto));
    }

    // 마감 전 전시회 전체 조회 Controller (승인만)
    @ApiOperation(value = "전시회 페이지 조회(태그[풍경, 인물, 꽃, 정물, 모네, 고흐, 바로크, 르네상스], 정렬[최신순(createdDate), 조회순(hits), 좋아요순(scrapCount)])")
    @GetMapping("")
    public ResponseEntity<List<ExhibitionResponseDto>> getExhibitions(@RequestParam(name = "tags", required = false) List<ExhibitionTagName> tags, @RequestParam(name = "sortBy", required = false) String sortBy) {
        return ResponseEntity.ok(exhibitionService.getExhibitions(tags, sortBy));
    }

    // 전시회 검색 Controller (승인만)
    @ApiOperation(value = "전시회명 검색")
    @GetMapping("/search")
    public ResponseEntity<List<ExhibitionResponseDto>> getSearchExhibitions(@RequestParam(name = "search") String search, @RequestParam(name = "tags", required = false) List<ExhibitionTagName> tags, @RequestParam(name = "sortBy", required = false) String sortBy) {
        return ResponseEntity.ok(exhibitionService.getSearchExhibitions(search, tags, sortBy));
    }

    // 전시회 상세 페이지 조회 Controller (승인만)
    @ApiOperation(value = "전시회 상세 페이지 조회")
    @GetMapping("/{exhibitionId}")
    public ResponseEntity<ExhibitionDetailResponseDto> getExhibitionDetail(@PathVariable("exhibitionId") Long exhibitionId) {
        return ResponseEntity.ok(exhibitionService.getExhibitionDetail(exhibitionId));
    }

    // 사용자의 전시 목록 조회 Controller (마이페이지 - 승인 & 대기 상관 없이)
    @ApiOperation(value = "사용자의 전시 목록 조회(마이페이지 - 나의 전시 목록)")
    @GetMapping("/user")
    public ResponseEntity<List<ExhibitionResponseDto>> getUserExhibitions() {
        User user = userService.getMyInfo();
        return ResponseEntity.ok(exhibitionService.getUserExhibitions(user));
    }

    // 해당 전시회 작가의 다른 전시 목록 조회 Controller (승인만)
    @ApiOperation(value = "해당 전시회 작가의 다른 전시 목록 조회")
    @GetMapping("/artist/{exhibitionId}")
    public ResponseEntity<List<ExhibitionResponseDto>> getArtistExhibitions(@PathVariable("exhibitionId") Long exhibitionId) {
        return ResponseEntity.ok(exhibitionService.getArtistExhibitions(exhibitionId));
    }

    // 팔로워/팔로우의 전시 목록 조회 Controller (승인만)
    @ApiOperation(value = "팔로워/팔로우의 전시 목록 조회")
    @GetMapping("/follow/{followId}")
    public ResponseEntity<List<ExhibitionResponseDto>> getFollowUserExhibitions(@PathVariable("followId") Long followId) {
        return ResponseEntity.ok(exhibitionService.getFollowUserExhibitions(followId));
    }
}
