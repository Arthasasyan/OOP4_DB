package com.company.service;

import com.company.DAO.DAO;

import java.util.List;
import java.util.Map;

public interface Service {
  void setDAO(DAO dao);
  boolean createShop(String name, String address);
  boolean createProduct(String name);
  boolean bringBatchOfProducts(String shopName, String productName, String count, String price);
  String findBestShop(String product);
  List<String> findForMoney(Float money);
  Float buy(String product, String shop, int count);
  String findBestShop(Map<String, Integer> batch);
}
