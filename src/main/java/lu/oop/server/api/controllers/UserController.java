package lu.oop.server.api.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    public ResponseEntity<IUserModel> getUserById(@PathVariable Long id) {
        Optional<IUserModel> oUser = userService.getById(id);
        if(oUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        IUserModel user = oUser.get();
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}/childGrounded")
    // Can also use hasRoles
    // The available roles match getRoleName
    @PreAuthorize("hasRole('parent')")
    public ResponseEntity<GroundedRes> isChildGrounded(@PathVariable Long id) {
        IParentModel loggedInUser = (IParentModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!loggedInUser.getId().equals(id)) {
            // Can only look at yourself
            logger.warn("User attempted to check other user");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // Not db needed since we already know the result of the user
        return ResponseEntity.ok(new GroundedRes(loggedInUser.childGrounded()));
    }
}
