package com.suntown.suntownshop.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
/**
 * ��ʽ����
 *
 * @author Ǯ��
 * @version 2015��7��17�� ����10:42:47
 *
 */
public class XCFlowLayout extends ViewGroup{
	 
    //�洢������View
    private List<List<View>> mAllChildViews = new ArrayList<List<View>>();
    //ÿһ�еĸ߶�
    private List<Integer> mLineHeight = new ArrayList<Integer>();
     
    public XCFlowLayout(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }
    public XCFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }
    public XCFlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
         
        //���ؼ��������Ŀ�Ⱥ͸߶��Լ���Ӧ�Ĳ���ģʽ
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
         
        //�����ǰViewGroup�Ŀ��Ϊwrap_content�����
        int width = 0;//�Լ������� ���
        int height = 0;//�Լ������ĸ߶�
        //��¼ÿһ�еĿ�Ⱥ͸߶�
        int lineWidth = 0;
        int lineHeight = 0;
         
        //��ȡ��view�ĸ���
        int childCount = getChildCount();
        for(int i = 0;i < childCount; i ++){
            View child = getChildAt(i);
            //������View�Ŀ�͸�
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            //�õ�LayoutParams
            MarginLayoutParams lp = (MarginLayoutParams) getLayoutParams();
            //��Viewռ�ݵĿ��
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            //��Viewռ�ݵĸ߶�
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            //����ʱ��
            if(lineWidth + childWidth > sizeWidth){
                //�Աȵõ����Ŀ��
                width = Math.max(width, lineWidth);
                //����lineWidth
                lineWidth = childWidth;
                //��¼�и�
                height += lineHeight;
                lineHeight = childHeight;
            }else{//���������
                //�����п�
                lineWidth += childWidth;
                //�õ�����и�
                lineHeight = Math.max(lineHeight, childHeight);
            }
            //�������һ����View�����
            if(i == childCount -1){
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }
        }
        //wrap_content
        setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width,
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
     
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // TODO Auto-generated method stub
        mAllChildViews.clear();
        mLineHeight.clear();
        //��ȡ��ǰViewGroup�Ŀ��
        int width = getWidth()-getPaddingLeft()-getPaddingRight();
        int lineWidth = getPaddingLeft()+getPaddingRight();
        int lineHeight = 0;
        //��¼��ǰ�е�view
        List<View> lineViews = new ArrayList<View>();
        int childCount = getChildCount();
        for(int i = 0;i < childCount; i ++){
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
             
            //�����Ҫ����
            if(childWidth + lineWidth + lp.leftMargin + lp.rightMargin > width){
                //��¼LineHeight
                mLineHeight.add(lineHeight);
                //��¼��ǰ�е�Views
                mAllChildViews.add(lineViews);
                //�����еĿ��
                lineWidth = getPaddingLeft()+getPaddingRight();
                lineHeight = childHeight + lp.topMargin + lp.bottomMargin;
                //����view�ļ���
                lineViews = new ArrayList();
            }
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);
            lineViews.add(child);
        }
        //�������һ��
        mLineHeight.add(lineHeight);
        mAllChildViews.add(lineViews);
         
        //������View��λ��
        int left = getPaddingLeft();
        int top = 0;
        //��ȡ����
        int lineCount = mAllChildViews.size();
        for(int i = 0; i < lineCount; i ++){
            //��ǰ�е�views�͸߶�
            lineViews = mAllChildViews.get(i);
            lineHeight = mLineHeight.get(i);
            for(int j = 0; j < lineViews.size(); j ++){
                View child = lineViews.get(j);
                //�ж��Ƿ���ʾ
                if(child.getVisibility() == View.GONE){
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int cLeft = left + lp.leftMargin;
                int cTop = top + lp.topMargin;
                int cRight = cLeft + child.getMeasuredWidth();
                int cBottom = cTop + child.getMeasuredHeight();
                //������View���в���
                child.layout(cLeft, cTop, cRight, cBottom);
                left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            }
            left = getPaddingLeft();
            top += lineHeight;
        }
         
    }
    /**
     * �뵱ǰViewGroup��Ӧ��LayoutParams
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        // TODO Auto-generated method stub
         
        return new MarginLayoutParams(getContext(), attrs);
    }
}
