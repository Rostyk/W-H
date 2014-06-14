package com.ros.workandhome.db.dao;

public interface BaseDAO<T> {
	    T getByID(Long ID);
	    T save(T type);
	    T update(T type);
	    void delete(T type);
	    Iterable <T> getAll();
}
