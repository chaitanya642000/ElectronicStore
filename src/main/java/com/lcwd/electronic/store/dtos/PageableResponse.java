package com.lcwd.electronic.store.dtos;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageableResponse<T> {

    List<T>content;
    Integer pageNumber;
    Integer totalPages;
    Integer pageSize;
    Long getTotalElements;
    Boolean isLastPage;

}
