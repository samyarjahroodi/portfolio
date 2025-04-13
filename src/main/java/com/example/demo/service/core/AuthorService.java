package com.example.demo.service.core;

import com.example.demo.dto.requestDto.ArticleRequestDto;
import com.example.demo.entity.Article;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.AuthorAccessDeniedException;
import com.example.demo.mapper.ArticleMapper;
import com.example.demo.security.IAuthenticationFacade;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {
    private IAuthenticationFacade authenticationFacade;
    private ArticleService articleService;
    private ArticleMapper articleMapper;

    public void submitContent(ArticleRequestDto articleRequestDto) {
        User user = authenticationFacade.getAuthenticatedUser();
        if (user.getRole().equals(Role.AUTHOR) && !user.isDisabledByAdmin()&&!user.isDeActive()) {
            Article article = articleMapper.articleRequestDtoToArticle(articleRequestDto);
            if (articleService.articleValidation(article)) {
                articleService.saveArticle(article);
            }
        } else {
            throw new AuthorAccessDeniedException("Author access denied.");
        }
    }
}
