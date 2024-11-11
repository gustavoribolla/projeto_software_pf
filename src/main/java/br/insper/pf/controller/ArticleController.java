package br.insper.pf.controller;

import br.insper.pf.exception.AccessDeniedException;
import br.insper.pf.model.Article;
import br.insper.pf.service.ArticleService;
import br.insper.pf.util.RequiresRole;
import br.insper.pf.util.TokenValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/artigos")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private TokenValidator tokenValidator;

    @RequiresRole({"ADMIN"})
    @PostMapping
    public ResponseEntity<String> createArticle(@RequestBody Article article, @RequestHeader("Authorization") String token) {
        try {
            String role = tokenValidator.validateTokenAndGetRole(token);
            if ("ADMIN".equals(role)) {
                articleService.createArticle(token, article);
                return ResponseEntity.ok("Article created successfully");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: Admins only");
            }
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @RequiresRole({"ADMIN"})
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteArticle(@PathVariable String id, @RequestHeader("Authorization") String token) {
        try {
            String role = tokenValidator.validateTokenAndGetRole(token);
            if ("ADMIN".equals(role)) {
                articleService.deleteArticle(token, id);
                return ResponseEntity.ok("Article deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: Admins only");
            }
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @RequiresRole({"ADMIN", "DEVELOPER"})
    @GetMapping
    public ResponseEntity<List<Article>> getAllArticles(@RequestHeader("Authorization") String token) {
        try {
            String role = tokenValidator.validateTokenAndGetRole(token);
            List<Article> articles = articleService.getAllArticles(token);
            return ResponseEntity.ok(articles);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @RequiresRole({"ADMIN", "DEVELOPER"})
    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable String id, @RequestHeader("Authorization") String token) {
        try {
            String role = tokenValidator.validateTokenAndGetRole(token);
            Article article = articleService.getArticleById(token, id);
            return ResponseEntity.ok(article);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }
}