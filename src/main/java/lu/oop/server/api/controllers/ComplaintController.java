package lu.oop.server.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lu.oop.server.app.models.complaints.ComplaintModel;
import lu.oop.server.app.models.complaints.IComplaintModel;
import lu.oop.server.app.models.users.IAdminModel;
import lu.oop.server.app.models.users.IUserModel;
import lu.oop.server.app.models.users.UserModel;
import lu.oop.server.app.services.IComplaintService;
import lu.oop.server.app.services.IUserService;
import lu.oop.server.app.services.UserService;

import java.util.List;
import java.util.Optional;

@RequestMapping("/complaints")
@RestController
public class ComplaintController {
  private IComplaintService complaintService;
  private Logger logger = LoggerFactory.getLogger(ComplaintController.class);
  private IUserService userService;    

  public static class ComplaintRequest {
    private String title;
    private String text;
    private int complaintantId;
    
    public String getTitle(){
      return title;
    }
    public String getText(){
      return text;
    }
    public int getComplaintantId(){
      return complaintantId;
    }

    public void setTitle(String title){
      this.title = title;
    }
    public void setText(String text){
      this.text = text;
    }
    public void setComplaintantId(int id){
      this.complaintantId = id;
    }
  }

  @Autowired
  ComplaintController(IComplaintService ComplaintService, IUserService UserService) {
      this.complaintService = ComplaintService;
      this.userService = UserService;
  }
  
  @PostMapping()
  public ResponseEntity<IComplaintModel> createComplaint(@RequestBody ComplaintRequest complaintRequest) {
      try {
          String title = complaintRequest.getTitle();
          String text = complaintRequest.getText();
          Long complaintantId = Long.valueOf(complaintRequest.getComplaintantId());

          IUserModel loggedInUser = (IUserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
          if(!loggedInUser.getId().equals(complaintantId)) {
            // Can only complain as yourself
            logger.warn("User attempted to send complaint as other user");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
          
          if(title == null){
              title = "";
          }
          else if(text == null){
            text = "";
          }


          complaintService.createComplaint(title, text, complaintantId);

          return ResponseEntity.status(HttpStatus.CREATED).build();
      } catch (IllegalArgumentException e) {
          return ResponseEntity.badRequest().build();
      }
  }

  @PatchMapping("/{id}/assign/{asigneeId}")
  @PreAuthorize("hasRole('admin')")
  public ResponseEntity<IComplaintModel> assignComplaint(@PathVariable int id, @PathVariable int asigneeId){
    try {
      Long complaintId = Long.valueOf(id);
      Long assigneeId = Long.valueOf(asigneeId);

      complaintService.assignComplaint(assigneeId, complaintId);

      return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
  }
  }


  @PatchMapping("/{id}/close")
  @PreAuthorize("hasRole('admin')")
  public ResponseEntity<IComplaintModel> closeComplaint(@PathVariable int id){
    try {

      IAdminModel loggedInUser = (IAdminModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      Optional<IUserModel> user = userService.getById(loggedInUser.getId());
      IAdminModel admin = (IAdminModel) user.get();
      Long complaintId = Long.valueOf(id);

      if(!admin.getAssignedComplaints()
      .stream()
      .map(ComplaintModel::getId)
      .anyMatch(value -> value.equals(complaintId))) 
      {
          // Can only resolve assigned complaint
          logger.warn("Admin attempted to close not assigned complaint");
          return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }

      complaintService.closeComplaint(complaintId);

      return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
  }
  }

  @GetMapping("/unassigned")
  @PreAuthorize("hasRole('admin')")
  public List<ComplaintModel> getUnassigned(){
    return complaintService.getUnasignedComplaints();
  }
}
