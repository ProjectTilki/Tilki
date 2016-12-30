package kasirgalabs;

import java.io.File;
import java.util.ArrayList;
import javax.swing.DefaultListModel;

public class FileListModel extends DefaultListModel {

    private String errorMessage = "";
    private final ArrayList<File> list = new ArrayList<File>();

    @Override
    public void addElement(Object element) {
        File fileElement = new File((String) element);
        if(fileElement.isDirectory()) {
            this.setErrorMessage("Klas\u00F6r y\u00FCkleyemezsiniz.");
            return;
        }
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).getAbsolutePath().equals(
                    fileElement.getAbsolutePath())) {
                return;
            }
            if(fileElement.getName().equals(list.get(i).getName())) {
                this.setErrorMessage(
                        "\"" + fileElement.getName() + "\" isimli bir dosya zaten mevcut.");
                return;
            }
        }
        list.add(fileElement);
        super.addElement(element);
    }

    @Override
    public boolean removeElement(Object element) {
        File fileElement = new File((String) element);
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).getAbsolutePath().equals(
                    fileElement.getAbsolutePath())) {
                list.remove(i);
            }
        }
        return super.removeElement(element);
    }

    public boolean areAllFilesExists() {
        boolean fileIsNotMissing = true;
        for(int i = 0; i < list.size(); i++) {
            if(!list.get(i).exists()) {
                this.removeElement(list.get(i).getAbsolutePath());
                this.setErrorMessage(
                        "Se\u00E7ti\u011Finiz dosyalardan biri bulunamadi!");
                fileIsNotMissing = false;
            }
        }
        return fileIsNotMissing;
    }

    @Override
    public boolean isEmpty() {
        if(list.isEmpty()) {
            this.setErrorMessage("Hi\u00E7bir dosya se\u00E7mediniz!");
            return true;
        }
        return false;
    }

    private void setErrorMessage(String errorMessage) {
        this.errorMessage = "<html><font color='red'>";
        this.errorMessage += errorMessage;
        this.errorMessage += "</font><html>";
    }

    public String getErrorMessage() {
        String temp = errorMessage;
        errorMessage = "";
        return temp;
    }
}
