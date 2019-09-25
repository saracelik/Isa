package com.example.ProjekatIsa.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.bouncycastle.crypto.agreement.DHStandardGroups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.ProjekatIsa.DTO.CarReservationDTO;
import com.example.ProjekatIsa.DTO.HotelDTO;
import com.example.ProjekatIsa.DTO.ReservationRoomDTO;
import com.example.ProjekatIsa.DTO.RoomDTO;
import com.example.ProjekatIsa.model.Car;
import com.example.ProjekatIsa.model.CarReservation;
import com.example.ProjekatIsa.model.Discount;
import com.example.ProjekatIsa.model.DiscountHotel;
import com.example.ProjekatIsa.model.FlightReservation;
import com.example.ProjekatIsa.model.Hotel;
import com.example.ProjekatIsa.model.RatingHotel;
import com.example.ProjekatIsa.model.RatingRoom;
import com.example.ProjekatIsa.model.RentACar;
import com.example.ProjekatIsa.model.ReservationRoom;
import com.example.ProjekatIsa.model.Room;
import com.example.ProjekatIsa.model.SearchFormServices;
import com.example.ProjekatIsa.model.User;
import com.example.ProjekatIsa.repository.DiscountHotelRepository;
import com.example.ProjekatIsa.repository.FlightRepository;
import com.example.ProjekatIsa.repository.FlightReservationRepository;
import com.example.ProjekatIsa.repository.HotelRepository;
import com.example.ProjekatIsa.repository.RatingRoomRepository;
import com.example.ProjekatIsa.repository.ReservationRoomRepository;
import com.example.ProjekatIsa.repository.RoomRepository;
import com.example.ProjekatIsa.repository.UserRepository;
import com.example.ProjekatIsa.service.HotelService;
import com.example.ProjekatIsa.service.ReservationRoomService;
import com.example.ProjekatIsa.service.RoomService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/rooms")
public class RoomController {

	@Autowired
	private RoomService roomService;
	
	@Autowired
	private RoomRepository roomRepository;
	
	@Autowired
	private HotelRepository hotelRepository;
	
	@Autowired 
	private ReservationRoomService reservationRoomService;
	
	@Autowired 
	private UserRepository userRepository;
	
	@Autowired
	private RatingRoomRepository ratingRoomRepository;
	
	@Autowired
	private DiscountHotelRepository dhRepository;
	
	@Autowired
	private FlightReservationRepository flightRepository;
	
	@Autowired
	private FlightRepository fRepository;
	
	@RequestMapping(
			value = "/getAll", 
			method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Room>  getRooms() {
		
		System.out.println("Number of rooms: " + roomService.getAll().size());
		
		return roomService.getAll();
	}
	
	@RequestMapping(
			value = "/getAllRooms/{id}", 
			method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getRooms(@PathVariable("id") Long id) {
		List<Room> returnList = new ArrayList<Room> ();
		Hotel h = hotelRepository.findOneById(id);
		returnList = roomRepository.findAllByHotel(h);
		if (returnList!=null) {
	        return new ResponseEntity<List<Room>>(returnList,HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value="/addRoom/{id}",
			method = RequestMethod.POST)
	public ResponseEntity<?> addRoom(@RequestBody RoomDTO newRoom,
										@PathVariable("id") Long id){
		System.out.println("Dosao u add room hotel");
		
		Hotel hotel = hotelRepository.findOneById(id);
		Room r =new Room(newRoom);
		r.setNumber(hotel.getRooms().size()+1);
		//dodavanje u model
		hotel.addRoom(r);
		r.setHotel(hotel);
		
		//cuvanje u bazu
		this.roomRepository.save(r);
		hotelRepository.save(hotel);
			return new ResponseEntity<>(null, HttpStatus.OK);
		
	}
	@RequestMapping(value="/deleteRoom/{id}",
			method = RequestMethod.GET)
	public ResponseEntity<?> deleteRoom(@PathVariable("id") Long id){
		System.out.println("Dosao u delete room ");

		Room rdel = roomRepository.findOneById(id);
		try {
			roomService.deleteRoom(rdel);
			return new ResponseEntity<>(null, HttpStatus.OK);
		}catch(Exception e ) {
			
		}
		
	return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		
	}
	
	@RequestMapping(value="/changeRoom/{id}",
			method = RequestMethod.POST)
	public ResponseEntity<?> changeRoom(@RequestBody RoomDTO room,
										@PathVariable("id") Long id){
		System.out.println("Dosao u change service");
		Room r2 = new Room(room); 
		

		Room r = roomRepository.findOneById(id);

		if (room.getCapacity()!=null) {
			r.setCapacity(r2.getCapacity());
		}
		if  (room.getPrice()!=null) {
			r.setPrice(r2.getPrice());
		}
		if(room.getRoom_description()!= null) {
			System.out.println(r2.getRoom_description());

			r.setRoom_description(r2.getRoom_description());
		}
		if (room.getRoom_average_rating()!=null) {
			r.setRoom_average_rating(r2.getRoom_average_rating());
		}
		
		try {
			roomRepository.save(r);
			return new ResponseEntity<>(null, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	
	}
	
	
	@RequestMapping(value="/searchRooms/{id}/{cenaod}/{cenado}",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> searchRooms(@RequestBody ReservationRoom roomReservation,
										@PathVariable("id") Long id,
										@PathVariable("cenaod") Long cenaOd,
										@PathVariable("cenado") Long cenaDo){
		
		System.out.println("Dosao u search rooms");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",
                Locale.ENGLISH); 
		   Date startDate = null;
		   Date endDate = null;
		   
		   try {
			   startDate = sdf.parse(roomReservation.getStartDate().toString());
			   endDate = sdf.parse(roomReservation.getEndDate().toString());
		   } catch (ParseException e) {
			   e.printStackTrace();
			   
		   }
		Hotel hotel = hotelRepository.findOneById(id);
		List<Room> allRooms = roomRepository.findAllByHotel(hotel);
		
		List<Room> returnList = new ArrayList<Room>();
		
		for (Room r : allRooms) {
			List<ReservationRoom> reservationRoom = reservationRoomService.findAllByRoom(r);
			boolean free = true;
			
			int reserved = 0;
			if (!reservationRoom.isEmpty()) {
				for(ReservationRoom res : reservationRoom) {
					
					free = checkforfree(res, endDate, startDate);
					if (!free) {
						reserved ++;
					}
				}
			}
			if (reserved == 0) {
				if (cenaOd == -1 && cenaDo == -1) {
					if (r.getRoom_description().equals(roomReservation.getCategory())) {
						returnList.add(r);
					}
				}
				else if (cenaOd==-1) {
					if (r.getRoom_description().equals(roomReservation.getCategory()) && r.getPrice() >= cenaOd) {
						returnList.add(r);
					}
				}
				else {
					if (r.getRoom_description().equals(roomReservation.getCategory()) && r.getPrice() >= cenaOd && r.getPrice() <= cenaDo) {
						returnList.add(r);
					}
				}
			}
		}
		
		return new ResponseEntity<List<Room>>(returnList,HttpStatus.OK);
	
	}
	
	public boolean checkforfree(ReservationRoom res, Date endDate, Date startDate) {
		
		//provjeravamo datum koji smo unijeli za preuzimanje vozila
		//ako je on nakon datuma vracanja vozila koji je registrovan -ok
		if(startDate.getTime() >= res.getEndDate().getTime()) {
			return true;
			
		}
		else {
			if(endDate.getTime() <= res.getStartDate().getTime())
			{
				return true;
			}
			else {
				return false;
			}
		}
	}
	//@PreAuthorize("hasAuthority('bookRoom')")
/*@RequestMapping(value="/checkbookRoom/{id}",
			method = RequestMethod.POST,
			consumes =MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public boolean checkbookRoom(@RequestBody ReservationRoomDTO roomRes,@PathVariable("id") Long id){
		
		System.out.println("Dosao u search rooms");
		List<FlightReservation> frList =  flightRepository.findAllByUserId(id);
		if (!frList.isEmpty()) {
			System.out.println("postoji flajt");
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",
	                Locale.ENGLISH); 
			   Date startDate = null;
			   Date endDate = null;
			   
			   try {
				   startDate = sdf.parse(roomRes.getStartDate().toString());
				   endDate = sdf.parse(roomRes.getEndDate().toString());
			   } catch (ParseException e) {
				   e.printStackTrace();
				   
			   }
			for(FlightReservation f : frList) {
				if(endDate.getTime() >= f.getStartDate().getTime() && endDate.getTime()<= f.getEndDate().getTime())
				{
					return true;
				} else if(startDate.getTime() >= f.getStartDate().getTime() && startDate.getTime() <= f.getEndDate().getTime())
				{
					return true;
				}
				
				
			}
			
			
			
			
			
			
			
		}
		
		
		
		
		return true;
	}*/
	
	
	

	//@PreAuthorize("hasAuthority('getRatingRoom')")
	@RequestMapping(value="/getRatingRoom/{id}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<RatingRoom>>  getRatingRoom(@PathVariable("id") Long idRoom) {

		List<RatingRoom> returnList = new ArrayList<RatingRoom>();
		Room room = roomService.findOneById(idRoom);
		returnList = ratingRoomRepository.findAllByRoom(room);
		
		return new ResponseEntity<List<RatingRoom>>(returnList,HttpStatus.OK);

	}
	@RequestMapping(value="/countAverageRating/{id}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Double countAverageRating(@PathVariable("id") Long idRoom) {
		System.out.println("dosau u izracunaj prosjek");

		Double finalCount = 0.0;
		int sum = 0;
		List<RatingRoom> returnList = new ArrayList<RatingRoom>();
		Room room = roomService.findOneById(idRoom);
		returnList = ratingRoomRepository.findAllByRoom(room);
		for (RatingRoom rr:returnList) {
			sum += rr.getRate();
		}
		if (!returnList.isEmpty()) {
			finalCount = (double) (sum/returnList.size());
		}
		System.out.println("prosjek je: "+finalCount);

		return finalCount;

	}
	
	@RequestMapping(value="/isReserved/{id}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public boolean isReserved(@PathVariable("id") Long idRoom) {

		Date today=new Date();
				
		List<ReservationRoom> returnList = new ArrayList<ReservationRoom>();
		Room room = roomService.findOneById(idRoom);
		returnList = reservationRoomService.findAllByRoom(room);
		
		if(!returnList.isEmpty()) {
			for (ReservationRoom rr: returnList) {
				if(today.getTime() <= rr.getStartDate().getTime() && today.getTime()<= rr.getEndDate().getTime())
				{
					System.out.println("pronsasao1  " + rr.getStartDate());
					return true;
				} 
			}
		}
		
		return false;

	}
	
	@RequestMapping(
			value = "/getDiscountRooms/{city}", 
			method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<DiscountHotel>>  getDiscountRooms(@PathVariable("city") String city) {
		
		List<Hotel> hotels = hotelRepository.findAllByCity(city);
		List<DiscountHotel> pomocni = new ArrayList<DiscountHotel>();
		List<DiscountHotel> returnValue = new ArrayList<DiscountHotel>();
		
		if (!hotels.isEmpty()) {
			
			for(Hotel h : hotels) {
				pomocni = dhRepository.findAllByHotel(h);
				if (!pomocni.isEmpty()) {
					for(DiscountHotel dh: pomocni) {
						returnValue.add(dh);
					}
				}
			}
		}
		
		
		
		System.out.println("Pronasao je discount soba: " + returnValue.size());
		return new ResponseEntity<List<DiscountHotel>>(returnValue, HttpStatus.OK);
	}
	

	@RequestMapping(value="/searchFast",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> searchFast(@RequestBody SearchFormServices searchForm){
		System.out.println("Dosao u search rooooom faaaaaaaaaast");
		
		List<DiscountHotel> povratna= new ArrayList<DiscountHotel>();
		
		//List<RentACar> all = rentalcarService.getAll();
		List<Hotel> all = hotelRepository.findAll();
		List<Hotel> returnList = new ArrayList<Hotel>();
		
		List<Hotel> returnList2 = new ArrayList<Hotel>();
		List<Hotel> returnList3 = new ArrayList<Hotel>();
		List<DiscountHotel> pomocna1= new ArrayList<DiscountHotel>();

		//ako se pretrazuje po nazivu
				if (searchForm.getNameHotel() != null) {
					for (Hotel h : all) {
						if (h.getName().contains(searchForm.getNameHotel())) {
							returnList.add(h);
						}
						returnList3 = returnList;
					}
					//ako se pretrazuje i po nazivu i po gradu
					if (searchForm.getCity() != null) {
						for (Hotel h : returnList) {
							if (h.getCity().equals(searchForm.getCity())) {
								returnList2.add(h);
							}
						}
						returnList3 = returnList2;
					}
				}
				//ako se ne pretrazuje po nazivu nego samo gradu 
				else {
					if (searchForm.getCity() != null) {
						for (Hotel h : all) {
							if (h.getCity().equals(searchForm.getCity())) {
								returnList.add(h);	
							}
						}
						returnList3 = returnList;
					}
					
					//ni po nazivu ni po gradu
					returnList3 = all;
				}
		
		//pretraga po datumu
		
		if (searchForm.getStartDate()!=null && searchForm.getEndDate()!=null) {
			System.out.println("u trecoj lisri ima hotela: " +returnList3.size() );
			for (Hotel hot : returnList3) {
				 boolean free = true;
				 
				System.out.println("usao u prvu petlju : svih hotela");
				//pronalazim sve sobe hotela
				pomocna1 = dhRepository.findAllByHotel(hot);
				int reserved = pomocna1.size();
				
				if (!pomocna1.isEmpty()) {			
					for(DiscountHotel dh : pomocna1) {
						free = checkforfreeDH(dh, searchForm.getEndDate(), searchForm.getStartDate());
						if (!free) {
							povratna.add(dh);
						}
					}
				}

			}


		}
		/*
		if (!returnList.isEmpty()) {
			for(Hotel hot: returnList) {
				List<DiscountHotel> dh = dhRepository.findAllByHotel(hot);
				povratna.addAll(dh);
			}
		}*/
		
		return new ResponseEntity<List<DiscountHotel>>(povratna, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/getDiscountRoomsid/{id}", 
			method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<DiscountHotel>>  getDiscountRoomsid(@PathVariable("id") Long id) {
		
		Hotel hotel = hotelRepository.findOneById(id);
		List<DiscountHotel> pomocni = new ArrayList<DiscountHotel>();
		
		if (hotel!=null) {
			pomocni= dhRepository.findAllByHotel(hotel);
			
		}
		return new ResponseEntity<List<DiscountHotel>>(pomocni, HttpStatus.OK);
	
	}
	
	public boolean checkforfreeDH(DiscountHotel res, Date endDate, Date startDate) {
		
		//provjeravamo datum koji smo unijeli za preuzimanje vozila
		//ako je on nakon datuma vracanja vozila koji je registrovan -ok
		if(startDate.getTime() >= res.getDateTo().getTime()) {
			return true;
			
		}
		else {
			if(endDate.getTime() <= res.getDateFrom().getTime())
			{
				return true;
			}
			else {
				return false;
			}
		}
	}
}
