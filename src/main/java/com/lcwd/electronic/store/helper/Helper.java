package com.lcwd.electronic.store.helper;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {

    public static <U, V> PageableResponse<V> getPageableResponse(Page<U> page, Class<V> type)
    {
        List<U> entity = page.getContent();
        List<V>entityDtos = entity.stream().map(object-> new ModelMapper().map(object,type)).collect(Collectors.toList());

        PageableResponse<V>pageableResponse = new PageableResponse<>();
        pageableResponse.setContent(entityDtos);
        pageableResponse.setPageSize(page.getSize());
        pageableResponse.setPageNumber(page.getNumber());
        pageableResponse.setGetTotalElements(page.getTotalElements());
        pageableResponse.setTotalPages(page.getTotalPages());
        pageableResponse.setIsLastPage(page.isLast());

        return pageableResponse;
    }


}
