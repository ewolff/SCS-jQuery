package com.ewolff.microservice.catalog;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends PagingAndSortingRepository<Item, Long>, CrudRepository<Item, Long> {

	List<Item> findByName(@Param("name") String name);

	List<Item> findByNameContaining(@Param("name") String name);

}
