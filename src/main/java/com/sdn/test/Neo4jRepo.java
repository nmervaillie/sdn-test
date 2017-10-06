package com.sdn.test;

import com.sdn.test.repository.CarRepository;
import com.sdn.test.repository.OwnsRepository;
import com.sdn.test.repository.PersonRepository;
import com.sdn.test.service.RetryService;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Neo4jRepo {

    private static final Logger LOG = LoggerFactory.getLogger(Neo4jRepo.class);

    @Autowired
    PersonRepository personRepository;

    @Autowired
    CarRepository carRepository;

    @Autowired
    OwnsRepository ownsRepository;

    @Autowired
	RetryService service;

    public void crudTest(Properties properties) {

        LOG.error("Sample CRUD started...");
        Long count = Long.parseLong(properties.getProperty("crud.count"));
        Long millsec = Long.parseLong(properties.getProperty("crud.sleep.millsec"));
        Long startTime = System.nanoTime();

        Stats stats = new Stats();

        for (long i = 0; i < count; i++) {

            LOG.info("Iteration :" + i);

            stats.cumulate(service.crud(i));

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
        sb.append("\nWrite Count :").append(stats.carCount + stats.personCount + stats.relCount);
        sb.append("\nUpdate Count :").append(stats.updateCount);
        sb.append("\nDelete Count :").append(stats.deleteCount);
        sb.append("\nTotal time in Sec :").append(TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS));
        LOG.error(sb.toString());
        LOG.error("Sample CRUD Completed");
    }

    public void readTest(Properties properties) {

        Long startId = Long.parseLong(properties.getProperty("read.start.id"));
        Long endId = Long.parseLong(properties.getProperty("read.end.id"));
        Long millsec = Long.parseLong(properties.getProperty("read.sleep.millsec"));

        LOG.error("Sample Read started...");
        Stats stats = new Stats();
        Long startTime = System.nanoTime();

        for (long i = startId; i < endId; i++) {
            stats.cumulate(service.read(i));
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
        sb.append("\nCar Read Count :").append(stats.carCount);
        sb.append("\nPerson Read Count :").append(stats.personCount);
        sb.append("\nOWNS Rel Count :").append(stats.relCount);
        sb.append("\nTotal time in Sec :").append(TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS));
        LOG.error(sb.toString());
        LOG.error("Sample read Completed");
    }


    public void writeTest(Properties properties) {

        LOG.error("Sample Write started...");
        Long count = Long.parseLong(properties.getProperty("write.count"));
        Long millsec = Long.parseLong(properties.getProperty("write.sleep.millsec"));
        Long startTime = System.nanoTime();

        for (long i = 0; i < count; i++) {
            service.createPersonOwnsCarNode();
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


    public void deleteTest(Properties properties) {
        LOG.error("Sample delete started...");

        Long startId = Long.parseLong(properties.getProperty("delete.rel.start.id"));
        Long endId = Long.parseLong(properties.getProperty("delete.rel.end.id"));
        Long millsec = Long.parseLong(properties.getProperty("delete.sleep.millsec"));

        Stats stat = new Stats();
        Long startTime = System.nanoTime();

        for (long i = startId; i < endId; i++) {
            stat.cumulate(service.deleteById(i));
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

}
