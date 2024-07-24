package com.liyaan.study.component;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.PageSlider;
import ohos.agp.components.PageSliderIndicator;
import ohos.agp.components.element.ShapeElement;
import ohos.app.Context;
import ohos.app.dispatcher.TaskDispatcher;
import ohos.app.dispatcher.task.TaskPriority;
import ohos.multimodalinput.event.TouchEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static ohos.global.icu.lang.UCharacter.NumericType.NONE;
import static ohos.multimodalinput.event.TouchEvent.*;

public class CustomPageSlider extends PageSlider implements PageSlider.PageChangedListener {

    private PageChangedAbstract mPageChangedAbstract;
    private PageSliderIndicator mPageSliderIndicator;

    /**
     * 处理轮播图收尾链接线程
     */
    private TaskDispatcher globalTaskDispatcher;

    /**
     * 线程池
     */
    private ScheduledExecutorService scheduledExecutorService;
    /**
     * 是否处于自动轮播状态
     */
    private boolean isAutoSlide = true;

    /**
     * 自动切换任务
     */
    private AutomaticSwitching automaticSwitching = new AutomaticSwitching();

    private Context mContext;
    private Integer index = 0;
    /**
     * 轮播数据长度
     */
    private Integer dataSize;
    /**
     * 切换过程时间，单位毫秒
     */
    private Integer pageSwitchTime = 500;
    /**
     * 自动切换时间
     */
    private static final Integer TIME_INTERVAL = 5000;
    public CustomPageSlider(Context context) {
        this(context,null);
    }

    public CustomPageSlider(Context context, AttrSet attrSet) {
        super(context, attrSet);
        this.mContext = context;
        this.globalTaskDispatcher = mContext.getGlobalTaskDispatcher(TaskPriority.DEFAULT);
//        this.dataSize = getProvider().getCount();

    }

    public void setDataSize(int size) {
        this.dataSize = size;
    }
//    public CustomPageSlider(Context context, AttrSet attrSet, String styleName) {
//        super(context, attrSet, styleName);
////        this.setPageSwitchTime(2000);
//        this.mContext = context;
//        this.dataSize = getProvider().getCount();
//        this.globalTaskDispatcher = mContext.getGlobalTaskDispatcher(TaskPriority.DEFAULT);
////        addTouchEventListener(this);
//
//
//    }

    public void setPageChangedAbstract(PageChangedAbstract pageChangedAbstract) {
        this.mPageChangedAbstract = pageChangedAbstract;
    }

    public void setPageSliderIndicator(PageSliderIndicator pageSliderIndicator,
                                       ShapeElement normalElement,ShapeElement selectedElement) {
        this.mPageSliderIndicator = pageSliderIndicator;
        pageSliderIndicator.setItemElement(normalElement, selectedElement);
        pageSliderIndicator.setItemOffset(30);
        pageSliderIndicator.setPageSlider(this);
    }

    @Override
    public void onPageSliding(int i, float v, int i1) {
        if (mPageChangedAbstract!=null){
            mPageChangedAbstract.onPageSliding(i,v,i1);
        }
    }

    @Override
    public void onPageSlideStateChanged(int i) {
        if (mPageChangedAbstract!=null){
            mPageChangedAbstract.onPageSlideStateChanged(i);
        }
    }

    @Override
    public void onPageChosen(int i) {
        // 更新index
        index = i;
        // 处理手动滑动逻辑
        if(! isAutoSlide){
            // 向左滑动，轮播图向右滚动
            if(index == dataSize - 1){
                index = 0;
                setCurrentPage(index, false);
            }
            // 向右滑动，轮播图向左滚动
            if(index == 0){
                index = dataSize - 1;
                setCurrentPage(index, false);
            }
        }
        if (mPageChangedAbstract!=null){
            mPageChangedAbstract.onPageChosen(i);
        }
    }
    abstract class PageChangedAbstract{
        abstract void onPageSliding(int i, float v, int i1);
        abstract void onPageSlideStateChanged(int i);
        abstract void onPageChosen(int i);
    }


    /**
     * 切换轮播图内容
     */
    class AutomaticSwitching implements Runnable{
        @Override
        public void run() {
            mContext.getUITaskDispatcher().asyncDispatch(() -> {
                index++;
                if(index == dataSize){
                    // 切换到第一个位置, 且立即切换
                    globalTaskDispatcher.asyncDispatch(new SlideShowTaskLast());
                }else{
                    setCurrentPage(index, true);
                }
            });
        }
    }

    class SlideShowTaskLast implements Runnable{
        @Override
        public void run() {
            try{
                Thread.sleep(pageSwitchTime);
            }catch (Exception e){

            }
            mContext.getUITaskDispatcher().syncDispatch(() -> {
                index = 0;
                setCurrentPage(index, false);
            });
        }
    }
    /**
     * 轮播图触摸事件
     */
    public void addTouchEventListener(){
        setTouchEventListener((component, touchEvent) -> {
            int action = touchEvent.getAction();
            // 最后一根手指抬起、取消、没有触摸活动时， 启动定时器
            if(action == PRIMARY_POINT_UP || action == CANCEL || action == NONE) {
                isAutoSlide = true;
                startPlaying();
            } else if (action == PRIMARY_POINT_DOWN) {
                isAutoSlide = false;
                // 存在触摸时，停止自动切换
                stopPlaying();
            }
            return true;
        });
    }
    /**
     * 定时器，自动切换图片
     */
    public void startPlaying(){
        if (this.scheduledExecutorService==null){
            // 初始化线程池
            this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            // 设置线程池
            this.scheduledExecutorService.scheduleWithFixedDelay(automaticSwitching, TIME_INTERVAL, TIME_INTERVAL, TimeUnit.MILLISECONDS);
        }

    }

    /**
     * 停止轮播释放资源
     */
    public void stopPlaying() {
        scheduledExecutorService.shutdown();
    }

}