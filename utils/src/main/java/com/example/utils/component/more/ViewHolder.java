package com.example.utils.component.more;

import ohos.agp.components.Component;
import ohos.agp.components.LayoutScatter;
import ohos.app.Context;

import java.lang.reflect.Constructor;
import java.util.HashMap;

public abstract class ViewHolder<Data> {

    protected HashMap<Integer, Component> mComponents;
    protected Component mRootComponent;
    protected Context context;
    protected int layoutId;
    protected int position;
    protected LayoutScatter layoutScatter;
    public ViewHolder(Context context) {
        super();
        this.context = context;
        if(layoutScatter == null){
            layoutScatter = LayoutScatter.getInstance(context);
        }

        mComponents = new HashMap<>();
    }

    public void setLayout(Data data){
        layoutId = getLayoutId(data,position);
        this.mRootComponent = layoutScatter.parse(layoutId,null,false);
        mRootComponent.setTag(this);
        findComponent(data,position);
    }

    public static <T> ViewHolder<T> get(Context context,Component convertView,T data,int pos,Class holderClass)  {
        ViewHolder holder = null;
        try {
            if(convertView == null){
                Constructor<ViewHolder<T>> declaredConstructor = holderClass.getDeclaredConstructor(Context.class);
                holder = declaredConstructor.newInstance(context);
                holder.position = pos;
                holder.setLayout(data);
            }else{
                holder = (ViewHolder)convertView.getTag();
                holder.position = pos;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        holder.convert(data,pos);
        return holder;
    }

    public Component getRootComponent(){
        return mRootComponent;
    }

    public abstract void convert(Data data,int position);

    /**
     * 通过componentId获取控件
     *
     * @param viewId
     * @return
     */
    public <T extends Component> T getComponent(int viewId) {
        Component view = mComponents.get(viewId);
        if (view == null) {
            view = mRootComponent.findComponentById(viewId);
            mComponents.put(viewId, view);
        }
        return (T) view;
    }

    protected abstract int getLayoutId(Data data,int position);

    protected abstract void findComponent(Data data,int position);
}
