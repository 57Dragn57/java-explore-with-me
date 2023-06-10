package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.CommentDto;
import ru.practicum.model.Comment;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class CommentMapper {
    public Comment toComment(CommentDto commentDto) {
        return Comment.builder()
                .id(commentDto.getId())
                .comment(commentDto.getComment())
                .commentator(UserMapper.toUser(commentDto.getCommentator()))
                .createDate(commentDto.getCreateDate())
                .build();
    }

    public CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .commentator(UserMapper.toUserDto(comment.getCommentator()))
                .createDate(comment.getCreateDate())
                .build();
    }

    public Set<CommentDto> toCommentsDtoSet(Set<Comment> comments) {
        return comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toSet());
    }
}
