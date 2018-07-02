package com.example.demo.payroll;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.payroll.dto.PersonalAnnualSalaryDTO;
import com.example.demo.payroll.property.PayrollProperty;
import com.example.demo.payroll.property.PayrollProperty.PersonalIncomeTaxProperty;
import com.example.demo.payroll.service.impl.CommonPayrollCalculatorImpl;
import com.example.demo.payroll.vo.PersonalMonthSalaryDetailVO;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class PayrollCalculatorTest {
    public static void main(String[] args){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("YYYYMMDD");

        CommonPayrollCalculatorImpl payrollCalculator = new CommonPayrollCalculatorImpl();
        payrollCalculator.setPayrollProperty(payrollProperty());

        PersonalAnnualSalaryDTO dto = new PersonalAnnualSalaryDTO();
        dto.setAnnualSalary(Long.valueOf(args[1]));
        dto.setName(args[0]);
        dto.setSalaryStartTime(dateTimeFormatter.parseDateTime(args[2]).toDate());
        dto.setSalaryEndTime(dateTimeFormatter.parseDateTime(args[3]).toDate());

        PersonalMonthSalaryDetailVO vo = payrollCalculator.calculateMonthlySalaryDetail(dto);
        String result = String.valueOf(vo.getName()) + "|" + (args[2] + " 至 " + args[3]) + "|" + vo.getTotalIncome() +
            "|" + vo.getIncomeTax() + "|" + vo.getPureIncome() +  "|" + vo.getPenision() +
            "|" + vo.getMedicalCare() +  "|" + vo.getUnemployment();

        System.out.println(result);
    }

    private static PayrollProperty payrollProperty(){
        PayrollProperty payrollProperty = new PayrollProperty();
        payrollProperty.setPersonalIncomeTaxProperties(personalIncomeTaxProperties());

        return payrollProperty;
    }

    private static List<PersonalIncomeTaxProperty> personalIncomeTaxProperties(){
        List<PersonalIncomeTaxProperty> ret = new ArrayList<>();

        ret.add(PersonalIncomeTaxProperty.builder().levelMin(0L).levelMax(1500L).levelPercent(0.03).build());
        ret.add(PersonalIncomeTaxProperty.builder().levelMin(1500L).levelMax(4500L).levelPercent(0.1).build());
        ret.add(PersonalIncomeTaxProperty.builder().levelMin(4500L).levelMax(9000L).levelPercent(0.2).build());
        ret.add(PersonalIncomeTaxProperty.builder().levelMin(9000L).levelMax(35000L).levelPercent(0.25).build());
        ret.add(PersonalIncomeTaxProperty.builder().levelMin(35000L).levelMax(55000L).levelPercent(0.3).build());
        ret.add(PersonalIncomeTaxProperty.builder().levelMin(55000L).levelMax(80000L).levelPercent(0.35).build());
        ret.add(PersonalIncomeTaxProperty.builder().levelMin(80000L).levelMax(Long.MAX_VALUE).levelPercent(0.45).build());

        return ret;
    }
}
