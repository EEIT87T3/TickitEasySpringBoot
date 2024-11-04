package com.eeit87t3.tickiteasy.admin.entity;

import jakarta.persistence.*;
import java.util.Objects;

import org.springframework.stereotype.Component;

/**
 * @author Lilian (Curriane)
 */
@Entity
@Table(name = "administrator")
@Component
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer adminId;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String name;

    // Getters
    public Integer getAdminId() {
        return adminId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    // Setters
    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Admin admin = (Admin) o;
        return Objects.equals(adminId, admin.adminId) &&
               Objects.equals(email, admin.email) &&
               Objects.equals(name, admin.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adminId, email, name);
    }

    // toString (不包含密碼)
    @Override
    public String toString() {
        return "Admin{" +
                "adminId=" + adminId +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}