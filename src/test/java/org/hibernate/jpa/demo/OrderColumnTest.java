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

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderColumnTest extends TestCase {
	private EntityManagerFactory entityManagerFactory;
	private static final Logger log = LoggerFactory.getLogger( OrderColumnTest.class );

	@Override
	protected void setUp() throws Exception {
		entityManagerFactory = Persistence.createEntityManagerFactory( "jpa2-demo" );
	}

	@Override
	protected void tearDown() throws Exception {
		entityManagerFactory.close();
	}

	public void testOrderColumn() {
		EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();

		PrintQueue queue = new PrintQueue( "demo-queue" );

		List<PrintJob> jobs = new ArrayList<PrintJob>();
		PrintJob job1 = new PrintJob();
		job1.setQueue( queue );
		jobs.add( job1 );

		PrintJob job2 = new PrintJob();
		job2.setQueue( queue );
		jobs.add( job2 );

		queue.setJobs( jobs );

		em.persist( queue );
		em.getTransaction().commit();
		em.close();

		em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();
		List<PrintQueue> list = em.createQuery( "from PrintQueue", PrintQueue.class ).getResultList();
		assertTrue( list.size() == 1 );

		queue = list.get( 0 );
		assertTrue( queue.getJobs().size() == 2 );

		PrintJob job3 = new PrintJob();
		job3.setQueue( queue );
		queue.getJobs().add( 0, job3 );

		em.getTransaction().commit();
	}
}
