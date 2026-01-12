/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raphael.pesapal_interview.models;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 *
 * @author mo
 */
@MappedSuperclass
@Getter
public abstract class BaseEntity{


    @Column(name = "trans_time")
    @CreationTimestamp(source = SourceType.VM)
    private LocalDateTime transTime;

    @Column(name = "update_time")
    @UpdateTimestamp(source = SourceType.VM)
    private LocalDateTime updateTime;


}