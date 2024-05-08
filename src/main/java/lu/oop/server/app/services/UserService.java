package lu.oop.server.app.services;

import lu.oop.server.app.models.users.IUserModel;
import lu.oop.server.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<IUserModel> getById(Long id) {
        return userRepository.findById(id).map(u -> u);
    }
}
