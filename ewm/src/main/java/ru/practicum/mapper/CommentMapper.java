package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.CommentDto;
import ru.practicum.dto.NewCommentDto;
import ru.practicum.model.Comment;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class CommentMapper {

    public Comment toComment(NewCommentDto commentDto) {
        return Comment.builder()
                .comment(commentDto.getComment())
                .build();
    }

    public CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .commentator(UserMapper.toUserDto(comment.getCommentator()))
                .createDate(comment.getCreateDate())
                .lastUpdate(comment.getLastUpdate())
                .build();
    }

    public Set<CommentDto> toCommentsDtoSet(Set<Comment> comments) {
        return comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toSet());
    }
}
