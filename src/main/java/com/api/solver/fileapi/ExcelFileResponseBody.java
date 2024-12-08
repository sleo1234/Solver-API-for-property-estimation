package com.api.solver.fileapi;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExcelFileResponseBody {

    private HashMap<String,Double> mapData;

}
