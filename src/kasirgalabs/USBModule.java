/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kasirgalabs;

import com.sun.jna.platform.win32.Advapi32Util;
import static com.sun.jna.platform.win32.WinReg.HKEY_CURRENT_USER;
import static com.sun.jna.platform.win32.WinReg.HKEY_LOCAL_MACHINE;


/**
 *
 * @author Kerim
 */
public class USBModule {   
    
    public USBModule() {
       
    }
    
    /**
     * This method should be called when the application starts.
     * This method will return if the application has admin rights
     * If application does not have admin rights, application should be stopped.
     * 
     * @return adminRights
     */    
    public static boolean checkAdminRights() {
        boolean adminRights = false;
        String groups[] = (new com.sun.security.auth.module.NTSystem()).getGroupIDs();
            for (String group : groups) {
                if (group.equals("S-1-5-32-544")){
                    adminRights = true;
                    return adminRights;
                }                    
            }
        return adminRights;
    }
    
    
    /**
     * This method will check if USB devices' mode.
     * If usb devices are disabled, then USB devices will be enabled with this method
     */
    public static void enableUSB(){
        int usbMode = Advapi32Util.registryGetIntValue(HKEY_LOCAL_MACHINE, "System\\CurrentControlSet\\Services\\USBSTOR", "Start");
        String message;
        
        if (usbMode == 3){
            message = "USB devices were already enabled";
        }
        else{
            usbMode = 3;
            Advapi32Util.registrySetIntValue(HKEY_LOCAL_MACHINE, "System\\CurrentControlSet\\Services\\USBSTOR", "Start", usbMode);
            message = "USB devices are enabled now";
        }
        
        System.out.println(message);        
    }

     /**
     * This method will check if USB devices' mode.
     * If usb devices are enabled, then USB devices will be disabled with this method
     */
    
    public static void disableUSB(){
        int usbMode = Advapi32Util.registryGetIntValue(HKEY_LOCAL_MACHINE, "System\\CurrentControlSet\\Services\\USBSTOR", "Start");
        String message;
        
        if (usbMode == 4){
            message = "USB devices were already disabled";
        }
        else{
            usbMode = 4;
            Advapi32Util.registrySetIntValue(HKEY_LOCAL_MACHINE, "System\\CurrentControlSet\\Services\\USBSTOR", "Start", usbMode);
            message = "USB devices are disabled now";
        }
        
        System.out.println(message);        
    }
}
