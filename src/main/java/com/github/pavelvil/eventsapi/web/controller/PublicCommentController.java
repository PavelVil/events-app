package com.github.pavelvil.eventsapi.web.controller;

import com.github.pavelvil.eventsapi.mapper.CommentMapper;
import com.github.pavelvil.eventsapi.service.CommentService;
import com.github.pavelvil.eventsapi.web.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public/comment")
@RequiredArgsConstructor
public class PublicCommentController {

    private final CommentService commentService;

    private final CommentMapper commentMapper;

    @GetMapping
    public ResponseEntity<List<CommentDto>> findAllComments(@RequestParam Long eventId) {
        return ResponseEntity.ok(
                commentMapper.toDtoList(commentService.findAllByEventId(eventId))
        );
    }

}
