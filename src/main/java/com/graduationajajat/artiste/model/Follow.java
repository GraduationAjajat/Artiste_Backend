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
// 팔로우
public class Follow extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "following")
    User following;

    @ManyToOne
    @JoinColumn(name = "follower")
    User follower;

}
