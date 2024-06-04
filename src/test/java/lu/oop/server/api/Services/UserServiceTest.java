package lu.oop.server.api.Services;

import lu.oop.server.app.models.tags.TagModel;
import lu.oop.server.app.models.users.ITeacherModel;
import lu.oop.server.app.models.users.IUserModel;
import lu.oop.server.app.models.users.StudentModel;
import lu.oop.server.app.models.users.TeacherModel;
import lu.oop.server.app.models.users.UserModel;
import lu.oop.server.app.repositories.TeacherRepository;
import lu.oop.server.app.repositories.UserRepository;
import lu.oop.server.app.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRecomendedUsers() {
        TagModel ageTag = new TagModel();
        ageTag.setType("age");
        ageTag.setName("16-19");

        TagModel establishmentTag = new TagModel();
        establishmentTag.setType("establishment");
        establishmentTag.setName("Latvijas Univeritāte");

        TagModel interestTag = new TagModel();
        interestTag.setType("interests");
        interestTag.setName("Astrology");


        // Setup current user with specific tags
        UserModel currentUser = new StudentModel();
        currentUser.setId(1L);
        currentUser.addTag(ageTag);
        currentUser.addTag(establishmentTag);
        currentUser.addTag(interestTag);

        // Setup other users with different tags
        UserModel user1 = new StudentModel();
        user1.setId(2L);
        user1.addTag(interestTag);
        user1.addTag(ageTag); // Match by age and interest

        UserModel user2 = new StudentModel();
        user2.setId(3L);
        user2.addTag(ageTag); // Match by age and establishment
        user2.addTag(establishmentTag);

        UserModel user3 = new StudentModel();
        user3.setId(4L);
        TagModel user3Tag = new TagModel();
        user3Tag.setType("interest");
        user3Tag.setName("Math");
        user3.addTag(user3Tag); //No match

        when(userRepository.findById(any())).thenReturn(Optional.of(currentUser));
        when(userRepository.findAll()).thenReturn(Arrays.asList(currentUser, user1, user2, user3));


        // Execute the method under test
        List<IUserModel> result = userService.getRecomendedUsers(1L);

        // Verify the results
        assertEquals(2, result.size());
        assertEquals(user2, result.get(0)); // User2 should come first due to both age and establishment match
        assertEquals(user1, result.get(1)); // User1 should come next due to age match
    }

    @Test
    void testGetRecomendedTeachers(){
      // Setup current user with specific tags
      UserModel currentUser = new StudentModel();
      currentUser.setId(1L);
      TagModel interestTag = new TagModel();
      interestTag.setType("interests");
      interestTag.setName("Theology");
      currentUser.addTag(interestTag);

      TeacherModel teacher1 = new TeacherModel();
      teacher1.setId(2L);
      teacher1.setSubject("Theology");

      TeacherModel teacher2 = new TeacherModel();
      teacher2.setId(3L);
      teacher2.setSubject("Astrology");

      TeacherModel teacher3 = new TeacherModel();
      teacher3.setId(4L);
      teacher3.setSubject("Theology");

      when(userRepository.findById(any())).thenReturn(Optional.of(currentUser));
      when(teacherRepository.findAll()).thenReturn(Arrays.asList(teacher1, teacher2, teacher3));


      // Execute the method under test
      List<ITeacherModel> result = userService.getRecomendedTeachers(1L);

      // Verify the results
      assertEquals(2, result.size());
      assertEquals(teacher1, result.get(0));

    }
}
