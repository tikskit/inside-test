package ru.tikskit.insidetest.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Auth {
    @Id
    private String name;
    @Column
    private String password;
}
