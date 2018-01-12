package com.example.hotel.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hotel.domain.Hotel;
import com.example.hotel.repository.HotelRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/hotels")
public class HotelController {

	@Autowired
	private HotelRepository hotelRepository;
	
	@GetMapping
	public Flux<Hotel> getAllHotels() {
		return hotelRepository.findAll();
	}
	
	@GetMapping("/test")
	public String test() {
		return "hello eureka";
	}
	
	@PostMapping
	public Mono<Hotel> createHotel(@Valid @RequestBody Hotel hotel) {
		return hotelRepository.save(hotel);
	}
	
	@GetMapping("/{id}")
	public Mono<ResponseEntity<Hotel>> getHotelById(@PathVariable(value = "id") String hotelId) {
		return hotelRepository.findById(hotelId)
				.map(savedHotel -> ResponseEntity.ok(savedHotel))
				.defaultIfEmpty(ResponseEntity.notFound().build());
    }
	
	@PutMapping("/{id}")
	public Mono<ResponseEntity<Hotel>> updateHotel(@PathVariable(value = "id") String hotelId,
													@Valid @RequestBody Hotel hotel) {
		return hotelRepository.findById(hotelId)
				.flatMap(existingHotel -> {
					existingHotel.setName(hotel.getName());
					return hotelRepository.save(existingHotel);
				})
				.map(updatedHotel -> new ResponseEntity<>(updatedHotel, HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> deleteHotel(@PathVariable(value = "id") String hotelId) {

		return hotelRepository.findById(hotelId)
				.flatMap(existingHotel ->
					hotelRepository.delete(existingHotel)
						.then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
				)
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	// Hotels are Sent to the client as Server Sent Events
	@GetMapping(value="/stream", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Hotel> streamAllHotels() {
		return hotelRepository.findAll();
	}
}
