package lu.oop.server.api.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;

import lu.oop.server.app.models.users.IParentModel;
import lu.oop.server.app.models.users.IUserModel;
import lu.oop.server.app.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/users")
@RestController
public class UserController {

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
    public ResponseEntity<GroundedRes> isChildGrounded(@PathVariable Long id) {
        Optional<IUserModel> oUser = userService.getById(id);
        if(oUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        IUserModel user = oUser.get();
        if(user instanceof IParentModel) {
            IParentModel p = (IParentModel) user;
            return ResponseEntity.ok(new GroundedRes(p.childGrounded()));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
