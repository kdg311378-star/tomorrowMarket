package com.stock.tomorrowMarket.interest.repository;

import com.stock.tomorrowMarket.interest.entity.SectorInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectorInterestRepository extends JpaRepository<SectorInterest, Long> {
}
