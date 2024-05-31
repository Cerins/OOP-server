package lu.oop.server.app.services;

import lu.oop.server.app.models.tags.ITagModel;
import lu.oop.server.app.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// TODO Maybe this should be merged with InitTagsService
@Service
public class TagService implements  ITagService {

    private TagRepository tagRepository;

    @Autowired
    public TagService(
            TagRepository tagRepository
    ) {
        this.tagRepository = tagRepository;
    }
    @Override
    public List<ITagModel> findAll() {
        return this.tagRepository.all();
    }
}
