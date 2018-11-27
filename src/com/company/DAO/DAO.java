package com.company.DAO;

import java.sql.ResultSet;
import java.util.List;
import java.util.Set;

public interface DAO {
  Set<List<String>> getData(String table) throws Exception;
  void insert(String table, List<String> values) throws Exception;
  void update(String table, String id, List<String> newValues) throws Exception;
}
