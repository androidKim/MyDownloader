package com.midas.mydownloader.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.midas.mydownloader.R;
import com.midas.mydownloader.structure.core.content;


public class dlg_message_box extends Dialog
{
	/****************************** Define ******************************/

	/****************************** UI Control ******************************/
	private LinearLayout m_ly_DlgBase = null;
	private TextView m_tv_Title = null;
	private TextView m_tv_Message = null;
	private Button m_btn_Yes = null;
	private Button m_btn_No = null;
	private Button m_btn_Cancel = null;
	private RadioGroup m_RadioGroup = null;
	private RadioButton m_rBtn_InStorage = null;
	private RadioButton m_rBtn_OutStorage = null;
	/****************************** Member ******************************/
	private Dialog m_This = null;
	private Context m_Context = null;
	private String m_strTitle = null;
	private String m_strMessage = null;
	private String m_strYes = null;
	private String m_strNo = null;
	private String m_strSaveLocation = null;//파일저장위치(내장,외장)
	private OnCallbackIF m_CallbackIF = null;
	/****************************** System Event ******************************/
	//---------------------------------------------------------------------------------------------------
	//
	public dlg_message_box(Context pContext, String strTitle, String strMessage, String strYes, String strNo, OnCallbackIF pCallbackIF)
	{
		super(pContext, android.R.style.Theme_Translucent_NoTitleBar);		
		// Set Dialog UI XML		
		setContentView(R.layout.dlg_message_box);
		
		m_This = this;
		m_Context = pContext;
		
		// Set Dialog Data
		m_strTitle = strTitle;
		m_strMessage=strMessage;
		m_strYes = strYes;
		m_strNo = strNo;
		
		// Set Callback Interface
		m_CallbackIF = pCallbackIF;
		
		// Set Dialog's UI
		SetBaseCtrl();
	}
	
	//---------------------------------------------------------------------------------------------------
	//
	@Override
	public void cancel()
	{
		// Call Callback Function
		if( m_CallbackIF != null )
			m_CallbackIF.OnClose(m_This);
	}
	/****************************** User Function ******************************/
	//---------------------------------------------------------------------------------------------------
	//
	public void SetBaseCtrl()
	{
		m_ly_DlgBase = (LinearLayout)findViewById(R.id.ly_DlgBase);
		m_tv_Title = (TextView)findViewById(R.id.tv_Title);
		m_tv_Message = (TextView)findViewById(R.id.tv_Message);
		m_btn_Yes = (Button)findViewById(R.id.btn_Yes);
		m_btn_No = (Button)findViewById(R.id.btn_No);
		m_RadioGroup = (RadioGroup)findViewById(R.id.radioGroup);
		m_rBtn_InStorage = (RadioButton)findViewById(R.id.rBtn_InStorage);
		m_rBtn_OutStorage = (RadioButton)findViewById(R.id.rBtn_ExStorage);

		if(m_strMessage != null)
			m_tv_Message.setText(m_strMessage);

		if(m_strTitle != null)
			m_tv_Title.setText(m_strTitle);

		if(m_strYes != null)
			m_btn_Yes.setText(m_strYes);

		if(m_strNo != null)
			m_btn_No.setText(m_strNo);


		//event listener..
		m_ly_DlgBase.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				cancel();
			}
		});

		m_btn_Yes.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// Call Callback Function
				if( m_CallbackIF != null )
				{
					m_CallbackIF.OnYes(m_This, m_strSaveLocation);
				}
			}
		});

		m_btn_No.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// Call Callback Function
				if( m_CallbackIF != null )
				{
					m_CallbackIF.OnNo(m_This);
				}
			}
		});

		m_RadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId){
					case R.id.rBtn_InStorage:
						m_strSaveLocation = content.SAVE_TYPE_INTERNAL;
						break;
					case R.id.rBtn_ExStorage:
						m_strSaveLocation = content.SAVE_TYPE_EXTERNAL;
						break;
				}
			}
		});
	}

	/****************************** Callback Interface ******************************/
	//---------------------------------------------------------------------------------------------------
	//
	public interface OnCallbackIF
	{
		void OnYes(Dialog pDialog, String strSaveLocation);
		void OnNo(Dialog pDialog);
		void OnCancel(Dialog pDialog);
		void OnClose(Dialog pDialog);
	}
}
//---------------------------------------------------------------------------------------------------
//