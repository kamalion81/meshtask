package com.mesh.task.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.stereotype.Component;

@Data
@Entity
@Table(name = "phone_data")
@NoArgsConstructor
public class PhoneData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    // @Pattern(regexp="^\\+7(\\d{10})$", message="Phone number must be in format +79123456789")
    @Column(length = 13)
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    public PhoneData(String s, User saved) {
        this.phone = s;
        this.user = saved;
    }
}
