package com.example.web.front;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import com.example.entity.UpDown;
import com.example.entity.User;
import com.example.service.UpDownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.example.dto.DMLExecution;
import com.example.dto.Result;

/**
 * 点赞或者点踩
 */
@RestController
@RequestMapping(value = "/topic")
public class UpDownController extends BaseController{

	@Autowired
	private UpDownService upDownService;
	
	@RequestMapping(value = "/vote",method = RequestMethod.GET)
	private Result<DMLExecution> up(Integer tid,boolean vote,HttpServletRequest request){
		User user = getUser(request);
		if(user == null) return new Result<>(false,"未登录");
		UpDown upDown = new UpDown();
		upDown.setUid(user.getUserId());
		upDown.setTid(tid);
		upDown.setUpDown(vote);
		upDown.setCreateDate(new Date());
		upDown.setDelete(false);
		DMLExecution save = upDownService.save(upDown);
		return new Result<DMLExecution>(true,save);
	}
	
	@RequestMapping(value = "/vote/count",method = RequestMethod.GET)
	private Result<Integer> count(Integer tid,boolean vote){
		int countUpOrDown = upDownService.countUpOrDown(tid, vote?1:0);
		Integer integer = new Integer(countUpOrDown);
		return new Result<Integer>(true, integer);
	}
}
