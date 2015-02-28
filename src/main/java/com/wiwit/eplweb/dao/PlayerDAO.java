package com.wiwit.eplweb.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.Player;

@Service
public class PlayerDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	public List<Player> getSquadsByTeamId(int teamId) {
		Session session = this.sessionFactory.getCurrentSession();
		return session.createQuery("from Player where team.id=" + teamId + " order by playerNumber asc").list();
	}
}
