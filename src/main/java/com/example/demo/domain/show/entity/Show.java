package com.example.demo.domain.show.entity;

import com.example.demo.common.entity.BaseEntityWithUpdate;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.awt.geom.Area;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Show extends BaseEntityWithUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;
    private String title; // 공연명
    private LocalDate startDate; // 공연 시작일
    private LocalDate endDate; // 공연 종료일
    private String theater; // 공연 시설명(공연장명)
    private String cast; // 출연진
    private String crew; // 제작진
    private String runtime; // 공연 런타임
    private String age; // 관람 연령
    private String company; // 제작사
    private String price; // 티켓 가격
    private String poster; // 포스터 이미지 경로
    @Column(columnDefinition = "TEXT")
    private String story; // 줄거리
    @Enumerated(EnumType.STRING)
    private Genre genre; // 장르
    @Enumerated(EnumType.STRING)
    private PerformanceStatus status; // 공연상태
    private Boolean openRun; // 오픈런 여부
    @Enumerated(EnumType.STRING)
    private Area area;
    @Column(length = 700)
    private String storyUrls; // 소개이미지 목록
    private String schedule; // 공연 시간
    @ColumnDefault(value = "0")
    private Integer views;
}
