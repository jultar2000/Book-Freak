package com.example.Authormodule.dao;

import com.example.Authormodule.entity.Author;
import com.example.Authormodule.exceptions.IncorrectDaoOperation;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoWriteException;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Component
public class AuthorDao {

    private final Logger log;
    private static final String AUTHORS_COLLECTION = "authors";
    private final MongoCollection<Author> authorsCollection;

    @Autowired
    public AuthorDao(MongoClient mongoClient,
                     @Value("${spring.data.mongodb.database}") String databaseName) {
        log = LoggerFactory.getLogger(this.getClass());
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        CodecRegistry pojoCodecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        this.authorsCollection =
                database.getCollection(AUTHORS_COLLECTION, Author.class).withCodecRegistry(pojoCodecRegistry);
    }

    /**
     * Inserts the 'author' object in the 'authors' collection.
     *
     * @param author - Author object to be inserted.
     * @return True if successful, throw IncorrectDaoOperation otherwise.
     */
    public boolean insertAuthor(Author author) {
        try {
            authorsCollection
                    .withWriteConcern(WriteConcern.MAJORITY)
                    .insertOne(author);
            return true;
        } catch (MongoWriteException e) {
            log.error("Could not insert `{}` into 'authors' collection: {}", author.getOid(), e.getMessage());
            throw new IncorrectDaoOperation(
                    MessageFormat.format("Author with Id `{0}` already exists.", author.getOid()));
        }
    }

    /**
     * Deletes the author document from the 'authors' collection with the provided authorId.
     *
     * @param authorId - id of the author to be deleted.
     * @return True if successful, throw IncorrectDaoOperation otherwise.
     */
    public boolean deleteAuthor(ObjectId authorId) {
        Bson find_query = Filters.in("_id", authorId);
        try {
            DeleteResult result = authorsCollection.deleteOne(find_query);
            if (result.getDeletedCount() < 1) {
                log.warn("Id '{}' not found in 'authors' collection. No author deleted.", authorId);
            }
            return true;
        } catch (Exception e) {
            String errorMessage = MessageFormat
                    .format("Could not delete `{0}` from 'authors' collection: {1}.", authorId, e.getMessage());
            throw new IncorrectDaoOperation(errorMessage);
        }
    }

    /**
     * Given the authorId, finds the author object in 'authors' collection.
     *
     * @param authorId - id of the author.
     * @return author object, if null throws IncorrectDaoOperation.
     */
    public Author findAuthor(ObjectId authorId) {
        Bson find_query = Filters.in("_id", authorId);
        Author author = authorsCollection.find(find_query).first();
        if (author == null) {
            throw new IncorrectDaoOperation(
                    MessageFormat.format("Author with Id `{0}` does not exist.", authorId));
        }
        return author;
    }

    /**
     * Finds all authors in 'authors' collection.
     *
     * @return list of found authors.
     */
    public List<Author> findAllAuthors() {
        List<Author> authors = new ArrayList<>();
        authorsCollection
                .find()
                .into(authors);
        return authors;
    }

    /**
     * Given the author's name and surname, finds the author object in 'authors' collection.
     *
     * @param name - name of the author.
     * @param surname - surname of the author.
     * @return author object, if null throw IncorrectDaoOperation.
     */
    public Author findAuthorByNameAndSurname(String name, String surname) {
        Bson find_query = Filters.and(
                Filters.in("name", name),
                Filters.in("surname", surname)
        );
        Author author = authorsCollection.find(find_query).first();
        if (author == null) {
            throw new IncorrectDaoOperation(
                    MessageFormat
                            .format(
                                    "Author with name `{0}` and surname `{1}` does not exist.", name, surname));
        }
        return author;
    }

    /**
     * Finds all authors in 'authors' collection with specified nationality.
     *
     * @param nationality - author's nationality
     * @return list of found authors that match specified criteria.
     */
    public List<Author> findAuthorsByNationality(String nationality) {
        List<Author> authors = new ArrayList<>();
        Bson find_query = Filters.in("nationality", nationality);
        authorsCollection
                .find(find_query)
                .into(authors);
        return authors;
    }

    /**
     * Finds all authors in 'authors' collection born after specified year.
     *
     * @param year - number from which the search is started.
     * @return list of found authors that match specified criteria.
     */
    public List<Author> findAuthorsBornAfterYear(int year) {
        List<Author> authors = new ArrayList<>();
        LocalDate dummy_date = LocalDate.of(year, 1, 1);
        Bson find_query = Filters.gte("birthDate", dummy_date);
        Bson sort = Sorts.ascending("birthDate");
        authorsCollection
                .find(find_query)
                .sort(sort)
                .into(authors);
        return authors;
    }

    /**
     * Given the author's id, finds the author object and updates his isAlive field.
     *
     * @param authorId - id of the author.
     * @param isAlive - isAlive boolean value
     * @return true if successful, false if not, throws IncorrectDaoOperation if field cannot be updated.
     */
    public boolean updateAuthorFields(ObjectId authorId, boolean isAlive) {
        Bson find_query = Filters.in("_id", authorId);
        Bson update = Updates.set("alive", isAlive);
        try {
            UpdateResult updateResult = authorsCollection.updateOne(find_query, update);
            if (updateResult.getModifiedCount() < 1) {
                log.warn(
                        "Author `{}` was not updated. Some field might not exist.",
                        authorId);
                return false;
            }
        } catch (MongoWriteException e) {
            String errorMessage =
                    MessageFormat.format(
                            "Issue caught while trying to update author `{}`: {}",
                            authorId,
                            e.getMessage());
            throw new IncorrectDaoOperation(errorMessage);
        }
        return true;
    }
}
