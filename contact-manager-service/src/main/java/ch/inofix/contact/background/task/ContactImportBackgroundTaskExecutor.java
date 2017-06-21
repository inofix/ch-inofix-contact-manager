package ch.inofix.contact.background.task;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

import com.liferay.exportimport.kernel.model.ExportImportConfiguration;
import com.liferay.portal.kernel.backgroundtask.BackgroundTask;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskConstants;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskExecutor;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskResult;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.transaction.TransactionInvokerUtil;
import com.liferay.portal.kernel.util.FileUtil;

import ch.inofix.contact.service.ContactLocalServiceUtil;

public class ContactImportBackgroundTaskExecutor extends BaseExportImportBackgroundTaskExecutor {

    public ContactImportBackgroundTaskExecutor() {

        _log.info("ContactImportBackgroundTaskExecutor()");

        // TODO
        // setBackgroundTaskStatusMessageTranslator(new
        // ContactExportImportBackgroundTaskStatusMessageTranslator());

        // Isolation level guarantees this will be serial in a group

        setIsolationLevel(BackgroundTaskConstants.ISOLATION_LEVEL_GROUP);
    }

    @Override
    public BackgroundTaskExecutor clone() {
        ContactImportBackgroundTaskExecutor contactImportBackgroundTaskExecutor = new ContactImportBackgroundTaskExecutor();

        contactImportBackgroundTaskExecutor
                .setBackgroundTaskStatusMessageTranslator(getBackgroundTaskStatusMessageTranslator());
        contactImportBackgroundTaskExecutor.setIsolationLevel(getIsolationLevel());

        return contactImportBackgroundTaskExecutor;
    }

    @Override
    public BackgroundTaskResult execute(BackgroundTask backgroundTask) throws Exception {

        ExportImportConfiguration exportImportConfiguration = getExportImportConfiguration(backgroundTask);

        List<FileEntry> attachmentsFileEntries = backgroundTask.getAttachmentsFileEntries();

        File file = null;

        for (FileEntry attachmentsFileEntry : attachmentsFileEntries) {
            try {
                file = FileUtil.createTempFile("lar");

                FileUtil.write(file, attachmentsFileEntry.getContentStream());

                _log.info(file.getAbsoluteFile());

                TransactionInvokerUtil.invoke(transactionConfig,
                        new ContactImportCallable(exportImportConfiguration, file));

            } catch (Throwable t) {
                if (_log.isDebugEnabled()) {
                    _log.debug(t, t);
                } else if (_log.isWarnEnabled()) {
                    _log.warn("Unable to import contacts: " + t.getMessage());
                }

                throw new SystemException(t);
            } finally {
                FileUtil.delete(file);
            }
        }

        return BackgroundTaskResult.SUCCESS;
    }

    private static final Log _log = LogFactoryUtil.getLog(ContactImportBackgroundTaskExecutor.class);

    private static class ContactImportCallable implements Callable<Void> {

        public ContactImportCallable(ExportImportConfiguration exportImportConfiguration, File file) {

            _exportImportConfiguration = exportImportConfiguration;
            _file = file;
        }

        @Override
        public Void call() throws PortalException {

            ContactLocalServiceUtil.importContacts(_exportImportConfiguration, _file);

            return null;
        }

        private final ExportImportConfiguration _exportImportConfiguration;
        private final File _file;

    }

}