package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.validate.UniqueTitle;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {


    private String categoryId;

    @NotBlank
    @UniqueTitle
    @Size(min = 3,message = "Title name should be atleast 3 characters")
    private String title;

    @Size(max = 300,message = "Description cannot be more than 300 words")
    @NotBlank
    private String description;

    @NotBlank
    private String coverImage;

}
