package com.kasirgalabs.tilki.client;

import com.kasirgalabs.tilki.utils.FileManager;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileListDropTarget extends DropTarget {

    public static final DataFlavor FLAVOR = DataFlavor.javaFileListFlavor;

    @Override
    public synchronized void drop(DropTargetDropEvent evt) {
        FileManager fileManager = FileManager.getInstance();
        evt.acceptDrop(DnDConstants.ACTION_COPY);
        Transferable transferable = evt.getTransferable();
        List<File> droppedFiles;
        try {
            droppedFiles = (List<File>) transferable.getTransferData(FLAVOR);
            fileManager.trackFiles(droppedFiles);
        }
        catch(UnsupportedFlavorException ex) {
        }
        catch(IOException ex) {
        }
    }
}
