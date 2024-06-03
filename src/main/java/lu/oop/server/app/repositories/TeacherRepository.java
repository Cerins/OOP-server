package lu.oop.server.app.repositories;

import lu.oop.server.app.models.users.TeacherModel;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<TeacherModel, Long> {
}
