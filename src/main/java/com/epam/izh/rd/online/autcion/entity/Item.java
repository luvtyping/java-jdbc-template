package com.epam.izh.rd.online.autcion.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Лот в ставке
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private Long itemId;
    private Double bidIncrement;
    private Boolean buyItNow;
    private String description;
    private LocalDate startDate;
    private Double startPrice;
    private LocalDate stopDate;
    private String title;
    private Long userId;
}
