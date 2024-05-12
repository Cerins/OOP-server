package lu.oop.server.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import lu.oop.server.app.models.files.FileModel;

public interface FileRepository extends JpaRepository<FileModel, Long> {
}
