package com.microservicesteam.adele.model.sample;

import static com.microservicesteam.adele.model.EventStatus.OPEN;

import java.math.BigDecimal;

import org.joda.time.DateTime;

import com.microservicesteam.adele.model.Coordinates;
import com.microservicesteam.adele.model.Event;
import com.microservicesteam.adele.model.ImmutableCoordinates;
import com.microservicesteam.adele.model.ImmutableEvent;
import com.microservicesteam.adele.model.ImmutablePosition;
import com.microservicesteam.adele.model.ImmutablePrice;
import com.microservicesteam.adele.model.ImmutableSector;
import com.microservicesteam.adele.model.ImmutableVenue;
import com.microservicesteam.adele.model.Sector;
import com.microservicesteam.adele.model.Venue;

public class SampleData {
	
	public static Event event() {
		return ImmutableEvent.builder().withName("Adele The Finale @ Wembley Stadium")
				.withDescription(
						"Wembley Stadium connected by EE is delighted to announce we will welcome Adele to the national stadium in the summer of 2017 for the finale of her world tour.")
				.withDateTime(DateTime.parse("2017-06-29T18:00:00+01:00"))
				.withStatus(OPEN)
				.withVenue(venue())
				.build();
	}

	public static Venue venue() {
		return ImmutableVenue.builder()
				.withAddress("Wembley Stadium, Wembley, London HA9 0WS")
				.withCoordinates(coordinates())
				.addSectors(sector())
				.build();
	}

	public static Sector sector() {
		return ImmutableSector.builder()
				.withCapacity(100)
				.withPosition(ImmutablePosition.builder().withId(1L).build())
				.withPrice(ImmutablePrice.builder().withAmount(BigDecimal.valueOf(150)).withCurrency("GBP").build())
				.build();
	}

	public static Coordinates coordinates() {
		return ImmutableCoordinates.builder()
				.withLatitude(51.5560208)
				.withLongitude(-0.2795188000000053)
				.build();
	}

}
