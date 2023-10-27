package com.lcwd.electronic.store.entities;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "categories")
public class Category {

    @Id
    @Column(name="category_id")
    private String categoryId;

    @Column(name = "title",nullable = false,length = 100)
    private String title;

    @Column(name="description",nullable = false,length = 200)
    private String description;

    @Column(name="coverImage")
    private String coverImage;

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Product>products = new ArrayList<>();

}
