package watchdogagent.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import oshi.SystemInfo;
import oshi.hardware.ComputerSystem;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.util.FormatUtil;
import watchdogagent.util.RandomUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ran Zhang
 * @since 2024/4/2
 */
@Data
@Slf4j
public class MachineCode {
    private boolean isRandomGen;
    private String code;

    private boolean valid(String s) {
        return StringUtils.hasText(s) && !"unknown".equals(s) && !"None".equals(s);
    }
    protected String replaceBlank(String str){
        String dest = null;
        if(str == null){
            return dest;
        }else{
            Pattern p = Pattern.compile("\\s*|\t|\r|\n|");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
            return dest;
        }
    }

    public MachineCode() {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        ComputerSystem computerSystem = hal.getComputerSystem();
        String serialNUmber = replaceBlank(computerSystem.getSerialNumber());
        if (valid(serialNUmber)) {
            log.info("use computer serial number:{} as machine id", serialNUmber);
            this.isRandomGen = false;
            this.code = serialNUmber;
            return;
        }

        serialNUmber = replaceBlank(computerSystem.getBaseboard().getSerialNumber());
        if (valid(serialNUmber)) {
            log.info("use baseboard serial number:{} as machine id", serialNUmber);
            this.isRandomGen = false;
            this.code = serialNUmber;
            return;
        }
        String firmName = computerSystem.getFirmware().getName();
        String version = computerSystem.getFirmware().getVersion();
        if (StringUtils.hasText(firmName) && StringUtils.hasText(version)) {
            log.info("use firmware name:{} and version:{} to generate machine id", firmName, version);
            this.code = RandomUtil.md5String(firmName + ";" + version);
            this.isRandomGen = false;
            return;
        }
        serialNUmber = macsToCode(macAddress(hal));
        if (StringUtils.hasText(serialNUmber)) {
            log.info("use mac address to generate machine id");
            isRandomGen = false;
            code = serialNUmber;
            return;
        }
        serialNUmber = getLinuxMachineId();
        if (StringUtils.hasText(serialNUmber)) {
            log.info("use linux machine id :{}", serialNUmber);
            isRandomGen = false;
            code = serialNUmber;
        } else {
            isRandomGen = true;
            code = RandomUtil.generateString(32);
            log.info("random generate:{}", serialNUmber);
        }


    }

    private String macsToCode(List<String> macAddress) {
        macAddress.sort(Comparator.naturalOrder());
        return RandomUtil.md5String(String.join(";", macAddress));
    }


    private List<String> macAddress(HardwareAbstractionLayer hal) {
        NetworkIF[] networkIFs = hal.getNetworkIFs();
        List<String> macAddress = new ArrayList<>();
        for (NetworkIF net : networkIFs) {
            macAddress.add(net.getMacaddr());
        }
        return macAddress;
    }


    public String toUriParams() {
        return "?isRandomGen=" + (isRandomGen ? 1 : 0) + "&code=" + code;
    }

    private String getLinuxMachineId() {
        String command = "cat /etc/machine-id";
        // setting uuid to null first
        String uuid = null;
        try {
            Process SerNumProcess
                    = Runtime.getRuntime().exec(command);
            BufferedReader sNumReader
                    = new BufferedReader(new InputStreamReader(
                    SerNumProcess.getInputStream()));

            uuid = sNumReader.readLine().trim();
            SerNumProcess.waitFor();
            sNumReader.close();
        } catch (Exception ex) {
            uuid = null;
        }
        return uuid;
    }

}
