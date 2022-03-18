package com.example.Authormodule.service;

import com.example.Authormodule.dao.AuthorDao;
import com.example.Authormodule.entity.Author;
import com.example.Authormodule.exceptions.IncorrectParameterException;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
public class AuthorService {

    private final AuthorDao authorDao;
    private final Logger log;

    @Autowired
    public AuthorService(AuthorDao authorDao) {
        this.authorDao = authorDao;
        log = LoggerFactory.getLogger(this.getClass());
    }

    private ObjectId convertIdToObjectId(String id) {
        try {
            return new ObjectId(id);
        } catch (Exception e){
            log.error("Cannot create ObjectId: {}", e.getMessage());
            String errorMessage = MessageFormat
                    .format("String Id `{0}` cannot be converted to ObjectId.", id);
            throw new IncorrectParameterException(errorMessage);
        }
    }

    public List<Author> findAllAuthors() {
        authorDao.findAllAuthors().forEach(author -> System.out.println(author.getId()));
        return authorDao.findAllAuthors();
    }

    public List<Author> findAuthorsBornBeforeSomeDate(String date) {
        return  authorDao.findAuthorsBornBeforeSpecificDate(date);
    }

    public List<Author> findAuthorsByNationality(String nationality) {
        return  authorDao.findAuthorsByNationality(nationality);
    }
    
    public Author createAuthor(Author author) {
        return authorDao.insertAuthor(author) ? author : null;
    }

    public boolean deleteAuthor(String id) {
        return authorDao.deleteAuthor(convertIdToObjectId(id));
    }

    public Author findAuthor(String id) {

        return authorDao.findAuthor(convertIdToObjectId(id));
    }

    public boolean updateAuthorFields(String id, boolean isAlive) {
        return authorDao.updateAuthorFields(convertIdToObjectId(id), isAlive);
    }
}
