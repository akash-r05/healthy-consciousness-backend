package com.healthyconsciousness.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "wellness_sleep_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SleepLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "hours_slept", nullable = false)
    private Double hoursSleep;

    @Column(name = "sleep_quality")
    private String sleepQuality;

    @Column(name = "logged_at", nullable = false)
    private LocalDateTime loggedAt;
}
