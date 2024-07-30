package com.liyaan.selectpicker;



import com.alibaba.fastjson.JSON;
import com.example.utils.ohospickers.entity.City;
import com.example.utils.ohospickers.entity.County;
import com.example.utils.ohospickers.entity.Province;
import com.example.utils.ohospickers.listeners.OnLinkageListener;
import com.example.utils.ohospickers.picker.AddressPicker;
import com.liyaan.selectpicker.Utils.AsyncTask;
import ohos.aafwk.ability.Ability;
import ohos.agp.window.dialog.CommonDialog;

import java.util.ArrayList;

import static com.liyaan.selectpicker.Utils.jsonUtils.getOriginalFundData;


/**
 * 获取地址数据并显示地址选择器
 */
public class AddressPickTask extends AsyncTask<String, Void, ArrayList<Province>> {
  private Ability activity;
  private CommonDialog dialog;
  private Callback callback;
  private String selectedProvince = "", selectedCity = "", selectedCounty = "";
  private boolean hideProvince = false;
  private boolean hideCounty = false;

  public AddressPickTask(Ability activity) {
    this.activity = activity;
  }

  public void setHideProvince(boolean hideProvince) {
    this.hideProvince = hideProvince;
  }

  public void setHideCounty(boolean hideCounty) {
    this.hideCounty = hideCounty;
  }

  public void setCallback(Callback callback) {
    this.callback = callback;
  }

  @Override
  protected void onPreExecute() {
    dialog = new CommonDialog(activity);
    dialog.setContentText("正在初始化数据...");
    dialog.show();
  }

  @Override
  protected ArrayList<Province> doInBackground(String... params) {
    if (params != null) {
      switch (params.length) {
        case 1:
          selectedProvince = params[0];
          break;
        case 2:
          selectedProvince = params[0];
          selectedCity = params[1];
          break;
        case 3:
          selectedProvince = params[0];
          selectedCity = params[1];
          selectedCounty = params[2];
          break;
        default:
          break;
      }
    }
    ArrayList<Province> data = new ArrayList<>();
    try {
      String json = getOriginalFundData(activity, "city.json");
      data.addAll(JSON.parseArray(json, Province.class));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return data;
  }

  @Override
  protected void onPostExecute(ArrayList<Province> result) {
    dialog.destroy();
    if (result.size() > 0) {
      AddressPicker picker = new AddressPicker(activity, result);
      picker.setCanLoop(true);
      picker.setHideProvince(hideProvince);
      picker.setHideCounty(hideCounty);
      if (hideCounty) {
        picker.setColumnWeight(1 / 3.0f, 2 / 3.0f);//将屏幕分为3份，省级和地级的比例为1:2
      } else {
        picker.setColumnWeight(2 / 8.0f, 3 / 8.0f, 3 / 8.0f);//省级、地级和县级的比例为2:3:3
      }
      picker.setSelectedItem(selectedProvince, selectedCity, selectedCounty);
      picker.setOnLinkageListener(callback);
      picker.show();
    } else {
      callback.onAddressInitFailed();
    }
  }

  interface Callback extends OnLinkageListener {

    void onAddressInitFailed();

    void onAddressPicked(Province province, City city, County county);
  }

}
