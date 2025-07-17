package com.beyond.basic.b2_bold.Post.dto;

import com.beyond.basic.b2_bold.Author.domain.Author;
import com.beyond.basic.b2_bold.Post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static ch.qos.logback.classic.spi.ThrowableProxyVO.build;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PostDetailDto {
    private Long id;
    private String title;
    private String contents;
    private String authorEmail;

    // 관계성 설정을 안 했을 때
//    public static PostDetailDto fromEntity(Post post, Author author) {
//        return PostDetailDto.builder()
//                .id(post.getId())
//                .title(post.getTitle())
//                .contents(post.getContents())
//                .authorEmail(author.getEmail())
//                .build();
//    }
    // 관계성 설정을 했을 떄
    public static PostDetailDto fromEntity(Post post) {

        return PostDetailDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .contents(post.getContents())
                .authorEmail(post.getAuthor().getEmail())
                .build();
    }

}
