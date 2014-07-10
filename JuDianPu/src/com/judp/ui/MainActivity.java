package com.judp.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.judp.R;

public class MainActivity extends Activity {
	
	private final static String url="http://www.judp.cc/?mod=wap";
	private WebView webui;
	private ProgressDialog loadingDialog=null;
	private LinearLayout msg_view=null;
	private Dialog sjbm_dialog;
	private final OnClickListener mTabClickListener = new OnClickListener() {
        public void onClick(View view) {
            RadioButton tabView = (RadioButton)view;
            final int checkedId =tabView.getId();
            switch (checkedId) {
			case R.id.radiobtn_1:
				if(webui!=null){
					webui.loadUrl("http://www.judp.cc/?mod=wap");
				}
				break;
			case R.id.radiobtn_2:
				if(webui!=null){
					webui.loadUrl("http://www.judp.cc/?mod=wap&ac=brand");
				}
				break;
			case R.id.radiobtn_3:
				if(webui!=null){
					webui.loadUrl("http://www.judp.cc/?mod=wap");
				}
				break;
			case R.id.radiobtn_4:
				if(webui!=null){
					webui.loadUrl("http://www.judp.cc/?mod=wap&ac=register");
				}
				break;
			case R.id.radiobtn_5:
				showSJBMDialog();
				break;
			}
        }
    };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_view);
		
		msg_view = (LinearLayout) findViewById(R.id.msg_view);
		msg_view.setVisibility(View.GONE);
		msg_view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(webui!=null){
					webui.reload();
				}
				
			}
		});
		webui = (WebView) findViewById(R.id.web_view);
		webui.getSettings().setJavaScriptEnabled(true);
		webui.getSettings().setBuiltInZoomControls(false);//����ʹ֧������  
		webui.loadUrl(url);
		webui.setWebViewClient(new WebViewClient(){
			
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				if(loadingDialog!=null && loadingDialog.isShowing()){
					loadingDialog.dismiss();
				}
				loadingDialog=ProgressDialog.show(MainActivity.this, "", getString(R.string.loading), true, true);
				msg_view.setVisibility(View.GONE);
				view.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				if(loadingDialog!=null && loadingDialog.isShowing()){
					loadingDialog.dismiss();
				}
//				msg_view.setVisibility(View.GONE);
			}
			
            @Override  
            public boolean shouldOverrideUrlLoading(WebView view, String url) {  
                // TODO Auto-generated method stub  
                view.loadUrl(url);// ʹ�õ�ǰWebView������ת  
                return true;//true��ʾ���¼��ڴ˴�����������Ҫ�ٹ㲥  
            }  
            @Override   //ת�����ʱ�Ĵ���  
            public void onReceivedError(WebView view, int errorCode,  
                    String description, String failingUrl) {  
                // TODO Auto-generated method stub  
            	if(loadingDialog!=null && loadingDialog.isShowing()){
					loadingDialog.dismiss();
				}
            	view.setVisibility(View.GONE);
            	msg_view.setVisibility(View.VISIBLE);
//                Toast.makeText(MainActivity.this, description, Toast.LENGTH_SHORT).show();  
            }  
        });  
		initTabbar();
	}
	
	@Override   //Ĭ�ϵ���˼������˳�Activity�����������������ʹ������WebView�ڷ���  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        // TODO Auto-generated method stub  
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	if(webui.canGoBack()){
        		webui.goBack();
        	}else{
        		AlertDialog.Builder builder=new Builder(this);
        		builder.setTitle(R.string.exit);
        		builder.setMessage(R.string.exit_msg);
        		builder.setNegativeButton(getString(R.string.exit_btn_yes), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						
						finish();
						
					}
				});
        		builder.setPositiveButton(getString(R.string.exit_btn_no), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						
						dialog.dismiss();
						
					}
				});
        		builder.show();
        	}
            return true;  
        }  
        return super.onKeyDown(keyCode, event);  
    }  
	
	private void initTabbar(){
		
		RadioGroup tab = (RadioGroup) findViewById(R.id.tab_bar);
		for(int i=0;i<tab.getChildCount();i++){
			View tabview=tab.getChildAt(i);
			tabview.setOnClickListener(mTabClickListener);
		}
	}
	
	/**
	 * ��ʾ�̼ұ����Ի���
	 */
	public void showSJBMDialog(){
		
		if(sjbm_dialog!=null && sjbm_dialog.isShowing()){
			sjbm_dialog.dismiss();
		}
		
		sjbm_dialog=new Dialog(this, R.style.dialogstyle);
		View view=LayoutInflater.from(this).inflate(R.layout.shangjiabm_view, null);
		Button btn_close=(Button) view.findViewById(R.id.btn_close);
		btn_close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(sjbm_dialog!=null && sjbm_dialog.isShowing()){
					sjbm_dialog.dismiss();
				}
			}
		});
		sjbm_dialog.setContentView(view);
		sjbm_dialog.setCanceledOnTouchOutside(false);
		sjbm_dialog.show();
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
