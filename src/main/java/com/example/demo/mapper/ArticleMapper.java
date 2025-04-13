package com.example.demo.mapper;

import com.example.demo.dto.requestDto.ArticleRequestDto;
import com.example.demo.entity.Article;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleMapper {
    Article articleRequestDtoToArticle(ArticleRequestDto dto);
}
