package app.domain;

import org.springframework.security.core.GrantedAuthority;


public enum Position implements GrantedAuthority {
    director,
    headOfDepartment,
    departmentSpecialist;

    @Override
    public String getAuthority() {
        return name();
    }
}
