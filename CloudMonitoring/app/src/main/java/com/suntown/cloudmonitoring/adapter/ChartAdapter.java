package com.suntown.cloudmonitoring.adapter;


import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.activity.form.FormChatActivity;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.SPUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.model.Message;


/**
 * Created by Administrator on 2017/1/16.
 */
public class ChartAdapter extends BaseQuickAdapter<Message,BaseViewHolder> {

    private Context context;
    private View view;
    private int mMinWidth;
    //item的最大宽度
    private int mMaxWidth;
    private LayoutInflater mInflater;
    private final String userName;
    private final long currentTime;
    private final long lastDayTime;
    private long createTime;

    public ChartAdapter(Context context, List<Message> data) {
        super(data);
        this.context=context;
        currentTime = System.currentTimeMillis();
        lastDayTime = 3600 * 24 * 1000;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        //最大宽度为屏幕宽度的百分之七十
        mMaxWidth = (int) (outMetrics.widthPixels * 0.7f);
        //最大宽度为屏幕宽度的百分之十五
        mMinWidth = (int) (outMetrics.widthPixels * 0.15f);
        mInflater = LayoutInflater.from(context);
        userName = SPUtils.getString(context, Constant.USER_NAME);
    }
//    contentType : image text voice
    @Override
    protected void convert(BaseViewHolder holder, Message message) {
        ContentType contentType = message.getContentType();
        if ("voice".equals(contentType)) {
            String fromName = message.getFromName();
            if ("fromName".equals(userName)){
                view = LayoutInflater.from(context).inflate(R.layout.item_recoder,null);
            }else{
                view = LayoutInflater.from(context).inflate(R.layout.item_recoder_left,null);
            }
             createTime = message.getCreateTime();
            int position = holder.getPosition();
            VoiceContent voiceContent = (VoiceContent) message.getContent();
           voiceContent.getLocalPath();//语音文件本地地址
            int duration = voiceContent.getDuration();//语音文件时长
            holder.setText(R.id.id_recoder_time,Math.round(duration) + "\"");
            View length = holder.getView(R.id.id_recoder_lenght);
            ViewGroup.LayoutParams lp = length.getLayoutParams();
            lp.width = (int) (mMinWidth + (mMaxWidth / 60f) * duration);
        } else if ("image".equals(contentType)){
            if ("fromName".equals(userName)) {
                view = LayoutInflater.from(context).inflate(R.layout.item_image_view, null);
            }else{
                view = LayoutInflater.from(context).inflate(R.layout.item_image_left, null);
            }
            createTime = message.getCreateTime();
            ImageContent imageContent = (ImageContent) message.getContent();
            String localThumbnailPath = imageContent.getLocalThumbnailPath();
            ImageView ivPhoto = holder.getView(R.id.iv_photo);
            Picasso.with(context).load(localThumbnailPath).into(ivPhoto);
        } else if ("text".equals(contentType)){
            if ("fromName".equals(userName)) {
                view = LayoutInflater.from(context).inflate(R.layout.item_text, null);
            }else{
                view = LayoutInflater.from(context).inflate(R.layout.item_text_left, null);
            }

            createTime = message.getCreateTime();
            TextContent textContent = (TextContent) message.getContent();
            String text = textContent.getText();
            holder.setText(R.id.tv_text,text);
        }
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        long time = createTime - currentTime;
//        if (time >lastDayTime) {
//            String format = formatter.format(createTime);
//            holder.setText(R.id.tv_time,format);
//        }else{
//            long l = time / (1000 * 60);
//            if (l>60) {
//                long l1 = l % 60;
//                long l2 = (l - l1) / 60;
//                holder.setText(R.id.tv_time,l1==0?l+"小时前": l2+"小时"+ l1 +"分钟前");
//            }else{
//                holder.setText(R.id.tv_time,l+"分钟前");
//            }
//        }
    }
}
