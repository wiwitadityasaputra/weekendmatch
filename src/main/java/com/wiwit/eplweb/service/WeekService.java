package com.wiwit.eplweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wiwit.eplweb.dao.WeekDAO;
import com.wiwit.eplweb.model.Week;

@Component
@Service
public class WeekService {

	@Autowired
	private WeekDAO weekDAO;
	
	@Transactional
	private List<Week> getLastFiveWeek(){
		return weekDAO.getLastFiveWeek();
	}
}