package org.apg;

import org.spongycastle.bcpg.HashAlgorithmTags;
import org.spongycastle.openpgp.PGPEncryptedData;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Vector;

public class Preferences {
    private static Preferences mPreferences;
    private SharedPreferences mSharedPreferences;

    public static synchronized Preferences getPreferences(Context context) {
        return getPreferences(context, false);
    }

    public static synchronized Preferences getPreferences(Context context, boolean force_new) {
        if (mPreferences == null || force_new) {
            mPreferences = new Preferences(context);
        }
        return mPreferences;
    }

    private Preferences(Context context) {
        mSharedPreferences = context.getSharedPreferences("APG.main", Context.MODE_PRIVATE);
    }

    public String getLanguage() {
        return mSharedPreferences.getString(Constants.pref.LANGUAGE, "");
    }

    public void setLanguage(String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.pref.LANGUAGE, value);
        editor.commit();
    }

    public int getPassPhraseCacheTtl() {
        int ttl = mSharedPreferences.getInt(Constants.pref.PASS_PHRASE_CACHE_TTL, 180);
        // fix the value if it was set to "never" in previous versions, which currently is not
        // supported
        if (ttl == 0) {
            ttl = 180;
        }
        return ttl;
    }

    public void setPassPhraseCacheTtl(int value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(Constants.pref.PASS_PHRASE_CACHE_TTL, value);
        editor.commit();
    }

    public int getDefaultEncryptionAlgorithm() {
        return mSharedPreferences.getInt(Constants.pref.DEFAULT_ENCRYPTION_ALGORITHM,
                PGPEncryptedData.AES_256);
    }

    public void setDefaultEncryptionAlgorithm(int value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(Constants.pref.DEFAULT_ENCRYPTION_ALGORITHM, value);
        editor.commit();
    }

    public int getDefaultHashAlgorithm() {
        return mSharedPreferences.getInt(Constants.pref.DEFAULT_HASH_ALGORITHM,
                HashAlgorithmTags.SHA256);
    }

    public void setDefaultHashAlgorithm(int value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(Constants.pref.DEFAULT_HASH_ALGORITHM, value);
        editor.commit();
    }

    public int getDefaultMessageCompression() {
        return mSharedPreferences.getInt(Constants.pref.DEFAULT_MESSAGE_COMPRESSION,
                Id.choice.compression.zlib);
    }

    public void setDefaultMessageCompression(int value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(Constants.pref.DEFAULT_MESSAGE_COMPRESSION, value);
        editor.commit();
    }

    public int getDefaultFileCompression() {
        return mSharedPreferences.getInt(Constants.pref.DEFAULT_FILE_COMPRESSION,
                Id.choice.compression.none);
    }

    public void setDefaultFileCompression(int value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(Constants.pref.DEFAULT_FILE_COMPRESSION, value);
        editor.commit();
    }

    public boolean getDefaultAsciiArmour() {
        return mSharedPreferences.getBoolean(Constants.pref.DEFAULT_ASCII_ARMOUR, false);
    }

    public void setDefaultAsciiArmour(boolean value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(Constants.pref.DEFAULT_ASCII_ARMOUR, value);
        editor.commit();
    }

    public boolean getForceV3Signatures() {
        return mSharedPreferences.getBoolean(Constants.pref.FORCE_V3_SIGNATURES, false);
    }

    public void setForceV3Signatures(boolean value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(Constants.pref.FORCE_V3_SIGNATURES, value);
        editor.commit();
    }

    public boolean hasSeenChangeLog(String version) {
        return mSharedPreferences.getBoolean(Constants.pref.HAS_SEEN_CHANGE_LOG + version, false);
    }

    public void setHasSeenChangeLog(String version, boolean value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(Constants.pref.HAS_SEEN_CHANGE_LOG + version, value);
        editor.commit();
    }

    public boolean hasSeenHelp() {
        return mSharedPreferences.getBoolean(Constants.pref.HAS_SEEN_HELP, false);
    }

    public void setHasSeenHelp(boolean value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(Constants.pref.HAS_SEEN_HELP, value);
        editor.commit();
    }

    public String[] getKeyServers() {
        String rawData = mSharedPreferences.getString(Constants.pref.KEY_SERVERS,
                Constants.defaults.KEY_SERVERS);
        Vector<String> servers = new Vector<String>();
        String chunks[] = rawData.split(",");
        for (int i = 0; i < chunks.length; ++i) {
            String tmp = chunks[i].trim();
            if (tmp.length() > 0) {
                servers.add(tmp);
            }
        }
        return servers.toArray(chunks);
    }

    public void setKeyServers(String[] value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        String rawData = "";
        for (int i = 0; i < value.length; ++i) {
            String tmp = value[i].trim();
            if (tmp.length() == 0) {
                continue;
            }
            if (!"".equals(rawData)) {
                rawData += ",";
            }
            rawData += tmp;
        }
        editor.putString(Constants.pref.KEY_SERVERS, rawData);
        editor.commit();
    }
}
