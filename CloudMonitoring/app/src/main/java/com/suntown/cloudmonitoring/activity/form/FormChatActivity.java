package com.suntown.cloudmonitoring.activity.form;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.weight.MediaPlayerManager;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;

public class FormChatActivity extends BaseActivity{

    private static final String TAG = "FormChatActivity";
    private static final int MAKE_PICTURE = 100;
    private static final int CHOOSE_PICTURE = 200;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private boolean isInputByKeyBoard = true;
    private static final String MEMBERS_COUNT = "membersCount";
    private static final String GROUP_NAME = "groupName";
    private static final String DRAFT = "draft";
    private static final String MsgIDs = "msgIDs";
    private static final String NAME = "name";
    public static final String NICKNAME = "nickname";
    private static final String TARGET_ID = "targetId";
    private static final String TARGET_APP_KEY = "targetAppKey";
    private static final String GROUP_ID = "groupId";
    private static final int REQUEST_CODE_TAKE_PHOTO = 4;
    private static final int REQUEST_CODE_SELECT_PICTURE = 6;
    private static final int RESULT_CODE_SELECT_PICTURE = 8;
    private static final int REQUEST_CODE_CHAT_DETAIL = 14;
    private static final int RESULT_CODE_CHAT_DETAIL = 15;
    private static final int RESULT_CODE_FRIEND_INFO = 17;
    private static final int REFRESH_LAST_PAGE = 0x1023;
    private static final int REFRESH_CHAT_TITLE = 0x1024;
    private static final int REFRESH_GROUP_NAME = 0x1025;
    private static final int REFRESH_GROUP_NUM = 0x1026;
    private boolean mShowSoftInput = false;
    private final UIHandler mUIHandler = new UIHandler(this);
    private String userid;
    private Conversation mConv;
    private boolean isChoosePic = false;
    private InputMethodManager imm;
    private boolean isChooseVoice = false;
    protected float mDensity;
    protected int mDensityDpi;
    protected int mAvatarSize;
    protected int mWidth;
    protected int mHeight;
    private Context mContext;
    Window mWindow;
    InputMethodManager mImm;
    private String mPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_form_chat);
        ButterKnife.bind(this);
        //TODO 获取名字
        userid = getIntent().getStringExtra(Constant.NAME);
        tvTitle.setText("与" + userid + "聊天中...");
        mConv = JMessageClient.getSingleConversation(userid);
//        JMessageClient.registerEventReceiver(this);
        initListener();
//        mChatAdapter = new MsgListAdapter(this, userid);
//        llMessage.setAdapter(mChatAdapter);
//        mChatAdapter.initMediaPlayer();
//        //监听下拉刷新
//        llMessage.setOnDropDownListener(() -> mUIHandler.sendEmptyMessageDelayed(REFRESH_LAST_PAGE, 1000));
        // 滑动到底部
//        setToBottom();
    }

    private void initListener() {
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    /**
     * 处理发送图片，刷新界面
     *
     * @param data intent
     */
    private void handleImgRefresh(Intent data) {
//        mChatAdapter.setSendImg(data.getIntArrayExtra(MsgIDs));
//        setToBottom();
    }

    //滑动到最底部
//    private void setToBottom() {
//        llMessage.clearFocus();
//        llMessage.post(new Runnable() {
//            @Override
//            public void run() {
//                llMessage.setSelection(llMessage.getAdapter().getCount() - 1);
//            }
//        });
//    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaPlayerManager.pause();
        //调用exitConversation之后，将恢复对应的通知栏提示
        JMessageClient.exitConversation();

    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaPlayerManager.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JMessageClient.unRegisterEventReceiver(this);
//        mChatAdapter.releaseMediaPlayer();
        mUIHandler.removeCallbacksAndMessages(null);
    }

    private static class UIHandler extends Handler {
        private final WeakReference<FormChatActivity> mActivity;

        public UIHandler(FormChatActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

//        @Override
//        public void handleMessage(android.os.Message msg) {
//            super.handleMessage(msg);
//            FormChatActivity activity = mActivity.get();
//            if (activity != null) {
//                switch (msg.what) {
//                    case REFRESH_LAST_PAGE:
//                        activity.mChatAdapter.dropDownToRefresh();
//                        llMessage.getAdapter().onDropDownComplete();
//                        if (activity.mChatAdapter.isHasLastPage()) {
//                            if (Build.VERSION.SDK_INT >= 21) {
//                                activity.mChatView.getListView()
//                                        .setSelectionFromTop(activity.mChatAdapter.getOffset(),
//                                                activity.mChatView.getListView().getHeaderHeight());
//                            } else {
//                                activity.mChatView.getListView().setSelection(activity.mChatAdapter
//                                        .getOffset());
//                            }
//                            activity.mChatAdapter.refreshStartPosition();
//                        } else {
//                            activity.mChatView.getListView().setSelection(0);
//                        }
//                        activity.mChatView.getListView()
//                                .setOffset(activity.mChatAdapter.getOffset());
//                        break;
//
//                }
//            }
//        }
    }
}
