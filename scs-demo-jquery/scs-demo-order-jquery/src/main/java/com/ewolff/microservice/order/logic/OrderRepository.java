package com.ewolff.microservice.order.logic;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.CrudRepository;

interface OrderRepository extends PagingAndSortingRepository<Order, Long>, CrudRepository<Order, Long>  {

}