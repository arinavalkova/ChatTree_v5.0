package main;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.net.InetSocketAddress;

public class ArgsParser {
    @Parameter(names = { "-hn", "-hostName" }, description = "mainFunctional.host.Host name")
    private String hostName = Invariables.DEFAULT_HOSTNAME;

    @Parameter(names = { "-hp", "-hostPort" }, description = "Port")
    private Integer hostPort = Invariables.DEFAULT_HOST_PORT;

    @Parameter(names = { "-l", "-loss" }, description = "Loss percentage")
    private Integer loss = Invariables.DEFAULT_LOSS;

    @Parameter(names = { "-pip", "parentIp" }, description = "Parent ip")
    private String parentIp;

    @Parameter(names = { "-pp", "-parentPort" }, description = "Port")
    private Integer parentPort;

    public ArgsParser(String[] args) {
        JCommander.newBuilder()
                .addObject(this)
                .build()
                .parse(args);
    }

    public String getHostName() {
        return hostName;
    }

    public Integer getLoss() {
        return loss;
    }

    public InetSocketAddress getAddress() {
        return new InetSocketAddress(Invariables.DEFAULT_PARENT_IP, hostPort);
    }

    public InetSocketAddress getParentAddress() {
        if(parentIp != null && parentPort != null)
            return new InetSocketAddress(parentIp, parentPort);
        return null;
    }
}