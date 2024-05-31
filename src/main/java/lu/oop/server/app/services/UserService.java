package lu.oop.server.app.services;

import lu.oop.server.app.models.complaints.ComplaintModel;
import lu.oop.server.app.models.users.IAdminModel;
import lu.oop.server.app.models.users.IUserModel;
import lu.oop.server.app.models.users.UserModel;
import lu.oop.server.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public Optional<IUserModel> getByEmail(String email) {
        return userRepository.findOneByEmail(email);
    }

    public Optional<IUserModel> getByLogin(String login) {
        return userRepository.findOneByLogin(login);
    }
    public IUserModel save(IUserModel user) {
        return userRepository.save((UserModel)user);
    }
    public List<Integer> getConversations(Long userId) {
        List<Integer> conversations = userRepository.getUserConversationQuery(userId);
        return conversations;
    }
    public List<ComplaintModel> getAssignedComplaints(Long id){
        Optional<UserModel> mbyUser = userRepository.findById(id);
        IAdminModel admin = (IAdminModel) mbyUser.get();
        return admin.getAssignedComplaints();
    }
    public ComplaintModel getActiveComplaint(Long id) {
        Optional<UserModel> mbyUser = userRepository.findById(id);
        UserModel user = mbyUser.get();
        List<ComplaintModel> complaints = user.getComplaints();

        if(complaints.size() <= 0)
            return null;

        ComplaintModel lastComplaint = complaints.get(complaints.size() - 1);

        if(!lastComplaint.isActive()){
            return null;
        }

        return lastComplaint;
    }
}
