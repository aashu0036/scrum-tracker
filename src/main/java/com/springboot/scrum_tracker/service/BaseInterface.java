package com.springboot.scrum_tracker.service;

public interface BaseInterface<T> {

	T findResourceById(Integer id);
}
