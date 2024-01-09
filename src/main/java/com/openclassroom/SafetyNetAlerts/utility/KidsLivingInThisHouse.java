package com.openclassroom.SafetyNetAlerts.utility;

import com.openclassroom.SafetyNetAlerts.model.Household;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KidsLivingInThisHouse {
    private String firstname;
    private String lastname;
    private long age;
    private Household household;
}
