package com.suntown.cloudmonitoring.activity.form;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.suntown.cloudmonitoring.R;
import com.suntown.cloudmonitoring.activity.FiltrateRegisterAct;
import com.suntown.cloudmonitoring.activity.HistortyActivity;
import com.suntown.cloudmonitoring.adapter.NameAdapter;
import com.suntown.cloudmonitoring.adapter.ShopAdapter;
import com.suntown.cloudmonitoring.api.ApiService;
import com.suntown.cloudmonitoring.base.BaseActivity;
import com.suntown.cloudmonitoring.bean.AllShopBean;
import com.suntown.cloudmonitoring.bean.FiltrateBean;
import com.suntown.cloudmonitoring.bean.FormListBean;
import com.suntown.cloudmonitoring.bean.UserListBean;
import com.suntown.cloudmonitoring.netUtils.RxSchedulers;
import com.suntown.cloudmonitoring.utils.Constant;
import com.suntown.cloudmonitoring.utils.SPUtils;
import com.suntown.cloudmonitoring.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.functions.Action1;

public class ProductFormActivity extends BaseActivity {

    private static final String TAG = "ProductFormActivity";
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.tv_shop_num)
    TextView tvShopNum;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.tv_receive)
    TextView tvReceive;
    private String serverIP;
    private ArrayList<FiltrateBean> filtrateBean;
    private List<AllShopBean.RECORDBean> record;
    private String sid;
    private String userid;
    private List<UserListBean.UsersBean> users;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_form);
        ButterKnife.bind(this);
        //TODO 获取所有门店
        serverIP = SPUtils.getString(this, Constant.SUBSERVER_IP);
        if ("".equals(serverIP)) {
            userid = SPUtils.getString(this, Constant.USER_ID);
            serverIP = SPUtils.getString(this, Constant.SERVER_IP);
        } else {
            userid = SPUtils.getString(this, Constant.SUB_USER_ID);
        }
        //获取门店列表
        getUserShopList(userid);

    }

    @OnClick({R.id.iv_back, R.id.ll_choose_shop, R.id.ll_choose_receive,R.id.tv_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_choose_shop:
                //TODO 选择门店
                if (!Utils.isFastClick()) {
                    if (0!=filtrateBean.size()) {
                        Intent intent = new Intent(ProductFormActivity.this, FiltrateRegisterAct.class);
                        intent.putParcelableArrayListExtra("record", filtrateBean);
                        startActivityForResult(intent, 200);
                    }else{
                        Utils.showToast(this,"数据加载中，请稍后");
                    }
                }
                break;
            case R.id.ll_choose_receive:
                //TODO 选择联系人 必须先选择门店 sid 不能为空
               Log.i(TAG," id="+sid);
                if (null==sid) {
                    Utils.showToast(ProductFormActivity.this,"请选择门店!");
                }else if (null==users) {
                    Utils.showToast(ProductFormActivity.this, "数据加载中");
                } else if (0==users.size()){
                    Utils.showToast(ProductFormActivity.this,"该门店下暂无接收人");
                }
                else if (!"".equals(sid)&&0<users.size()) {
                    showPopupWindow(view);
                }else{
                    Utils.showToast(ProductFormActivity.this,"请选择门店!");
                }
                break;
            case R.id.tv_commit:
                //TODO  保存工单 generateWorkform  参数 workform = {"adduser":"","acceptuser":"","addcause":"”，sid:“”}
                String context= etContent.getText().toString();
                String name = tvReceive.getText().toString();
                if ("".equals(sid)||"".equals(context)||"".equals(name)){
                    Utils.showToast(this,"请完善工单信息");
                }else{
                    //上传工单信息
                    upLoadServer(userid,name,context,sid);
                }
                break;
        }
    }

    /**
     * 提交工单
     * @param userid
     * @param name
     * @param context
     * @param id
     */
    private void upLoadServer(String userid, String name, String context, String id) {
        Gson gson = new Gson();
        FormBean formBean = new FormBean();
        formBean.adduser=userid;
        formBean.acceptuser=name;
        formBean.addcause = context;
        formBean.sid =id;
        String json = gson.toJson(formBean);
        Log.i(TAG,"json-"+json);
        new Retrofit.Builder().
                addConverterFactory(GsonConverterFactory.create())
//                                                                        baseUrl(serverIP)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl("http://192.168.0.12:8080/").build().
                create(ApiService.class).generateWorkform(json).compose(RxSchedulers.io_main()).subscribe( formBaseBean-> {
            Log.i(TAG,"formBaseBean:"+formBaseBean.toString());
            if (1==formBaseBean.record){
                Utils.showToast(this,"工单提交成功");
                sid= "";
                etContent.setText("");
                tvReceive.setText("");
                tvShopNum.setText("");
                tvShopName.setText("");
            }else{
                Utils.showToast(this,"工单提交失败，请重试");
            }
        }, throwable -> {
            Log.i(TAG,"throwable-"+throwable);
        });
    }

    ListView lvList;
    /**
     * 弹出对话框
     * @param v
     */
    private void showPopupWindow(View v) {
        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.group_list, null);
            lvList = (ListView) view.findViewById(R.id.lv_list);
            NameAdapter adapter = new NameAdapter(this, users);
            lvList.setAdapter(adapter);
            popupWindow = new PopupWindow(view,300, 500);
        }
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.colorWhite));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
//        int[] location = new int[2];
//        popupWindow.showAsDropDown(v, location[0] - popupWindow.getWidth(), location[1]);
        WindowManager manager=(WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int xpos=manager.getDefaultDisplay().getWidth()/2-popupWindow.getWidth()/2;
        //xoff,yoff基于anchor的左下角进行偏移。
        popupWindow.showAsDropDown(v,xpos, 0);
//        popupWindow.showAsDropDown(parent);
        lvList.setOnItemClickListener((parent1, view, position, id) -> {
            String acceptName = users.get(position).userid;
            tvReceive.setText(acceptName);
            if (popupWindow != null) {
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 获取联系人列表
     * @param sid
     */
    private void getUsersBySid(String sid) {
         new Retrofit.Builder().
                addConverterFactory(GsonConverterFactory.create())
//                                                                        baseUrl(serverIP)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl("http://192.168.0.12:8080/").build().
                 create(ApiService.class).getUsersBySid(sid).compose(RxSchedulers.io_main()).subscribe(userListBean -> {
             Log.i(TAG,"userListBean:"+userListBean.toString());
                    users = userListBean.users;
                 }, throwable -> {
             Log.i(TAG,"throwable-"+throwable);
         });
    }
    /**
     * 获取用户管辖的门店
     */
    private void getUserShopList(String userid) {
//        getUserShopList
        Log.i(TAG,"serverIp-"+serverIP);
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID, userid);
        String ip = Constant.formatBASE_HOST(serverIP);
        Retrofit retrofit = new Retrofit.Builder().
                addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).baseUrl(ip).build();
        ApiService service = retrofit.create(ApiService.class);
        Observable<AllShopBean> apForm = service.getAllShopInfo(params);
        apForm.compose(RxSchedulers.io_main()).subscribe(allShopBean -> {
            record = allShopBean.RECORD;
            Log.i(TAG,"record-"+ record);
            filtrateBean = new ArrayList<>();
            for (AllShopBean.RECORDBean recordBean : record) {
                FiltrateBean bean = new FiltrateBean(recordBean.ANAME, recordBean.SNAME);
                filtrateBean.add(bean);
            }
        }, throwable -> {
            Log.i(TAG,"throwable-"+throwable);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 200:
                if (resultCode == 300) {
                    String name = data.getStringExtra(Constant.STRING_DATA);
                    if (!"".equals(name)) {
                        for (AllShopBean.RECORDBean recordBean : record) {
                            if (recordBean.SNAME.equals(name)) {
                                String num = recordBean.SID;
                                sid = num;
                                tvShopName.setText(recordBean.SNAME);
                                tvShopNum.setText(num);
                                Log.i(TAG,"num:"+num);
                                //获取联系人 getusersbysid
                                getUsersBySid(num);
                            }
                        }
                    }
                }
                break;
        }
    }
}
