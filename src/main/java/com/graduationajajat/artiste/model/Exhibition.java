package com.graduationajajat.artiste.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 전시회
public class Exhibition extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "exhibition_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "exhibition_name", nullable = false)
    private String exhibitionName;

    @Column(name = "exhibition_start_date", nullable = false)
    private LocalDateTime exhibitionStartDate;

    @Column(name = "exhibition_end_date", nullable = false)
    private LocalDateTime exhibitionEndDate;

    @Column(name = "exhibition_desc", nullable = false)
    private String exhibitionDesc;

    @Column(name = "exhibition_state", nullable = false)
    private ExhibitionState exhibitionState;

    @Column(name = "scrap_count")
    private int scrapCount = 0;

    @Column(name = "comment_count")
    private int commentCount = 0;

    @Column(name = "hits")
    private int hits = 0;

}
