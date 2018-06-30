package com.midas.mydownloader.ui.act;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.midas.mydownloader.MyApp;
import com.midas.mydownloader.R;
import com.midas.mydownloader.common.Constant;
import com.midas.mydownloader.structure.core.content;
import com.midas.mydownloader.ui.dialog.dlg_message_box;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class ActMain extends AppCompatActivity
{

    /************************** Define **************************/
    public static String DB_PROVIDER = "com.midas.myfilelist.MyContentProvider";
    public static final int PROGRESS_TYPE = 0;

    /************************** Member **************************/
    public MyApp m_App = null;
    public Context m_Context = null;
    public Activity m_Activity = null;

    public content m_ContentInfo = null;
    public long m_nTotal = 0;
    public int m_nLenghtOfFile = 0;
    /************************** Controller **************************/
    // Progress Dialog
    private ProgressDialog m_ProgressDialog = null;
    private CheckBox m_cb_Service = null;//서비스 중지여부 설정
    /************************** System Fucntion **************************/
    //--------------------------------------------------------------------
    //
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        m_Context = this;
        m_Activity = this;
        m_App = new MyApp(m_Context);

        initValue();
        recvIntentData();
        setInitLayout();
    }

    //----------------------------------------------------------------------------------------------
    //권한변경에대한 callback
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case Constant.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)//획득완료.
                {
                    if(m_ContentInfo != null)
                        new DownloadFileFromURL().execute(m_ContentInfo);
                }
                else
                {
                    Toast.makeText(m_Context, "저장소 권한 획득이 필요합니다.", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                break;
        }
    }
    /************************** User Fucntion **************************/
    //--------------------------------------------------------------------
    //
    public void initValue()
    {

    }
    //--------------------------------------------------------------------
    //
    public void recvIntentData()
    {
        Intent pIntent = getIntent();
        if(pIntent == null)
            return;

        if (pIntent.hasExtra("title") && pIntent.hasExtra("url"))
        {
            String title = pIntent.getStringExtra("title");
            String url = pIntent.getStringExtra("url");

            content pInfo = new content(title, url);
            //show dialog
            showMessageBox(pInfo);
        }
    }
    //--------------------------------------------------------------------
    //
    public void setInitLayout()
    {
        m_cb_Service = (CheckBox)findViewById(R.id.cb_Service);



        //event..
        m_cb_Service.setOnCheckedChangeListener(onCheckService);

        settingView();
    }
    //--------------------------------------------------------------------
    //
    public void settingView()
    {
        getStatusDataFromAnotherApp();
    }
    //--------------------------------------------------------------------
    //
    public void showMessageBox(final content pInfo)
    {
        m_App.ShowMessageBox(m_Context,
                pInfo.title, m_Context.getResources().getString(R.string.str_download_msg),
                m_Context.getResources().getString(R.string.str_dlg_ok),
                m_Context.getResources().getString(R.string.str_dlg_cancel),
                new dlg_message_box.OnCallbackIF()
        {
            @Override
            public void OnYes(Dialog pDialog, String strSaveLocation)
            {
                if(pDialog == null)
                    return;

                if(strSaveLocation != null)
                    pInfo.save_location = strSaveLocation;
                else
                    pInfo.save_location = content.SAVE_TYPE_INTERNAL;//default..

                getPermission(pInfo);
                pDialog.dismiss();
            }

            @Override
            public void OnNo(Dialog pDialog)
            {

                if(pDialog == null)
                    return;

                pDialog.dismiss();
            }

            @Override
            public void OnCancel(Dialog pDialog)
            {
                if(pDialog == null)
                    return;

                pDialog.dismiss();
            }

            @Override
            public void OnClose(Dialog pDialog)
            {
                if (pDialog == null)
                    return;

                pDialog.dismiss();
            }
        });
    }

    //----------------------------------------------------------------------------------------------
    //
    public void getPermission(content pInfo)
    {
        if(pInfo != null)
            m_ContentInfo = pInfo;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)//6.0(23)이상 권한설정필요
        {
            int nReadExternalStorage = ContextCompat.checkSelfPermission(m_Activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(nReadExternalStorage == PackageManager.PERMISSION_GRANTED)//
            {
                new DownloadFileFromURL().execute(m_ContentInfo);
            }
            else
            {
                ActivityCompat.requestPermissions(m_Activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
        else//22이하..
        {

        }
    }

    //--------------------------------------------------------------------
    //다운로드 중인 content 정보를 A앱의 sqlite db 에 저장
    public void setDownloadContent()
    {

    }

    //--------------------------------------------------------------------
    //Showing Dialog
    @Override
    protected Dialog onCreateDialog(int id)
    {
        switch (id)
        {
            case PROGRESS_TYPE: //
                m_ProgressDialog = new ProgressDialog(this);
                m_ProgressDialog.setMessage("다운로드 중..");
                m_ProgressDialog.setIndeterminate(false);
                m_ProgressDialog.setMax(100);
                m_ProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                m_ProgressDialog.setCancelable(true);
                m_ProgressDialog.show();
                return m_ProgressDialog;
            default:
                return null;
        }
    }

    //--------------------------------------------------------------------
    //Background Async Task to download file
    class DownloadFileFromURL extends AsyncTask<content, String, String>
    {
        /**
         * Before starting background thread Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            showDialog(PROGRESS_TYPE);
        }

        @Override
        protected String doInBackground(content... params)
        {
            int count;
            try
            {
                URL url = new URL(params[0].url);
                URLConnection pConn = url.openConnection();
                pConn.connect();

                final int nOriginSize = pConn.getContentLength();
                m_nLenghtOfFile = pConn.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream
                OutputStream output = null;
                if(params[0].save_location.equals(content.SAVE_TYPE_EXTERNAL))
                    output = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + "/" +params[0].title);//
                else
                    output = new FileOutputStream(m_Context.getFilesDir()+"/"+params[0].title);//

                byte data[] = new byte[1024];
                m_nTotal = 0;

                while (((count = input.read(data)) != -1) && !Thread.interrupted())
                {
                    m_nTotal += count;

                    publishProgress("" + (int) ((m_nTotal * 100) / m_nLenghtOfFile));
                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return null;
        }


        protected void onProgressUpdate(final String... progress)
        {
            m_ProgressDialog.setProgress(Integer.parseInt(progress[0]));

            //setDatabase
            //String[] pParams = {"title param", "url param", "status param", "cursize param", "full isze param"};
            //getContentResolver().query(Uri.parse("content://"+DB_PROVIDER+"/set_content"), pParams, null, null, null);
        }

        @Override
        protected void onPostExecute(String file_url)
        {
            // dismiss the dialog after the file was downloaded
            dismissDialog(PROGRESS_TYPE);
        }
    }
    //--------------------------------------------------------------------
    //FileListApp으로 부터 status값 수신
    public void getStatusDataFromAnotherApp()
    {
        Cursor pCursor = null;
        pCursor = getContentResolver().query(Uri.parse("content://"+DB_PROVIDER+"/get_status"), null, null, null, null);
        String strStatus = "";
        if(pCursor != null)
        {
            while(pCursor.moveToNext())
            {
                strStatus += pCursor.getString(0);
            }
        }


        if(strStatus == null)
            return;

        if(strStatus.contains("Y"))
            m_cb_Service.setChecked(true);
        else
            m_cb_Service.setChecked(false);
    }

    //--------------------------------------------------------------------
    //
    public void setStatusData(boolean bValue)
    {
        Cursor pCursor = null;
        if(bValue)
        {
            pCursor = getContentResolver().query(Uri.parse("content://"+DB_PROVIDER+"/set_status"), null, "Y", null, null);
        }
        else
        {
            pCursor = getContentResolver().query(Uri.parse("content://"+DB_PROVIDER+"/set_status"), null, "N", null, null);
        }

        if(pCursor == null)
            return;

        pCursor.close();
    }
    /************************ listener ************************/
    //--------------------------------------------------------------------
    //
    CompoundButton.OnCheckedChangeListener onCheckService = new CompoundButton.OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            setStatusData(isChecked);
        }
    };
}
