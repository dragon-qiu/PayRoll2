package com.example.demo.payroll.vo;

import java.util.Date;

import lombok.Data;

@Data
public class PersonalMonthSalaryDetailVO {
    private String name;

    private Date monthlySalaryStartTime;

    private Date monthlySalaryEndTime;

    private Long totalIncome = 0L;

    private Long incomeTax = 0L;

    private Long pureIncome = 0L;

    private Long penision  = 0L;

    private Long medicalCare = 0L;

    private Long unemployment = 0L;
}
