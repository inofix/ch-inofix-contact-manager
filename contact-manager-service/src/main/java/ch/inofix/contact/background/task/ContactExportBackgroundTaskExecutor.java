package ch.inofix.contact.background.task;

import java.io.File;

import com.liferay.exportimport.kernel.model.ExportImportConfiguration;
import com.liferay.portal.kernel.backgroundtask.BackgroundTask;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskExecutor;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskManagerUtil;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskResult;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Time;

import ch.inofix.contact.service.ContactLocalServiceUtil;

/**
 * @author Christian Berndt
 * @created 2017-06-19 00:09
 * @modified 2017-11-14 23:13
 * @version 1.0.1
 */
public class ContactExportBackgroundTaskExecutor extends BaseExportImportBackgroundTaskExecutor {

    public ContactExportBackgroundTaskExecutor() {
        // TODO
        // setBackgroundTaskStatusMessageTranslator(new
        // ContactExportImportBackgroundTaskStatusMessageTranslator());
    }

    @Override
    public BackgroundTaskExecutor clone() {
        ContactExportBackgroundTaskExecutor contactExportBackgroundTaskExecutor = new ContactExportBackgroundTaskExecutor();

        contactExportBackgroundTaskExecutor
                .setBackgroundTaskStatusMessageTranslator(getBackgroundTaskStatusMessageTranslator());
        contactExportBackgroundTaskExecutor.setIsolationLevel(getIsolationLevel());

        return contactExportBackgroundTaskExecutor;
    }

    @Override
    public BackgroundTaskResult execute(BackgroundTask backgroundTask) throws PortalException {

        ExportImportConfiguration exportImportConfiguration = getExportImportConfiguration(backgroundTask);

        long userId = backgroundTask.getUserId();

        StringBundler sb = new StringBundler(4);

        sb.append(StringUtil.replace(exportImportConfiguration.getName(), CharPool.SPACE, CharPool.UNDERLINE));
        sb.append(StringPool.DASH);
        sb.append(Time.getTimestamp());
        sb.append(".zip");

        File xmlFile = ContactLocalServiceUtil.exportContactsAsFile(exportImportConfiguration);

        BackgroundTaskManagerUtil.addBackgroundTaskAttachment(userId, backgroundTask.getBackgroundTaskId(),
                sb.toString(), xmlFile);

        return BackgroundTaskResult.SUCCESS;
    }
    
    private static final Log _log = LogFactoryUtil.getLog(ContactExportBackgroundTaskExecutor.class.getName()); 
}
