package repository;

import org.springframework.data.jpa.repository.JpaRepository;

import model.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, Long> {


}