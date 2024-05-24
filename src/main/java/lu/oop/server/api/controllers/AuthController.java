package lu.oop.server.api.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lu.oop.server.api.exceptions.RequestException;
import lu.oop.server.api.utils.JwtUtil;
import lu.oop.server.app.models.users.IUserModel;
import lu.oop.server.app.models.users.ParentModel;
import lu.oop.server.app.models.users.StudentModel;
import lu.oop.server.app.models.users.TeacherModel;
import lu.oop.server.app.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequestMapping("/auth")
@RestController
public class AuthController {

    private IUserService userService;

    private JwtUtil jwtUtil;

    @Autowired
    public AuthController(IUserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }
    public static class AuthLoginReq {
        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        AuthLoginReq(String email, String password) {
            this.email = email;
            this.password = password;
        }

        private String email;
        private String password;
    }
    public static class AuthLoginRes {
        public String getToken() {
            return token;
        }

        @JsonProperty("token")
        private String token;

        public AuthLoginRes(String token) {
            this.token = token;
        }
    }
    @PostMapping("/login")
    public ResponseEntity<AuthLoginRes> login(@RequestBody AuthLoginReq req ) throws RequestException {
        Optional<IUserModel> userRes = this.userService.getByEmail(req.getEmail());
        String errorMsg = "wrong login or password";
        String msg = "the given login or password does not match";
        if(userRes.isEmpty()) {
            throw new RequestException(
                    HttpStatus.BAD_REQUEST,
                    errorMsg,
                    msg,
                    "user wrong email"
            );
        }
        IUserModel user = userRes.get();
        // Check if password matches
        if(!user.passwordMatches(req.getPassword())) {
            throw new RequestException(
                    HttpStatus.BAD_REQUEST,
                    errorMsg,
                    msg,
                    "user wrong password"
            );
        }
        String token = this.jwtUtil.generateToken(user.getId());
        return ResponseEntity.ok(new AuthLoginRes(token));
    }

    private static class AuthRegisterReq {
        String firstName;

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public String getDescription() {
            return description;
        }

        public String getRole() {
            return role;
        }

        public String getPhone() {
            return phone;
        }

        String lastName;
        String email;
        String password;
        String description;

        String role;

        String phone;

    }

    @PostMapping("/register")
    public ResponseEntity<IUserModel> register(@RequestBody AuthRegisterReq req) throws RequestException {
        IUserModel usr;
        String role = req.getRole();
        if(role.equals("student")) {
            usr = new StudentModel();
        }else if(role.equals("parent")) {
            usr = new ParentModel();
        } else if(role.equals("teacher")) {
            usr = new TeacherModel();
        } else {
            throw new RequestException(
                    HttpStatus.BAD_REQUEST,
                    "invalid role",
                    "invalid role",
                    String.format("%s is not a valid role", role)
            );
        }
        usr.setEmail(req.getEmail());
        usr.setFirstName(req.getFirstName());
        usr.setLastName(req.getLastName());
        usr.setDescription(req.getDescription());
        usr.setPassword(req.getPassword());
        usr.setPhone(req.getPhone());
        usr = this.userService.save(usr);
        return ResponseEntity.ok(usr);
    }

    @PostMapping("/logout")
    public ResponseEntity logout() {
        // Do nothing ATM
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
