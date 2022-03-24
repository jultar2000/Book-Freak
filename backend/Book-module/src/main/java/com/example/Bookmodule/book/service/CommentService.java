package com.example.Bookmodule.book.service;

import com.example.Bookmodule.book.dao.CommentDao;
import com.example.Bookmodule.book.entity.Comment;
import com.example.Bookmodule.exceptions.IncorrectParameterException;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;

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
            throw new IncorrectParameterException(errorMessage);
        }
    }

    public Comment insertComment(String text) {
        Comment comment = Comment
                .builder()
                .date(new Date())
                .text(text)
                .build();
        return commentDao.insertComment(comment) ? comment : null;
    }

    public boolean deleteComment(String commentId) {
        return commentDao.deleteComment(convertStringIdToObjectId(commentId));
    }

    public Comment findComment(String id) {
        return commentDao.findComment(convertStringIdToObjectId(id));
    }

    //TODO> chceck how commentId is working with the book
    public boolean updateComment(String commentId, String text) {
        return commentDao.updateComment(convertStringIdToObjectId(commentId), text);
    }
}
