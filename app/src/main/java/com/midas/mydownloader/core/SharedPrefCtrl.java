package com.midas.mydownloader.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Set;

public class SharedPrefCtrl
{
    /************************* Define *************************/

    /************************* Member *************************/
    private SharedPreferences m_Preference = null;
    /************************* UserFunction *************************/
    //------------------------------------------------------------------------
    //
    public SharedPrefCtrl()
    {

    }

    //------------------------------------------------------------------------
    //
    public void Uninit()
    {
        if (m_Preference != null)
        {
            m_Preference = null;
        }
    }

    //------------------------------------------------------------------------
    //
    public boolean Init(Context context)
    {
        m_Preference = PreferenceManager.getDefaultSharedPreferences(context);
        if (m_Preference == null)
        {
            return false;
        }

        return true;
    }
    //------------------------------------------------------------------------
    //
    public void clearData(Context context)
    {
        m_Preference = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = m_Preference.edit();
        editor.clear();
        editor.commit();
    }

    /*********************************  Function *********************************/
    //------------------------------------------------------------------------
    //String형
    private void SavePreferenceStr(String strKey, String strValue)
    {
        SharedPreferences.Editor editor = m_Preference.edit();
        editor.putString(strKey, strValue);
        editor.commit();
    }

    private String LoadPreferenceStr(String strKey, String strDefault)
    {
        if (m_Preference == null)
            return null;

        return m_Preference.getString(strKey, strDefault);
    }

    //------------------------------------------------------------------------
    //int 형
    private void SavePreferenceInt(String strKey, int nValue)
    {
        SharedPreferences.Editor editor = m_Preference.edit();
        editor.putInt(strKey, nValue);
        editor.commit();
    }

    private int LoadPreferenceInt(String strKey, int nDefault)
    {
        if (m_Preference == null)
            return nDefault;

        return m_Preference.getInt(strKey, nDefault);
    }

    //------------------------------------------------------------------------
    //long 형
    private void SavePreferenceLong(String strKey, long nValue)
    {
        SharedPreferences.Editor editor = m_Preference.edit();
        editor.putLong(strKey, nValue);
        editor.commit();
    }

    private long LoadPreferenceLong(String strKey, long nDefault)
    {
        if (m_Preference == null)
            return nDefault;

        return m_Preference.getLong(strKey, nDefault);
    }

    //------------------------------------------------------------------------
    //float형
    private void SavePreferenceFloat(String strKey, float nValue)
    {
        SharedPreferences.Editor editor = m_Preference.edit();
        editor.putFloat(strKey, nValue);
        editor.commit();
    }

    private float LoadPreferenceFolat(String strKey, float nDefault)
    {
        if (m_Preference == null)
            return nDefault;

        return m_Preference.getFloat(strKey, nDefault);
    }

    //------------------------------------------------------------------------
    //bool형
    private void SavePreferenceBoolean(String strKey, boolean bDefault)
    {
        SharedPreferences.Editor editor = m_Preference.edit();
        editor.putBoolean(strKey, bDefault);
        editor.commit();
    }

    private boolean LoadPreferenceBoolean(String strKey, boolean bDefault)
    {
        if (m_Preference == null)
            return bDefault;

        return m_Preference.getBoolean(strKey, bDefault);
    }

    //------------------------------------------------------------------------
    //set형
    private void SavePreferenceSet(String strKey, Set<String> setDefault)
    {
        SharedPreferences.Editor editor = m_Preference.edit();
        editor.remove(strKey);//삭제하고
        editor.commit();

        editor.putStringSet(strKey, setDefault);//새로생성, 이렇게 해야 SET갱신이 됨..
        editor.commit();
    }

    private Set<String> LoadPreferenceSet(String strKey, Set<String> setDefault)
    {
        if (m_Preference == null)
            return setDefault;

        return m_Preference.getStringSet(strKey, setDefault);
    }
}
