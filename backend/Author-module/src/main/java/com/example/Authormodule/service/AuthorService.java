package com.example.Authormodule.service;

import com.example.Authormodule.dao.AuthorDao;
import com.example.Authormodule.entity.Author;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AuthorService {

    private final AuthorDao authorDao;

    private ObjectId convertStringIdToObjectId(String id) {
        try {
            return new ObjectId(id);
        } catch (Exception e) {
            log.error("Cannot create ObjectId: {}", e.getMessage());
            String errorMessage = MessageFormat
                    .format("String Id `{0}` cannot be converted to ObjectId.", id);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public List<Author> findAllAuthors() {
        return authorDao.findAllAuthors();
    }

    public Author findAuthorByNameAndSurname(String name, String surname) {
        return authorDao.findAuthorByNameAndSurname(name, surname);
    }

    public List<Author> findAuthorsBornAfterYear(int year) {
        return authorDao.findAuthorsBornAfterYear(year);
    }

    public List<Author> findAuthorsByNationality(String nationality) {
        return authorDao.findAuthorsByNationality(nationality);
    }

    public boolean createAuthor(Author author) {
        return authorDao.insertAuthor(author);
    }

    public boolean deleteAuthor(String id) {
        ObjectId oid = convertStringIdToObjectId(id);
        return authorDao.deleteAuthor(oid);
    }

    public Author findAuthor(String id) {
        return authorDao.findAuthor(convertStringIdToObjectId(id));
    }

    public boolean updateAuthorFields(String id, boolean isAlive) {
        return authorDao.updateAuthorFields(convertStringIdToObjectId(id), isAlive);
    }
}
