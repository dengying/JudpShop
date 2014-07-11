package com.judp.ui;

import java.security.acl.LastOwnerException;

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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.RenderPriority;
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
	private RadioGroup radioGroup;
	private int currentCheckedItem=-1;
	private AlertDialog exitDialog;
	public final static String HomeUrl="http://www.judp.cc/?mod=wap";
	public final static String BrandUrl="http://www.judp.cc/?mod=wap&ac=brand";
	public final static String registerUrl="http://www.judp.cc/?mod=wap&ac=register";
	private final OnClickListener mTabClickListener = new OnClickListener() {
        public void onClick(View view) {
            RadioButton tabView = (RadioButton)view;
            final int checkedId =tabView.getId();
            switch (checkedId) {
			case R.id.radiobtn_1:
				if(webui!=null){
					webui.loadUrl(HomeUrl);
				}
				currentCheckedItem=R.id.radiobtn_1;
				break;
			case R.id.radiobtn_2:
				if(webui!=null){
					webui.loadUrl(BrandUrl);
				}
				currentCheckedItem=R.id.radiobtn_2;
				break;
			case R.id.radiobtn_3:
				if(webui!=null){
					webui.loadUrl(HomeUrl);
				}
				currentCheckedItem=R.id.radiobtn_3;
				break;
			case R.id.radiobtn_4:
				if(webui!=null){
					webui.loadUrl(registerUrl);
				}
				currentCheckedItem=R.id.radiobtn_4;
				break;
			case R.id.radiobtn_5:
				showSJBMDialog();
				if(radioGroup!=null && currentCheckedItem!=-1){
					radioGroup.check(currentCheckedItem);
				}
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
		WebSettings settings=webui.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setBuiltInZoomControls(false);//设置使支持缩放  
		settings.setSaveFormData(true);
		settings.setRenderPriority(RenderPriority.HIGH);
		settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webui.loadUrl(url);
		webui.setWebViewClient(new WebViewClient(){
			
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
//				if(loadingDialog!=null && loadingDialog.isShowing()){
//					loadingDialog.dismiss();
//				}
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
                view.loadUrl(url);// 使用当前WebView处理跳转  
                return true;//true表示此事件在此处被处理，不需要再广播  
            }  
            @Override   //转向错误时的处理  
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
	
	@Override   //默认点回退键，会退出Activity，需监听按键操作，使回退在WebView内发生  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        // TODO Auto-generated method stub  
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	if(webui.canGoBack()){
        		switch (currentCheckedItem) {
				case R.id.radiobtn_1:
					if(HomeUrl.toLowerCase().equals(webui.getUrl())){
						showExitDialog();
					}else{
						webui.goBack();
					}
					break;
				case R.id.radiobtn_2:
					if(BrandUrl.toLowerCase().equals(webui.getUrl().toLowerCase())){
						showExitDialog();
					}else{
						webui.goBack();
					}
					break;
				case R.id.radiobtn_3:
					if(HomeUrl.toLowerCase().equals(webui.getUrl())){
						showExitDialog();
					}else{
						webui.goBack();
					}
					break;
				case R.id.radiobtn_4:
					if(registerUrl.toLowerCase().equals(webui.getUrl())){
						showExitDialog();
					}else{
						webui.goBack();
					}
					break;
				default:
					showExitDialog();
					break;
				}
        		
        	}else{
        		showExitDialog();
        	}
            return true;  
        }  
        return super.onKeyDown(keyCode, event);  
    }  
	
	private void initTabbar(){
		
		radioGroup = (RadioGroup) findViewById(R.id.tab_bar);
		for(int i=0;i<radioGroup.getChildCount();i++){
			View tabview=radioGroup.getChildAt(i);
			tabview.setOnClickListener(mTabClickListener);
		}
		radioGroup.check(R.id.radiobtn_3);
		currentCheckedItem=R.id.radiobtn_3;
	}
	
	/**
	 * 显示商家报名对话框
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
	
	/**
	 * 显示退出窗口
	 */
	private void showExitDialog(){
		
		if(exitDialog!=null && exitDialog.isShowing()){
			return;
		}
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
		exitDialog=builder.create();
		exitDialog.show();
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(webui!=null){
			webui.clearCache(true);
		}
		super.onDestroy();
	}

}
