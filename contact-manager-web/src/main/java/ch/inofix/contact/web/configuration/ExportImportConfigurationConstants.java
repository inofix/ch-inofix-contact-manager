package ch.inofix.contact.web.configuration;

import com.liferay.portal.kernel.util.StringPool;

/**
 *
 * @author Christian Berndt
 * @created 2017-06-20 19:36
 * @modified 2017-06-20 19:36
 * @version 1.0.0
 *
 */
public class ExportImportConfigurationConstants {

    public static final int TYPE_EXPORT_CONTACTS = 0;

    public static final String TYPE_EXPORT_CONTACTS_LABEL = "export-contacts";

    public static final int TYPE_IMPORT_CONTACTS = 1;

    public static final String TYPE_IMPORT_CONTACTS_LABEL = "import-contacts";

    public static String getTypeLabel(int type) {
        if (type == TYPE_EXPORT_CONTACTS) {
            return TYPE_EXPORT_CONTACTS_LABEL;
        } else if (type == TYPE_IMPORT_CONTACTS) {
            return TYPE_IMPORT_CONTACTS_LABEL;
        } else {
            return StringPool.BLANK;
        }
    }
}