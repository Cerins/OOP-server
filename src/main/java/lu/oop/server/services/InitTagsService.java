package lu.oop.server.services;

import lu.oop.server.app.models.tags.TagModel;
import lu.oop.server.app.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InitTagsService {

    private TagRepository repository;

    @Autowired
    InitTagsService(TagRepository repository) {
        this.repository = repository;
    }

    public void initTags() {
        // Human readable tags
        String[] tags = new String[]{
                "age:16-19",
                "age:19-23",
                "age:23-",
                "establishment:Rīgas Tehniskā Universitāte",
                "establishment:Latvijas Univeritāte",
                "establishment:RTU Inženierzinātņu vidusskola",
                "establishment:Rīgas Valsts 1. ģimnāzija",
                "establishment:Rīgas Valsts 2. ģimnāzija",
                "establishment:Rīgas Valsts 3. ģimnāzija",
                "interests:Ķīmija",
                "interests:Matemātika",
                "interests:Algebra",
                "interests:Latviešu valoda",
                "interests:Teoloģija",
                "interests:Datu struktūras un algoritmi",
                "interests:Angļu valoda",
                "interests:Senās Romas vēsture",
                "interests:Vācu valoda"
        };
        if(repository.count() == 0) {
            for(String tag: tags) {
                System.out.println(tag);
                String[] spl = tag.split(":");
                TagModel t = new TagModel(
                        spl[0],
                        spl[1]
                );
                repository.save(t);
            }
        }
    }
}

