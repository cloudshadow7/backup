package com.dabaitu.store.demo.mianshi.objectiva;

import lombok.Data;

import java.util.Date;
import java.util.StringJoiner;

/**
 * @Author: ZhangLuYang
 * @Date: 2020/3/17 14:12
 */
public class Test {
/*
    测试题目：
        请为XX公司设计一套工资计算系统。
        XX公司有三种类型的雇工， 不同类型的员工有不同的工资计算方式，
        具体如下。 另外该公司有一种福利，如果该月员工过生日，则公司会额外奖励100元。
        固定工资的员工，每月固定工资为6000元。
        小时工，每小时薪资为35元。每月工作160小时，超出部分薪资按照1.3倍发放。
        销售人员， 基础工资为每月3000，每月基础销售额应为20000，
                如果销售额为20000-30000，则超出部分（超出20000部分）提成率为5%，
                如果销售额为30000及以上，则超出部分（超出20000部分）提成率为8%。
        请注意
        员工的固定工资，时薪，提成率和底薪都可能会调整。
        员工类型可能会增加。
*/

    public static void main(String[] args) {
        //创建一个销售员工
        Emp emp1 = new SaleEmp("张三", new Date(), 40000.00);
        Emp emp2 = new SaleEmp("张三", new Date(), 30000.00);
        Emp emp3 = new SaleEmp("张三", new Date(), 20000.00);
        Emp emp4 = new SaleEmp("张三", new Date(), 10000.00);

        //查询他们的分别工资是多少
        CalSalary calSalary = new SaleEmpCalSalaryImpl();
        Double e1 = calSalary.calSalary(emp1);
        Double e2 = calSalary.calSalary(emp2);
        Double e3 = calSalary.calSalary(emp3);
        Double e4 = calSalary.calSalary(emp4);

        System.out.println(emp1);
        System.out.println("e1: " +e1);
        System.out.println("e2  " +e2);
        System.out.println("e2  " +e3);
        System.out.println("e2  " +e4);
    }

}

/* 使用lombok简化实体get set */
// 员工
@Data
class Emp {
    public String name;
    public Date birthday;
}

// Emp 员工的子类: 固定工资的员工
class SalaryEmp extends Emp {
    private String type;

    public SalaryEmp() {
        this.type = "salary";
    }
}

// Emp 员工的子类: 小时工
@Data
class HourEmp extends Emp {
    private String type;
    private int workingHours;

    public HourEmp(String type, int workingHours) {
        this.workingHours = workingHours;
        this.type = "hour";
    }
}

// Emp 员工的子类: 销售员工
@Data
class SaleEmp extends Emp {
    private String type;
    private Double amount; //销售额

    SaleEmp(String name, Date birthday, Double amount) {
        super.setName(name);
        super.setBirthday(birthday);
        this.amount = amount;
        this.type = "sale";
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SaleEmp.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("birthday=" + birthday)
                .add("type='" + type + "'")
                .add("amount=" + amount)
                .toString();
    }
}


//计算员工薪资的抽象类
//计算薪水的抽象方法
abstract class CalSalary<T> {
    public abstract Double calSalary(T emp);
}

class SalaryEmpCalSalaryImpl extends CalSalary<SalaryEmp> {

    private final Double price = 3000.00;
    private final Double min = 1.3;
    private final Double max = 1.3;

    //固定工资的员工，每月固定工资为6000元。
    @Override
    public Double calSalary(SalaryEmp emp) {
        return 6000.00;
    }
}

class HourEmpCalSalaryImpl extends CalSalary<HourEmp> {

    private final Double power = 1.3;

    //每月160小时内 => 小时工每小时 = 35元
    //每月大于160小时的 => 总时间-160 = 35 * 1.3;
    // totalPrice = 160 * 35 + (totalTime - 160) * (35 * 1.3)
    @Override
    public Double calSalary(HourEmp hourEmp) {
        int hours = hourEmp.getWorkingHours();
        Double totalPrice = 160 * 35 + (hours - 160) * (35 * power);
        return totalPrice;
    }
}

class SaleEmpCalSalaryImpl extends CalSalary<SaleEmp> {

    private final Double price = 3000.00;
    //任务线,超出算提成
    private final Double minPrice = 20000.00;
    private final Double maxPrice = 30000.00;

    //提成率
    private final Double minPower = 0.05;
    private final Double maxPower = 0.08;

    //固定工资的员工，每月固定工资为6000元。
    @Override
    public Double calSalary(SaleEmp saleEmp) {
        //总销售额
        Double amount = saleEmp.getAmount();
        //5%的提成计算
        if (amount > minPrice && amount <= maxPrice) {
            return getPrice(amount, minPower);
        }
        //8%的提成计算
        if (amount > maxPrice) {
            return getPrice(amount, maxPower);
        }
        return price;
    }

    //计算提成私有函数
    private Double getPrice(Double amount, Double minPower) {
        double v = (amount - minPrice) * minPower;
        //底薪+提成
        return price + v;
    }
}



