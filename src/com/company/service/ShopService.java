package com.company.service;

import com.company.DAO.DAO;
import com.company.DAO.DatabaseDAO;
import com.company.DAO.FileDAO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class ShopService implements Service {
  private DAO dao;

  public ShopService()
  {

  }

  public ShopService(String propertyFile) throws FileNotFoundException, Exception
  {
    File property = new File(propertyFile);
    if(!property.exists())
    {
      throw  new FileNotFoundException();
    }
    Scanner sc = new Scanner(new FileReader(property));
    if(sc.nextLine().split("=")[1].equals("database"))
    {
      String url = sc.nextLine().split("=")[1];
      String user = sc.nextLine().split("=")[1];
      String password = sc.nextLine().split("=")[1];
      dao = new DatabaseDAO(url, user, password);
    }
    else
    {
      String shop= sc.nextLine().split("=")[1];
      String product = sc.nextLine().split("=")[1];
      String productsInShops = sc.nextLine().split("=")[1];
      dao = new FileDAO(shop,product,productsInShops);
    }
  }

  @Override
  public void setDAO(DAO dao) {
    this.dao=dao;
  }

  @Override
  public boolean createShop(String name, String address) {
    try
    {
      List<String> list = new ArrayList<>();
      name="'"+name+"'";
      address="'"+address+"'";
      list.add(name); list.add(address);
      dao.insert("Shop", list);
    }
    catch(Exception e)
    {
      System.out.println(name+" was not created");
      System.out.println(e);
      return false;
    }
    return true;
  }

  @Override
  public boolean createProduct(String name) {
    try
    {
      var productSet = dao.getData("Product");
      for(List<String> list : productSet)
      {
        if(list.get(1).equals(name))
        {
          throw new Exception(name + " already exists");
        }
      }
      name="'"+name+"'";
      List<String> list = new ArrayList<>();
      list.add(name);
      dao.insert("Product", list);
    }
    catch (Exception e)
    {
      System.out.println(name + " was not inserted");
      System.out.println(e);
      return false;
    }
    return true;
  }

  @Override
  public Float buy(String product, String shop, int count) {
    Float result = (float)-1;
    try {
      var shopSet = dao.getData("Shop");
      String shopID="";
      String productID="";
      String relationID="";
      for(List<String> list : shopSet)
      {
        if(list.get(1).equals(shop))
        {
          shopID=list.get(0);
          break;
        }
      }
      if(shopID.equals(""))
      {
        throw new Exception("Shop " + shop + " not found");
      }

      var productSet = dao.getData("Product");
      for(List<String> list : productSet)
      {
        if(list.get(1).equals(product))
        {
          productID=list.get(0);
          break;
        }
      }
      if(productID.equals(""))
      {
        throw new Exception("Product " + product + " not found");
      }

      var relationSet=dao.getData("ProductsInShops");
      Integer productsInShop=0;
      Float price =(float)0;
      for(List<String> list : relationSet)
      {
        if(list.get(1).equals(shopID) && list.get(2).equals(productID))
        {
          relationID=list.get(0);
          productsInShop = Integer.parseInt(list.get(3).replaceAll(" ",""));
          price = Float.parseFloat(list.get(4).replaceAll(" ",""));
          break;
        }
      }
      if(relationID.equals(""))
      {
        throw new Exception("Product " + product + " not found in shop " + shop);
      }
      if((int)productsInShop>=count)
      {
        result=price*(float)count;
        productsInShop-=count;
        List<String> list = new ArrayList<>();
        list.add(shopID); list.add(productID);
        list.add(productsInShop.toString());
        list.add(price.toString());
        dao.update("ProductsInShops", relationID, list);
      }
      else
      {
        throw new Exception("Not enough of " + product + " in " +shop);
      }
    }
    catch (Exception e)
    {
      System.out.println(e);
      return (float)-1;
    }
    return result;
  }
  @Override
  public boolean bringBatchOfProducts(String shop, String product, String count, String price) {
    try
    {
      var shopSet = dao.getData("Shop");
      String shopID="";
      String productID="";
      String relationID="";
      for(List<String> list : shopSet)
      {
        if(list.get(1).equals(shop))
        {
          shopID=list.get(0);
          break;
        }
      }
      if(shopID.equals(""))
      {
        throw new Exception("Shop " + shop + " not found");
      }

      var productSet = dao.getData("Product");
      for(List<String> list : productSet)
      {
        if(list.get(1).equals(product))
        {
          productID=list.get(0);
          break;
        }
      }
      if(productID.equals(""))
      {
        throw new Exception("Product " + product + " not found");
      }

      var relationSet=dao.getData("ProductsInShops");
      Integer productsInShop=0;
      Float priceInTable =(float)0;
      for(List<String> list : relationSet) {
        if (list.get(1).equals(shopID) && list.get(2).equals(productID)) {
          relationID = list.get(0);
          productsInShop = Integer.parseInt(list.get(3).replaceAll(" ", ""));
          break;
        }
      }
      List<String> list = new ArrayList<>();
      list.add(shopID); list.add(productID);

      if(!relationID.equals(""))
      {
        list.add(((Integer)(productsInShop+Integer.parseInt(count))).toString());
        list.add(price);
        dao.update("ProductsInShops",relationID, list);
      }
      else
      {
        list.add(count); list.add(price);
        dao.insert("ProductsInShops", list);
      }
    }
    catch(Exception e)
    {
      System.out.println(e);
      e.printStackTrace();
      return false;
    }
    return true;
  }

  @Override
  public List<String> findForMoney(Float money, String shop) {
    List<String> result = new ArrayList<>();
    try {
      var shopSet = dao.getData("Shop");
      String shopID = "";
      for(List<String> list : shopSet)
      {
        if(shop.equals(list.get(1)))
        {
          shopID=list.get(0);
          break;
        }
      }
      if(shopID.equals(""))
      {
        throw new Exception(shop+" not found");
      }
      var relationSet=dao.getData("ProductsInShops");
      var productSet = dao.getData("Product");
      for(List<String> list : relationSet)
      {
        if(!list.get(1).equals(shopID))
        {
          continue;
        }
        Float price = Float.parseFloat(list.get(4).replaceAll(" ",""));
        if(price>money)
        {
          continue;
        }

        for(var productList:productSet)
        {
          if(productList.get(0).equals(list.get(2)))
          {

            result.add((Math.min((int)((money/price)), Integer.parseInt(list.get(3)))+ " of " + productList.get(1)));
            break;
          }
        }
      }
    }
    catch (Exception e)
    {
      System.out.println(e);
      return null;
    }
    return result;

  }

  @Override
  public String findBestShop(Map<String, Integer> batch) {
    String bestShop ="";
    try
    {
      String bestShopID ="";
      Map<String, Integer> batchByID = new HashMap<>();
      var productSet=dao.getData("Product");
      for(var entry : batch.entrySet())
      {
        String proudctID = "";
        Float minPrice=(float)-1;
        for(var list:productSet)
        {
          if(entry.getKey().equals(list.get(1)))
          {
            proudctID=list.get(0);
            break;
          }
        }
        if(proudctID.equals(""))
        {
          throw new Exception(entry.getKey() + " not found");
        }
        batchByID.put(proudctID, entry.getValue());
      }
      var relationSet=dao.getData("ProductsInShops");
      String shopID = "";
      var shopSet = dao.getData("Shop");
      for(var entry : batchByID.entrySet())
      {
        boolean found = false;
        for(var relationList : relationSet)
        {
          if(entry.getKey().equals(relationList.get(1)))
          {
            shopID=relationList.get(1);
            found = true;
            break;
          }
        }
        if(!found)
        {
          throw new Exception("Batch not found in any shop");
        }
      }
      for(var shopList : shopSet)
      {
        if(shopID.equals(shopList.get(0)))
        {
          bestShop=shopList.get(1);
        }
      }
    }
    catch(Exception e)
    {
      System.out.println(e);
      return "";
    }
    return bestShop;
  }

  @Override
  public String findBestShop(String product) {
    String bestShop = "";
    try
    {
      String productID = "";
      var productSet=dao.getData("Product");
      for(var list : productSet)
      {
        if(product.equals(list.get(1)))
        {
          productID=list.get(0);
          break;
        }
      }
      if(productID.equals(""))
      {
        throw new Exception(product+" not found");
      }
      var relationSet=dao.getData("ProductsInShops");
      Float minPrice=(float)-1;
      String shopID="";
      for(var list : relationSet)
      {
        if(productID.equals(list.get(2)))
        {
          if(minPrice==-1)
          {
            minPrice=Float.parseFloat(list.get(4).replaceAll(" ", ""));
            shopID=list.get(1);
          }
          else{
            Float price=Float.parseFloat(list.get(4).replaceAll(" ", ""));
            if(minPrice<price)
            {
              minPrice=price;
              shopID=list.get(1);
            }
          }
        }
      }
      if(shopID.equals("")){
        throw new Exception(product + " is not in any shop");
      }
      var shopSet=dao.getData("Shop");

      for(var list : shopSet)
      {
        if(shopID.equals(list.get(0)))
        {
          bestShop=list.get(1);
          break;
        }
      }
    }
    catch (Exception e)
    {
      System.out.println(e);
      return "";
    }
    return bestShop;
  }
}
