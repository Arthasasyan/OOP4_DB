package com.company.service;

import com.company.DAO.DAO;

import java.util.List;
import java.util.Map;

public class ShopService implements Service {
  private DAO dao;

  @Override
  public void setDAO(DAO dao) {
    this.dao=dao;
  }

  @Override
  public boolean createProduct(String name) {
    return false;
  }

  @Override
  public Float buy(String product, String shop, int count) {
    return null;
  }

  @Override
  public List<String> findForMoney(Float money) {
    return null;
  }

  @Override
  public String findBestShop(Map<String, Integer> batch) {
    return null;
  }

  @Override
  public String findBestShop(String product) {
    return null;
  }

  @Override
  public void bringBatchOfProducts(String shopName, String productName, String count, String price) {

  }

  @Override
  public void createShop(String name, String address) {

  }
}
