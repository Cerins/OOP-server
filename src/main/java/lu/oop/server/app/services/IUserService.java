package lu.oop.server.app.services;

import lu.oop.server.app.models.complaints.ComplaintModel;
import lu.oop.server.app.models.tags.ITagModel;
import lu.oop.server.app.models.users.IUserModel;
import lu.oop.server.app.models.users.UserModel;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    public Optional<IUserModel> getById(Long id);
    public List<Integer> getConversations(Long userId);
    public List<ComplaintModel> getAssignedComplaints(Long id);
    public ComplaintModel getActiveComplaint(Long id);
    public Optional<IUserModel> getByEmail(String email);

    public Optional<IUserModel> getByLogin(String login);

    public IUserModel save(IUserModel user);

    public void delete(IUserModel user);

    public boolean saveUserWithTags(
            IUserModel user,
            ITagModel[] tags
    );

    public List<IUserModel> getByUsername(String username);
}
