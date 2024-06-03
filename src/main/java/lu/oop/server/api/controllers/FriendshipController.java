package lu.oop.server.api.controllers;


import lu.oop.server.api.exceptions.RequestException;
import lu.oop.server.app.models.users.FriendshipModel;
import lu.oop.server.app.models.users.IUserModel;
import lu.oop.server.app.models.users.UserModel;
import lu.oop.server.app.repositories.FriendshipRepository;
import lu.oop.server.app.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/friendships")
@RestController
public class FriendshipController {
    private IUserService userService;

    private FriendshipRepository friendshipRepository;

    @Autowired
    public FriendshipController(
            IUserService userService,
            FriendshipRepository friendshipRepository
    ) {
        this.userService = userService;
        this.friendshipRepository = friendshipRepository;
    }

    private static class FriendshipReq {
        public String getLogin() {
            return login;
        }

        private String login;



    }

    @PostMapping()
    public ResponseEntity<FriendshipModel> createFriendship(@RequestBody FriendshipReq req) throws RequestException {
        IUserModel loggedInUser = (IUserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String login = req.getLogin();
        Optional<IUserModel> user = userService.getByLogin(login);
        if(user.isEmpty()) {
            throw new RequestException(
                    HttpStatus.NOT_FOUND,
                    "user not found",
                    "the given user does not exist"
            );
        }
        boolean friendshipExists = friendshipRepository.existsByUsers((UserModel) loggedInUser, (UserModel) user.get());
        if(friendshipExists) {
            throw new RequestException(
                    HttpStatus.BAD_REQUEST,
                    "already exists",
                    "friendship request was already created"
            );
        }
        FriendshipModel m = new FriendshipModel((UserModel) loggedInUser, (UserModel) user.get(), FriendshipModel.STATUS_INIT);
        friendshipRepository.save(m);
        return ResponseEntity.ok(m);
    }
    @PatchMapping("{id}/approve")
    public ResponseEntity approve(@PathVariable Long id) throws RequestException {
        IUserModel loggedInUser = (IUserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<FriendshipModel> m = friendshipRepository.findById(id);
        if(m.isEmpty()) {
            throw new RequestException(
                    HttpStatus.NOT_FOUND,
                    "not found",
                    "friendship not"
            );
        }
        FriendshipModel fr = m.get();
        if(!fr.getResponder().getId().equals(loggedInUser.getId())) {
            throw new RequestException(
                    HttpStatus.FORBIDDEN,
                    "not responder",
                    "user is not responder",
                    String.format("Responder id %s does not match %s", fr.getResponder().getId(), loggedInUser.getId()));
        }
        fr.setStatus(FriendshipModel.STATUS_ACCEPTED);
        friendshipRepository.save(fr);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("{id}/reject")
    public ResponseEntity reject(@PathVariable Long id) throws RequestException {
        IUserModel loggedInUser = (IUserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<FriendshipModel> m = friendshipRepository.findById(id);
        if(m.isEmpty()) {
            throw new RequestException(
                    HttpStatus.NOT_FOUND,
                    "not found",
                    "friendship not"
            );
        }
        FriendshipModel fr = m.get();
        if(!fr.getResponder().getId().equals(loggedInUser.getId())) {
            throw new RequestException(
                    HttpStatus.FORBIDDEN,
                    "not responder",
                    "user is not responder",
                    String.format("Responder id %s does not match %s", fr.getResponder().getId(), loggedInUser.getId()));
        }
        fr.setStatus(FriendshipModel.STATUS_REJECTED);
        friendshipRepository.save(fr);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
