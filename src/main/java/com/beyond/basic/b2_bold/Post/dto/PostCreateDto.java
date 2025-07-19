package com.beyond.basic.b2_bold.Post.dto;

import com.beyond.basic.b2_bold.Author.domain.Author;
import com.beyond.basic.b2_bold.Post.domain.Post;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PostCreateDto {
    @NotEmpty
    private String title;
    private String contents;
    @NotNull // 숫자는 NotEmpty 사용불가
    private Long authorId;

    public Post toEntity(Author author) {
        return Post.builder()
                .title(this.title)
                .contents(this.contents)
                //.authorId(this.authorId)
                .author(author)
                .delYn("N")
                .build();
    }
}
