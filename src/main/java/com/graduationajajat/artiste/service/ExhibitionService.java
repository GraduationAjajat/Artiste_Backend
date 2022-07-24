package com.graduationajajat.artiste.service;

import com.graduationajajat.artiste.dto.ExhibitionDetailResponseDto;
import com.graduationajajat.artiste.dto.ExhibitionDto;
import com.graduationajajat.artiste.dto.ExhibitionResponseDto;
import com.graduationajajat.artiste.model.*;
import com.graduationajajat.artiste.repository.ArtRepository;
import com.graduationajajat.artiste.repository.ExhibitionRepository;
import com.graduationajajat.artiste.repository.ExhibitionTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;
    private final ExhibitionTagRepository exhibitionTagRepository;
    private final ArtRepository artRepository;

    // 전시회 등록
    @Transactional
    public Exhibition createExhibition(User user, ExhibitionDto exhibitionDto) {

        // 전시회 등록
        Exhibition exhibition = Exhibition.builder()
                    .user(user)
                    .exhibitionName(exhibitionDto.getExhibitionName())
                    .startDate(exhibitionDto.getStartDate())
                    .endDate(exhibitionDto.getEndDate())
                    .exhibitionDesc(exhibitionDto.getExhibitionDesc())
                    .build();

        exhibition = exhibitionRepository.save(exhibition);

        // 전시회 특징 등록
        for(Tag tag : exhibitionDto.getTagList()) {
            ExhibitionTag exhibitionTag = ExhibitionTag.builder()
                    .exhibition(exhibition)
                    .tag(tag)
                    .build();
            exhibitionTagRepository.save(exhibitionTag);
        }

        // 작품 등록
        artRepository.saveAll(exhibitionDto.getArtList());

        return exhibition;
    }

    // 마감 전 전시회 전체 조회
    public List<ExhibitionResponseDto> getExhibitions(List<Tag> tags, String sortBy) {
        if(sortBy == null) sortBy = "createdDate";
        LocalDateTime now = LocalDateTime.now(); // 지금 기준 마감 전
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy); // 정렬

        List<Exhibition> exhibitionList = exhibitionRepository.findAllByEndDateBefore(now, sort);

        // 전시회 태그
        List<ExhibitionResponseDto> exhibitionResponseDtoList = new ArrayList<>();
        for(Exhibition exhibition : exhibitionList) {

            List<Tag> tagList = exhibitionTagRepository.findByExhibitionId(exhibition.getId());
            // 카테고리 분류
            if(!tags.isEmpty() && !Objects.equals(tagList, tags)) continue;
            List<Art> artList = artRepository.findAllByExhibitionId(exhibition.getId());

            exhibitionResponseDtoList.add(ExhibitionResponseDto.builder()
                    .exhibitionName(exhibition.getExhibitionName())
                    .thumbnail(artList.get(0).getArtImage())
                    .username(exhibition.getUser().getUsername())
                    .startDate(exhibition.getStartDate())
                    .endDate(exhibition.getEndDate())
                    .tagList(tagList)
                    .scrapCount(exhibition.getScrapCount()).build());
        }
        return exhibitionResponseDtoList;
    }

    // 검색
    public List<ExhibitionResponseDto> getSearchExhibitions(String search, List<Tag> tags, String sortBy) {
        if(sortBy == null) sortBy = "createdDate";
        LocalDateTime now = LocalDateTime.now(); // 지금 기준 마감 전
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy); // 정렬

        List<Exhibition> exhibitionList = exhibitionRepository.findAllByExhibitionNameContainsAndEndDateBefore(search, now, sort);

        // 전시회 태그
        List<ExhibitionResponseDto> exhibitionResponseDtoList = new ArrayList<>();
        for(Exhibition exhibition : exhibitionList) {

            List<Tag> tagList = exhibitionTagRepository.findByExhibitionId(exhibition.getId());
            // 카테고리 분류
            if(!tags.isEmpty() && !Objects.equals(tagList, tags)) continue;
            List<Art> artList = artRepository.findAllByExhibitionId(exhibition.getId());

            exhibitionResponseDtoList.add(ExhibitionResponseDto.builder()
                    .exhibitionName(exhibition.getExhibitionName())
                    .thumbnail(artList.get(0).getArtImage())
                    .username(exhibition.getUser().getUsername())
                    .startDate(exhibition.getStartDate())
                    .endDate(exhibition.getEndDate())
                    .tagList(tagList)
                    .scrapCount(exhibition.getScrapCount()).build());
        }
        return exhibitionResponseDtoList;
    }

    // 전시회 상세 페이지 조회
    public ExhibitionDetailResponseDto getExhibitionDetail(Long exhibitionId) {
        Exhibition exhibition = exhibitionRepository.findById(exhibitionId).orElse(null);
        exhibition.setHits(exhibition.getHits() + 1); // 조회 수 증가
        List<Art> artList = artRepository.findAllByExhibitionId(exhibitionId);
        assert exhibition != null;
        return ExhibitionDetailResponseDto.builder()
                .exhibitionName(exhibition.getExhibitionName())
                    .username(exhibition.getUser().getUsername())
                    .exhibitionDesc(exhibition.getExhibitionDesc())
                    .artList(artList).build();
    }

    // 나의 전시 목록 조회
    public List<ExhibitionResponseDto> getUserExhibitions(User user) {
        List<Exhibition> exhibitionList = exhibitionRepository.findAllByUserId(user.getId(),Sort.by(Sort.Direction.DESC, "createdDate"));
        List<ExhibitionResponseDto> exhibitionResponseDtoList = new ArrayList<>();
        for(Exhibition exhibition : exhibitionList) {
            List<Tag> tagList = exhibitionTagRepository.findByExhibitionId(exhibition.getId());
            List<Art> artList = artRepository.findAllByExhibitionId(exhibition.getId());
            exhibitionResponseDtoList.add(ExhibitionResponseDto.builder()
                    .exhibitionId(exhibition.getId())
                    .exhibitionName(exhibition.getExhibitionName())
                    .thumbnail(artList.get(0).getArtImage())
                    .username(exhibition.getUser().getUsername())
                    .startDate(exhibition.getStartDate())
                    .endDate(exhibition.getEndDate())
                    .tagList(tagList)
                    .scrapCount(exhibition.getScrapCount()).build());
        }

        return exhibitionResponseDtoList;
    }

    // 작가의 전시 목록 조회
    public List<ExhibitionResponseDto> getArtistExhibitions(Long exhibitionId) {
        Exhibition exhibition = exhibitionRepository.findById(exhibitionId).orElse(null);
        assert exhibition != null;
        return getUserExhibitions(exhibition.getUser());

    }
}
