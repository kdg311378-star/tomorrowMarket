package com.stock.tomorrowMarket.sector.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SECTORS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SECTORS_ID")
    private Long sectorsId;

    @Column(name = "NAME", nullable = false)
    private String name;
}
