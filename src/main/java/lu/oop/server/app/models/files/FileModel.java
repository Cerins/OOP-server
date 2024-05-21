package lu.oop.server.app.models.files;

import lu.oop.server.app.models.users.*;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "file")
public class FileModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    @JsonProperty("name")
    private String name;

    @Column(name = "data", nullable =  false, columnDefinition = "BYTEA")
    private byte[] data;

    @ManyToOne
    @JoinColumn(name = "createdBy")
    @JsonProperty("createdBy")
    private UserModel createdBy;

    public FileModel() {
      this.id = null;
    }

    public void setFile(byte[] data){
      this.data = data;
    }

    public void setCreator(UserModel user){
      this.createdBy = user;
    }

    public void setName(String name){
      this.name = name;
    }

    public byte[] getFile(){
      return this.data;
    }
}
