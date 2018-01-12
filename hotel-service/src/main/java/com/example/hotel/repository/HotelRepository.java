package com.example.hotel.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.example.hotel.domain.Hotel;

@Repository
public interface HotelRepository extends ReactiveMongoRepository<Hotel, String>  {

}
