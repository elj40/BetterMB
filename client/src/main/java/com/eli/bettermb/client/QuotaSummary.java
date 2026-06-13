package com.eli.bettermb.client;

import java.util.List;

public class QuotaSummary {
    public static class QuotaIncreaseDTO {
        public int quotaIncreaseCode;
        public String quotaIncreaseAmount;
    }

    public static class QuotaDecreaseDTO {
        public int quotaDecreaseCode;
        public String quotaDecreaseAmount;
    }

    public static class CobIncreaseDTO {
        public int cobIncreaseCode;
        public String cobIncreaseAmount;
    }

    public static class CobDecreaseDTO {
        public int cobDecreaseCode;
        public String cobDecreaseAmount;
    }
    public String quotaPendingMessage;
    public String currentQuotaDesc;
    public String cobQuotaDesc;
    public String balanceDesc;
    public String mealUsageDesc;
    public List<QuotaIncreaseDTO> quotaIncreaseDTO;
    public List<QuotaDecreaseDTO> quotaDecreaseDTO;
    public List<CobIncreaseDTO> cobIncreaseDTO;
    public List<CobDecreaseDTO> cobDecreaseDTO;

}
