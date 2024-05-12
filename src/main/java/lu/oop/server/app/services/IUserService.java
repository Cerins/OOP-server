package lu.oop.server.app.services;

import lu.oop.server.app.models.users.IUserModel;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    public Optional<IUserModel> getById(Long id);
    public List<Integer> getConversations(Long userId);
}
