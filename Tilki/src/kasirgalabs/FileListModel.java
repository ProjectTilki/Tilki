package kasirgalabs;

import java.io.File;
import java.util.ArrayList;
import javax.swing.DefaultListModel;

public class FileListModel<E> extends DefaultListModel<E> {
    private String errorMessage;
    private final ArrayList<File> list = new ArrayList<File>();
    
    @Override
    public void addElement(E element) {
        File fileElement = new File((String)element);
        if(fileElement.isDirectory()) {
            errorMessage = "Klas\u00F6r y\u00FCkleyemezsiniz.";
            return;
        }
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).getAbsolutePath().equals(fileElement.getAbsolutePath())) {
                return;
            }
            if(fileElement.getName().equals(list.get(i).getName())) {
                errorMessage = "\"" + fileElement.getName() + "\" isimli bir dosya zaten mevcut.";
                return;
            }
        }
        list.add(fileElement);
        super.addElement(element);
    }
    
    @Override
    public boolean removeElement(Object element) {
        File fileElement = new File((String)element);
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).getAbsolutePath().equals(fileElement.getAbsolutePath())) {
                list.remove(i);
            }
        }
        return super.removeElement(element);
    }
}
