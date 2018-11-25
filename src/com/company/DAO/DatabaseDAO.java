package com.company.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DatabaseDAO implements DAO{

  private Connection connection;
  public DatabaseDAO(String connectionString, String user, String password) throws SQLException
  {
    try {
      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
      connection = DriverManager.getConnection(connectionString, user, password);
    }
    catch (ClassNotFoundException e)
    {
      System.out.println(e);
    }
    catch (SQLException e)
    {
      throw e;
    }

  }

  @Override
  public void insert(String table, List<String> values){
    try {
      Statement statement = connection.createStatement();
      String val = "";
      for (String s : values) {
        val += s + ",";
      }
      val = val.substring(0, val.length() - 1); //removing last coma
      statement.executeQuery("INSERT INTO " + table + " VALUES(" + val + ")");
    }
    catch(SQLException e) {
      System.out.println(e);
    }
    }

  @Override
  public Set<List<String>> getData(String table) {
    Set<List<String>> result= new HashSet<>();
    String query = "Select * from "+table;
    Statement statement;
    ResultSet rs = null;
    try
    {
      statement=connection.createStatement();
      rs=statement.executeQuery(query);
      while (rs.next()) {
        ArrayList<String> list = new ArrayList<>();
        if(table.equals("Product") || table.equals("Shop"))
        {
          list.add(rs.getString(1)); list.add(rs.getString(2));
        }
        else if(table.equals("ProductsInShops"))
        {
          for(int i=2;i<=5;i++)
          {
            list.add(rs.getString(i));
          }
        }
        result.add(list);
      }
    }
    catch(Exception e)
    {
      System.out.println(e);
    }

    return result;
  }
}
