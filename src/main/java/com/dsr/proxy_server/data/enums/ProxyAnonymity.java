package com.dsr.proxy_server.data.enums;

public enum  ProxyAnonymity {
    None,
    Low,
    Medium,
    High,
    HighKeepAlive,
    All,
    Unknown;

    /**
     * This class estimates value of the given ProxyAnonymity entity based on the level of anonymity
     * @param proxyAnonymity entity that needs to be estimated
     * @return level of anonymity (the higher the better)
     */
    public static Integer getAnonimityLevel(ProxyAnonymity proxyAnonymity){
        switch (proxyAnonymity){
            case Unknown:
                return 0;
            case None:
                return 1;
            case Low:
                return 2;
            case All:
                return 3;
            case Medium:
                return 4;
            case High:
                return 5;
            case HighKeepAlive:
                return 6;
        }
        return -1;
    }
}
