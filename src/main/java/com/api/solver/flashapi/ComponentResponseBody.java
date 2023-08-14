package com.api.solver.flashapi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComponentResponseBody {


    public List<Double> Tc;
    public List<Double> Pc;

    public List<Double> omega;


    @Override
    public String toString() {
        return "ComponentResponseBody{" +
                "Tc=" + Tc +
                ", Pc=" + Pc +
                ", omega=" + omega +
                '}';
    }



}