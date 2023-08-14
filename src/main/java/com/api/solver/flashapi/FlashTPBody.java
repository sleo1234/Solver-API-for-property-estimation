package com.api.solver.flashapi;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class FlashTPBody {

    private List<String> names;

    private List<Double> xmol;

    private Double t;

    private Double p;



}
