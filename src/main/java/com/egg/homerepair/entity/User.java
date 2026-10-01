package com.egg.homerepair.entity;

import com.egg.homerepair.enums.Professions;
import com.egg.homerepair.enums.Roles;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
public class User {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(nullable = false, length = 60)
    private String name;

    @Column(nullable = false, length = 60)
    private String lastname;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    private Date unsubscription;

    @Column(length = 30)
    private String phone;

    @Column(nullable = false)
    private Boolean alta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Roles role;

    private String image;

    @Column(length = 500)
    private String description;

    private int rating;

    @Temporal(TemporalType.TIMESTAMP)
    private Date subscription;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private Professions profession;
}
