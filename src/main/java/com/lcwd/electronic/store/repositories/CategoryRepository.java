package com.lcwd.electronic.store.repositories;

import com.lcwd.electronic.store.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,String> {
    @Query(value = "select * from categories where title=:title",nativeQuery = true)
    String isFieldUnique(@Param("title") String title);
}
