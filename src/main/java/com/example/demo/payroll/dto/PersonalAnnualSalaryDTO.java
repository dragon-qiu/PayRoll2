package com.example.demo.payroll.dto;

import java.util.Date;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class PersonalAnnualSalaryDTO {
    private String name;

    private Long annualSalary;
    @DateTimeFormat(pattern = "yyyyMMdd")
    private Date salaryStartTime;
    @DateTimeFormat(pattern = "yyyyMMdd")
    private Date salaryEndTime;
}
