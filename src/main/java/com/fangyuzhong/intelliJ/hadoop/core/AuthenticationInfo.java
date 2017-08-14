package com.fangyuzhong.intelliJ.hadoop.core;

import com.fangyuzhong.intelliJ.hadoop.core.util.CommonUtil;
import com.fangyuzhong.intelliJ.hadoop.core.util.StringUtil;
import com.fangyuzhong.intelliJ.hadoop.core.util.TimeUtil;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public class AuthenticationInfo
        implements com.fangyuzhong.intelliJ.hadoop.core.util.Cloneable<AuthenticationInfo>
{
    private long timestamp = System.currentTimeMillis();
    private boolean osAuthentication;
    private boolean emptyPassword;
    private boolean supported = true;
    private String user;
    private String password;

    public String getUser()
    {
        return this.user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public String getPassword()
    {
        return this.password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public boolean isOsAuthentication()
    {
        return this.osAuthentication;
    }

    public void setOsAuthentication(boolean osAuthentication)
    {
        this.osAuthentication = osAuthentication;
    }

    public boolean isSupported()
    {
        return this.supported;
    }

    public void setSupported(boolean supported)
    {
        this.supported = supported;
    }

    public boolean isEmptyPassword()
    {
        return this.emptyPassword;
    }

    public void setEmptyPassword(boolean emptyPassword)
    {
        this.emptyPassword = emptyPassword;
    }

    public boolean isProvided()
    {
        return (!this.supported) || (this.osAuthentication) || ((StringUtil.isNotEmpty(this.user)) && ((StringUtil.isNotEmpty(this.password)) || (this.emptyPassword)));
    }

    public boolean isOlderThan(long millis)
    {
        return TimeUtil.isOlderThan(this.timestamp, millis);
    }

    public boolean isSame(AuthenticationInfo authenticationInfo)
    {
        return (this.osAuthentication == authenticationInfo.osAuthentication) && (CommonUtil.safeEqual(this.user, authenticationInfo.user)) && (CommonUtil.safeEqual(this.password, authenticationInfo.password));
    }

    public AuthenticationInfo clone()
    {
        AuthenticationInfo authenticationInfo = new AuthenticationInfo();
        authenticationInfo.user = this.user;
        authenticationInfo.password = this.password;
        authenticationInfo.osAuthentication = this.osAuthentication;
        authenticationInfo.emptyPassword = this.emptyPassword;
        authenticationInfo.supported = this.supported;
        return authenticationInfo;
    }
}

