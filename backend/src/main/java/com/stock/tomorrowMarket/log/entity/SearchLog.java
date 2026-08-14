package com.stock.tomorrowMarket.log.entity;

import com.stock.tomorrowMarket.stock.entity.Stock;
import com.stock.tomorrowMarket.user.entity.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "SEARCH_LOG")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEARCH_LOG_ID")
    private Long searchLogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USERS_ID")
    private Users user; // 비로그인 사용자 검색도 허용할 경우를 대비해 nullable 가능성 열어둠 (요구사항에 따라 다름)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STOCK_ID", nullable = false)
    private Stock stock;

    @Column(name = "SEARCH_TIME", nullable = false, updatable = false)
    private LocalDateTime searchTime;

    @PrePersist
    protected void onCreate() {
        this.searchTime = LocalDateTime.now();
    }

    @Builder
    public SearchLog(Users user, Stock stock) {
        this.user = user;
        this.stock = stock;
    }
}
