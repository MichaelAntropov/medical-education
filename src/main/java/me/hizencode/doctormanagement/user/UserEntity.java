package me.hizencode.doctormanagement.user;

import me.hizencode.doctormanagement.certificates.CertificateEntity;
import me.hizencode.doctormanagement.courses.CourseEntity;
import me.hizencode.doctormanagement.user.role.RoleEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.Collection;
import java.util.stream.Collectors;

@Entity
@Table(schema = "doctor_management", name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "active")
    private boolean active;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<RoleEntity> roles;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinTable(name = "courses",
            joinColumns = @JoinColumn(name = "id"))
    private Collection<CourseEntity> courses;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinTable(name = "certificates",
            joinColumns = @JoinColumn(name = "id"))
    private Collection<CertificateEntity> certificates;

    public UserEntity() {
    }

    public UserEntity(String username, String password, String email, boolean active, Collection<RoleEntity> roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.active = active;
        this.roles = roles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Collection<RoleEntity> getRoles() {
        return roles;
    }

    public Collection<GrantedAuthority> getAuthorities() {
        return this.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    public void setRoles(Collection<RoleEntity> roles) {
        this.roles = roles;
    }

    public Collection<CourseEntity> getCourses() {
        return courses;
    }

    public void setCourses(Collection<CourseEntity> courses) {
        this.courses = courses;
    }

    public Collection<CertificateEntity> getCertificates() {
        return certificates;
    }

    public void setCertificates(Collection<CertificateEntity> certificates) {
        this.certificates = certificates;
    }
}
