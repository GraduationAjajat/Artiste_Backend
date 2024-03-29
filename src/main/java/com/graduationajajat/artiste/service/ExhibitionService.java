package com.graduationajajat.artiste.service;

import com.graduationajajat.artiste.dto.request.ArtDto;
import com.graduationajajat.artiste.dto.request.ExhibitionDto;
import com.graduationajajat.artiste.dto.response.*;
import com.graduationajajat.artiste.model.*;
import com.graduationajajat.artiste.repository.ArtRepository;
import com.graduationajajat.artiste.repository.ExhibitionRepository;
import com.graduationajajat.artiste.repository.ExhibitionTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;
    private final ExhibitionTagRepository exhibitionTagRepository;
    private final ArtRepository artRepository;
    private final FileProcessService fileProcessService;
    private final ScrapService scrapService;

    // 전시회 등록
    @Transactional
    public Exhibition createExhibition(User user, ExhibitionDto exhibitionDto) {

        // 전시회 등록
        Exhibition exhibition = Exhibition.builder()
                .user(user)
                .exhibitionName(exhibitionDto.getExhibitionName())
                .exhibitionStartDate(exhibitionDto.getExhibitionStartDate())
                .exhibitionEndDate(exhibitionDto.getExhibitionEndDate())
                .exhibitionDesc(exhibitionDto.getExhibitionDesc())
                .exhibitionState(ExhibitionState.WAIT)
                .build();

        exhibition = exhibitionRepository.save(exhibition);

        // 전시회 특징 등록
        for (ExhibitionTagName tag : exhibitionDto.getTagList()) {
            ExhibitionTag exhibitionTag = ExhibitionTag.builder()
                    .exhibition(exhibition)
                    .tag(tag)
                    .build();
            exhibitionTagRepository.save(exhibitionTag);
        }

        // 작품 등록
        List<Art> artList = new ArrayList<>();
        for (ArtDto artDto : exhibitionDto.getArtList()) {
            String url = fileProcessService.uploadImage(artDto.getArtImage(), FileFolder.ART_IMAGES);
            Art art = Art.builder()
                    .exhibition(exhibition)
                    .artImage(url)
                    .artName(artDto.getArtName())
                    .artDesc(artDto.getArtDesc())
                    .build();
            artList.add(art);
        }
        artRepository.saveAll(artList);

        return exhibition;
    }

    // 마감 전 승인된 전시회 전체 조회
    public List<ExhibitionResponseDto> getExhibitions(User user, ExhibitionTagName tags, String sortBy) {
        if (sortBy == null) sortBy = "createdDate";
        LocalDateTime now = LocalDateTime.now(); // 지금 기준 마감 전
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy); // 정렬

        List<Exhibition> exhibitionList = exhibitionRepository
                .findAllByExhibitionStateAndExhibitionEndDateAfter(ExhibitionState.APPROVAL, now, sort);

        return getExhibitionWithTagsList(user, exhibitionList, tags);
    }

    // 대기 중인 전시회 전체 조회
    public List<WaitingExhibitionResponseDto> getWaitingExhibitions() {

        List<Exhibition> exhibitionList = exhibitionRepository
                .findAllByExhibitionState(ExhibitionState.WAIT);

        List<WaitingExhibitionResponseDto> waitingExhibitionResponseDtoList = new ArrayList<>();
        for (Exhibition exhibition : exhibitionList) {

            // 전시회 카테고리 조회
            List<ExhibitionTagName> tagList = getTagList(exhibition.getId());

            List<Art> artList = artRepository.findAllByExhibitionId(exhibition.getId());

            waitingExhibitionResponseDtoList.add(WaitingExhibitionResponseDto.builder()
                    .exhibitionId(exhibition.getId())
                    .exhibitionName(exhibition.getExhibitionName())
                    .exhibitionThumbnail(artList.get(0).getArtImage())
                    .exhibitionArtistName(exhibition.getUser().getUsername())
                    .exhibitionStartDate(exhibition.getExhibitionStartDate())
                    .exhibitionEndDate(exhibition.getExhibitionEndDate())
                    .tagList(tagList)
                    .exhibitionState(exhibition.getExhibitionState())
                    .build());
        }
        return waitingExhibitionResponseDtoList;
    }

    // 대기 중인 전시회 승인
    @Transactional
    public void approveExhibitions(Long exhibitionId) {
        Exhibition exhibition = exhibitionRepository.getById(exhibitionId);
        exhibition.setExhibitionState(ExhibitionState.APPROVAL);
    }

    // 승인된 전시회 검색
    public List<ExhibitionResponseDto> getSearchExhibitions(User user, String search, ExhibitionTagName tags, String sortBy) {
        if (sortBy == null) sortBy = "createdDate";
        LocalDateTime now = LocalDateTime.now(); // 지금 기준 마감 전
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy); // 정렬

        List<Exhibition> exhibitionList = exhibitionRepository
                .findAllByExhibitionStateAndExhibitionNameContainsAndExhibitionEndDateAfter(
                        ExhibitionState.APPROVAL, search, now, sort
                );

        return getExhibitionWithTagsList(user, exhibitionList, tags);

    }

    // 전시회 상세 페이지 조회
    public ExhibitionDetailResponseDto getExhibitionDetail(Long exhibitionId) {
        Exhibition exhibition = exhibitionRepository.findById(exhibitionId).orElse(null);
        exhibition.setHits(exhibition.getHits() + 1); // 조회 수 증가
        // 전시회 태그 조회
        List<ExhibitionTagName> tagList = getTagList(exhibitionId);
        // 전시회 작품 조회
        List<Art> artList = artRepository.findAllByExhibitionId(exhibitionId);
        List<ArtResponseDto> artDtoList = new ArrayList<>();
        for (Art art : artList) {
            ArtResponseDto artDto = ArtResponseDto.builder()
                    .artId(art.getId())
                    .artImage(art.getArtImage())
                    .artName(art.getArtName())
                    .artDesc(art.getArtDesc())
                    .build();
            artDtoList.add(artDto);
        }
        assert exhibition != null;
        return ExhibitionDetailResponseDto.builder()
                .exhibitionId(exhibition.getId())
                .exhibitionName(exhibition.getExhibitionName())
                .exhibitionArtist(exhibition.getUser())
                .exhibitionDesc(exhibition.getExhibitionDesc())
                .exhibitionStartDate(exhibition.getExhibitionStartDate())
                .exhibitionEndDate(exhibition.getExhibitionEndDate())
                .tagList(tagList)
                .artList(artDtoList).build();
    }

    // 사용자 전시 목록 조회 (승인 & 대기 상관 없이)
    public List<ExhibitionResponseDto> getUserExhibitions(User user) {
        List<Exhibition> exhibitionList = exhibitionRepository.findAllByUserId(user.getId(), Sort.by(Sort.Direction.DESC, "createdDate"));
        return getExhibitionList(user, exhibitionList);
    }

    // 해당 전시회 작가의 다른 승인된 전시 목록 조회
    public List<ExhibitionResponseDto> getArtistExhibitions(User user, Long exhibitionId) {
        Exhibition exhibition = exhibitionRepository.findByIdAndExhibitionState(exhibitionId, ExhibitionState.APPROVAL).orElse(null);
        List<Exhibition> exhibitionList = new ArrayList<>();
        if(exhibition != null) {
            exhibitionList = exhibitionRepository.findAllByUserIdAndIdNot(exhibition.getUser().getId(), exhibitionId, Sort.by(Sort.Direction.DESC, "createdDate"));
        }
        return getExhibitionList(user, exhibitionList);
    }

    // 팔로워/팔로우의 승인된 전시 목록 조회
    public List<ExhibitionResponseDto> getFollowUserExhibitions(User user, Long followId) {
        List<Exhibition> exhibitionList = exhibitionRepository.findAllByUserIdAndExhibitionState(
                followId, ExhibitionState.APPROVAL, Sort.by(Sort.Direction.DESC, "createdDate"));
        return getExhibitionList(user, exhibitionList);
    }

    // 전시회 카테고리 조회 메소드
    public List<ExhibitionTagName> getTagList(Long exhibitionId) {
        List<ExhibitionTag> exhibitionTagList = exhibitionTagRepository.findAllByExhibitionId(exhibitionId);
        List<ExhibitionTagName> tagList = new ArrayList<>();
        for (ExhibitionTag exhibitionTag : exhibitionTagList) {
            tagList.add(exhibitionTag.getTag());
        }
        return tagList;
    }

    // 전시회 조회 메소드 (with 태그)
    private List<ExhibitionResponseDto> getExhibitionWithTagsList(User user, List<Exhibition> exhibitionList,
                                                                  ExhibitionTagName tags) {
        List<ExhibitionResponseDto> exhibitionResponseDtoList = new ArrayList<>();
        for (Exhibition exhibition : exhibitionList) {

            // 전시회 카테고리 조회
            List<ExhibitionTagName> tagList = getTagList(exhibition.getId());

            // 카테고리 분류
            if (tags != null && !tagList.contains(tags)) continue;
            List<Art> artList = artRepository.findAllByExhibitionId(exhibition.getId());

            exhibitionResponseDtoList.add(ExhibitionResponseDto.builder()
                    .exhibitionId(exhibition.getId())
                    .exhibitionName(exhibition.getExhibitionName())
                    .exhibitionThumbnail(artList.get(0).getArtImage())
                    .exhibitionArtistName(exhibition.getUser().getUsername())
                    .exhibitionStartDate(exhibition.getExhibitionStartDate())
                    .exhibitionEndDate(exhibition.getExhibitionEndDate())
                    .exhibitionState(exhibition.getExhibitionState())
                    .scrapFlag(scrapService.checkUserScrap(user.getId(), exhibition.getId()))
                    .tagList(tagList)
                    .scrapCount(exhibition.getScrapCount())
                    .commentCount(exhibition.getCommentCount()).build());
        }
        return exhibitionResponseDtoList;
    }

    // 전시회 조회 메소드
    private List<ExhibitionResponseDto> getExhibitionList(User user, List<Exhibition> exhibitionList) {
        List<ExhibitionResponseDto> exhibitionResponseDtoList = new ArrayList<>();
        for (Exhibition exhibition : exhibitionList) {

            // 전시회 카테고리 조회
            List<ExhibitionTagName> tagList = getTagList(exhibition.getId());

            List<Art> artList = artRepository.findAllByExhibitionId(exhibition.getId());

            exhibitionResponseDtoList.add(ExhibitionResponseDto.builder()
                    .exhibitionId(exhibition.getId())
                    .exhibitionName(exhibition.getExhibitionName())
                    .exhibitionThumbnail(artList.get(0).getArtImage())
                    .exhibitionArtistName(exhibition.getUser().getUsername())
                    .exhibitionStartDate(exhibition.getExhibitionStartDate())
                    .exhibitionEndDate(exhibition.getExhibitionEndDate())
                    .tagList(tagList)
                    .scrapFlag(scrapService.checkUserScrap(user.getId(), exhibition.getId()))
                    .exhibitionState(exhibition.getExhibitionState())
                    .scrapCount(exhibition.getScrapCount())
                    .commentCount(exhibition.getCommentCount()).build());
        }
        return exhibitionResponseDtoList;
    }

}
