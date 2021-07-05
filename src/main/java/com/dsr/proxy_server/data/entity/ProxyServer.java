package com.dsr.proxy_server.data.entity;

import com.dsr.proxy_server.data.enums.ProxyAnonymity;
import com.dsr.proxy_server.data.enums.ProxyProtocol;
import com.dsr.proxy_server.data.enums.ProxySourceType;
import com.dsr.proxy_server.data.enums.YesNoAny;
import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * This class represents the entity of proxy server, that's stored in the database
 */
@Entity
@Table(name = "proxy_servers", schema = "public")
public class ProxyServer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer proxyServerId;
    private String ip;
    private Integer port;
    @Column(columnDefinition = "VARCHAR")
    private ProxyProtocol type;
    @Column(columnDefinition = "VARCHAR")
    private ProxyAnonymity anonymity;
    private Double uptime;
    @Column(columnDefinition = "VARCHAR")
    private YesNoAny available;
    @Column(columnDefinition = "DATE")
    private Date lastTimeCheck;
    @Column(columnDefinition = "VARCHAR")
    private ProxySourceType source;
    /**
     * This variable is used to store the number of calls for the current proxy server during the day
     * (it's nullified once a day by a thread)
     */
    private Integer workload;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    public ProxySourceType getSource() {
        return source;
    }

    public void setSource(ProxySourceType source) {
        this.source = source;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public ProxyProtocol getType() {
        return type;
    }

    public void setType(ProxyProtocol type) {
        this.type = type;
    }

    public ProxyAnonymity getAnonymity() {
        return anonymity;
    }

    public void setAnonymity(ProxyAnonymity anonymity) {
        this.anonymity = anonymity;
    }

    public Double getUptime() {
        return uptime;
    }

    public void setUptime(Double uptime) {
        this.uptime = uptime;
    }

    public YesNoAny getAvailable() {
        return available;
    }

    public void setAvailable(YesNoAny available) {
        this.available = available;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Date getLastTimeCheck() {
        return lastTimeCheck;
    }

    public void setLastTimeCheck(Date lastTimeCheck) {
        this.lastTimeCheck = lastTimeCheck;
    }

    public Integer getWorkload() {
        return workload;
    }

    public void setWorkload(Integer workload) {
        this.workload = workload;
    }

    public Integer getProxyServerId() {
        return proxyServerId;
    }

    public void setProxyServerId(Integer proxyServerId) {
        this.proxyServerId = proxyServerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProxyServer that = (ProxyServer) o;
        return Objects.equals(ip, that.ip) &&
                Objects.equals(port, that.port) &&
                type == that.type &&
                anonymity == that.anonymity &&
                Objects.equals(uptime, that.uptime) &&
                available == that.available &&
                Objects.equals(lastTimeCheck, that.lastTimeCheck) &&
                source == that.source &&
                Objects.equals(workload, that.workload) &&
                Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port, type, anonymity, uptime, available, lastTimeCheck, source, workload, country);
    }

    @Override
    public String toString() {
        return "ProxyServer{" +
                "proxyServerId=" + proxyServerId +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", type=" + type +
                ", anonymity=" + anonymity +
                ", uptime=" + uptime +
                ", available=" + available +
                ", lastTimeCheck=" + lastTimeCheck +
                ", source=" + source +
                ", workload=" + workload +
                ", country=" + country +
                '}';
    }
}
