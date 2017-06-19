package ch.inofix.contact.background.task;

import com.liferay.exportimport.kernel.model.ExportImportConfiguration;
import com.liferay.portal.kernel.backgroundtask.BackgroundTask;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskExecutor;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskResult;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Time;

/**
 * @author Christian Berndt
 * @created 2017-06-19 00:09
 * @modified 2017-06-19 00:09
 * @version 1.0.0
 */
public class ContactExportBackgroundTaskExecutor extends BaseExportImportBackgroundTaskExecutor {

    public ContactExportBackgroundTaskExecutor() {
        // TODO
        // setBackgroundTaskStatusMessageTranslator(new
        // ContactExportImportBackgroundTaskStatusMessageTranslator());
    }

    @Override
    public BackgroundTaskExecutor clone() {
        ContactExportBackgroundTaskExecutor taskRecordExportBackgroundTaskExecutor = new ContactExportBackgroundTaskExecutor();

        taskRecordExportBackgroundTaskExecutor
                .setBackgroundTaskStatusMessageTranslator(getBackgroundTaskStatusMessageTranslator());
        taskRecordExportBackgroundTaskExecutor.setIsolationLevel(getIsolationLevel());

        return taskRecordExportBackgroundTaskExecutor;
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

        // TODO
//        File xmlFile = ContactLocalServiceUtil.exportContactsAsFile(exportImportConfiguration);

//        BackgroundTaskManagerUtil.addBackgroundTaskAttachment(userId, backgroundTask.getBackgroundTaskId(),
//                sb.toString(), xmlFile);

        return BackgroundTaskResult.SUCCESS;
    }
}