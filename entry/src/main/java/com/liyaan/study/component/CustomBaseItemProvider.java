package com.liyaan.study.component;

import com.liyaan.study.ResourceTable;
import com.liyaan.study.entity.resp.ArticleListBean;
import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public abstract class CustomBaseItemProvider<T,K extends CustomViewHolder> extends BaseItemProvider {

    private Component mComponent;

    protected List<T> mData;
    protected int mLayoutResId;
    public CustomBaseItemProvider(int layoutResId, List<T> data) {
        this.mData = data == null ? new ArrayList<T>() : data;
        if (layoutResId != 0) {
            this.mLayoutResId = layoutResId;
        }
    }
    @Override
    public Object getItem(int position) {
        if (mData!=null && mData.size()>0){
            return mData.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int id) {
        return id;
    }
    @Override
    public int getCount() {
        return mData!=null ? mData.size():0;
    }

    @Override
    public Component getComponent(int position, Component component, ComponentContainer componentContainer) {
        K views = null;
        if (component==null){
            mComponent = LayoutScatter.getInstance(componentContainer.getContext())
                    .parse(mLayoutResId, componentContainer, false);
            views = createBaseViewHolder(mComponent);
            mComponent.setTag(views);
        }else{
            mComponent = component;
            views = (K) mComponent.getTag();
        }
        hindViewComponent(position,mData.get(position),views);
        return mComponent;
    }
    public abstract void hindViewComponent(int position,T t,K views);


    public void addArticleListBean(List<T> data) {
        if (data.size()>0){
            this.mData.addAll(data);
            notifyDataChanged();
        }
    }
    protected K createBaseViewHolder(Component view) {
        Class temp = getClass();
        Class z = null;
        while (z == null && null != temp) {
            z = getInstancedGenericKClass(temp);
            temp = temp.getSuperclass();
        }
        K k;
        // 泛型擦除会导致z为null
        if (z == null) {
            k = (K) new CustomViewHolder(view);
        } else {
            k = createGenericKInstance(z, view);
        }
        return k != null ? k : (K) new CustomViewHolder(view);
    }
    private Class getInstancedGenericKClass(Class z) {
        Type type = z.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            for (Type temp : types) {
                if (temp instanceof Class) {
                    Class tempClass = (Class) temp;
                    if (CustomViewHolder.class.isAssignableFrom(tempClass)) {
                        return tempClass;
                    }
                } else if (temp instanceof ParameterizedType) {
                    Type rawType = ((ParameterizedType) temp).getRawType();
                    if (rawType instanceof Class && CustomViewHolder.class.isAssignableFrom((Class<?>) rawType)) {
                        return (Class<?>) rawType;
                    }
                }
            }
        }
        return null;
    }

    private K createGenericKInstance(Class z, Component view) {
        try {
            Constructor constructor;
            // inner and unstatic class
            if (z.isMemberClass() && !Modifier.isStatic(z.getModifiers())) {
                constructor = z.getDeclaredConstructor(getClass(), Component.class);
                constructor.setAccessible(true);
                return (K) constructor.newInstance(this, view);
            } else {
                constructor = z.getDeclaredConstructor(Component.class);
                constructor.setAccessible(true);
                return (K) constructor.newInstance(view);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}