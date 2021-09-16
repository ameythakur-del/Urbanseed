package model;

public class Coupon {
    String code, percent, condition;

    public Coupon() {
    }

    public Coupon(String code, String percent, String condition) {
        this.code = code;
        this.percent = percent;
        this.condition = condition;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
