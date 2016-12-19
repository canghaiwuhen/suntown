package com.suntown.cloudmonitoring.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/11/11.
 */
public class MaptypeBean implements Parcelable {

    /**
     * eslWarn : {"oriMaptype":0}
     */

    public EslWarnBean eslWarn;


    public static class EslWarnBean implements Parcelable {
        /**
         * oriMaptype : 0
         */

        public int oriMaptype;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.oriMaptype);
        }

        public EslWarnBean() {
        }

        protected EslWarnBean(Parcel in) {
            this.oriMaptype = in.readInt();
        }

        public static final Parcelable.Creator<EslWarnBean> CREATOR = new Parcelable.Creator<EslWarnBean>() {
            @Override
            public EslWarnBean createFromParcel(Parcel source) {
                return new EslWarnBean(source);
            }

            @Override
            public EslWarnBean[] newArray(int size) {
                return new EslWarnBean[size];
            }
        };

        @Override
        public String toString() {
            return "EslWarnBean{" +
                    "oriMaptype=" + oriMaptype +
                    '}';
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.eslWarn, flags);
    }

    public MaptypeBean() {
    }

    protected MaptypeBean(Parcel in) {
        this.eslWarn = in.readParcelable(EslWarnBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<MaptypeBean> CREATOR = new Parcelable.Creator<MaptypeBean>() {
        @Override
        public MaptypeBean createFromParcel(Parcel source) {
            return new MaptypeBean(source);
        }

        @Override
        public MaptypeBean[] newArray(int size) {
            return new MaptypeBean[size];
        }
    };
}
