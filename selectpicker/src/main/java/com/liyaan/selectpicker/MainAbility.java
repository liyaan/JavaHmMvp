package com.liyaan.selectpicker;


import com.alibaba.fastjson.JSON;
import com.example.utils.ohospickers.common.LineConfig;
import com.example.utils.ohospickers.entity.City;
import com.example.utils.ohospickers.entity.County;
import com.example.utils.ohospickers.entity.Province;
import com.example.utils.ohospickers.listeners.OnItemPickListener;
import com.example.utils.ohospickers.listeners.OnLinkageListener;
import com.example.utils.ohospickers.listeners.OnMoreItemPickListener;
import com.example.utils.ohospickers.listeners.OnSingleWheelListener;
import com.example.utils.ohospickers.picker.*;
import com.example.utils.ohospickers.util.DateDeal;
import com.example.utils.ohospickers.util.DateUtils;
import com.example.utils.ohospickers.util.TimeUtils;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.utils.Color;
import ohos.agp.window.dialog.ToastDialog;

import java.util.*;

import static com.example.utils.ohospickers.common.Constants.DateMode.MONTH_DAY;
import static com.example.utils.ohospickers.common.Constants.DateMode.YEAR_MONTH;
import static com.example.utils.ohospickers.common.Constants.TimeMode.HOUR_24;
import static com.liyaan.selectpicker.Utils.jsonUtils.getOriginalFundData;
import static ohos.agp.utils.LayoutAlignment.*;

public class MainAbility extends BaseBaseAbility implements Component.ClickedListener {
  @Override
  protected Component getContentView() {
    return inflateView(ResourceTable.Layout_ability_main);
  }

  @Override
  protected void setContentViewAfter(Component contentView) {
    Button nestView = (Button) findComponentById(ResourceTable.Id_nest_view);
    Button animationStyle = (Button) findComponentById(ResourceTable.Id_animation_style);
    Button animator = (Button) findComponentById(ResourceTable.Id_animator);
    Button dateRangePicker = (Button) findComponentById(ResourceTable.Id_date_range_picker);
    Button yearMonthDayPicker = (Button) findComponentById(ResourceTable.Id_year_month_day_picker);
    Button yearMonthDayTimePicker = (Button) findComponentById(ResourceTable.Id_year_month_day_time_picker);
    Button yearMonthPicker = (Button) findComponentById(ResourceTable.Id_year_month_picker);
    Button monthDayPicker = (Button) findComponentById(ResourceTable.Id_month_day_picker);
    Button timePicker = (Button) findComponentById(ResourceTable.Id_time_picker);
    Button optionPicker = (Button) findComponentById(ResourceTable.Id_option_picker);
    Button linkagePicker = (Button) findComponentById(ResourceTable.Id_linkage_picker);
    Button constellationPicker = (Button) findComponentById(ResourceTable.Id_constellation_picker);
    Button numberPicker = (Button) findComponentById(ResourceTable.Id_number_picker);
    Button addressPicker = (Button) findComponentById(ResourceTable.Id_address_picker);
    Button address2Picker = (Button) findComponentById(ResourceTable.Id_address2_picker);
    Button address3Picker = (Button) findComponentById(ResourceTable.Id_address3_picker);
    address3Picker.setClickedListener(this);
    address2Picker.setClickedListener(this);
    addressPicker.setClickedListener(this);
    numberPicker.setClickedListener(this);
    constellationPicker.setClickedListener(this);
    linkagePicker.setClickedListener(this);
    optionPicker.setClickedListener(this);
    timePicker.setClickedListener(this);
    monthDayPicker.setClickedListener(this);
    yearMonthPicker.setClickedListener(this);
    yearMonthDayTimePicker.setClickedListener(this);
    yearMonthDayPicker.setClickedListener(this);
    dateRangePicker.setClickedListener(this);
    animator.setClickedListener(this);
    animationStyle.setClickedListener(this);
    nestView.setClickedListener(this);
  }

  @Override
  public void onStart(Intent intent) {
    super.onStart(intent);
  }

  @Override
  public void onBackPressed() {
    AppManager.getInstance().exitApp();
  }

  public void onNestView() {
    Intent intent = new Intent();
    Operation operation = new Intent.OperationBuilder()
        .withBundleName("cn.addapp.ohospickers")
        .withAbilityName(NextAbility.class)
        .build();
    intent.setOperation(operation);
    startAbility(intent);
  }

  public void onAnimationStyle() {
    NumberPicker picker = new NumberPicker(getAbility());
    picker.setAnimationStyle(ResourceTable.Animation_popup, ResourceTable.Animation_popup_background);
    picker.setCanLoop(false);
    picker.setOffset(2);//偏移量
    picker.setRange(10.5, 20, 1.5);//数字范围
    picker.setSelectedItem(18.0);
    picker.setLabel("℃");
    picker.setWeightEnable(true);
    picker.setOnNumberPickListener(new NumberPicker.OnNumberPickListener() {
      @Override
      public void onNumberPicked(int index, Number item) {
        new ToastDialog(MainAbility.this).setText("index=" + index + ", item=" + item.doubleValue()).show();
      }
    });
    picker.show();
  }

  public void onAnimator() {
    CustomPicker picker = new CustomPicker(getAbility());
    picker.setOffset(1);//显示的条目的偏移量，条数为（offset*2+1）
    picker.setGravity(CENTER);//居中
    picker.setOnItemPickListener(new OnItemPickListener<String>() {
      @Override
      public void onItemPicked(int position, String option) {
        new ToastDialog(MainAbility.this).setText("index=" + position + ", item=" + option).show();
      }
    });
    picker.show();
  }

  public void onDateRangePicker() {
    final DateRangePicker picker = new DateRangePicker(getAbility());
    picker.setTopLineVisible(false);
    LineConfig lineConfig = new LineConfig();
    lineConfig.setColor(Color.GRAY.getValue());
    lineConfig.setDividerType(LineConfig.DividerType.FILL);
    picker.setLineConfig(lineConfig);
    picker.setGravity(CENTER);
//        picker.setDateRangeStart(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
//            Calendar.getInstance().get(Calendar.DAY_OF_MONTH));//09:00
//        picker.setBackgroundRes(com.framework.base.R.drawable.bg_white_radius12);
//        picker.setWheelModeEnable(true);
//        picker.setWeightEnable(true);
    picker.setOuterLabelEnable(false);
    picker.setSelectedTextColor(Color.YELLOW.getValue());//前四位值是透明度
    picker.setUnSelectedTextColor(Color.GRAY.getValue());
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1;
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    //调整为当前时间
    picker.setSelectedItem(year, month, day);
    picker.setOnDateTimePickListener(new DateRangePicker.OnYearMonthDayRangePickListener() {
      @Override
      public void onDateTimePicked(String year, String monthStart, String dayStart, String monthEnd, String dayEnd) {
        String startDate = year + "-" + monthStart + "-" + dayStart + " 00:00:00";
        String endDate = year + "-" + monthEnd + "-" + dayEnd + " 23:59:59";
        long sTime = TimeUtils.string2Date(startDate, DateDeal.YYYY_MM_dd_HH_MM_SS).getTime();
        long eTime = TimeUtils.string2Date(endDate, DateDeal.YYYY_MM_dd_HH_MM_SS).getTime();
        if (eTime < sTime) {
          new ToastDialog(MainAbility.this).setText("结束日期不能小于开始日期").show();
        } else {
          new ToastDialog(MainAbility.this).setText(year + monthStart + dayStart + monthEnd + dayEnd).show();
        }
        picker.dismiss();
      }
    });
    picker.show();
  }

  /*
   * 年月日选择
   * */
  public void onYearMonthDayPicker() {
    final DatePicker picker = new DatePicker(getAbility());
    picker.setTopPadding(15);
    picker.setRangeStart(2016, 8, 29);
    picker.setRangeEnd(2111, 1, 11);
    picker.setSelectedItem(2050, 10, 14);
    picker.setWeightEnable(true);
    picker.setLineColor(Color.BLACK.getValue());
    picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
      @Override
      public void onDatePicked(String year, String month, String day) {
        new ToastDialog(MainAbility.this).setText(year + "-" + month + "-" + day).show();
      }
    });
    picker.setOnWheelListener(new DatePicker.OnWheelListener() {
      @Override
      public void onYearWheeled(int index, String year) {
        picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
      }

      @Override
      public void onMonthWheeled(int index, String month) {
        picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
      }

      @Override
      public void onDayWheeled(int index, String day) {
        picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
      }
    });
    picker.show();
  }

  /*
   * 年月日时间选择
   * */
  public void onYearMonthDayTimePicker() {
    DateTimePicker picker = new DateTimePicker(getAbility(), HOUR_24);
    picker.setActionButtonTop(false);
    picker.setDateRangeStart(2017, 1, 1);
    picker.setDateRangeEnd(2025, 11, 11);
    picker.setSelectedItem(2018, 6, 16, 0, 0);
    picker.setTimeRangeStart(9, 0);
    picker.setTimeRangeEnd(20, 30);
    picker.setCanLinkage(false);
    picker.setTitleText("请选择");
    picker.setStepMinute(5);
    picker.setWeightEnable(true);
    picker.setCanceledOnTouchOutside(true);
    LineConfig config = new LineConfig();
    config.setColor(Color.BLUE.getValue());//线颜色
    config.setAlpha(120);//线透明度
    config.setVisible(true);//线不显示 默认显示
    picker.setLineConfig(config);
    picker.setOuterLabelEnable(true);
//        picker.setLabel(null,null,null,null,null);
    picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
      @Override
      public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
        new ToastDialog(MainAbility.this).setText(year + "-" + month + "-" + day + " " + hour + ":" + minute).show();
      }
    });
    picker.show();
  }


  public void onYearMonthPicker() {
    DatePicker picker = new DatePicker(getAbility(), YEAR_MONTH);
    picker.setGravity(TOP | HORIZONTAL_CENTER);
    picker.setWidth((int) (picker.getScreenWidthPixels() * 0.6));
    picker.setRangeStart(2016, 10, 14);
    picker.setRangeEnd(2020, 11, 11);
    picker.setSelectedItem(2017, 9);
    picker.setCanLinkage(true);
    picker.setWeightEnable(true);
    picker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
      @Override
      public void onDatePicked(String year, String month) {
        new ToastDialog(MainAbility.this).setText(year + "-" + month).show();
      }
    });
    picker.show();
  }

  public void onMonthDayPicker() {
    DatePicker picker = new DatePicker(getAbility(), MONTH_DAY);
    picker.setGravity(CENTER);//弹框居中
    picker.setCanLoop(false);
    picker.setWeightEnable(true);
    picker.setCanLinkage(true);
    LineConfig lineConfig = new LineConfig();
    lineConfig.setColor(Color.GREEN.getValue());
    picker.setLineConfig(lineConfig);
//        picker.setLineColor(Color.BLACK);
    picker.setRangeStart(5, 1);
    picker.setRangeEnd(12, 31);
    picker.setSelectedItem(10, 14);
    picker.setOnDatePickListener(new DatePicker.OnMonthDayPickListener() {
      @Override
      public void onDatePicked(String month, String day) {
        new ToastDialog(MainAbility.this).setText(month + "-" + day).show();
      }
    });
    picker.show();
  }

  public void onTimePicker() {
    TimePicker picker = new TimePicker(getAbility(), HOUR_24);
    picker.setRangeStart(9, 0);//09:00
    picker.setRangeEnd(18, 0);//18:30
    picker.setTopLineVisible(false);
    picker.setLineVisible(false);
    picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
      @Override
      public void onTimePicked(String hour, String minute) {
        new ToastDialog(MainAbility.this).setText(hour + ":" + minute).show();
      }
    });
    picker.show();
  }

  public void onOptionPicker() {
    ArrayList<String> list = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      String s = "";
      if (i < 10) {
        s = "0" + i;
      } else {
        s = i + "";
      }
      list.add(s);
    }
//        String[] ss = (String[]) list.toArray();
    SinglePicker<String> picker = new SinglePicker<>(getAbility(), list);
    picker.setCanLoop(false);//不禁用循环
    picker.setLineVisible(true);
    picker.setTextSize(18);
    picker.setSelectedIndex(2);
    //启用权重 setWeightWidth 才起作用
    picker.setLabel("分");
    picker.setItemWidth(100);
//        picker.setWeightEnable(true);
//        picker.setWeightWidth(1);
    picker.setOuterLabelEnable(true);
    picker.setSelectedTextColor(Color.GREEN.getValue());//前四位值是透明度
    picker.setUnSelectedTextColor(Color.BLACK.getValue());
    picker.setOnSingleWheelListener(new OnSingleWheelListener() {
      @Override
      public void onWheeled(int index, String item) {
        new ToastDialog(MainAbility.this).setText("index=" + index + ", item=" + item).show();
      }
    });
    picker.setOnItemPickListener(new OnItemPickListener<String>() {
      @Override
      public void onItemPicked(int index, String item) {
        new ToastDialog(MainAbility.this).setText("index=" + index + ", item=" + item).show();
      }
    });
    picker.show();
  }

  public void onLinkagePicker() {
    LinkagePicker.DataProvider provider = new LinkagePicker.DataProvider() {

      @Override
      public boolean isOnlyTwo() {
        return true;
      }

      @Override
      public List<String> provideFirstData() {
        ArrayList<String> firstList = new ArrayList<>();
        firstList.add("12");
        firstList.add("24");
        return firstList;
      }

      @Override
      public List<String> provideSecondData(int firstIndex) {
        ArrayList<String> secondList = new ArrayList<>();
        for (int i = 1; i <= (firstIndex == 0 ? 12 : 24); i++) {
          String str = DateUtils.fillZero(i);
//                    if (firstIndex == 0) {
//                        str += "￥";
//                    } else {
//                        str += "$";
//                    }
          secondList.add(str);
        }
        return secondList;
      }

      @Override
      public List<String> provideThirdData(int firstIndex, int secondIndex) {
        return null;
      }

    };
    LinkagePicker picker = new LinkagePicker(getAbility(), provider);
    picker.setCanLoop(false);
    picker.setLabel("小时制", "点");
    picker.setSelectedIndex(0, 8);
    //picker.setSelectedItem("12", "9");
    picker.setOnMoreItemPickListener(new OnMoreItemPickListener<String>() {

      @Override
      public void onItemPicked(String first, String second, String third) {
        new ToastDialog(MainAbility.this).setText(first + "-" + second + "-" + third).show();
      }
    });
    picker.show();
  }

  public void onConstellationPicker() {
    boolean isChinese = Locale.getDefault().getDisplayLanguage().contains("中文");
    SinglePicker<String> picker = new SinglePicker<>(getAbility(),
        isChinese ? new String[]{
            "水瓶座", "双鱼座", "白羊", "金牛座", "双子座", "巨蟹座",
            "狮子座", "处女座", "天秤座", "天蝎座", "射手", "摩羯座"
        } : new String[]{
            "Aquarius", "Pisces", "Aries", "Taurus", "Gemini", "Cancer",
            "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius", "Capricorn"
        });
    picker.setCanLoop(false);//不禁用循环
    picker.setTopBackgroundColor(0xFFEEEEEE);
    picker.setTopHeight(50);
    picker.setTopLineColor(0xFF33B5E5);
    picker.setTopLineHeight(1);
    picker.setTitleText(isChinese ? "请选择" : "Please pick");
    picker.setTitleTextColor(new Color(0xFF999999));
    picker.setTitleTextSize(52);
    picker.setCancelTextColor(new Color(0xFF33B5E5));
    picker.setCancelTextSize(53);
    picker.setSubmitTextColor(new Color(0xFF33B5E5));
    picker.setSubmitTextSize(53);
    picker.setSelectedTextColor(0xFFEE0000);
    picker.setUnSelectedTextColor(0xFF999999);
    LineConfig config = new LineConfig();
    config.setColor(Color.BLUE.getValue());//线颜色
    config.setAlpha(120);//线透明度
//        config.setRatio(1);//线比率
    picker.setLineConfig(config);
    picker.setItemWidth(200);
    picker.setBackgroundColor(new Color(0xFFE1E1E1));
    //picker.setSelectedItem(isChinese ? "处女座" : "Virgo");
    picker.setSelectedIndex(7);
    picker.setOnItemPickListener(new OnItemPickListener<String>() {
      @Override
      public void onItemPicked(int index, String item) {
        new ToastDialog(MainAbility.this).setText("index=" + index + ", item=" + item).show();
      }
    });
    picker.show();
  }

  public void onNumberPicker() {
    NumberPicker picker = new NumberPicker(getAbility());
    picker.setWidth(picker.getScreenWidthPixels() / 2);
    picker.setCanLoop(false);
    picker.setLineVisible(false);
    picker.setOffset(2);//偏移量
    picker.setRange(145, 200, 1);//数字范围
    picker.setSelectedItem(172);
    picker.setLabel("厘米");
    picker.setWeightEnable(true);
    picker.setOnNumberPickListener(new NumberPicker.OnNumberPickListener() {
      @Override
      public void onNumberPicked(int index, Number item) {
        new ToastDialog(MainAbility.this).setText("index=" + index + ", item=" + item.intValue()).show();
      }
    });
    picker.show();
  }

  public void onAddressPicker() {
    AddressPickTask task = new AddressPickTask(getAbility());
    task.setHideProvince(false);
    task.setHideCounty(false);
    task.setCallback(new AddressPickTask.Callback() {
      @Override
      public void onAddressInitFailed() {
        new ToastDialog(MainAbility.this).setText("数据初始化失败").show();
      }

      @Override
      public void onAddressPicked(Province province, City city, County county) {
        if (county == null) {
          new ToastDialog(MainAbility.this).setText(province.getAreaName() + city.getAreaName()).show();
        } else {
          new ToastDialog(MainAbility.this).setText(province.getAreaName() + city.getAreaName() + county.getAreaName()).show();
        }
      }
    });
    task.execute("贵州", "毕节", "纳雍");
  }

  public void onAddress2Picker() {
    try {
      ArrayList<Province> data = new ArrayList<>();
      String json = getOriginalFundData(this, "city2.json");
      data.addAll(JSON.parseArray(json, Province.class));
      AddressPicker picker = new AddressPicker(getAbility(), data);
      picker.setCanLoop(true);
      picker.setHideProvince(true);
      picker.setSelectedItem("贵州", "贵阳", "花溪");
      picker.setOnLinkageListener(new OnLinkageListener() {
        @Override
        public void onAddressPicked(Province province, City city, County county) {
          new ToastDialog(MainAbility.this).setText("province : " + province + ", city: " + city + ", county: " + county).show();
        }
      });
      picker.show();
    } catch (Exception e) {
      new ToastDialog(MainAbility.this).setText(e.toString()).show();
    }
  }


  public void onAddress3Picker() {
    AddressPickTask task = new AddressPickTask(getAbility());
    task.setHideCounty(true);
    task.setCallback(new AddressPickTask.Callback() {
      @Override
      public void onAddressInitFailed() {
        new ToastDialog(MainAbility.this).setText("数据初始化失败").show();
      }

      @Override
      public void onAddressPicked(Province province, City city, County county) {
        new ToastDialog(MainAbility.this).setText(province.getAreaName() + " " + city.getAreaName()).show();
      }
    });
    task.execute("四川", "阿坝");
  }

  @Override
  public void onClick(Component component) {
    if (component.getId() == ResourceTable.Id_nest_view) {

      onNestView();
    }else if (component.getId() ==ResourceTable.Id_animation_style) {
      onAnimationStyle();
    }else if (component.getId() ==ResourceTable.Id_animator){
      onAnimator();
    }else if (component.getId() ==ResourceTable.Id_date_range_picker){
      onDateRangePicker();
    }else if (component.getId() ==ResourceTable.Id_year_month_day_picker){
      onYearMonthDayPicker();
    }else if (component.getId() ==ResourceTable.Id_year_month_day_time_picker){
      onYearMonthDayTimePicker();
    }else if (component.getId() ==ResourceTable.Id_year_month_picker) {
      onYearMonthPicker();
    }else if (component.getId() ==ResourceTable.Id_month_day_picker) {
      onMonthDayPicker();
    }else if (component.getId() ==ResourceTable.Id_time_picker) {
      onTimePicker();
    }else if (component.getId() ==ResourceTable.Id_option_picker) {
      onOptionPicker();
    }else if (component.getId() ==ResourceTable.Id_linkage_picker) {
      onLinkagePicker();
    }else if (component.getId() ==ResourceTable.Id_constellation_picker) {
      onConstellationPicker();
    }else if (component.getId() ==ResourceTable.Id_number_picker) {
      onNumberPicker();
    }else if (component.getId() ==ResourceTable.Id_address_picker) {
      onAddressPicker();
    }else if (component.getId() ==ResourceTable.Id_address2_picker) {
      onAddress2Picker();
    }else if (component.getId() ==ResourceTable.Id_address3_picker) {
      onAddress3Picker();
    }
  }
}
