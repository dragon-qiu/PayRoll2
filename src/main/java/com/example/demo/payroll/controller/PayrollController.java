package com.example.demo.payroll.controller;

import java.util.List;

import com.example.demo.payroll.dto.PersonalAnnualSalaryDTO;
import com.example.demo.payroll.service.PayrollCalculator;
import com.example.demo.payroll.vo.PersonalMonthSalaryDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payroll")
public class PayrollController {
    @Autowired
    private PayrollCalculator payrollCalculator;

    @GetMapping("calculate")
    public PersonalMonthSalaryDetailVO calculate(PersonalAnnualSalaryDTO dto){
        return payrollCalculator.calculateMonthlySalaryDetail(dto);
    }

    @GetMapping("calculateList")
    public List<PersonalMonthSalaryDetailVO> calculateList(PersonalAnnualSalaryDTO dto){
        return payrollCalculator.calculateMonthSalaryDetailList(dto);
    }
}
