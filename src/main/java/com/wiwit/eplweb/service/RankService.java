package com.wiwit.eplweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wiwit.eplweb.dao.RankDAO;
import com.wiwit.eplweb.model.Rank;

@Component
public class RankService {

	@Autowired
	private RankDAO rankDAO;

	public List<Rank> getFiveHighestLastRank() {
		return this.rankDAO.getFiveHighestLastRank();
	}
	
	public List<Rank> getLatestRank() {
		return this.rankDAO.getLatestRank();
	}	
	
	public List<Rank> getRankByWeekNumber(int weekNumber) {
		return this.rankDAO.getRankByWeekNumber(weekNumber);
	}
	
	public List<Rank> getRankByWeekNumber(String weekNumber) {
		return getRankByWeekNumber(Integer.valueOf(weekNumber));
	}	
}
