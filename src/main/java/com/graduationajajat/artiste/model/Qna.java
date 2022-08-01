package com.graduationajajat.artiste.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 문의사항
public class Qna extends Timestamped {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "qna_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "qna_content", nullable = false)
    private String qnaContent;

    @Column(name = "qna_answer")
    private String qnaAnswer;

    @Column(name = "qna_state")
    private Boolean qnaState;
}
