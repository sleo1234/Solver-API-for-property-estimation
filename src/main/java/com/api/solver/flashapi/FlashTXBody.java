package com.api.solver.flashapi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FlashTXBody {

    private List<String> names;

    private List<Double> xmol;

    private Double t;

    private Double x;

}