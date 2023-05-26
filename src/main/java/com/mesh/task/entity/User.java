package com.mesh.task.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.userdetails.*;

import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(length = 500)
    private String name;

    @Column(name = "date_of_birth")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate dateOfBirth;

    @Length(min = 8, max = 500)
    private String password;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    // private @Size(min = 1)
    @ToString.Exclude
    private List<PhoneData> phones;

    // @Size(min = 1)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @ToString.Exclude
    private List<EmailData> emails;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    // @NotNull
    private Account account;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return this.name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
