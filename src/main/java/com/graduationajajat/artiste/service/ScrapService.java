package com.graduationajajat.artiste.service;

import com.graduationajajat.artiste.model.Exhibition;
import com.graduationajajat.artiste.model.Scrap;
import com.graduationajajat.artiste.model.User;
import com.graduationajajat.artiste.repository.ExhibitionRepository;
import com.graduationajajat.artiste.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScrapService {
    private final ScrapRepository scrapRepository;
    private final ExhibitionRepository exhibitionRepository;

    // 사용자 찜 목록 조회
    @Transactional
    public List<Scrap> getScrapsByUserId(Long userId) {
        return scrapRepository.findAllByUserId(userId);
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
    public boolean checkUserScrap(User user, Long exhibitionId) {
        return scrapRepository.existsByUserIdAndExhibitionId(user.getId(), exhibitionId);
    }

}
