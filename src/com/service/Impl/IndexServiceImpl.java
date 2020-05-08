package com.service.Impl;

import com.entity.myKill;
import com.mapper.Interface.mykillMapper;
import com.service.IndexService;
import com.util.myTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


/**
 * @author false
 * @date 20/4/9 18:47
 */
@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private mykillMapper mykillMapper;

    public List<myKill> findAll(String str){
        Date starttime = myTimeUtil.parse(str);
        Date endtime = myTimeUtil.endTime(str);
        List<myKill> res = mykillMapper.findAll(starttime,endtime);
        return res;
    }
}
