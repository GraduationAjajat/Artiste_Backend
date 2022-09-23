package com.graduationajajat.artiste.service;

import com.graduationajajat.artiste.dto.response.ExhibitionResponseDto;
import com.graduationajajat.artiste.model.*;
import com.graduationajajat.artiste.repository.ArtRepository;
import com.graduationajajat.artiste.repository.ExhibitionRepository;
import com.graduationajajat.artiste.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScrapService {
    private final ScrapRepository scrapRepository;
    private final ExhibitionRepository exhibitionRepository;
    private final ArtRepository artRepository;
    private final ExhibitionService exhibitionService;

    // 사용자 찜 목록 조회
    @Transactional
    public List<ExhibitionResponseDto> getScrapsByUserId(Long userId) {
        List<Scrap> scrapList = scrapRepository.findAllByUserId(userId);
        List<ExhibitionResponseDto> exhibitionResponseDtoList = new ArrayList<>();
        for(Scrap scrap : scrapList) {
            Exhibition exhibition = scrap.getExhibition();

            // 전시회 카테고리 조회
            List<ExhibitionTagName> tagList = exhibitionService.getTagList(exhibition.getId());

            // 전시회 작품 조회
            List<Art> artList = artRepository.findAllByExhibitionId(exhibition.getId());

            ExhibitionResponseDto exhibitionResponseDto = ExhibitionResponseDto.builder()
                    .exhibitionId(exhibition.getId())
                    .exhibitionThumbnail(artList.get(0).getArtImage())
                    .exhibitionName(exhibition.getExhibitionName())
                    .exhibitionArtistName(exhibition.getUser().getNickname())
                    .exhibitionStartDate(exhibition.getExhibitionStartDate())
                    .exhibitionState(exhibition.getExhibitionState())
                    .exhibitionEndDate(exhibition.getExhibitionEndDate())
                    .tagList(tagList)
                    .scrapCount(exhibition.getScrapCount())
                    .scrapFlag(checkUserScrap(userId, exhibition.getId()))
                    .commentCount(exhibition.getCommentCount())
                    .build();

            exhibitionResponseDtoList.add(exhibitionResponseDto);
        }
        return exhibitionResponseDtoList;
    }

    // 사용자 찜 추가
    @Transactional
    public void createScrap(User user, Long exhibitionId) {
        Exhibition exhibition = exhibitionRepository.findById(exhibitionId).get();
        Scrap scrap = Scrap.builder().user(user).exhibition(exhibition).build();

        exhibition.setScrapCount(exhibition.getScrapCount() + 1); // 찜 수 증가

        scrapRepository.save(scrap);
    }

    // 사용자 찜 삭제
    @Transactional
    public void deleteScrap(User user, Long exhibitionId) {
        scrapRepository.deleteByUserIdAndExhibitionId(user.getId(), exhibitionId);
        Exhibition exhibition = exhibitionRepository.findById(exhibitionId).get();
        exhibition.setScrapCount(exhibition.getScrapCount() - 1); // 찜 수 감소

    }

    // 사용자 찜 여부
    @Transactional(readOnly = true)
    public boolean checkUserScrap(Long userId, Long exhibitionId) {
        return scrapRepository.existsByUserIdAndExhibitionId(userId, exhibitionId);
    }

}
