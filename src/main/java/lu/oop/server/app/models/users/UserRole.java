package lu.oop.server.app.models.users;

public enum UserRole {
    STUDENT(0),
    TEACHER(1),
    PARENT(2),
    ADMIN(3),
    UNK(4);


    private Integer value;

    UserRole(Integer v) {
        value = v;
    }

    public Integer getValue() {
        return value;
    }
}
