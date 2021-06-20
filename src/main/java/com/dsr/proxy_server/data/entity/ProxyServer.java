package com.dsr.proxy_server.data.entity;

import com.dsr.proxy_server.data.enums.ProxyAnonymity;
import com.dsr.proxy_server.data.enums.ProxyProtocol;
import com.dsr.proxy_server.data.enums.YesNoAny;

import javax.persistence.*;
import java.util.Objects;

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

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProxyServer that = (ProxyServer) o;
        return Objects.equals(ip, that.ip) &&
                Objects.equals(port, that.port) &&
                Objects.equals(type, that.type) &&
                anonymity == that.anonymity &&
                Objects.equals(uptime, that.uptime) &&
                available == that.available &&
                Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port, type, anonymity, uptime, available, country);
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
                ", country=" + country +
                '}';
    }
}
