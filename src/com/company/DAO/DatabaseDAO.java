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
  public void insert(String table, List<String> values) throws Exception{
    if(!(table.equals("Shop")||table.equals("Product")||table.equals("ProductsInShops")))
    {
      throw new Exception("Wrong table name");
    }
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
  public Set<List<String>> getData(String table) throws Exception {
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
        if(table.equals("Product"))
        {
          list.add(rs.getString(1)); list.add(rs.getString(2));
        }
        else if(table.equals("Shop"))
        {
          for(int i=1;i<=3;i++)
          {
            list.add(rs.getString(i));
          }
        }
        else if(table.equals("ProductsInShops"))
        {
          for(int i=1;i<=5;i++)
          {
            list.add(rs.getString(i));
          }
        }
        else
        {
          throw new Exception("Wrong table name");
        }
        result.add(list);
      }
    }
    catch(Exception e)
    {
      System.out.println(e);
      throw e;
    }

    return result;
  }

  @Override
  public void update(String table, String id, List<String> newValues) throws Exception {
    String query="";
    if(!(table.equals("Shop")||table.equals("Product")||table.equals("ProductsInShops")))
    {
      throw new Exception("Wrong table name");
    }
    try {
      ResultSetMetaData rsmd = connection.createStatement().executeQuery("SELECT * FROM "+table).getMetaData();

      query = "UPDATE " + table + " SET ";
      for(int i=2;i<=rsmd.getColumnCount();i++)
      {
        query+= rsmd.getColumnName(i)+" = "+newValues.get(i-2)+", ";
      }
      query=query.substring(0,query.length()-2);
      query+=" WHERE "+rsmd.getColumnName(1)+" = "+id;
      connection.prepareStatement(query).execute();
    }
    catch (Exception e)
    {
      System.out.println(query);
      throw e;
    }

  }
}
