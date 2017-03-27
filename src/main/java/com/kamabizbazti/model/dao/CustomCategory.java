package com.kamabizbazti.model.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kamabizbazti.model.enumerations.CategoryType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "Category")
public class CustomCategory extends GeneralCategory {

    @ManyToOne()
    //@JoinColumn(name = "id")
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public CustomCategory() {
        super();
    }

    public CustomCategory(User user, String name) {
        super(name, CategoryType.CUSTOM);
        this.user = user;
        super.setuId(user.getId());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        super.setuId(user.getId());
    }

    public void setName(String name) {
        super.setName(name);
        super.setType(CategoryType.CUSTOM);
    }

    @Override
    public String toString() {
        return "CustomCategory [user=" + user + "]";
    }

}