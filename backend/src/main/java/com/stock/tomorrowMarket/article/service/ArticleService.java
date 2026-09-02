package com.stock.tomorrowMarket.article.service;

import com.stock.tomorrowMarket.article.dto.ArticleResponse;
import com.stock.tomorrowMarket.article.entity.Article;
import com.stock.tomorrowMarket.article.repository.ArticleRepository;
import com.stock.tomorrowMarket.article.repository.ArticleSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;

    public Page<ArticleResponse> getArticles(Long stockId, Long sectorId, String category, Pageable pageable) {
        Specification<Article> spec = Specification.where(null);

        if (stockId != null) {
            spec = spec.and(ArticleSpecification.byStockId(stockId));
        } else if (sectorId != null) {
            spec = spec.and(ArticleSpecification.bySectorId(sectorId));
        } else if (category != null) {
            if ("지표".equals(category)) {
                spec = spec.and(ArticleSpecification.isIndicator());
            } else if ("시장".equals(category)) {
                spec = spec.and(ArticleSpecification.isMarket());
            }
        }

        Page<Article> articles = articleRepository.findAll(spec, pageable);
        return articles.map(ArticleResponse::from);
    }
}
