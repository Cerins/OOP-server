package lu.oop.server.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import lu.oop.server.api.controllers.MessageController.MessageRequest;
import lu.oop.server.app.models.complaints.IComplaintModel;
import lu.oop.server.app.models.messages.IMessageModel;
import lu.oop.server.app.services.IComplaintService;
import lu.oop.server.app.services.IMessageService;

public class ComplaintController {
  private IComplaintService complaintService;

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
  }

  @Autowired
  ComplaintController(IComplaintService complaintService) {
      this.complaintService = complaintService;
  }
  
  @PostMapping()
  public ResponseEntity<IComplaintModel> createComplaint(@RequestBody ComplaintRequest complaintRequest) {
      try {
          String title = complaintRequest.getTitle();
          String text = complaintRequest.getText();
          Long complaintantId = Long.valueOf(complaintRequest.getComplaintantId());
          
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
}
