package com.examples.examLibrary.repository.mysql;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.examples.examLibrary.hibernate.util.HibernateUtil;
import com.examples.examLibrary.model.Library;
import com.examples.examLibrary.repository.LibraryRepository;

public class LibraryMySQLRepository implements LibraryRepository {
	
private static final Logger LOGGER = LogManager.getLogger(LibraryMySQLRepository.class);
	
	private Session session;
	private Transaction transaction;
	
	public LibraryMySQLRepository(Properties settings) {
		HibernateUtil.setProperties(settings);
	}
	
	protected Session getSession() {
		return this.session;
	}

	@Override
	public List<Library> getAllLibraries() {
		List<Library> libraries = new ArrayList<>();
		session = null;
		transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
	        transaction = session.beginTransaction();
	        libraries = session.createQuery("FROM Library", Library.class).list();
		} catch(Exception e) {
			if(transaction != null && transaction.isActive())
				transaction.rollback();
			LOGGER.error(e.getMessage(), e);
		} finally {
			if(session != null && session.isConnected())
				session.close();
		}
		return libraries;
	}

	@Override
	public Library findLibraryById(String idLibrary) {
		Library library = null;
		session = null;
		transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
	        transaction = session.beginTransaction();
	        library = session.get(Library.class, idLibrary);
	        transaction.commit();
		} catch(Exception e) {
			if(transaction != null && transaction.isActive())
				transaction.rollback();
			LOGGER.error(e.getMessage(), e);
		} finally {
			if(session != null && session.isConnected())
				session.close();
		}
		return library;
	}	

	@Override
	public void saveLibrary(Library library) {
		session = null;
		transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.save(library);
			transaction.commit();
		} catch(Exception e) {
			if(transaction != null && transaction.isActive())
				transaction.rollback();
			LOGGER.error(e.getMessage(), e);
		} finally {
			if(session != null && session.isConnected())
				session.close();
		}  
	}

	@Override
	public void deleteLibrary(String idLibrary) {
		session = null;
		transaction = null;
		Library libraryFound = findLibraryById(idLibrary);
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.delete(libraryFound);
			transaction.commit();
		} catch(Exception e) {
			if(transaction != null && transaction.isActive())
				transaction.rollback();
			LOGGER.error(e.getMessage(), e);
		} finally {
			if(session != null && session.isConnected())
				session.close();
		}
	}
}
