package lu.oop.server.app.service;

import lu.oop.server.app.model.UserModel;
import lu.oop.server.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Optional<UserModel> getById(Long id) {
        return userRepository.findById(id);
    }
}
