package com.example.Bookmodule.author.service;

import com.example.Bookmodule.author.dao.AuthorDao;
import com.example.Bookmodule.author.entity.Author;
import com.example.Bookmodule.exceptions.IncorrectParameterException;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class AuthorService {

    private final AuthorDao authorDao;
    private final Logger log;

    @Autowired
    public AuthorService(AuthorDao authorDao) {
        log = LoggerFactory.getLogger(this.getClass());
        this.authorDao = authorDao;
    }

    private ObjectId convertStringIdToObjectId(String id) {
        try {
            return new ObjectId(id);
        } catch (Exception e) {
            log.error("Cannot create ObjectId: {}", e.getMessage());
            String errorMessage = MessageFormat
                    .format("String Id `{0}` cannot be converted to ObjectId.", id);
            throw new IncorrectParameterException(errorMessage);
        }
    }

    public Author createAuthor(Author author) {
        return authorDao.insertAuthor(author) ? author : null;
    }

    public Author findAuthor(String id) {
        return authorDao.findAuthor(convertStringIdToObjectId(id));
    }

    public boolean deleteAuthor(String id) {
        return authorDao.deleteAuthor(convertStringIdToObjectId(id));
    }


}