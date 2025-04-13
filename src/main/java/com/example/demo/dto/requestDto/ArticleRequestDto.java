package com.example.demo.dto.requestDto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleRequestDto {
    private String title;

    private String content;
}
