package com.example.utils.component.more;

import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.app.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BaseProvider<T> extends BaseItemProvider {

    private List<T> data;
    private Context context;
    protected boolean enableMult = false;
    protected ArrayList<Class<ViewHolder<T>>> holders;
    protected HashMap<Class<T>,Class<ViewHolder<T>>> map;
    protected int multCount = 1;
    /**
     * 注册holder
     * @param holder
     */
    public BaseProvider<T> register(Class<T> pojo,Class<ViewHolder<T>> holder){
        if(holders == null){
            holders = new ArrayList<>();
            map = new HashMap<>();
        }
        holders.add(holder);
        map.put(pojo,holder);
        return this;
    }

    /**
     * 是否允许多布局
     * @param enableMult
     * @return
     */
    public BaseProvider<T> mult(boolean enableMult){
        this.enableMult = enableMult;
        return this;
    }

    public BaseProvider(Context context) {
        super();
        this.context = context;
        data = new ArrayList<>();
    }

    public BaseProvider(Context context,List<T> data) {
        super();
        this.data = data;
        this.context = context;
    }

    public void refreshData(List<T> data){
        setData(data);
        notifyDataChanged();
    }

    public void setData(List<T> data){
        this.data = data;
    }

    public void more(List<T> more){
        this.data.addAll(more);
        notifyDataChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public T getItem(int i) {
        return data.get(i);
    }

    public void setMultCount(int count) {
        this.multCount = count;
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public int getItemComponentType(int position) {
        T data = getItem(position);

        if(data instanceof Mult){
            Mult mult = (Mult)data;
            Class holderClass = map.get(data.getClass());
            return holderClass.hashCode()+mult.mult();
        }

        return position;
    }

    @Override
    public int getComponentTypeCount() {
        return multCount;
    }

    @Override
    public Component getComponent(int pos, Component component, ComponentContainer componentContainer) {
        T itemData = data.get(pos);

        //单布局
        Class<ViewHolder<T>> holderClass = map.get(itemData.getClass());
        if(holderClass == null){
            throw new RuntimeException("请先注册holder和数据类型");
        }
        T data = itemData;
        ViewHolder<T> holder = ViewHolder.<T>get(context,component,data,pos,holderClass);
        return holder.getRootComponent();
    }


}