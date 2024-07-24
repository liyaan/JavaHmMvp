package com.example.utils.component.more;

import ohos.app.Context;

import java.util.List;

public class ProviderCreator {

    public static class Builder<T>{
        private BaseProvider<T> provider;

        public Builder(Context context) {
            super();
            provider = new BaseProvider<>(context);
        }

        public Builder<T> register(Class pojo, Class holder){
            provider.register(pojo,holder);
            return this;
        }

        public Builder<T> setData(List<T> data){
            provider.setData(data);
            return this;
        }

        public Builder<T> multCount(int count){
            provider.setMultCount(count);
            return this;
        }

        public BaseProvider<T> create(){
            return provider;
        }
    }
}