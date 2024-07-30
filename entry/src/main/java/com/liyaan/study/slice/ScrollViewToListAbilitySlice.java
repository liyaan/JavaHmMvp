package com.liyaan.study.slice;


import com.liyaan.study.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import java.util.ArrayList;
import java.util.List;

public class ScrollViewToListAbilitySlice extends AbilitySlice {
    private ListContainer mListContainer;
    private List<String> list = new ArrayList<>();
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_scroll_list_slice);
        //NestedScrollView ListContainer 嵌套需要给ListContainer固定高度 鸿蒙目前不支持拦截操作
        mListContainer = (ListContainer) findComponentById(ResourceTable.Id_listContainer);

        mListContainer.getComponentTreeObserver().addTreeLayoutChangedListener(new ComponentTreeObserver.GlobalLayoutListener(){

            @Override
            public void onGlobalLayoutUpdated() {
                System.out.println("CustomScrollView aaaa");
            }
        });

        initData();
        mListContainer.setBoundarySwitch(true);
        mListContainer.setItemProvider(new MyP(list));

    }

    @Override
    public void onActive() {
        super.onActive();
        setListContainerHeight(mListContainer);
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);

    }

    private void initData(){
        if (list.size()>0)return;
        for (int i=0;i<20;i++){
            list.add("item title "+i);
        }
    }

    private void setListContainerHeight(ListContainer listContainer) {
        //获取当前listContainer的适配器
        BaseItemProvider baseItemProvider = listContainer.getItemProvider();
        if (baseItemProvider == null){
            return;
        }
        int itemHeight = 0;
        for (int i = 0; i < baseItemProvider.getCount(); i++) {
            //循环将listContainer适配器的Item数据进行累加
            Component listItem = baseItemProvider.getComponent(i, null, listContainer);
            System.out.println("CustomScrollView listItem.getHeight() = "+listItem.getHeight());
            itemHeight += listItem.getHeight();
        }
        //对当前listContainer进行高度赋值
        ComponentContainer.LayoutConfig config = listContainer.getLayoutConfig();
        //这边加上(listContainer.getBoundaryThickness() * (BaseItemProvider.getCount()+1))
        //listContainer.getBoundaryThickness() 就是分界线的高度
        //(BaseItemProvider.getCount()+1) 是Item的数量  加1  是因为顶部还有一条分界线
        config.height = itemHeight
                + (listContainer.getBoundaryThickness() * (baseItemProvider.getCount()+1));
        //赋值
        System.out.println("CustomScrollView config.height = "+config.height
                +" itemHeight = "+itemHeight+"  BaseItemProvider.getCount() = "+baseItemProvider.getCount());
        listContainer.setLayoutConfig(config);
    }

    static class MyP extends BaseItemProvider{
        private List<String> mList;
        private  Component mComponent;

        public MyP(List<String> list) {
            this.mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int i) {
            return mList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public Component getComponent(int i, Component component, ComponentContainer componentContainer) {
            if (component==null){
                mComponent = LayoutScatter.getInstance(componentContainer.getContext())
                        .parse(ResourceTable.Layout_item_scroll_list, componentContainer, false);
            }else{
                mComponent = component;
            }
            Text tv = (Text) mComponent.findComponentById(ResourceTable.Id_sampleTv);
            DirectionalLayout sampleTvLayout = (DirectionalLayout) mComponent.findComponentById(ResourceTable.Id_sampleTvLayout);
            tv.setText(mList.get(i));
            ComponentContainer.LayoutConfig layoutConfig =  new ComponentContainer.LayoutConfig();
            if (tv.getHeight()==0){
                layoutConfig.height = tv.getTextSize()+tv.getPaddingTop()+tv.getPaddingBottom();
            }else{
                layoutConfig.height = tv.getHeight();
            }

            mComponent.setLayoutConfig(layoutConfig);
            return mComponent;
        }
    }

}
