package ch.inofix.contact.service.util;

import java.util.ArrayList;
import java.util.List;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;

import ch.inofix.contact.model.Contact;
import ch.inofix.contact.service.ContactLocalServiceUtil;

/**
 *
 * @author Christian Berndt
 * @created 2017-06-18 23:48
 * @modified 2017-06-18 23:48
 * @version 1.0.1
 *
 */
public class ContactUtil {

    public static List<Contact> getContacts(Hits hits) {

        List<Document> documents = ListUtil.toList(hits.getDocs());

        List<Contact> taskRecords = new ArrayList<Contact>();

        for (Document document : documents) {
            try {
                long taskRecordId = GetterUtil.getLong(document.get(Field.ENTRY_CLASS_PK));

                Contact taskRecord = ContactLocalServiceUtil.getContact(taskRecordId);
                taskRecords.add(taskRecord);

            } catch (Exception e) {

                if (_log.isErrorEnabled()) {
                    _log.error(e.getMessage());
                }
            }
        }

        return taskRecords;
    }

    private static final Log _log = LogFactoryUtil.getLog(ContactUtil.class.getName());

}