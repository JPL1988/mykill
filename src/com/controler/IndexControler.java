package com.controler;

import com.entity.myKill;
import com.service.Impl.IndexServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author false
 * @date 20/4/9 20:24
 */
@Controller
public class IndexControler {
    Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private IndexServiceImpl indeximp;

    @RequestMapping("/killindex/{data_batch}")
    public String index(@PathVariable("data_batch") String data_batch, Model model){
        List<myKill> list =indeximp.findAll(data_batch);
        model.addAttribute("list",list);
        return "killindex";
    }

}
