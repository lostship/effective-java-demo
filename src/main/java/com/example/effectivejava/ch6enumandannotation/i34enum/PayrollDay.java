package com.example.effectivejava.ch6enumandannotation.i34enum;

public enum PayrollDay {
    MON,
    TUE,
    WED,
    THU,
    FRI,
    SAT(PayType.WEEKEND),
    SUN(PayType.WEEKEND);

    private final PayType payType;

    PayrollDay() {
        this.payType = PayType.WORKDAY;
    }

    PayrollDay(PayType payType) {
        this.payType = payType;
    }

    int pay(int hoursWorked, int payRate) {
        return payType.pay(hoursWorked, payRate);
    }

    private enum PayType {
        WORKDAY {
            @Override
            int pay(int hoursWorked, int payRate) {
                int basePay = hoursWorked * payRate;
                int overtimePay = hoursWorked <= HOURS_PER_SHIFT ? 0 : (hoursWorked - HOURS_PER_SHIFT) * payRate / 2;
                return basePay + overtimePay;
            }
        },
        WEEKEND {
            @Override
            int pay(int hoursWorked, int payRate) {
                int basePay = hoursWorked * payRate;
                int overtimePay = basePay / 2;
                return basePay + overtimePay;
            }
        };

        private static final int HOURS_PER_SHIFT = 8;

        abstract int pay(int hoursWorked, int payRate);
    }
}
