package com.example.demo.payroll.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.payroll.dto.PersonalAnnualSalaryDTO;
import com.example.demo.payroll.property.PayrollProperty;
import com.example.demo.payroll.property.PayrollProperty.PersonalIncomeTaxProperty;
import com.example.demo.payroll.service.PayrollCalculator;
import com.example.demo.payroll.utils.DateTimeUtils;
import com.example.demo.payroll.vo.PersonalMonthSalaryDetailVO;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@EnableConfigurationProperties(PayrollProperty.class)
public class CommonPayrollCalculatorImpl implements PayrollCalculator{
    @Autowired
    @Setter
    private PayrollProperty payrollProperty;

    @Override
    public PersonalMonthSalaryDetailVO calculateMonthlySalaryDetail(PersonalAnnualSalaryDTO dto) {

        check(dto);

        PersonalMonthSalaryDetailVO ret = new PersonalMonthSalaryDetailVO();

        Long monthlySalary = dto.getAnnualSalary() / 12;
        ret.setTotalIncome(monthlySalary);
        ret.setMonthlySalaryEndTime(dto.getSalaryEndTime());
        ret.setMonthlySalaryStartTime(dto.getSalaryStartTime());
        ret.setName(dto.getName());
        setContribute(ret);
        setIncome(ret);

        return ret;
    }

    @Override
    public List<PersonalMonthSalaryDetailVO> calculateMonthSalaryDetailList(PersonalAnnualSalaryDTO dto) {
        List<PersonalMonthSalaryDetailVO> ret = new ArrayList<>();

        DateTime start = new DateTime(dto.getSalaryStartTime().getTime());
        DateTime end = new DateTime(dto.getSalaryEndTime());

        if (start.isAfter(end)){
            return ret;
        }

        DateTime currMonthStart = start;
        DateTime currMonthEnd = start.dayOfMonth().withMaximumValue();
        currMonthEnd = currMonthEnd.isBefore(end) ? currMonthEnd:end;

        do {
            Integer monthWeekDays = DateTimeUtils.monthWeekDays(currMonthStart);
            Integer monthWorkDays = DateTimeUtils.weekDays(currMonthStart,currMonthEnd);
            PersonalMonthSalaryDetailVO vo = new PersonalMonthSalaryDetailVO();

            Long monthlySalary = dto.getAnnualSalary() * monthWorkDays/ (12 * monthWeekDays);
            vo.setTotalIncome(monthlySalary);
            vo.setMonthlySalaryEndTime(currMonthEnd.toDate());
            vo.setMonthlySalaryStartTime(currMonthStart.toDate());
            vo.setName(dto.getName());
            setContribute(vo);
            setIncome(vo);
            ret.add(vo);
            currMonthStart = currMonthStart.plusMonths(1).dayOfMonth().withMinimumValue();
            currMonthEnd = currMonthEnd.plusMonths(1).dayOfMonth().withMaximumValue();
            currMonthEnd = currMonthEnd.isBefore(end) ? currMonthEnd:end;
        }while (!currMonthStart.isAfter(end));

        return ret;
    }

    /**
     * 设置三险一金
     * @param vo
     *          待设置值的返回数据
     */
    private void setContribute(PersonalMonthSalaryDetailVO vo){
        if (vo.getTotalIncome() <= payrollProperty.getConributeBaseMin()){
            return;
        }

        Long contributeBase = Math.min(vo.getTotalIncome(),payrollProperty.getConributeBaseMax());
        vo.setMedicalCare(Double.valueOf(contributeBase * payrollProperty.getMedicarePercent()).longValue());
        vo.setPenision(Double.valueOf(contributeBase * payrollProperty.getPenisionPercent()).longValue());
        vo.setUnemployment(Double.valueOf(contributeBase * payrollProperty.getUnemploymentPercent()).longValue());
    }

    private void setIncome(PersonalMonthSalaryDetailVO vo){
        Long incomeTaxBase = vo.getTotalIncome() - vo.getMedicalCare() - vo.getPenision() - vo.getUnemployment();

        double incomeTax = 0d;
        //所得税计算逻辑
        for (PersonalIncomeTaxProperty personalIncomeTaxProperty:payrollProperty.getPersonalIncomeTaxProperties()){
            if (incomeTaxBase >= personalIncomeTaxProperty.getLevelMin() && incomeTaxBase <= personalIncomeTaxProperty.getLevelMax()){
                incomeTax += (incomeTaxBase - personalIncomeTaxProperty.getLevelMin()) * personalIncomeTaxProperty.getLevelPercent();
            }else if (incomeTaxBase > personalIncomeTaxProperty.getLevelMax()){
                incomeTax += (personalIncomeTaxProperty.getLevelMax() - personalIncomeTaxProperty.getLevelMin()) * personalIncomeTaxProperty.getLevelPercent();
            }
        }

        vo.setIncomeTax(Double.valueOf(incomeTax).longValue());
        vo.setPureIncome(incomeTaxBase - vo.getIncomeTax());
    }

    private void check(PersonalAnnualSalaryDTO dto){
        if (dto == null || dto.getSalaryStartTime() == null || dto.getSalaryEndTime() == null){
            throw new RuntimeException("缺少参数!");
        }

        DateTime startDateTime = new DateTime(dto.getSalaryStartTime().getTime());
        DateTime endDateTime = new DateTime(dto.getSalaryEndTime().getTime());

        if (startDateTime.year().get() != endDateTime.year().get() ||
            startDateTime.monthOfYear().get() != endDateTime.monthOfYear().get()  ||
            startDateTime.dayOfMonth().get() != startDateTime.dayOfMonth().withMinimumValue().dayOfMonth().get() ||
            endDateTime.dayOfMonth().get() != endDateTime.dayOfMonth().withMaximumValue().dayOfMonth().get()){
            throw new RuntimeException("日期输入不合法，请检查！");
        }
    }
}
