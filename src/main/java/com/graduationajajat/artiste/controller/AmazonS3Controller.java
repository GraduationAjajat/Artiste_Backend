package com.graduationajajat.artiste.controller;

import com.graduationajajat.artiste.model.FileFolder;
import com.graduationajajat.artiste.service.AwsS3Service;
import com.graduationajajat.artiste.service.FileProcessService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
public class AmazonS3Controller {

    private final FileProcessService fileProcessService;

    /**
     * Amazon S3에 파일 업로드
     * @return 성공 시 200 Success와 함께 업로드 된 파일의 파일명 리스트 반환
     */
    @ApiOperation(value = "Amazon S3에 파일 업로드", produces = "multipart/form-data")
    @PostMapping(value = "/art", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<String>> uploadArtList(@RequestParam("artList") List<MultipartFile> artList) {
        List<String> urlList = new ArrayList<>();
        for(MultipartFile multipartFile : artList) {
            String url = fileProcessService.uploadImage(multipartFile, FileFolder.ART_IMAGES);
            urlList.add(url);
        }
        return ResponseEntity.ok().body(urlList);
    }

}
