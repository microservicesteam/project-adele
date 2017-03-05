package com.microservicesteam.adele.model.data;

import javax.persistence.Entity;

import com.microservicesteam.adele.model.User;

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
