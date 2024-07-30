package com.example.utils.ohospickers.listeners;


import com.example.utils.ohospickers.entity.City;
import com.example.utils.ohospickers.entity.County;
import com.example.utils.ohospickers.entity.Province;

public interface OnLinkageListener {
  /**
   * 选择地址
   *
   * @param province the province
   * @param city     the city
   * @param county   the county ，if {@code hideCounty} is true，this is null
   */
  void onAddressPicked(Province province, City city, County county);
}
