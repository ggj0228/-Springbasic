package com.beyond.basic.b2_bold.Post.dto;

import com.beyond.basic.b2_bold.Post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class PostListDto {
    private Long id;
    private String title;

    public static PostListDto fromListEntity(Post post) {
        return  PostListDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .build();
    }
}
