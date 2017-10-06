package com.sdn.test.service;

import com.sdn.test.Stats;
import com.sdn.test.domain.Car;
import com.sdn.test.domain.OwnsRelationship;
import com.sdn.test.domain.Person;
import com.sdn.test.repository.CarRepository;
import com.sdn.test.repository.OwnsRepository;
import com.sdn.test.repository.PersonRepository;

import java.security.SecureRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppService {

	private static final Logger LOG = LoggerFactory.getLogger(AppService.class);

	@Autowired
	private CarRepository carRepository;

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private OwnsRepository ownsRepository;

	private SecureRandom random = new SecureRandom();

	@Transactional(readOnly = true)
	public Stats read(long i) {

		Car car = carRepository.findOne(i);
		Person person = personRepository.findOne(i);
		OwnsRelationship ownsRelationship = ownsRepository.findOne(i);

		Stats stats = new Stats();
		if (car != null) stats.carCount++;
		if (person != null) stats.personCount++;
		if (ownsRelationship != null) stats.relCount++;
		return stats;
	}

	@Transactional
	public Stats crud(long i) {
		Stats stats = new Stats();
		Person person = createPerson();
		stats.personCount++;

		if (random.nextInt(10) == 5) {
			LOG.info("Just Creating only Person Node");
			return stats;
		}

		Car car = createCar();
		stats.carCount++;

		OwnsRelationship ownsRelationship = createOwnsRelation(person, car);

		if (car.getId() % 5 == 0) {
			LOG.info("Update - Car id " + car.getId());
			car = carRepository.findOne(car.getId());
			car.setCarName("BMW");
			carRepository.save(car);
			LOG.info("{}", car);
			stats.updateCount++;
		}

		if (car.getId() % 9 == 0) {
			LOG.info("Delete car, person and owns rel Using - Car id " + car.getId());
			LOG.info("{}", ownsRelationship);
			carRepository.delete(car);
			personRepository.delete(person);
			ownsRepository.delete(ownsRelationship);
			stats.deleteCount =+ 3;
		}
		return stats;
	}

	@Transactional
	public Stats deleteById(Long id) {
		Stats stat = new Stats();
		OwnsRelationship ownsRelationship = ownsRepository.findOne(id);
		if( ownsRelationship != null){
			Person person = ownsRelationship.getPerson();
			if(person != null ) {
				personRepository.delete(person);
				stat.personCount++;
			}

			Car car = ownsRelationship.getCar();
			if( car != null) {
				carRepository.delete(car);
				stat.carCount++;
			}
			ownsRepository.delete(ownsRelationship);
			stat.relCount++;
		}
		return stat;
	}

	@Transactional
	public Person createPerson() {
		Person person = new Person();
		person.setName("John Doe " + random.nextInt(10000));
		person = personRepository.save(person);
		return person;
	}

	@Transactional
	public Car createCar() {
		Car car = new Car();
		car.setCarName("i20");
		car = carRepository.save(car);
		return car;
	}

	@Transactional
	public OwnsRelationship createOwnsRelation(Person person, Car car) {
		OwnsRelationship ownsRelationship = new OwnsRelationship();
		ownsRelationship.setCar(car);
		ownsRelationship.setPerson(person);
		ownsRepository.save(ownsRelationship);
		return ownsRelationship;
	}

	@Transactional
	public void createPersonOwnsCarNode() {
		createOwnsRelation(createPerson(), createCar());
	}
}
