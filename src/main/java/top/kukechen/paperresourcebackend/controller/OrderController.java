package top.kukechen.paperresourcebackend.controller;

import com.mongodb.client.result.UpdateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.kukechen.paperresourcebackend.model.Grade;
import top.kukechen.paperresourcebackend.model.Order;
import top.kukechen.paperresourcebackend.model.Paper;
import top.kukechen.paperresourcebackend.model.PapersReq;
import top.kukechen.paperresourcebackend.restservice.Response;
import top.kukechen.paperresourcebackend.restservice.ResponseWrap;
import top.kukechen.paperresourcebackend.service.MongoDBUtil;
import top.kukechen.paperresourcebackend.service.PageModel;
import top.kukechen.paperresourcebackend.units.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static top.kukechen.paperresourcebackend.restservice.Response.STAUTS_FAILED;
import static top.kukechen.paperresourcebackend.restservice.Response.STAUTS_OK;

@PassToken
@RestController
@RequestMapping("/order")
public class OrderController {

    private static Logger logger = LoggerFactory.getLogger(PaperController.class);



    @PostMapping("/add")
    @PassToken
    public Response<Order> addOrder(@RequestBody Order order) {
        MongoTemplate mongoTemplate = MongoDBUtil.mongodbUtil.mongoTemplate;
        Query query = Query.query(Criteria.where("orderName").is(order.getOrderName()));
        Order _order = mongoTemplate.findOne(query, Order.class);
        if (_order != null) {
            return new Response<Order>(STAUTS_FAILED, "订单已存在");
        }
        mongoTemplate.save(order);
        return new Response(0, order);
    }


    @PostMapping("/list")
    @PassToken
    public Response<Grade> getOrderList() {
        MongoTemplate mongoTemplate = MongoDBUtil.mongodbUtil.mongoTemplate;
        List<Paper> list = mongoTemplate.findAll(Paper.class, "paper");
        return new Response(STAUTS_OK, list);
    }

    @PostMapping("/delete")
    @PassToken
    public Response<Order> deleteOrder(@RequestBody ResponseWrap rw) {
        String id = rw.getId();
        logger.info("开始删除订单: " + id);
        int result = MongoDBUtil.removeById(id, "order");
        logger.info("删除订单: id为" + id + " 删除结果 - result: " + result);
        if (result == 1) {
            return new Response(STAUTS_OK, "删除成功");
        } else {
            return new Response(STAUTS_FAILED, "删除失败");
        }
    }


    @PostMapping("/page")
    @PassToken
    public Response<Order> getOrderPage(@RequestBody ResponseWrap rw) {
        HashMap query = new HashMap<String, Object>();
        PageModel page = MongoDBUtil.findSortPageCondition(Order.class, "order", rw.getQuery(), rw.getPageNo(), rw.getPageSize(), Sort.Direction.ASC, "created");
        return new Response(STAUTS_OK, page);
    }

    @PostMapping("/edit")
    @PassToken
    public Response<Paper> editOrder(@RequestBody Order order) throws IllegalAccessException {
        MongoDBUtil.updateMulti("orderId", order.getId(), CommonUtils.getObjectToMap(order), "order", 1);
        return new Response(0, "修改成功");
    }
}
