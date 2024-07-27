package com.spring.redsocial.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;
    private String text;
    private String file;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;
    @PrePersist
    private void onCreate(){
        this.createdAt = LocalDateTime.now();
    }
}
