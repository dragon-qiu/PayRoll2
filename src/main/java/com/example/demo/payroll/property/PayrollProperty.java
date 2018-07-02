package com.example.demo.payroll.property;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "payroll")
public class PayrollProperty {
    /**
     * 缴费基数下限
     */
    private Long conributeBaseMin = 4279L;
    /**
     * 缴费基数上限
     */
    private Long conributeBaseMax= 21396L;
    /**
     * 养老金比例
     */
    private Double penisionPercent = 0.08;
    /**
     * 医疗保险比例
     */
    private Double medicarePercent = 0.02;
    /**
     * 失业金比例
     */
    private Double unemploymentPercent = 0.005;

    private List<PersonalIncomeTaxProperty> personalIncomeTaxProperties;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PersonalIncomeTaxProperty {
        /**
         * 档位下限
         */
        private Long levelMin = 0L;
        /**
         * 档位上限
         */
        private Long levelMax = Long.MAX_VALUE;
        /**
         * 档位应缴比例
         */
        private Double levelPercent;
    }
}


