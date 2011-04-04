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
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

import org.hibernate.annotations.IndexColumn;

@Entity
public class PrintQueue {
	@Id
	private String name;

	@OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderColumn(name = "PRINT_ORDER")
	private List<PrintJob> jobs;


	private PrintQueue() {
		// this form used by Hibernate
	}

	public PrintQueue(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public List<PrintJob> getJobs() {
		return jobs;
	}

	public void setJobs(List<PrintJob> jobs) {
		this.jobs = jobs;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append( "PrintQueue" );
		sb.append( "{name='" ).append( name ).append( '\'' );
		sb.append( ", jobs=" ).append( jobs );
		sb.append( '}' );
		return sb.toString();
	}
}