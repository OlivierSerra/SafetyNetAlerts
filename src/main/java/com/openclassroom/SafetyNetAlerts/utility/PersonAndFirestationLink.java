package com.openclassroom.SafetyNetAlerts.utility;

import java.util.HashMap;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonAndFirestationLink {
    private List<PersonPhone> persons;
    private HashMap<String, Integer> AdultsAndChildren;
}
