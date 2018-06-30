package com.midas.mydownloader;

import android.app.Application;
import android.content.Context;

import com.midas.mydownloader.core.SharedPrefCtrl;
import com.midas.mydownloader.ui.dialog.dlg_message_box;


/**
 * Created by taejun on 2018. 6. 20..
 */

public class MyApp extends Application
{

    /************************* Define *************************/

    /************************* Member *************************/
    public MyApp m_This = null;
    public Context m_Context = null;
    public SharedPrefCtrl m_SpCtrl = null;
    public boolean m_bInit = false;
    /************************* Controller *************************/
    public dlg_message_box m_DlgMessageBox = null;
    /************************* System Function *************************/
    //--------------------------------------------------------------
    //
    @Override
    public void onCreate()
    {
        super.onCreate();
    }
    /************************* 생성자 *************************/
    //--------------------------------------------------------------
    //
    public MyApp()
    {

    }
    //--------------------------------------------------------------
    //
    public MyApp(Context pContext)
    {
        if(pContext == null)
            return;

        if(m_This == null)
        {
            m_This = this;
            m_Context = pContext;
            init();
        }
    }
    /************************* User Function *************************/
    //--------------------------------------------------------------
    //
    public void init()
    {
        if(m_bInit == false)
        {
            if(m_SpCtrl == null)
                m_SpCtrl = new SharedPrefCtrl();

            m_bInit = true;
        }
    }

    //---------------------------------------------------------------------------------------------------
    // 메시지 박스 출력(Yes,No)
    public void ShowMessageBox(Context pContext, String strTitle, String strMessage, String strYes, String strNo, dlg_message_box.OnCallbackIF pCallback) {
        // Already Show..
        if (m_DlgMessageBox != null) {
            // Close Dialog
            m_DlgMessageBox.dismiss();
            m_DlgMessageBox = null;
        }

        // Create New Message Box Dialog
        m_DlgMessageBox = new dlg_message_box(pContext, strTitle, strMessage, strYes, strNo, pCallback);
        m_DlgMessageBox.show();
    }


}
