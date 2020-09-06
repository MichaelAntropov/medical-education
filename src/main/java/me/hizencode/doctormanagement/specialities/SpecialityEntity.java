package me.hizencode.doctormanagement.specialities;

import javax.persistence.*;

@Entity
@Table(schema = "doctor_management", name = "speciality")
public class SpecialityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    public SpecialityEntity() {
    }

    public SpecialityEntity(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SpecialityEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
