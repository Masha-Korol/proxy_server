package com.dsr.proxy_server.service;

import com.dsr.proxy_server.data.dto.ProxyServersResponse.CountryResult;
import com.dsr.proxy_server.data.entity.Country;
import com.dsr.proxy_server.repositories.CountryRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * This class contains the logic that's connected with countries
 */
@Service
public class CountryService {

    private final CountryRepository countryRepository;
    private static final Logger logger = LogManager.getLogger(CountryService.class);

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    /**
     * This method add country to the database if it's not there yet
     *
     * @param countryResult entity of type CountryResult
     */
    public Country add(CountryResult countryResult) {
        Country byNameEn = countryRepository.findByNameEn(countryResult.getNameEn());
        if (byNameEn == null) {
            Country country = new Country(countryResult.getNameEn());
            Country savedCountry = countryRepository.save(country);
            logger.info("country " + country.toString() + " has been added");
            return savedCountry;
        }
        return byNameEn;
    }
}
