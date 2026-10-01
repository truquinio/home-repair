package com.egg.homerepair.entity;

import com.egg.homerepair.enums.Professions;
import com.egg.homerepair.enums.Roles;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
public class User {
    
    @Id
    @GeneratedValue (generator = "uuid")
    @GenericGenerator (name = "uuid", strategy = "uuid2")
    private String id;

    private String name;
    private String lastname;
    private String email;
    private String password;
    private Date unsubscription;
    private String phone;
    private Boolean alta;

    @Enumerated(EnumType.STRING)
    private Roles role;

    private String image;
    
    //Provider---------------------------
    
    private String description;
    private int rating;
    private Date subscription;
    //private int priceTime; (A futuro)
    
    @Enumerated(EnumType.STRING)
    private Professions profession;
    
    //Customer---------------------------
    
    //private Work works; (A futuro)
}
