/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.style.background.image;

import java.awt.Component;
import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.fr.base.BaseUtils;
import com.fr.general.Inter;
import com.fr.design.style.ChooseFileView;


/**
 * This class used to choose image files.
 */
public class ImageFileChooser extends JFileChooser {

    public ImageFileChooser() {
        ExampleFileFilter bothFilter = new ExampleFileFilter(
                new String[]{"jpg", "gif", "png", "bmp"},
                Inter.getLocText("Image-Image_Files"));
        bothFilter.setExtensionListInDescription(true);
        this.addChoosableFileFilter(bothFilter);
        this.setAcceptAllFileFilterUsed(false);

        // Create Custom FileView
        ChooseFileView fileView = new ChooseFileView();
        fileView.putIcon("jpg", BaseUtils.readIcon("/com/fr/base/images/dialog/file/jpgFile.gif"));
        fileView.putIcon("gif", BaseUtils.readIcon("/com/fr/base/images/dialog/file/gifFile.gif"));
        fileView.putIcon("png", BaseUtils.readIcon("/com/fr/base/images/dialog/file/pngFile.png"));
        fileView.putIcon("bmp", BaseUtils.readIcon("/com/fr/base/images/dialog/file/bmpFile.gif"));

        this.setFileView(fileView);
    }

    public int showDialog(Component parent, String approveButtonText) {
        return super.showDialog(parent, approveButtonText);
    }


    /**
     * A convenience implementation of FileFilter that filters out
     * all files except for those type extensions that it knows about.
     * <p/>D:\finereport\develop\code\test\TestCase\WEB-INF\reportlets\TestCase\01903.cpt

     * Extensions are of the type ".foo", which is typically found on
     * Windows and Unix boxes, but not on Macinthosh. Case is ignored.
     * <p/>
     * Example - create a new filter that filerts out all files
     * but gif and jpg image files:
     * <p/>
     * JFileChooser chooser = new JFileChooser();
     * ExampleFileFilter filter = new ExampleFileFilter(
     * new String{"gif", "jpg"}, "JPEG & GIF Images")
     * chooser.addChoosableFileFilter(filter);
     * chooser.showOpenDialog(this);
     *
     * @author Jeff Dinkins
     * @version 1.12 12/03/01
     */
    class ExampleFileFilter extends FileFilter {
        private Hashtable filters = null;
        private String description = null;
        private String fullDescription = null;
        private boolean useExtensionsInDescription = true;

        /**
         * Creates a file filter. If no filters are added, then all
         * files are accepted.
         *
         * @see #addExtension
         */
        public ExampleFileFilter() {
            this.filters = new Hashtable();
        }

        /**
         * Creates a file filter that accepts files with the given extension.
         * Example: new ExampleFileFilter("jpg");
         *
         * @see #addExtension
         */
        public ExampleFileFilter(String extension) {
            this(extension, null);
        }

        /**
         * Creates a file filter that accepts the given file type.
         * Example: new ExampleFileFilter("jpg", "JPEG Image Images");
         * <p/>
         * Note that the "." before the extension is not needed. If
         * provided, it will be ignored.
         *
         * @see #addExtension
         */
        public ExampleFileFilter(String extension, String description) {
            this();
            if (extension != null) addExtension(extension);
            if (description != null) setDescription(description);
        }

        /**
         * Creates a file filter from the given string array.
         * Example: new ExampleFileFilter(String {"gif", "jpg"});
         * <p/>
         * Note that the "." before the extension is not needed adn
         * will be ignored.
         *
         * @see #addExtension
         */
        public ExampleFileFilter(String[] filters) {
            this(filters, null);
        }

        /**
         * Creates a file filter from the given string array and description.
         * Example: new ExampleFileFilter(String {"gif", "jpg"}, "Gif and JPG Images");
         * <p/>
         * Note that the "." before the extension is not needed and will be ignored.
         *
         * @see #addExtension
         */
        public ExampleFileFilter(String[] filters, String description) {
            this();
            for (int i = 0; i < filters.length; i++) {
                // add filters one by one
                addExtension(filters[i]);
            }
            if (description != null) setDescription(description);
        }

        /**
         * Return true if this file should be shown in the directory pane,
         * false if it shouldn't.
         * <p/>
         * Files that begin with "." are ignored.
         *
         * @see #getExtension
         */
        public boolean accept(File f) {
            if (f != null) {
                if (f.isDirectory()) {
                    return true;
                }
                String extension = getExtension(f);
                if (extension != null && filters.get(getExtension(f)) != null) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Return the extension portion of the file's name .
         *
         * @see #getExtension
         * @see javax.swing.filechooser.FileFilter#accept
         */
        public String getExtension(File f) {
            if (f != null) {
                String filename = f.getName();
                int i = filename.lastIndexOf('.');
                if (i > 0 && i < filename.length() - 1) {
                    return filename.substring(i + 1).toLowerCase();
                }
            }
            return null;
        }

        /**
         * Adds a filetype "dot" extension to filter against.
         * <p/>
         * For example: the following code will create a filter that filters
         * out all files except those that end in ".jpg" and ".tif":
         * <p/>
         * ExampleFileFilter filter = new ExampleFileFilter();
         * filter.addExtension("jpg");
         * filter.addExtension("tif");
         * <p/>
         * Note that the "." before the extension is not needed and will be ignored.
         */
        public void addExtension(String extension) {
            if (filters == null) {
                filters = new Hashtable(5);
            }
            filters.put(extension.toLowerCase(), this);
            fullDescription = null;
        }


        /**
         * Returns the human readable description of this filter. For
         * example: "JPEG and GIF Image Files (*.jpg, *.gif)"
         *
         * @see javax.swing.filechooser.FileFilter#getDescription
         */
        public String getDescription() {
            if (fullDescription == null) {
                if (description == null || isExtensionListInDescription()) {
                    fullDescription = description == null ? "(" : description + " (";
                    // build the description from the extension list
                    Enumeration extensions = filters.keys();
                    if (extensions != null) {
                        fullDescription += "." + extensions.nextElement();
                        while (extensions.hasMoreElements()) {
                            fullDescription += ", ." + extensions.nextElement();
                        }
                    }
                    fullDescription += ")";
                } else {
                    fullDescription = description;
                }
            }
            return fullDescription;
        }

        /**
         * Sets the human readable description of this filter. For
         * example: filter.setDescription("Gif and JPG Images");
         */
        public void setDescription(String description) {
            this.description = description;
            fullDescription = null;
        }

        /**
         * Determines whether the extension list (.jpg, .gif, etc) should
         * show up in the human readable description.
         * <p/>
         * Only relevent if a description was provided in the constructor
         * or using setDescription();
         */
        public void setExtensionListInDescription(boolean b) {
            useExtensionsInDescription = b;
            fullDescription = null;
        }

        /**
         * Returns whether the extension list (.jpg, .gif, etc) should
         * show up in the human readable description.
         * <p/>
         * Only relevent if a description was provided in the constructor
         * or using setDescription();
         */
        public boolean isExtensionListInDescription() {
            return useExtensionsInDescription;
        }
    }
}
