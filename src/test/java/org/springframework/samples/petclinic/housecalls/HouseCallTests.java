package org.springframework.samples.petclinic.housecalls;

+import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.owner.*;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
// Ensure that if the mysql profile is active we connect to the real database:
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class HouseCallTests {
	@Autowired
	protected OwnerRepository owners;

	@Autowired
	protected VetRepository vets;

	@Test
	public void tryCreateHouseCall(){
		HouseCall newHouseCall = new HouseCall();
		newHouseCall.setDescription("This is a test instance");
		newHouseCall.setZipcode("36574ZT");
		newHouseCall.setExpectedDuration(Duration.ofHours(1));
		assertEquals(newHouseCall.getDescription(), "This is a test instance");
		assertEquals(newHouseCall.getZipcode(), "36574ZT", "Zipcode is empty, not entered correctly");
	}

	@Test
	public void tryCreateHouseCallWithWrongZipCode(){
		HouseCall newHouseCall = new HouseCall();
		newHouseCall.setDescription("This is a test instance");
		newHouseCall.setZipcode("3657ZT");
		newHouseCall.setExpectedDuration(Duration.ofHours(1));
		assertEquals(newHouseCall.getDescription(), "This is a test instance");
		assertEquals(newHouseCall.getZipcode(), "");
	}

	@Test
	public void tryCreateHouseCallWithStringValueForExpectedDuration(){
		HouseCall newHouseCall = new HouseCall();
		newHouseCall.setDescription("This is a test instance");
		newHouseCall.setZipcode("3657ZT");
		newHouseCall.setExpectedDuration(Duration.parse("PT1H30M"));
		assertEquals(newHouseCall.getDescription(), "This is a test instance");
		assertEquals(newHouseCall.getZipcode(), "");
		assertEquals(Duration.parse("PT1H30M"), Duration.ofHours(1).plusMinutes(30));
	}

	@Test
	public void tryCreateHouseCallWithStringValueForDate(){
		HouseCall newHouseCall = new HouseCall();
		newHouseCall.setDescription("This is a test instance");
		newHouseCall.setZipcode("3657ZT");
		newHouseCall.setDate(LocalDate.parse("2018-12-27"));
		assertEquals(newHouseCall.getDescription(), "This is a test instance");
		assertEquals(newHouseCall.getZipcode(), "");
		assertEquals(newHouseCall.getDate(), LocalDate.of(2018, 12, 27));
	}

	@Test
	public void tryCreateHouseCallWithBadHouseNumberForLocation(){
		HouseCall newHouseCall = new HouseCall();
		newHouseCall.setDescription("This is a test instance");
		newHouseCall.setZipcode("3657ZT");
		newHouseCall.setDate(LocalDate.parse("2018-12-27"));
		newHouseCall.setLocation("somestreet", 1.5);
		assertEquals(newHouseCall.getDescription(), "This is a test instance");
		assertEquals(newHouseCall.getZipcode(), "");
		assertEquals(newHouseCall.getDate(), LocalDate.of(2018, 12, 27));
		assertEquals(newHouseCall.getHouseNumber(), -1);
	}

	@Test
	@Transactional
	public void tryAddHouseCallAsVisit(){
		Owner owner6 = this.owners.findById(6);
		Pet pet7 = owner6.getPet(7);
		int found = pet7.getVisits().size();
		HouseCall houseCall = new HouseCall();
		houseCall.setDescription("test");

		owner6.addVisit(pet7.getId(), houseCall);
		this.owners.save(owner6);

		assertThat(pet7.getVisits()) //
			.hasSize(found + 1) //
			.allMatch(value -> value.getId() != null);
	}

	@Test
	@Transactional
	public void tryAddHouseCallAsVisitWithCorrectZipCode(){
		Owner owner6 = this.owners.findById(6);
		Pet pet7 = owner6.getPet(7);
		int found = pet7.getVisits().size();
		HouseCall houseCall = HouseCall.getHouseCall("36572ZT");
		houseCall.setDescription("test");

		owner6.addVisit(pet7.getId(), houseCall);
		this.owners.save(owner6);

		assertThat(pet7.getVisits()) //
			.hasSize(found + 1) //
			.allMatch(value -> value.getId() != null);
	}

	@Test
	@Transactional
	public void tryAddHouseCallAsVisitWithWrongZipCode(){
		Owner owner6 = this.owners.findById(6);
		Pet pet7 = owner6.getPet(7);
		int found = pet7.getVisits().size();
		HouseCall houseCall = HouseCall.getHouseCall("372ZT");

		if(houseCall != null){
			owner6.addVisit(pet7.getId(), houseCall);
			this.owners.save(owner6);
		}

		assertThat(pet7.getVisits()) //
			.hasSize(found) //
			.allMatch(value -> value.getId() != null);
	}
}
