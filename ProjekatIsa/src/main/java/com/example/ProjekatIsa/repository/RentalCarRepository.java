package com.example.ProjekatIsa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ProjekatIsa.model.Car;
import com.example.ProjekatIsa.model.Filijale;
import com.example.ProjekatIsa.model.RentACar;

@Repository
public interface RentalCarRepository extends JpaRepository<RentACar, Long> {
	List<RentACar> findAll();
	RentACar findOneById(Long id);
	RentACar findOneByCar(Car car);
	RentACar findOneByFilijale(Filijale fil);
	RentACar save(RentACar rentacar);
	List<RentACar> findAllByCity(String city);
 

}
