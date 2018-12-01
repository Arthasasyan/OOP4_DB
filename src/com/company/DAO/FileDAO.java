package com.company.DAO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class FileDAO implements DAO {

  private File shop;
  private File product;
  private File productsInShops;

  public FileDAO(String shop, String product, String productsInShops) throws FileNotFoundException
  {
    this.shop=new File(shop);
    this.product=new File(product);
    this.productsInShops=new File(productsInShops);
    if(!(this.shop.exists()||this.productsInShops.exists()||this.product.exists()))
    {
      throw new FileNotFoundException();
    }
  }

  @Override
  public Set<List<String>> getData(String table) throws Exception {
    Set<List<String>> result=new HashSet<>();
    FileReader reader;
    if(table.equals("Shop"))
    {
      reader=new FileReader(shop);
    }
    else if(table.equals("Product"))
    {
      reader = new FileReader(product);
    }
    else if(table.equals("ProductsInShops"))
    {
      reader = new FileReader(productsInShops);
    }
    else
    {
      throw new Exception("Wrong table name");
    }
    Scanner sc = new Scanner(reader);
    while(sc.hasNextLine())
    {
      result.add(Arrays.asList(sc.nextLine().split(",")));
    }
    return result;
  }

  @Override
  public void insert(String table, List<String> values) throws Exception{
    var tableSet = this.getData(table);
    String lastIDString="";

    for(List<String> list : tableSet)
    {
      lastIDString=list.get(0);
    }
    Integer lastID=0;
    if(!lastIDString.equals(""))
    {
      lastID=Integer.parseInt(lastIDString);
    }
    String toAdd=lastID.toString();
    for(String s : values)
    {
      toAdd+=","+s;
    }
    FileWriter writer;
    if(table.equals("Shop"))
    {
      writer=new FileWriter(shop);
    }
    else if(table.equals("Product"))
    {
      writer = new FileWriter(product);
    }
    else
    {
      writer = new FileWriter(productsInShops);
    }
    writer.append(toAdd);
  }

  @Override
  public void update(String table, String id, List<String> newValues) throws Exception {
    var tableSet=this.getData(table);

    FileWriter writer;
    if(table.equals("Shop"))
    {
      writer=new FileWriter(shop);
    }
    else if(table.equals("Product"))
    {
      writer = new FileWriter(product);
    }
    else
    {
      writer = new FileWriter(productsInShops);
    }

    String file = "";
    for(List<String> list : tableSet)
    {
      if(!list.get(0).equals(id))
      {
        for(String s : list)
        {
          file+=s+",";
        }
        file=file.substring(0,file.length()-1);
      }
      else
      {
        file+=id;
        for(String s : newValues)
        {
          file+=","+s;
        }
      }
    }
    writer.write(file);
  }
}
