package com.microservicesteam.adele.booking.domain.data;

import com.microservicesteam.adele.booking.domain.Visitor;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class VisitorDo extends AbstractDo<Long> {
	
	public final String name;
	
	public final LocalDate birthDate;
	
	public final String address;
	
	public final BigDecimal discountInPercent;
	
	public VisitorDo() {
		super(null);
		this.name = null;
		this.birthDate = null;
		this.address = null;
		this.discountInPercent = null;
	}

	private VisitorDo(Long id, String name, LocalDate birthDate, String address, BigDecimal discountInPercent) {
		super(id);
		this.name = name;
		this.birthDate = birthDate;
		this.address = address;
		this.discountInPercent = discountInPercent;
	}
	
	public Visitor toImmutable() {
		return Visitor.builder()
				.withName(name)
				.withBirthDate(birthDate)
				.withAddress(address)
				.withDiscountInPercent(discountInPercent)
				.build();
	}
	
	public static VisitorDo fromImmutable(Visitor visitor) {
		return new VisitorDo(visitor.id(), visitor.name(), visitor.birthDate(), visitor.address(), visitor.discountInPercent());
	}

}