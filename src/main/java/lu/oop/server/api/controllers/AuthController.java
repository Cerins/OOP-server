package lu.oop.server.api.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lu.oop.server.api.exceptions.RequestException;
import lu.oop.server.api.utils.JwtUtil;
import lu.oop.server.app.models.files.FileModel;
import lu.oop.server.app.models.tags.ITagModel;
import lu.oop.server.app.models.tags.TagModel;
import lu.oop.server.app.models.users.*;
import lu.oop.server.app.repositories.FileRepository;
import lu.oop.server.app.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RequestMapping("/auth")
@RestController
public class AuthController {

    private IUserService userService;

    // TODO hmm
    private FileRepository fileRepository;

    private JwtUtil jwtUtil;

    @Autowired
    public AuthController(
            IUserService userService,
            JwtUtil jwtUtil,
            FileRepository fileRepository
    ) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.fileRepository = fileRepository;
    }
    public static class AuthLoginReq {
        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }

        AuthLoginReq(String login, String password) {
            this.login = login;
            this.password = password;
        }

        private String login;
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
        Optional<IUserModel> userRes = this.userService.getByLogin(req.getLogin());
        String errorMsg = "wrong login or password";
        String msg = "the given login or password does not match";
        if(userRes.isEmpty()) {
            throw new RequestException(
                    HttpStatus.BAD_REQUEST,
                    errorMsg,
                    msg,
                    "user wrong login"
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
    // More about annotations here https://medium.com/paysafe-bulgaria/springboot-dto-validation-good-practices-and-breakdown-fee69277b3b0
    private static class AuthRegisterReq {

        private static class ReqTag {
            String name;

            public void setName(String name) {
                this.name = name;
            }

            public void setType(String type) {
                this.type = type;
            }

            String type;
        }
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
        @Email(message = "email must be valid")
        @NotBlank(message = "email can not be blank")
        String email;
        String password;
        String description;

        String role;

        String phone;

        public String getLogin() {
            return login;
        }

        String login;

        ReqTag[] tags;

        byte[] picture;

        public ReqTag[] getTags() {
            return tags;
        }

        public byte[] getPicture() {
            return picture;
        }

    }

    @PostMapping("/register")
    public ResponseEntity<IUserModel> register(@Valid @RequestBody AuthRegisterReq req) throws RequestException {
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
        usr.setLogin(req.getLogin());
        List<TagModel> pTags = new LinkedList<>();
        AuthRegisterReq.ReqTag[] rTags = req.getTags();
        if(rTags != null) {
            for(AuthRegisterReq.ReqTag rTag: rTags) {
                TagModel tmp = new TagModel(
                        rTag.type,
                        rTag.name
                );
                pTags.add(tmp);
            }

        }
        boolean saved = this.userService.saveUserWithTags(usr, pTags.toArray(new TagModel[0]));
        if(!saved) {
            // Clean up
            userService.delete(usr);
            throw new RequestException(
                    HttpStatus.BAD_REQUEST,
                    "bad tag",
                    "one of the given tags does not exist",
                    "user gave non existent tag"
            );

        }
        byte[] fileByes = req.getPicture();
        if(fileByes != null) {
            FileModel f = new FileModel();
            f.setCreator((UserModel) usr);
            f.setName("pfp");
            f.setFile(fileByes);
            fileRepository.save(f);
            usr.setAvatarId(f.getId());
            userService.save(usr);
        }

        return ResponseEntity.ok(usr);
    }

    @PostMapping("/logout")
    public ResponseEntity logout() {
        // Do nothing ATM
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
