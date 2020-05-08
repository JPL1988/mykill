package com.controler;

import com.entity.myKill;
import com.entity.mykillOrder;
import com.exception.killException;
import com.exception.repeatException;
import com.result.Exposer;
import com.result.myKillResult;
import com.service.killService;
import com.util.SEtime;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author false
 * @date 20/4/9 21:59
 */
@Controller
public class mykillControler {

    Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private killService killService;

    @RequestMapping("/killdetail/{killid}")
    public String killdetail(@PathVariable("killid")Long killid, Model model){
        if(killid==null){
            return "killindex";
        }
        myKill mykill = killService.findById(killid,true);
        if(mykill==null){
            return "soldOut";
        }
        model.addAttribute("product",mykill);
        model.addAttribute("time",new SEtime(mykill.getStartTime().getTime(),mykill.getEndTime().getTime()));
        return "kill";
    }

    @ResponseBody
    @RequestMapping("/killdetail/{killid}/exposer")
    public Exposer exposer(@PathVariable("killid")Long killid){
        return killService.exposer(killid);
    }
    @ResponseBody
    @RequestMapping("/time/now")
    public myKillResult<Long> getDate(){
        Date date = new Date();
        return new myKillResult(true,date.getTime());
    }

    @ResponseBody
    @RequestMapping(value = "/killdetail/kill/{killid}/{md5}",
                    method = RequestMethod.POST)
    public myKillResult<String> executeKill(@PathVariable("killid")Long killid,
                                                 @PathVariable("md5")String md5,
                                                 @RequestParam("phone") Long userPhone){
        myKillResult myKillResult;
        try {
            myKillResult = killService.executeKill(killid,md5,userPhone);
        }catch (killException e){
            return new myKillResult<String>(false,"","秒杀失败");
        }catch (repeatException e){
            return new myKillResult<>(false,"","秒杀失败，请勿重复秒杀");
        }catch (Exception e){
            return new myKillResult<String>(false,"","秒杀失败");
        }
        return myKillResult;
    }
    @RequestMapping("/success/{killid}/{orderid}/{phone}")
    public String success(Model model,
                          @PathVariable("killid")Long killid,
                          @PathVariable("orderid")long orderid,
                          @PathVariable("phone")long phone){
        if(killid==null||String.valueOf(orderid).length()!=18){
            return "error";
        }
        mykillOrder order = killService.success(orderid);
        if(order==null){
            return "error";
        }
        myKill mykill = killService.findById(killid,true);
        model.addAttribute("order",order);
        if (order.getStatus() == 1) {
            model.addAttribute("status","已付款");
        }else if(order.getStatus() == 0){
            model.addAttribute("status","未付款");
        }else {
            model.addAttribute("status","已失效");
        }
        model.addAttribute("product",mykill);
        model.addAttribute("time",new SEtime(mykill.getStartTime().getTime(),mykill.getEndTime().getTime()));
        return "success";
    }

    @ResponseBody
    @RequestMapping("/payfor/{killid}/{orderid}/{userPhone}")
    public String pay(@PathVariable("killid")Long killid,
                      @PathVariable("userPhone")Long userPhone,
                      @PathVariable("orderid")long orderid){
        if(killid==null||userPhone==null){
            return "error";
        }
        boolean res = false;
        try {
            res= killService.pay(orderid);
        }catch (Exception e){
            return "false";
        }
        return String.valueOf(res);
    }

}
