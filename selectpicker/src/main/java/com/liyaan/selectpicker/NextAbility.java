package com.liyaan.selectpicker;


import com.example.utils.ohospickers.listeners.OnItemPickListener;
import com.example.utils.ohospickers.listeners.OnMoreItemPickListener;
import com.example.utils.ohospickers.listeners.OnMoreWheelListener;
import com.example.utils.ohospickers.listeners.OnSingleWheelListener;
import com.example.utils.ohospickers.picker.CarNumberPicker;
import com.example.utils.ohospickers.picker.SinglePicker;
import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.StackLayout;
import ohos.agp.components.Text;
import ohos.agp.utils.Color;
import ohos.agp.utils.TextTool;
import ohos.agp.window.dialog.ToastDialog;

/**
 * 内嵌选择器
 *
 * @author matt
 * blog: addapp.cn
 */
public class NextAbility extends BaseAbility {
  private CarNumberPicker picker;
  Text textView;

  @Override
  protected Component getContentView() {
    return inflateView(ResourceTable.Layout_ability_next);
  }

  @Override
  protected void setContentViewAfter(Component contentView) {
    textView = findView(ResourceTable.Id_wheelview_tips);

    StackLayout viewGroup = findView(ResourceTable.Id_wheelview_single);
    viewGroup.addComponent(onSinglePicker());

    picker = new CarNumberPicker(this);
    picker.setWeightEnable(true);
    picker.setColumnWeight(0.5f, 0.5f, 1);
    picker.setTextSize(18);
    picker.setSelectedTextColor(0xFF279BAA);//前四位值是透明度
    picker.setUnSelectedTextColor(0xFF999999);
    picker.setCanLoop(true);
    picker.setOffset(3);
    picker.setOnMoreItemPickListener(new OnMoreItemPickListener<String>() {
      @Override
      public void onItemPicked(String s1, String s2, String s3) {
        s3 = !TextTool.isNullOrEmpty(s3) ? ",item3: " + s3 : "";
        new ToastDialog(NextAbility.this).setText("item1: " + s1 + ",item2: " + s2 + s3).show();
      }
    });
    picker.setOnMoreWheelListener(new OnMoreWheelListener() {
      @Override
      public void onFirstWheeled(int index, String item) {
        textView.setText(item + ":" + picker.getSelectedSecondItem());

      }

      @Override
      public void onSecondWheeled(int index, String item) {
        textView.setText(picker.getSelectedFirstItem() + ":" + item);
      }

      @Override
      public void onThirdWheeled(int index, String item) {

      }
    });
    DirectionalLayout viewGroup1 = findView(ResourceTable.Id_wheelview_container);
    viewGroup1.addComponent(picker.getContentView());

    findComponentById(ResourceTable.Id_nest_back).setClickedListener(new Component.ClickedListener() {
      @Override
      public void onClick(Component component) {
        terminateAbility();
      }
    });
    findComponentById(ResourceTable.Id_nest_carnumber).setClickedListener(new Component.ClickedListener() {
      @Override
      public void onClick(Component component) {
        picker.show();
      }
    });
  }


  public Component onSinglePicker() {
//        String[] ss = (String[]) list.toArray();
    SinglePicker<String> picker = new SinglePicker<>(this, new String[]{"开封", "郑州", "广州", "北京", "成都"});
    picker.setCanLoop(false);//不禁用循环
    picker.setLineVisible(true);
    picker.setLineColor(Color.rgb(0, 174, 133));
    picker.setTextSize(30);
    picker.setSelectedIndex(2);
    //启用权重 setWeightWidth 才起作用
//        picker.setLabel("分");
//        picker.setItemWidth(100);
//        picker.setWeightEnable(true);
//        picker.setWeightWidth(1);
    picker.setOuterLabelEnable(true);
    picker.setSelectedTextColor(Color.rgb(0, 174, 133));//前四位值是透明度
    picker.setUnSelectedTextColor(Color.BLACK.getValue());
    picker.setOnSingleWheelListener(new OnSingleWheelListener() {
      @Override
      public void onWheeled(int index, String item) {
        textView.setText("index=" + index + ", item=" + item);
//                ToastUtils.showShort("index=" + index + ", item=" + item);
      }
    });
    picker.setOnItemPickListener(new OnItemPickListener<String>() {
      @Override
      public void onItemPicked(int index, String item) {
        textView.setText("index=" + index + ", item=" + item);
//                ToastUtils.showShort("index=" + index + ", item=" + item);
      }
    });
//        picker.show();
    return picker.getContentView();
  }
}
