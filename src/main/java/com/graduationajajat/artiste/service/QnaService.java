package com.graduationajajat.artiste.service;

import com.graduationajajat.artiste.model.Comment;
import com.graduationajajat.artiste.model.Qna;
import com.graduationajajat.artiste.model.User;
import com.graduationajajat.artiste.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QnaService {
    private final QnaRepository qnaRepository;

    // 문의사항 목록 조회
    public List<Qna> getQnas() {
        return qnaRepository.findAll();
    }

    // 사용자 문의사항 추가
    @Transactional
    public void createQna(User user, String qnaContent) {
        Qna qna = Qna.builder().user(user).qnaContent(qnaContent).qnaState(false).build();
        qnaRepository.save(qna);
    }

    // 관리자 문의사항 답변
    @Transactional
    public void answerQna(Long qnaId, String qnaAnswer) {
        Qna qna = qnaRepository.findById(qnaId).get();

        qna.setQnaAnswer(qnaAnswer);
        qna.setQnaState(true);

    }
}
