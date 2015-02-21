package com.wiwit.eplweb.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.Team;

@Service
public class TeamDAO {
	private static final Logger logger = LoggerFactory.getLogger(TeamDAO.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	public List<Team> findAll() {
		Session session = this.sessionFactory.getCurrentSession();
		List<Team> result = session.createQuery("from Team order by Name asc").list();

		logger.info("Team loaded successfully, teams size=" + result.size());
		return result;
	}

	@Transactional
	public Team findByIdAndName(int id, String simpleName) {
		Session session = this.sessionFactory.getCurrentSession();
		List<Team> list = session
				.createQuery(
						"from Team where id=" + id + " and simpleName='"
								+ simpleName + "'").setMaxResults(1).list();
		if (list == null || list.isEmpty()) {
			logger.info("Can't find Team with team.id=" + id
					+ ", and team.simpleName=" + simpleName);
			return null;
		}
		Team result = list.get(0);

		logger.info("Team loaded successfully, team.id =" + result.getId());
		return result;
	}
}
