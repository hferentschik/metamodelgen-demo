/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2010, Red Hat Inc. or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.hibernate.jpa.demo;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CriteriaQueryTest extends TestCase {
	private EntityManagerFactory entityManagerFactory;
	private static final Logger log = LoggerFactory.getLogger( CriteriaQueryTest.class );

	@Override
	protected void setUp() throws Exception {
		entityManagerFactory = Persistence.createEntityManagerFactory( "jpa2-demo" );
		createTestData();
	}

	@Override
	protected void tearDown() throws Exception {
		deleteTestData();
		entityManagerFactory.close();
	}

	public void testBasicUsage() {
		// let's pull events from the database and list them
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		List<Event> result = entityManager.createQuery( "from Event where title like '%follow%' ", Event.class )
				.getResultList();
		assertTrue( result.size() == 1 );
		for ( Event event : result ) {
			log.debug( "Event (" + event.getDate() + ") : " + event.getTitle() );
		}

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	public void testTypeSafeCriteriaApi() {
		// let's do a type safe criteria query
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		CriteriaBuilder queryBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Event> query = queryBuilder.createQuery( Event.class );
		Root<Event> event = query.from( Event.class );
		query.where( queryBuilder.like( event.get( Event_.title ), "%follow%" ) );


		List<Event> events = entityManager.createQuery( query ).getResultList();
		assertTrue( events.size() == 1 );
		for ( Event resultEvent : events ) {
			log.debug( "Event (" + resultEvent.getDate() + ") : " + resultEvent.getTitle() );
		}

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	private void createTestData() {
		// create a couple of events...
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist( new Event( "Our very first event!", new Date() ) );
		entityManager.persist( new Event( "A follow up event", new Date() ) );
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	private void deleteTestData() {
		// delete all events...
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		List<Event> result = entityManager.createQuery( "from Event", Event.class ).getResultList();
		for ( Event event : result ) {
			entityManager.remove( event );
		}
		entityManager.getTransaction().commit();
		entityManager.close();
	}
}
