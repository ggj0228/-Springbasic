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

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PostCreateDto {
    @NotEmpty
    private String title;
    private String contents;
    @Builder.Default
    private String appointment = "N";
    // 시간 정보는 직접 LocalDateTime으로 형변환하는 경우가 많음.
    private String appointmentTime;
    private String category;

    public Post toEntity(Author author, LocalDateTime appointmentTime) {
        return Post.builder()
                .title(this.title)
                .contents(this.contents)
                //.authorId(this.authorId)
                .author(author)
                .delYn("N")
                .appointment(this.appointment)
                .appointmentTime(appointmentTime)
                .category(this.category)
                .build();
    }
}
