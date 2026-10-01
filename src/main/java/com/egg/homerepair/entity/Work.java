package com.egg.homerepair.entity;

import com.egg.homerepair.enums.WorkStatus;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
public class Work {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(nullable = false, length = 100)
    private String workName;

    @Column(nullable = false, length = 700)
    private String workDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private WorkStatus workStatus;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private User userCustomerId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "provider_id", nullable = false)
    private User userProviderId;

    @Column(length = 500)
    private String review;

    private int ratingWork;
}
