package lu.oop.server.api.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lu.oop.server.api.exceptions.RequestException;
import lu.oop.server.app.models.users.IParentModel;
import lu.oop.server.app.models.users.IUserModel;
import lu.oop.server.app.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/users")
@RestController
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    private class GroundedRes {
        @JsonProperty("grounded")
        boolean grounded;

        public GroundedRes(boolean g) {
            grounded = g;
        }
    }
    private IUserService userService;

    @Autowired
    UserController(IUserService userService) {
        this.userService = userService;
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

    @GetMapping("/{id}/childGrounded")
    // Can also use hasRoles
    // The available roles match getRoleName
    @PreAuthorize("hasRole('parent')")
    public ResponseEntity<GroundedRes> isChildGrounded(@PathVariable Long id) throws RequestException {
        IParentModel loggedInUser = (IParentModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long currentID = loggedInUser.getId();
        if(!currentID.equals(id)) {
            // Can only look at yourself
            throw new RequestException(HttpStatus.FORBIDDEN, "not own item", String.format("user %s requested %s status", currentID, id));
        }
        // Not db needed since we already know the result of the user
        return ResponseEntity.ok(new GroundedRes(loggedInUser.childGrounded()));
    }

    @GetMapping("/{id}/conversations")
    public ResponseEntity<List<Integer>> getMessageById(@PathVariable Long id) {
        List<Integer> conversation = userService.getConversations(id);
        return ResponseEntity.ok(conversation);
    }
}
