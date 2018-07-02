package com.example.demo.payroll.service;

import java.util.List;

import com.example.demo.payroll.dto.PersonalAnnualSalaryDTO;
import com.example.demo.payroll.vo.PersonalMonthSalaryDetailVO;

public interface PayrollCalculator {
    PersonalMonthSalaryDetailVO calculateMonthlySalaryDetail(PersonalAnnualSalaryDTO dto);

    List<PersonalMonthSalaryDetailVO> calculateMonthSalaryDetailList(PersonalAnnualSalaryDTO dto);
}
