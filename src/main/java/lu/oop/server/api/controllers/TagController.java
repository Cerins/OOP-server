package lu.oop.server.api.controllers;

import lu.oop.server.app.models.tags.ITagModel;
import lu.oop.server.app.services.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/tags")
@RestController
public class TagController {
    private ITagService tagService;

    @Autowired
    public TagController(
            ITagService tagService
    ) {
        this.tagService = tagService;
    }

    @GetMapping()
    public ResponseEntity<List<ITagModel>> getTags() {
        return ResponseEntity.ok((tagService.findAll()));
    }
}
