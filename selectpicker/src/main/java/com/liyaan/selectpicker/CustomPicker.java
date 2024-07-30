package com.liyaan.selectpicker;


import com.example.utils.ohospickers.common.LineConfig;
import com.example.utils.ohospickers.listeners.OnSingleWheelListener;
import com.example.utils.ohospickers.picker.SinglePicker;
import ohos.aafwk.ability.Ability;
import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorGroup;
import ohos.agp.animation.AnimatorProperty;
import ohos.agp.components.*;

/**
 * 自定义顶部及底部
 *
 */
public class CustomPicker extends SinglePicker<String> implements OnSingleWheelListener {
  private Text titleView;

  public CustomPicker(Ability activity) {
    super(activity, new String[]{
        "Java/ohos", "PHP/MySQL", "HTML/CSS/JS", "C/C++"
    });
    setSelectedIndex(1);
    setLineConfig(new LineConfig(0.06f));
    setOnSingleWheelListener(this);
  }

  @Override
  protected void showAfter() {
    ComponentContainer rootView = getRootView();
    AnimatorGroup animatorSet = new AnimatorGroup();
    AnimatorProperty alpha = new AnimatorProperty();
    alpha.setTarget(rootView);
    alpha.alphaFrom(0).alpha(1);
    AnimatorProperty translation = new AnimatorProperty();
    translation.setTarget(rootView);
    translation.moveFromY(300).moveToY(0);
    animatorSet.runParallel(alpha, translation);
    animatorSet.setDuration(2000);
    animatorSet.setCurveType(Animator.CurveType.ACCELERATE_DECELERATE);
    animatorSet.start();
  }

  @Override
  public void dismiss() {
    ComponentContainer rootView = getRootView();
    AnimatorGroup animatorSet = new AnimatorGroup();
    AnimatorProperty alpha = new AnimatorProperty();
    alpha.setTarget(rootView);
    alpha.alphaFrom(1).alpha(0);
    AnimatorProperty translation = new AnimatorProperty();
    translation.setTarget(rootView);
    translation.moveFromX(0).moveToX(rootView.getWidth());
    AnimatorProperty rotation = new AnimatorProperty();
    rotation.setTarget(rootView);
    rotation.rotate(120);
    animatorSet.runParallel(alpha, translation, rotation);
    animatorSet.setDuration(2000);
    animatorSet.setCurveType(Animator.CurveType.ACCELERATE_DECELERATE);
    animatorSet.setStateChangedListener(new Animator.StateChangedListener() {
      @Override
      public void onStart(Animator animator) {

      }

      @Override
      public void onStop(Animator animator) {

      }

      @Override
      public void onCancel(Animator animator) {

      }

      @Override
      public void onEnd(Animator animator) {
        dismissImmediately();
      }

      @Override
      public void onPause(Animator animator) {

      }

      @Override
      public void onResume(Animator animator) {

      }
    });
    animatorSet.start();
  }

  @Override
  protected Component makeHeaderView() {
    Component view = LayoutScatter.getInstance(activity).parse(ResourceTable.Layout_picker_header, null, false);
    titleView = (Text) view.findComponentById(ResourceTable.Id_picker_title);
    titleView.setText(titleText);
    view.findComponentById(ResourceTable.Id_picker_close).setClickedListener(new Component.ClickedListener() {
      @Override
      public void onClick(Component component) {
        dismiss();
      }
    });
    return view;
  }

  @Override
  protected Component makeFooterView() {
    Component view = LayoutScatter.getInstance(activity).parse(ResourceTable.Layout_picker_footer, null, false);
    Button submitView = (Button) view.findComponentById(ResourceTable.Id_picker_submit);
    submitView.setText(submitText);
    submitView.setClickedListener(new Component.ClickedListener() {
      @Override
      public void onClick(Component component) {
        dismiss();
        onSubmit();
      }
    });
    Button cancelView = (Button) view.findComponentById(ResourceTable.Id_picker_cancel);
    cancelView.setText(cancelText);
    cancelView.setClickedListener(new Component.ClickedListener() {
      @Override
      public void onClick(Component component) {
        dismiss();
        onCancel();
      }
    });
    return view;
  }

  @Override
  public void onWheeled(int index, String item) {
    if (titleView != null) {
      titleView.setText(item);
    }
  }

  @Override
  public void onSubmit() {
    super.onSubmit();
  }

  @Override
  protected void onCancel() {
    super.onCancel();
  }
}
