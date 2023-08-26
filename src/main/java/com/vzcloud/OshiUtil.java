package com.vzcloud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vzcloud.entity.*;

import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @version v3.3
 * @ClassName: OshiUtil
 * @author: http://www.wgstart.com
 * @date: November 19, 2021
 * @Description: Oshi tool class
 * @Copyright: 2017-2022 wgcloud. All rights reserved.
 */
public class OshiUtil {

    private static Logger logger = LoggerFactory.getLogger(OshiUtil.class);

    private static CommonConfig commonConfig = (CommonConfig) ApplicationContextHelper.getBean(CommonConfig.class);

    private static Runtime r = Runtime.getRuntime();


    /**
     * Get memory use information
     *
     * @return
     */
    public static MemState memory(GlobalMemory memory) throws Exception {
        MemState memState = new MemState();
        long total = memory.getTotal() / 1024L / 1024L;
        long free = memory.getAvailable() / 1024L / 1024L;
        double usePer = (double) (total - free) / (double) total;
        memState.setUsePer(FormatUtil.formatDouble(usePer * 100, 1));
        memState.setHostname(commonConfig.getBindIp());
        return memState;
    }


    /**
     * Get the CPU usage, wait rate, free rate
     *
     * @return
     * @throws Exception
     */
    public static CpuState cpu(CentralProcessor processor) throws Exception {

        long[] prevTicks = processor.getSystemCpuLoadTicks();
        // Wait a second...
        Util.sleep(1000);

        CpuState cpuState = new CpuState();
        cpuState.setSys(FormatUtil.formatDouble(processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100, 1));
        cpuState.setHostname(commonConfig.getBindIp());
        return cpuState;
    }

    /**
     * Get the operating system information
     *
     * @return
     * @throws Exception
     */
    public static com.vzcloud.entity.SystemInfo os(CentralProcessor processor, OperatingSystem os) throws Exception {
        com.vzcloud.entity.SystemInfo systemInfo = new com.vzcloud.entity.SystemInfo();
        systemInfo.setHostname(commonConfig.getBindIp());
        systemInfo.setCpuCoreNum(processor.getLogicalProcessorCount() + "");
        String cpuInfo = processor.toString();
        if (cpuInfo.indexOf("\n") > 0) {
            cpuInfo = cpuInfo.substring(0, cpuInfo.indexOf("\n"));
        }
        systemInfo.setCpuXh(cpuInfo);
        systemInfo.setVersion(os.toString());
        systemInfo.setVersionDetail(os.toString());
        systemInfo.setState("1");
        return systemInfo;
    }

    /**
     * Get disk usage information
     *
     * @throws Exception
     */
    public static List<DeskState> file(Timestamp t, FileSystem fileSystem) throws Exception {

        List<DeskState> list = new ArrayList<DeskState>();
        List<OSFileStore> fsArray = fileSystem.getFileStores();
        for (OSFileStore fs : fsArray) {
            long usable = fs.getUsableSpace();
            long total = fs.getTotalSpace();

            DeskState deskState = new DeskState();
            deskState.setFileSystem(fs.getName());
            deskState.setHostname(commonConfig.getBindIp());
            deskState.setUsed(((total - usable) / 1024 / 1024 / 1024) + "G");
            deskState.setAvail((usable / 1024 / 1024 / 1024) + "G");
            deskState.setSize((total / 1024 / 1024 / 1024) + "G");
            double usedSize = (total - usable);
            double usePercent = 0;
            if (total > 0) {
                usePercent = FormatUtil.formatDouble(usedSize / total * 100D, 2);
            }
            deskState.setUsePer(usePercent + "%");
            deskState.setCreateTime(t);
            list.add(deskState);
        }

        return list;
    }

    /**
     * Get the system load
     *
     * @return
     */
    public static SysLoadState getLoadState(com.vzcloud.entity.SystemInfo systemInfo, CentralProcessor processor) throws Exception {
        SysLoadState sysLoadState = new SysLoadState();
        if (systemInfo == null) {
            return null;
        }
        if (systemInfo.getVersionDetail().indexOf("Microsoft") > -1) {
            //Windows system does not support load indicators
            return null;
        }
        double[] loadAverage = processor.getSystemLoadAverage(3);

        sysLoadState.setOneLoad(loadAverage[0]);
        sysLoadState.setHostname(commonConfig.getBindIp());
        sysLoadState.setFiveLoad(loadAverage[1]);
        sysLoadState.setFifteenLoad(loadAverage[2]);
        return sysLoadState;
    }


    /**
     * Get process information
     *
     * @return
     */
    public static AppState getLoadPid(String pid, OperatingSystem os, GlobalMemory memory) throws Exception {

        try {
            List<Integer> pidList = new ArrayList<>();
            pidList.add(Integer.valueOf(pid));
            List<OSProcess> procs = os.getProcesses(pidList);

            for (int i = 0; i < procs.size() && i < 5; i++) {
                OSProcess p = procs.get(i);

                AppState appState = new AppState();
                appState.setCpuPer(FormatUtil.formatDouble(100d * (p.getKernelTime() + p.getUserTime()) / p.getUpTime(), 2));
                appState.setMemPer(FormatUtil.formatDouble(100d * p.getResidentSetSize() / memory.getTotal(), 2));
                return appState;
            }

        } catch (Exception e) {
            logger.error("Get process information error", e);
        }

        return null;
    }


    /**
     * Get network bandwidth
     *
     * @param hal
     * @return
     * @throws Exception
     */
    public static NetIoState net(HardwareAbstractionLayer hal) throws Exception {
        long rxBytesBegin = 0;
        long txBytesBegin = 0;
        long rxPacketsBegin = 0;
        long txPacketsBegin = 0;
        long rxBytesEnd = 0;
        long txBytesEnd = 0;
        long rxPacketsEnd = 0;
        long txPacketsEnd = 0;
        List<NetworkIF> listBegin = hal.getNetworkIFs();
        for (NetworkIF net : listBegin) {
            rxBytesBegin += net.getBytesRecv();
            txBytesBegin += net.getBytesSent();
            rxPacketsBegin += net.getPacketsRecv();
            txPacketsBegin += net.getPacketsSent();
        }

        //Pay for 3 seconds
        Thread.sleep(5000);
        List<NetworkIF> listEnd = hal.getNetworkIFs();
        for (NetworkIF net : listEnd) {
            rxBytesEnd += net.getBytesRecv();
            txBytesEnd += net.getBytesSent();
            rxPacketsEnd += net.getPacketsRecv();
            txPacketsEnd += net.getPacketsSent();
        }

        long rxBytesAvg = (rxBytesEnd - rxBytesBegin) / 3 / 1024;
        long txBytesAvg = (txBytesEnd - txBytesBegin) / 3 / 1024;
        long rxPacketsAvg = (rxPacketsEnd - rxPacketsBegin) / 3 / 1024;
        long txPacketsAvg = (txPacketsEnd - txPacketsBegin) / 3 / 1024;
        NetIoState netIoState = new NetIoState();
        netIoState.setRxbyt(rxBytesAvg + "");
        netIoState.setTxbyt(txBytesAvg + "");
        netIoState.setRxpck(rxPacketsAvg + "");
        netIoState.setTxpck(txPacketsAvg + "");
        netIoState.setHostname(commonConfig.getBindIp());
        return netIoState;
    }
}
