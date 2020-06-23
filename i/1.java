package com.dabaitu.store.demo.mianshi;

import lombok.Data;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

/**
 * @Author: ZhangLuYang
 * @Date: 2020/3/11 13:50
 */
public class Test {
    /*

    用户故事3:作为一个用户我可以在电商平台查询自己的积分总额，积分有效期，积分消费记录

    1）根据需求设计积分模块，模块设计必须满足面向对象设计思想，支持业务逻辑在未来可扩展，易维护。
    2）积分模块需要能够与电商平台其他模块集成，能够为其他电商模块提供服务
    3）画出架构设计图
    代码实现要求
        1）3个用户故事必须都完成类，接口定义
        不需要实现dao层, 只需定义dao层接口即可,
        不需要设计数据库表,只需定义需要的JavaBean即可,
        类和接口设计需要尽可能体现面向对象设计思想, 体现出可扩展性,可维护性
        2）至少一个用户故事需要完成service层代码实现
        3）JavaBean字段根据个人对需求的理解进行设计
   */

}

/**
 * 用户故事1: 作为一个用户, 我可以在电商平台（包括用户模块，订单模块，评论模块，签到模块）购物，
 * 通过订单, 每日签到，为商品添加评论获取电商平台积分。
 */

@Service
interface CommentService {

    //评论消息
    public void addComment(Long userId);

}

class CommentServiceImpl implements CommentService {

    @Autowired
    private final ScoreService scoreService;

    CommentServiceImpl(ScoreService scoreService) {
        this.scoreService = scoreService;
    }


    @Override
    public void addComment(Long userId) {
        if (findComment(userId)) {
            //TODO 插入积分
        }
    }

    //插入积分方法
    private void giveOutScore(int score, Long userId) {
        this.scoreService.insertScore(score, userId, 2);
    }

    //2）通过评论获取积分 ，根据评论评论数量获取积分，获取积分数=评论次数x 5
    private boolean findComment(Long userId) {
        //TODO 查询评论记录表,若超出5次,则不添加积分
        return false;
    }
}


//签到
interface SingInService {
    /* 3）通过签到获取积分，根据签到次数获取积分
       a．积分当月每天都签到成功，获取积分数=当月天数x 20 ?每月结算一次积分?
       b．积分当月未能每天签到成功，获取积分数=签到次数x10 ?每月结算一次积分?
       */
    public void singIn(Long userId);

    //定时任务,每月末零时统一计算签到积分并发放
    public void giveOutScore();
}

//签到服务接口实现
class SingInServiceImpl implements SingInService {

    @Override
    public void singIn(Long userId) {
        //TODO 签到时根据用户Id更新签到人的签到数组
    }

    @Override
    public void giveOutScore() {
        //TODO 思路 :
        //  遍历每一个用户singInDays[]中得签到记录 ,并获取当月总天数,
        //     1) 若签到天数 = 当月总天数 则 天数 X 20,添加到积分表中.
        //     2) 若签到天数 < 当月总天数 则 签到次数 X 10,添加到积分表中.
    }
}

//订单服务层
interface OrderService {
 /*
    用户故事 2: 作为一个用户, 我可以在电商平台消费积分，在下单时通过积分减免订单金额
        在进行购物消费时，每10个积分可减免1元订单金额

    1）通过购物消费，根据订单金额的额度获取积分，
        a.订单金额在500元内，获取积分数=订单金额数x 0.1，
        b.订单金额在500到1000之间，获取积分数=订单金额数x 0.15
        c.订单金额在 1000以上的，获取积分数=订单金额数x 0.20
 */

    // TODO 思路:　
    //     消费时,可以使用积分抵扣金额. 同时可获取实际付款的金额对应的积分.

}

//订单实现层
class OrderServiceImpl {

}


interface ScoreService {
    //插入积分
    boolean insertScore(int num, Long userId, int type);
}

class ScoreServiceImpl implements ScoreService {

    @Autowired
    private final ScoreDao scoreDao;

    ScoreServiceImpl(ScoreDao scoreDao) {
        this.scoreDao = scoreDao;
    }

    @Override
    public boolean insertScore(int num, Long userId, int type) {
        Score score = new Score(num, type, userId);
        int result = this.scoreDao.insertScore(score);
        return result > 0;
    }
}

//积分mapper
@Mapper
interface ScoreDao {
    int insertScore(Score score);
}

/***  entity  ***/


//积分
@Data
class Score {
    /*4）积分有效期
        a)通过消费获取的积分有效期为3个月
        b)通过评论获取的积分有效期为2个月
        c)通过签到获取的积分有效期为1个月
    */
    //获取时间
    private Date createTime;
    //过期时间
    private Date deleteTime;
    //积分
    private int score;
    //获取方式(1消费 2评论 3签到 )
    private int type;
    //积分归属
    private Long userId;

    //有参构造函数
    public Score(int score, int type, Long userId) {
        //根据不同的获取方式设置到期时间
        if (type == 1) {
            this.deleteTime = getTime(90);
        }
        if (type == 2) {
            this.deleteTime = getTime(60);
        }
        if (type == 3) {
            this.deleteTime = getTime(30);
        }
        this.score = score;
        this.type = type;
        this.userId = userId;
    }

    private Date getTime(int day) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, day);
        return cal.getTime();
    }
}

//订单
@Data
class order {
    //商品Id
    private Long productId;
    //用户Id
    private Long userId;
    //实际价格
    private Double price;
    //优惠价格
    private Double favourablePrice;
    //抵扣积分数
    private Integer score;
}

// 用户
@Data
class User {
    private Long userId; //用户Id
    private String username; //用户名
    private String password; //用户密码
    private Long totalScore; //当前总积分
    private int[] singInDays = new int[31]; //签到统计数组,下标+1等于每月的日期, 1代表签到,0代表未签到
}

//评论
@Data
class Comment {
    //评论时间
    private Date createTime;
    //评论人
    private Long userId;
    //评论内容
    private String content;
    //评论状态 (1通过 2驳回 3审核中)
    private int type;
}