package com.example.Bookmodule.book.dao;

import com.example.Bookmodule.book.entity.Comment;
import com.example.Bookmodule.exceptions.IncorrectDaoOperation;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoWriteException;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Component
public class CommentDao {

    private final Logger log;
    private static final String AUTHORS_COLLECTION = "comments";
    private final MongoCollection<Comment> commentsCollection;

    @Autowired
    public CommentDao(MongoClient mongoClient,
                      @Value("${spring.data.mongodb.database}") String databaseName) {
        log = LoggerFactory.getLogger(this.getClass());
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        CodecRegistry pojoCodecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        this.commentsCollection =
                database.getCollection(AUTHORS_COLLECTION, Comment.class).withCodecRegistry(pojoCodecRegistry);
    }

    /**
     * Inserts the comment object in the 'comments' collection.
     *
     * @param comment - Comment object to be inserted.
     * @return True if successful, throw IncorrectDaoOperation otherwise.
     */
    public boolean insertComment(Comment comment) {
        try {
            commentsCollection.withWriteConcern(WriteConcern.MAJORITY).insertOne(comment);
            return true;
        } catch (MongoWriteException e) {
            log.error("Could not insert `{}` into 'comments' collection: {}", comment.getOid(), e.getMessage());
            throw new IncorrectDaoOperation(
                    MessageFormat.format("Comment with id `{0}` already exists.", comment.getOid()));
        }
    }

    /**
     * Deletes the comment document from the 'comments' collection with the provided commentId.
     *
     * @param commentId - id of the comment to be deleted.
     * @return True if successful, throw IncorrectDaoOperation otherwise.
     */
    public boolean deleteComment(ObjectId commentId) {
        Bson find_query = Filters.in("_id", commentId);
        try {
            DeleteResult result = commentsCollection.deleteOne(find_query);
            if (result.getDeletedCount() < 1) {
                log.warn("Id '{}' not found in 'comments' collection. No comments deleted.", commentId);
            }
            return true;
        } catch (Exception e) {
            String errorMessage = MessageFormat
                    .format("Could not delete `{0}` from 'comments' collection: {1}.", commentId, e.getMessage());
            throw new IncorrectDaoOperation(errorMessage);
        }
    }

    /**
     * Given the commentId, finds the comment object in 'comments' collection.
     *
     * @param commentId - id of the comment.
     * @return comment object, if null throws IncorrectDaoOperation.
     */
    public Comment findComment(ObjectId commentId) {
        Bson find_query = Filters.in("_id", commentId);
        Comment comment = commentsCollection.find(find_query).first();
        if (comment == null) {
            throw new IncorrectDaoOperation(
                    MessageFormat.format("Comment with Id `{0}` does not exist.", commentId));
        }
        return comment;
    }

    /**
     * Finds all comments in 'comments' collection that belong to specific book.
     *
     * @param bookId - if of the book.
     * @return list of found comments.
     */
    public List<Comment> findBookComments(ObjectId bookId) {
        Bson find_query = Filters.in("book_oid", bookId);
        List<Comment> comments = new ArrayList<>();
        commentsCollection
                .find(find_query)
                .into(comments);
        return comments;
    }

    /**
     * Deletes the comment document from the 'comments' collection that belong to specific book.
     *
     * @param bookId - if of the book.
     */
    public void deleteAllBookComments(ObjectId bookId) {
        Bson find_query = Filters.in("book_oid", bookId);
        try {
            DeleteResult result = commentsCollection.deleteMany(find_query);
            if (result.getDeletedCount() < 1) {
                log.warn("No comments deleted for book id '{}'", bookId);
            }
        } catch (Exception e) {
            String errorMessage = MessageFormat
                    .format("Could not delete comments from 'comments' collection: {1}.", e.getMessage());
            throw new IncorrectDaoOperation(errorMessage);
        }
    }

    /**
     * Given the comment's id, finds comment object and updates text field.
     *
     * @param commentId - id of the comment to be updated
     * @param text   - string value of text.
     * @return true if successful, false if not, throws IncorrectDaoOperation if field cannot be updated.
     */
    public boolean updateComment(ObjectId commentId, String text, Comment comment) {
        Bson find_query = Filters.in("_id", commentId);
        Bson update = Updates.combine(
                Updates.set("text", text),
                Updates.set("date", new Date())
        );
        try {
            UpdateResult updateResult = commentsCollection.updateOne(find_query, update);
            if (updateResult.getModifiedCount() < 1) {
                log.warn(
                        "Author `{}` was not updated. Some field might not exist.",
                        commentId);
                return false;
            }
        } catch (MongoWriteException e) {
            String errorMessage =
                    MessageFormat.format(
                            "Issue caught while trying to update comment `{}`: {}",
                            commentId,
                            e.getMessage());
            throw new IncorrectDaoOperation(errorMessage);
        }
        return true;
    }
}