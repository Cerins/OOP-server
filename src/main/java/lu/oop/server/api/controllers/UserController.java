package lu.oop.server.api.controllers;

import lu.oop.server.app.models.complaints.ComplaintModel;
import lu.oop.server.app.models.files.FileModel;
import lu.oop.server.app.models.users.*;
import lu.oop.server.api.exceptions.RequestException;
import lu.oop.server.app.repositories.FileRepository;
import lu.oop.server.app.repositories.FriendshipRepository;
import lu.oop.server.app.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RequestMapping("/users")
@RestController
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);
    private IUserService userService;

    private FileRepository fileRepository;

    private FriendshipRepository friendshipRepository;

    @Autowired
    UserController(
            IUserService userService,
            FileRepository fileRepository,
            FriendshipRepository friendshipRepository
    ) {
        this.userService = userService;
        this.fileRepository = fileRepository;
        this.friendshipRepository  = friendshipRepository;
    }
    @GetMapping("/{id}")
    public ResponseEntity<IUserModel> getUserById(@PathVariable Long id) throws RequestException {
        Optional<IUserModel> oUser = userService.getById(id);
        if(oUser.isEmpty()) {
            throw new RequestException(HttpStatus.NOT_FOUND, "user not exists", String.format("user %s does not exist", id));
        }
        IUserModel user = oUser.get();
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}/picture")
    public ResponseEntity<String> getPicture(@PathVariable Long id) throws RequestException {
        Optional<IUserModel> oUser = userService.getById(id);
        if(oUser.isEmpty()) {
            throw new RequestException(HttpStatus.NOT_FOUND, "user not exists", String.format("user %s does not exist", id));
        }
        IUserModel user = oUser.get();
        if(user.getAvatarId() != null) {
            Optional<FileModel> f = fileRepository.findById(user.getAvatarId());
            if(f.isEmpty()) {
                throw new RuntimeException(String.format("%s user's avatarID points to non existent file", user.getId()));
            }
            String res = Base64.getEncoder().encodeToString(f.get().getFile());
            return ResponseEntity.ok(res);
        }
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{id}/conversations")
    public ResponseEntity<List<Integer>> getMessageById(@PathVariable Long id) {
        List<Integer> conversation = userService.getConversations(id);
        return ResponseEntity.ok(conversation);
    }

    @GetMapping("/{id}/assignedComplaints")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<ComplaintModel>> assignedComplaints(@PathVariable Long id) {
        IAdminModel loggedInUser = (IAdminModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!loggedInUser.getId().equals(id)) {
            // Can only look at yourself
            logger.warn("User attempted to check other user");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        // DB required to initialize the complaint collection
        return ResponseEntity.ok(userService.getAssignedComplaints(id));
    }

    @GetMapping("/{id}/activeComplaint")
    public ResponseEntity<ComplaintModel> activeComplaint(@PathVariable Long id) {
        IUserModel loggedInUser = (IUserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!loggedInUser.getId().equals(id)) {
            // Can only look at yourself
            logger.warn("User attempted to check other user");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        return ResponseEntity.ok(userService.getActiveComplaint(id));
    }

    @GetMapping("{id}/friendships")
    public ResponseEntity<List<UserModel>> friendships(@PathVariable Long id) throws RequestException {
        Optional<IUserModel> oUser = userService.getById(id);
        if(oUser.isEmpty()) {
            throw new RequestException(HttpStatus.NOT_FOUND, "user not exists", String.format("user %s does not exist", id));
        }
        IUserModel user = oUser.get();
        List<FriendshipModel> frens = friendshipRepository.findByUserAndStatus((UserModel) user, FriendshipModel.STATUS_ACCEPTED);
        List<UserModel> frenUsers = new LinkedList<>();
        for(FriendshipModel fr: frens) {
            frenUsers.add(fr.getOther((UserModel) user));
        }
        return ResponseEntity.ok(frenUsers);
    }
    @GetMapping("{id}/friendships/to")
    public ResponseEntity<List<UserModel>> getIncomingFriendships(@PathVariable Long id) throws RequestException {
        Optional<IUserModel> oUser = userService.getById(id);
        if(oUser.isEmpty()) {
            throw new RequestException(HttpStatus.NOT_FOUND, "user not exists", String.format("user %s does not exist", id));
        }
        IUserModel user = oUser.get();
        List<FriendshipModel> incominFrens = friendshipRepository.findByResponderAndStatus((UserModel) user, FriendshipModel.STATUS_INIT);
        List<UserModel> frenUsers = new LinkedList<>();
        for(FriendshipModel fr: incominFrens) {
            frenUsers.add(fr.getOther((UserModel) user));
        }
        return ResponseEntity.ok(frenUsers);
    }
    @GetMapping("{id}/friendships/from")
    public ResponseEntity<List<UserModel>> getOutcomingFriendships(@PathVariable Long id) throws RequestException {
        Optional<IUserModel> oUser = userService.getById(id);
        if(oUser.isEmpty()) {
            throw new RequestException(HttpStatus.NOT_FOUND, "user not exists", String.format("user %s does not exist", id));
        }
        IUserModel user = oUser.get();
        List<FriendshipModel> outcominFrens = friendshipRepository.findByRequestorAndStatus((UserModel) user, FriendshipModel.STATUS_INIT);
        List<UserModel> frenUsers = new LinkedList<>();
        for(FriendshipModel fr: outcominFrens) {
            frenUsers.add(fr.getOther((UserModel) user));
        }
        return ResponseEntity.ok(frenUsers);
    }
}
