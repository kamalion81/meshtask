package com.mesh.task.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "email_data")
@NoArgsConstructor
public class EmailData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(length = 200)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    public EmailData(String s, User user) {
        this.email = s;
        this.user = user;
    }
}
