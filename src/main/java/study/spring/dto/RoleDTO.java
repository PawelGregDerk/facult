package study.spring.dto;

public enum RoleDTO {
    ROLE_USER,
    ROLE_ADMIN,
    ROLE_STUDENT;

    public Integer getIdentity() {
        return ordinal();
    }

    public static RoleDTO getByIdentity(Integer identity) {
        return RoleDTO.values()[identity];
    }
}
