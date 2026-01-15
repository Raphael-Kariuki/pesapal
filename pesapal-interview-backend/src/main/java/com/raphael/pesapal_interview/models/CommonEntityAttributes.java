package com.raphael.pesapal_interview.models;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Embeddable
public class CommonEntityAttributes {
    @Basic(optional = false)
    @Column(name = "user_name", nullable = false, updatable = false, insertable = true)
    private String userName;

    @Basic(optional = false)
    @Column(name = "update_user", nullable = true, updatable = true, insertable = false)
    private String updateUser;


    @Basic
    @Column(name = "inactive")
    private Boolean inactive;


}
