package com.microservicesteam.adele.booking.domain.data;


import com.microservicesteam.adele.booking.domain.User;

import javax.persistence.Entity;

@Entity
public class UserDo extends AbstractDo<Long> {

    private UserDo() {
        this(null);
    }

    private UserDo(Long id) {
        super(id);
    }

    public User toImmutable() {
        return User.builder()
                .id(id)
                .build();
    }

    public static UserDo fromImmutable(User user) {
        return new UserDo(user.id());
    }

}
