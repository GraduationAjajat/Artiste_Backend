package com.graduationajajat.artiste.controller;

import com.graduationajajat.artiste.dto.request.ArtDto;
import com.graduationajajat.artiste.dto.request.ExhibitionDto;
import com.graduationajajat.artiste.dto.response.ExhibitionDetailResponseDto;
import com.graduationajajat.artiste.dto.response.ExhibitionResponseDto;
import com.graduationajajat.artiste.model.ExhibitionTagName;
import com.graduationajajat.artiste.model.FileFolder;
import com.graduationajajat.artiste.model.User;
import com.graduationajajat.artiste.service.ExhibitionService;
import com.graduationajajat.artiste.service.FileProcessService;
import com.graduationajajat.artiste.service.UserService;
import com.sun.istack.Nullable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public ResponseEntity<Object> createExhibition(@Nullable @RequestPart(value = "artImageList", required = false) List<MultipartFile> artImageList,
                                                   @RequestPart(value = "artNameList", required = false) List<String> artNameList,
                                                   @RequestPart(value = "artDescList", required = false) List<String> artDescList,
                                                   @RequestPart(value = "exhibitionName", required = false) String exhibitionName,
                                                   @RequestPart(value = "exhibitionStartDate", required = false) LocalDateTime exhibitionStartDate,
                                                   @RequestPart(value = "exhibitionEndDate", required = false) LocalDateTime exhibitionEndDate,
                                                   @RequestPart(value = "exhibitionDesc", required = false) String exhibitionDesc,
                                                   @RequestPart(value = "tagList", required = false) List<ExhibitionTagName> tagList) {
        User user = userService.getMyInfo();
        List<ArtDto> artDtoList = new ArrayList<>();
        for(int i = 0; i < artImageList.size(); i++) {
            ArtDto artDto = ArtDto.builder().artImage(artImageList.get(i)).artName(artNameList.get(i)).artDesc(artDescList.get(i)).build();
            artDtoList.add(artDto);
        }
        ExhibitionDto exhibitionDto = ExhibitionDto.builder()
                .exhibitionName(exhibitionName).exhibitionStartDate(exhibitionStartDate).exhibitionEndDate(exhibitionEndDate).exhibitionDesc(exhibitionDesc).tagList(tagList).artList(artDtoList).build();
        return ResponseEntity.ok(exhibitionService.createExhibition(user, exhibitionDto));
    }

    // Style-Transfer API 호출 Controller
    @ApiOperation(value = "이미지 변환(style-transfer)")
    @PostMapping("/transfer")
    public ResponseEntity<Object> styleTransfer(@RequestPart(value = "image1") MultipartFile styleFile, @RequestPart(value = "image2") MultipartFile contentFile) {
        // 이미지 변환
        String transferUrl = "http://ec2-3-35-8-193.ap-northeast-2.compute.amazonaws.com:5000/transfer";

        // Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Body
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("style_img", styleFile);
        body.add("content_img", contentFile);

        // POST
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> transferResponse = restTemplate.postForEntity(transferUrl, requestEntity, String.class);

        HttpHeaders transferHeader = new HttpHeaders();
        transferHeader.add("Content-Type", "application/json; charset=UTF-8");

        return new ResponseEntity<>(transferResponse, transferHeader, HttpStatus.OK);
    }

    // 마감 전 전시회 전체 조회 Controller (승인만)
    @ApiOperation(value = "전시회 페이지 조회(태그[풍경, 인물, 꽃, 정물, 모네, 고흐, 바로크, 르네상스], 정렬[최신순(createdDate), 조회순(hits), 좋아요순(scrapCount)])")
    @GetMapping("")
    public ResponseEntity<List<ExhibitionResponseDto>> getExhibitions(@RequestParam(name = "tags", required = false) ExhibitionTagName tags, @RequestParam(name = "sortBy", required = false) String sortBy) {
        User user = userService.getMyInfo();
        return ResponseEntity.ok(exhibitionService.getExhibitions(user, tags, sortBy));
    }

    // 전시회 검색 Controller (승인만)
    @ApiOperation(value = "전시회명 검색")
    @GetMapping("/search")
    public ResponseEntity<List<ExhibitionResponseDto>> getSearchExhibitions(@RequestParam(name = "search") String search, @RequestParam(name = "tags", required = false) ExhibitionTagName tags, @RequestParam(name = "sortBy", required = false) String sortBy) {
        User user = userService.getMyInfo();
        return ResponseEntity.ok(exhibitionService.getSearchExhibitions(user, search, tags, sortBy));
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
        User user = userService.getMyInfo();
        return ResponseEntity.ok(exhibitionService.getArtistExhibitions(user, exhibitionId));
    }

    // 팔로워/팔로우의 전시 목록 조회 Controller (승인만)
    @ApiOperation(value = "팔로워/팔로우의 전시 목록 조회")
    @GetMapping("/follow/{followId}")
    public ResponseEntity<List<ExhibitionResponseDto>> getFollowUserExhibitions(@PathVariable("followId") Long followId) {
        User user = userService.getMyInfo();
        return ResponseEntity.ok(exhibitionService.getFollowUserExhibitions(user, followId));
    }
}
