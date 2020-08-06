package me.hizencode.doctormanagement.user.role;

public class Role {
    private String name;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    public Role(RoleEntity roleEntity) {
        this.name = roleEntity.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
