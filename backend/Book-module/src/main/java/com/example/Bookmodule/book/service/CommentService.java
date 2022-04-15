package com.example.Bookmodule.book.service;

import com.example.Bookmodule.book.dao.CommentDao;
import com.example.Bookmodule.book.entity.Comment;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class CommentService {

    private final CommentDao commentDao;
    private final Logger log;

    @Autowired
    public CommentService(CommentDao commentDao) {
        log = LoggerFactory.getLogger(this.getClass());
        this.commentDao = commentDao;
    }

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

    public List<Comment> findBookComments(String movieId) {
        return commentDao.findBookComments(convertStringIdToObjectId(movieId));
    }

    public boolean insertComment(String bookId, String username, String text) {
        if (username == null || username.equals("")) {
            log.error("Incorrect username!");
            return false;
        }
        Comment comment = Comment
                .builder()
                .book_oid(convertStringIdToObjectId(bookId))
                .username(username)
                .date(new Date())
                .text(text)
                .build();
        return commentDao.insertComment(comment);
    }

    public boolean deleteComment(String commentId, String username) {
        if (!commentDao.findComment(convertStringIdToObjectId(commentId)).getUsername().equals(username)) {
            log.error("Comment {} does not belong to user with username {}", commentId, username);
            return false;
        }
        return commentDao.deleteComment(convertStringIdToObjectId(commentId));
    }

    public void deleteAllBookComments(String bookId) {
        commentDao.deleteAllBookComments(convertStringIdToObjectId(bookId));
    }

    public boolean updateComment(String commentId, String username, String text) {
        Comment comment = commentDao.findComment(convertStringIdToObjectId(commentId));
        if (!comment.getUsername().equals(username)) {
            log.error("Comment {} does not belong to user with username {}", commentId, username);
            return false;
        }
        return commentDao.updateComment(convertStringIdToObjectId(commentId), text, comment);
    }
}