package com.microservices.adele.event.domain.data;

import com.microservices.adele.event.domain.User;

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
                .withId(id)
                .build();
    }

    public static UserDo fromImmutable(User user) {
        return new UserDo(user.id());
    }

}
