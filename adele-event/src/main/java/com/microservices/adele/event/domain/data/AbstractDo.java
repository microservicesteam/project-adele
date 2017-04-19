package com.microservices.adele.event.domain.data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractDo<ID> {
    
    @Id
    @GeneratedValue
    public final ID id;
    
    protected AbstractDo(ID id) {
        this.id = id;
    }

}
