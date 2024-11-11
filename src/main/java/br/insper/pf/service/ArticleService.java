package br.insper.pf.service;

import br.insper.pf.exception.AccessDeniedException;
import br.insper.pf.model.Article;
import br.insper.pf.repository.ArticleRepository;
import br.insper.pf.util.TokenValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TokenValidator tokenValidator;

    public void createArticle(String token, Article article) {
        if (!tokenValidator.validateToken(token, "ADMIN")) {
            throw new AccessDeniedException("Access denied: Admins only");
        }
        articleRepository.save(article);
    }

    public void deleteArticle(String token, String id) {
        if (!tokenValidator.validateToken(token, "ADMIN")) {
            throw new AccessDeniedException("Access denied: Admins only");
        }
        articleRepository.deleteById(id);
    }

    public List<Article> getAllArticles(String token) {
        if (!tokenValidator.validateToken(token, "ADMIN", "DEVELOPER")) {
            throw new AccessDeniedException("Access denied: Admins and Developers only");
        }
        return articleRepository.findAll();
    }

    public Article getArticleById(String token, String id) {
        if (!tokenValidator.validateToken(token, "ADMIN", "DEVELOPER")) {
            throw new AccessDeniedException("Access denied: Admins and Developers only");
        }
        Optional<Article> article = articleRepository.findById(id);
        return article.orElse(null);
    }
}