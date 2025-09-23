package com.example.project_telegram_bot.entity;

import com.example.project_telegram_bot.entity.enums.Interval;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserTg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long chatId;

    @Enumerated(EnumType.STRING)
    private Interval interval;
}
