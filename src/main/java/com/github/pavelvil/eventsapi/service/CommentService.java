package com.github.pavelvil.eventsapi.service;

import com.github.pavelvil.eventsapi.entity.Comment;
import com.github.pavelvil.eventsapi.exception.EntityNotFoundException;
import com.github.pavelvil.eventsapi.model.PageModel;
import com.github.pavelvil.eventsapi.repository.CommentRepository;
import com.github.pavelvil.eventsapi.repository.EventRepository;
import com.github.pavelvil.eventsapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    public List<Comment> findAllByEventId(Long eventId) {
        return findAllByEventId(eventId, null).getContent();
    }

    public Page<Comment> findAllByEventId(Long eventId, PageModel pageModel) {
        return commentRepository.findAllByEventId(
                eventId,
                pageModel == null ? Pageable.unpaged() : pageModel.toPageRequest()
        );
    }

    @Transactional
    public Comment save(Comment comment, Long userId, Long eventId) {
        var currentEvent = eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                MessageFormat.format("Event with id {0} not found!", eventId)
                        )
                );
        return userRepository.findById(userId)
                .map(user -> {
                    comment.setUser(user);
                    comment.setEvent(currentEvent);

                    return commentRepository.save(comment);
                })
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                MessageFormat.format("User with id {0} not found!", userId)
                        )
                );
    }

    @Transactional
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

    public boolean hasInEvent(Long commentId, Long eventId, Long authorId) {
        return commentRepository.existsByIdAndEventIdAndUserId(commentId, eventId, authorId);
    }

}
