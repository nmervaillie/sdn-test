package com.sdn.test;

import com.sdn.test.domain.Car;
import com.sdn.test.domain.OwnsRelationship;
import com.sdn.test.domain.Person;
import com.sdn.test.repository.CarRepository;
import com.sdn.test.repository.OwnsRepository;
import com.sdn.test.repository.PersonRepository;

import java.security.SecureRandom;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.neo4j.driver.v1.exceptions.ServiceUnavailableException;
import org.neo4j.driver.v1.exceptions.SessionExpiredException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class Neo4jRepo {

    private static final Logger LOG = LoggerFactory.getLogger(Neo4jRepo.class);

    @Autowired
    PersonRepository personRepository;

    @Autowired
    CarRepository carRepository;

    @Autowired
    OwnsRepository ownsRepository;

    SecureRandom random = new SecureRandom();


    @Retryable(value = {SessionExpiredException.class, ServiceUnavailableException.class}, maxAttempts=10, backoff=@Backoff(delay= 3000, multiplier = 2))
    public void crudTest(Properties properties) {

        LOG.error("Sample CRUD started...");
        Long count = Long.parseLong(properties.getProperty("crud.count"));
        Long millsec = Long.parseLong(properties.getProperty("crud.sleep.millsec"));
        Long startTime = System.nanoTime();

        Long carCount = 0L;
        Long personCount = 0L;
        Long relCount = 0L;
        Long updateCount = 0L;
        Long deleteCount = 0L;

        SecureRandom random = new SecureRandom();

        for (long i = 0; i < count; i++) {

            LOG.info("Iteration :" + i);

            Person person = createPerson();
            personCount++;

            if (random.nextInt(10) == 5) {
                LOG.info("Just Creating only Person Node");
                continue;
            }

            Car car = createCar();
            carCount++;

            OwnsRelationship ownsRelationship = createOwnsRelation(person, car);

            if (car.getId() % 5 == 0) {
                LOG.info("Update - Car id " + car.getId());
                car = carRepository.findOne(car.getId());
                car.setCarName("BMW");
                carRepository.save(car);
                LOG.info("{}", car);
                updateCount++;
            }

            if (car.getId() % 9 == 0) {
                LOG.info("Delete car, person and owns rel Using - Car id " + car.getId());
                LOG.info("{}", ownsRelationship);
                carRepository.delete(car);
                personRepository.delete(person);
                ownsRepository.delete(ownsRelationship);
                deleteCount = deleteCount + 3;
            }


            try {
                if (millsec > 0) {
                    Thread.sleep(millsec);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Long elapsedTime = System.nanoTime() - startTime;

        StringBuffer sb = new StringBuffer();
        sb.append("\nCRUD Statistics");
        sb.append("\nRead Count :").append(count);
        sb.append("\nWrite Count :").append(carCount + personCount + relCount);
        sb.append("\nUpdate Count :").append(updateCount);
        sb.append("\nDelete Count :").append(deleteCount);
        sb.append("\nTotal time in Sec :").append(TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS));
        LOG.error(sb.toString());
        LOG.error("Sample CRUD Completed");
    }


    @Transactional(readOnly = true)
    public void readTest(Properties properties) {

        Long startId = Long.parseLong(properties.getProperty("read.start.id"));
        Long endId = Long.parseLong(properties.getProperty("read.end.id"));
        Long millsec = Long.parseLong(properties.getProperty("read.sleep.millsec"));

        LOG.error("Sample Read started...");
        Long carCount = 0L;
        Long personCount = 0L;
        Long relCount = 0L;
        Long startTime = System.nanoTime();

        for (long i = startId; i < endId; i++) {
            Car car = carRepository.findOne(i);
            Person person = personRepository.findOne(i);
            OwnsRelationship ownsRelationship = ownsRepository.findOne(i);

            if (car != null) carCount++;
            if (person != null) personCount++;
            if (ownsRelationship != null) relCount++;
            try {
                if (millsec > 0) {
                    Thread.sleep(millsec);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Long elapsedTime = System.nanoTime() - startTime;

        StringBuffer sb = new StringBuffer();
        sb.append("\nRead Statistics");
        sb.append("\nTotal Reads made:").append((endId - startId) * 3);
        sb.append("\nCar Read Count :").append(carCount);
        sb.append("\nPerson Read Count :").append(personCount);
        sb.append("\nOWNS Rel Count :").append(relCount);
        sb.append("\nTotal time in Sec :").append(TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS));
        LOG.error(sb.toString());
        LOG.error("Sample read Completed");
    }


    @Retryable(value = {SessionExpiredException.class, ServiceUnavailableException.class}, maxAttempts=10, backoff=@Backoff(delay= 3000, multiplier = 2))
    @Transactional
    public void writeTest(Properties properties) {

        LOG.error("Sample Write started...");
        Long count = Long.parseLong(properties.getProperty("write.count"));
        Long millsec = Long.parseLong(properties.getProperty("write.sleep.millsec"));
        Long startTime = System.nanoTime();

        for (long i = 0; i < count; i++) {
            createPersonOwnsCarNode();
            try {
                if (millsec > 0) {
                    Thread.sleep(millsec);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Long elapsedTime = System.nanoTime() - startTime;

        StringBuffer sb = new StringBuffer();
        sb.append("\nWrite Statistics \nWrite Count (Person,Car,OWNS rel):").append(count * 3);
        sb.append("\nTotal time in Sec :").append(TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS));
        LOG.error(sb.toString());
        LOG.error("Sample Write Completed");
    }


    class Stat {
        Long carCount = 0L;
        Long personCount = 0L;
        Long relCount = 0L;
    }

    @Retryable(value = {SessionExpiredException.class, ServiceUnavailableException.class}, maxAttempts=10, backoff=@Backoff(delay= 3000, multiplier = 2))
    public void deleteTest(Properties properties) {
        LOG.error("Sample delete started...");

        Long startId = Long.parseLong(properties.getProperty("delete.rel.start.id"));
        Long endId = Long.parseLong(properties.getProperty("delete.rel.end.id"));
        Long millsec = Long.parseLong(properties.getProperty("delete.sleep.millsec"));

        Stat stat = new Stat();
        Long startTime = System.nanoTime();

        for (long i = startId; i < endId; i++) {
            deleteById(i,stat);
            try {
                if (millsec > 0) {
                    Thread.sleep(millsec);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Long elapsedTime = System.nanoTime() - startTime;

        StringBuffer sb = new StringBuffer();
        sb.append("\nCar delete Count :").append(stat.carCount);
        sb.append("\nPerson delete Count :").append(stat.personCount);
        sb.append("\nOWNS Rel delete Count :").append(stat.relCount);
        sb.append("\nTotal time in Sec :").append(TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS));
        LOG.error(sb.toString());
        LOG.error("Sample Delete Completed");

    }


    @Transactional
    public void deleteById(Long id, Stat stat) {
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
