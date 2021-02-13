package io.opentelemetry.example.flight;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FlighController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FlighController.class);
	
	private FlightService flightService;
	
	public FlighController(FlightService flightService) {
		this.flightService = flightService;
	}

	@RequestMapping("/template")
	public List<Flight> flights(@RequestParam(value = "origin", defaultValue = "India") String origin) {
		LOGGER.info("processing Request");
		return flightService.getFlights(origin);		
	}
}