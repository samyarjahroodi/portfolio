package com.example.demo.service.core;

import com.example.demo.entity.Article;
import com.example.demo.repository.ArticleRepository;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {
    private ArticleRepository articleRepository;

    public void saveArticle(Article article) {
        articleRepository.save(article);
    }

    public boolean articleValidation(Article article) {
        if (article.getTitle() == null && article.getContent() == null) {
            return false;
        }

        return article.getContent().length() >= 200;
    }
}
