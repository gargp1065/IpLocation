package com.gl.eirs.iplocation.service;

import com.gl.eirs.iplocation.dto.IpInformation;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;

import static com.gl.eirs.iplocation.constants.Constants.ipType4;

@Service
public class CheckIpLocationUtils {

    public String convertIp(IpInformation ipInformation) {
        String ipType = ipInformation.getIpType();
        if(ipType.equalsIgnoreCase(ipType4)) {
            return convertIpv4ToNumber("a.b.c.d");
        }
        return "";
    }

    public String convertIpv4ToNumber(String ip) {
        // ip address is of type a.b.c.d
        String[] ipParts = ip.trim().split("\\.", -1);
        long a = Long.parseLong(ipParts[0]), b = Long.parseLong(ipParts[1]),
                c = Long.parseLong(ipParts[2]), d = Long.parseLong(ipParts[3]);
        long ipNumber = 16777216*a + 65536*b + 256*c + d;
        return Long.toString(ipNumber);
    }

    public String numberToIp4(long ipNumber) {
        // ip address is of type a.b.c.d

        int w = (int) ((ipNumber/16777216) % 256 + 256)%256 ;
        int x = (int) ((ipNumber/65536) % 256 + 256)%256 ;
        int y = (int) ((ipNumber/256) % 256 + 256)%256 ;
        int z = (int) ((ipNumber)%256 + 256)%256;
        return w + "." + x + "." + y + "." + z;
    }

    public String convertIpv6ToNumber(String ip) {
        // break down the ip based on the separator.
        String[] ipParts = ip.trim().split(":", -1);
        // now each part is in hexa, covert to decimal.
        BigInteger ipNumber = BigInteger.valueOf(0);
        BigInteger base = BigInteger.valueOf(65536);
        for(int i=0;i<8;i++) {
            // now each part is in hexa, covert to decimal.
            BigInteger n = new BigInteger(ipParts[i], 16);
            BigInteger exponent = BigInteger.valueOf(7-i);
            BigInteger power = base.pow(exponent.intValue());
            BigInteger result = power.multiply(n);
            ipNumber = ipNumber.add(result);
        }
        return ipNumber.toString();
    }

        public String numberToIpv6(String ip) {
            ArrayList<String> list = new ArrayList<>();
            BigInteger ipNumber = new BigInteger(ip);
            BigInteger base = BigInteger.valueOf(65536);
            StringBuilder ans = new StringBuilder();
            for (int i = 7; i >= 0; i--) {
                BigInteger exponent = BigInteger.valueOf(i);
                BigInteger power = base.pow(exponent.intValue());
                BigInteger n = ipNumber.divide(power).mod(base).add(base).mod(base);
                String hexaN = Integer.toHexString(n.intValue());
                StringBuilder zeros = new StringBuilder();
                if(hexaN.length() < 4) {
                    int diff = 4 - hexaN.length();
                    System.out.println("Difference is = " + diff);
                    while(diff > 0) {
                        zeros.append('0');
                        diff--;
                    }
                }
                hexaN = zeros.toString() + hexaN;
                System.out.println("Value is = " + hexaN);
                if (i != 0) {
                    ans.append(hexaN).append(":");
                } else {
                    ans.append(hexaN);
                }
            }
            return ans.toString();
        }
}
