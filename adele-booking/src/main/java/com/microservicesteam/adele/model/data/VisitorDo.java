package com.microservicesteam.adele.model.data;

import java.time.LocalDate;

import javax.persistence.Entity;

import com.microservicesteam.adele.model.Visitor;

@Entity
public class VisitorDo extends AbstractDo<Long> {
	
	public final String name;
	
	public final LocalDate birthDate;
	
	public final String address;
	
	public final int discountInPercent;
	
	public VisitorDo() {
		super(null);
		this.name = null;
		this.birthDate = null;
		this.address = null;
		this.discountInPercent = 0;
	}

	private VisitorDo(Long id, String name, LocalDate birthDate, String address, int discountInPercent) {
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
