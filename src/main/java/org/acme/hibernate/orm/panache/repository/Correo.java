package org.acme.hibernate.orm.panache.repository;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Cacheable
public class Correo {

    @Id
    @GeneratedValue
    public Long id;

    @Column(length = 40, unique = true)
    public String name;

    public Correo() {
    }

    public Correo(String name) {
        this.name = name;
    }
}
