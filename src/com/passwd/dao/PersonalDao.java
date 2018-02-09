package com.passwd.dao;

import com.passwd.model.SearchResult;

public interface PersonalDao {

	public SearchResult basic(String query, boolean analyzed, int start, int size);

	public SearchResult metatype(String query, boolean analyzed, String objectType, String propertyType, int start,
			int size);

}
