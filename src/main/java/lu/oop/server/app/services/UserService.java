package lu.oop.server.app.services;

import jakarta.transaction.Transactional;
import lu.oop.server.app.models.complaints.ComplaintModel;
import lu.oop.server.app.models.tags.ITagModel;
import lu.oop.server.app.models.tags.TagModel;
import lu.oop.server.app.models.users.IAdminModel;
import lu.oop.server.app.models.users.ITeacherModel;
import lu.oop.server.app.models.users.IUserModel;
import lu.oop.server.app.models.users.UserModel;
import lu.oop.server.app.repositories.TagRepository;
import lu.oop.server.app.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.lang.Arrays;

import java.util.List;
import java.util.Set;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    private UserRepository userRepository;

    private TagRepository tagRepository;
    @Autowired
    public UserService(
            UserRepository userRepository,
            TagRepository tagRepository
    ) {
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
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
    public void delete(IUserModel user) {
        userRepository.delete((UserModel)user);
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
    @Transactional
    public boolean saveUserWithTags(IUserModel user,
                                    ITagModel[] tags
                ) {
        userRepository.save((UserModel) user);
        if(tags != null) {
            for (TagModel pTag: (TagModel[]) tags) {
                Optional<ITagModel> tagOptional = tagRepository.findByNameAndType(
                        pTag.getName(),
                        pTag.getType()
                );
                if (tagOptional.isEmpty()) {
                    return false; // Tag not found
                }
                ITagModel tag = tagOptional.get();
                user.addTag(tag);
            }
        }
        userRepository.save((UserModel) user); // Save the user with the updated tags
        return true; // Successfully saved
    }

    public List<IUserModel> getByUsername(String username){
        List<IUserModel> users = userRepository.getListByLogin(username);
        return users;
    }

    public List<IUserModel> getRecomendedUsers(Long id) {
        Optional<UserModel> mbyUser = userRepository.findById(Long.valueOf(id));
        UserModel currUser = mbyUser.get();
        Set<TagModel> userTags = new HashSet<>(Arrays.asList(currUser.getTags()));

        List<UserModel> allUsers = userRepository.findAll();
        List<IUserModel> filteredUsers = new LinkedList<IUserModel>();
        List<IUserModel> priorityUsers = new LinkedList<IUserModel>();
        
        for(UserModel user : allUsers){
            if (user.getId().equals(id)) {
                continue; // Skip the current user
            }

            Set<TagModel> tags = new HashSet<>(Arrays.asList(user.getTags()));
            tags.retainAll(userTags); //Common tags

            boolean agesMatch = tags.stream().anyMatch(tag -> tag.getType().equals("age"));
            boolean establishmentMatch = tags.stream().anyMatch(tag -> tag.getType().equals("establishment"));

            //If age group doesnt match dont show, show higher if establisment matches
            if (agesMatch) {
                if(establishmentMatch){
                    priorityUsers.add(user);
                }
                else{
                    filteredUsers.add(user);
                }
            }
        }

        priorityUsers.addAll(filteredUsers);

        return priorityUsers;
    }

    public List<ITeacherModel> getRecomendedTeachers(Long id) {
        Optional<UserModel> mbyUser = userRepository.findById(Long.valueOf(id));
        UserModel currUser = mbyUser.get();
        Set<TagModel> userTags = new HashSet<>(Arrays.asList(currUser.getTags()));

        List<ITeacherModel> allTeachers = userRepository.getAllTeachers();
        List<ITeacherModel> filteredTeachers = new LinkedList<ITeacherModel>();

        for(ITeacherModel teacher: allTeachers){
            String teacherSubject = teacher.getSubject();
            if(userTags.stream().anyMatch(tag -> tag.getName().equals(teacherSubject))){
                filteredTeachers.add(teacher);
            }
        }

        return filteredTeachers;
    }
}
