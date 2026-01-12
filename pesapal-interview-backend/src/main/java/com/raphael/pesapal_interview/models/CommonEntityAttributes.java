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
    @Column(name = "user_id", nullable = false, updatable = false, insertable = true)
    private Long userId;

    @Basic(optional = false)
    @Column(name = "update_user_id", nullable = true, updatable = true, insertable = false)
    private Long updateUserId;


    @Basic
    @Column(name = "inactive")
    private Boolean inactive;


}
