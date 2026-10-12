package com.healthyconsciousness.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "wellness_stress_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StressLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "stress_level", nullable = false)
    private Integer stressLevel;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "logged_at", nullable = false)
    private LocalDateTime loggedAt;
}
