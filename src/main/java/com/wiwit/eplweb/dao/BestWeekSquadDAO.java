package com.wiwit.eplweb.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.BestWeekSquad;

@Service
public class BestWeekSquadDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public List<BestWeekSquad> getBestSquadByWeekId(int weekId) {
		Session session = this.sessionFactory.getCurrentSession();
		return session.createQuery(
				"from BestWeekSquad where week.id = " + weekId + " order by number asc").list();
	}
}