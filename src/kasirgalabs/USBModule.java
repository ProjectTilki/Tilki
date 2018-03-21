package kasirgalabs;


import com.sun.jna.Native;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Shell32;
import static com.sun.jna.platform.win32.WinReg.HKEY_CURRENT_USER;
import static com.sun.jna.platform.win32.WinReg.HKEY_LOCAL_MACHINE;

/**
 *
 * @author Kerim
 */



public class USBModule {   

interface MyShell32 extends Shell32{
	boolean IsUserAnAdmin();
}

    
    public USBModule() {
       
    }
    
    static MyShell32 myShell32 = (MyShell32) Native.loadLibrary("shell32",MyShell32.class);

    
    /**
     * This method should be called when the application starts.
     * This method will return if the application has admin rights
     * If application does not have admin rights, application should be stopped.
     * 
     * @return adminRights
     */    
    public static boolean checkAdminRights() {
        return myShell32.IsUserAnAdmin();
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
