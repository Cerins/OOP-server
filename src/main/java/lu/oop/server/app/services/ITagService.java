package lu.oop.server.app.services;

import lu.oop.server.app.models.tags.ITagModel;

import java.util.List;

public interface ITagService {
    public List<ITagModel> findAll();
}
