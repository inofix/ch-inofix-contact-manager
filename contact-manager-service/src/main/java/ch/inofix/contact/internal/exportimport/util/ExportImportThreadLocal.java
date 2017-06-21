package ch.inofix.contact.internal.exportimport.util;

import com.liferay.portal.kernel.util.AutoResetThreadLocal;

/**
 *
 * @author Christian Berndt
 * @created 2017-06-21 18:54
 * @modified 2017-06-21 18:54
 * @version 1.0.0
 *
 *          Based on the model of
 *          com.liferay.exportimport.kernel.lar.ExportImportThreadLocal
 *
 */
public class ExportImportThreadLocal {

    public static boolean isDataDeletionImportInProcess() {
        if (isContactDataDeletionImportInProcess() || isPortletDataDeletionImportInProcess()) {

            return true;
        }

        return false;
    }

    public static boolean isExportInProcess() {
        if (isContactExportInProcess() || isPortletExportInProcess()) {
            return true;
        }

        return false;
    }

    public static boolean isImportInProcess() {
        if (isDataDeletionImportInProcess() || isContactImportInProcess() || isContactValidationInProcess()
                || isPortletImportInProcess() || isPortletValidationInProcess()) {

            return true;
        }

        return false;
    }

    public static boolean isInitialContactStagingInProcess() {
        return _initialContactStagingInProcess.get();
    }

    public static boolean isContactDataDeletionImportInProcess() {
        return _contactDataDeletionImportInProcess.get();
    }

    public static boolean isContactExportInProcess() {
        return _contactExportInProcess.get();
    }

    public static boolean isContactImportInProcess() {
        return _contactImportInProcess.get();
    }

    public static boolean isContactStagingInProcess() {
        return _contactStagingInProcess.get();
    }

    public static boolean isContactValidationInProcess() {
        return _contactValidationInProcess.get();
    }

    public static boolean isPortletDataDeletionImportInProcess() {
        return _portletDataDeletionImportInProcess.get();
    }

    public static boolean isPortletExportInProcess() {
        return _portletExportInProcess.get();
    }

    public static boolean isPortletImportInProcess() {
        return _portletImportInProcess.get();
    }

    public static boolean isPortletStagingInProcess() {
        return _portletStagingInProcess.get();
    }

    public static boolean isPortletValidationInProcess() {
        return _portletValidationInProcess.get();
    }

    public static boolean isStagingInProcess() {
        if (isContactStagingInProcess() || isPortletStagingInProcess()) {
            return true;
        }

        return false;
    }

    public static void setInitialContactStagingInProcess(boolean initialContactStagingInProcess) {

        _initialContactStagingInProcess.set(initialContactStagingInProcess);
    }

    public static void setContactDataDeletionImportInProcess(boolean contactDataDeletionImportInProcess) {

        _contactDataDeletionImportInProcess.set(contactDataDeletionImportInProcess);
    }

    public static void setContactExportInProcess(boolean contactExportInProcess) {
        _contactExportInProcess.set(contactExportInProcess);
    }

    public static void setContactImportInProcess(boolean contactImportInProcess) {
        _contactImportInProcess.set(contactImportInProcess);
    }

    public static void setContactStagingInProcess(boolean contactStagingInProcess) {

        _contactStagingInProcess.set(contactStagingInProcess);
    }

    public static void setContactValidationInProcess(boolean contactValidationInProcess) {

        _contactValidationInProcess.set(contactValidationInProcess);
    }

    public static void setPortletDataDeletionImportInProcess(boolean portletDataDeletionImportInProcess) {

        _portletDataDeletionImportInProcess.set(portletDataDeletionImportInProcess);
    }

    public static void setPortletExportInProcess(boolean portletExportInProcess) {

        _portletExportInProcess.set(portletExportInProcess);
    }

    public static void setPortletImportInProcess(boolean portletImportInProcess) {

        _portletImportInProcess.set(portletImportInProcess);
    }

    public static void setPortletStagingInProcess(boolean portletStagingInProcess) {

        _portletStagingInProcess.set(portletStagingInProcess);
    }

    public static void setPortletValidationInProcess(boolean portletValidationInProcess) {

        _portletValidationInProcess.set(portletValidationInProcess);
    }

    private static final ThreadLocal<Boolean> _initialContactStagingInProcess = new AutoResetThreadLocal<>(
            ExportImportThreadLocal.class + "._initialContactStagingInProcess", false);
    private static final ThreadLocal<Boolean> _contactDataDeletionImportInProcess = new AutoResetThreadLocal<>(
            ExportImportThreadLocal.class + "._contactDataDeletionImportInProcess", false);
    private static final ThreadLocal<Boolean> _contactExportInProcess = new AutoResetThreadLocal<>(
            ExportImportThreadLocal.class + "._contactExportInProcess", false);
    private static final ThreadLocal<Boolean> _contactImportInProcess = new AutoResetThreadLocal<>(
            ExportImportThreadLocal.class + "._contactImportInProcess", false);
    private static final ThreadLocal<Boolean> _contactStagingInProcess = new AutoResetThreadLocal<>(
            ExportImportThreadLocal.class + "._contactStagingInProcess", false);
    private static final ThreadLocal<Boolean> _contactValidationInProcess = new AutoResetThreadLocal<>(
            ExportImportThreadLocal.class + "._contactValidationInProcess", false);
    private static final ThreadLocal<Boolean> _portletDataDeletionImportInProcess = new AutoResetThreadLocal<>(
            ExportImportThreadLocal.class + "._portletDataDeletionImportInProcess", false);
    private static final ThreadLocal<Boolean> _portletExportInProcess = new AutoResetThreadLocal<>(
            ExportImportThreadLocal.class + "._portletExportInProcess", false);
    private static final ThreadLocal<Boolean> _portletImportInProcess = new AutoResetThreadLocal<>(
            ExportImportThreadLocal.class + "._portletImportInProcess", false);
    private static final ThreadLocal<Boolean> _portletStagingInProcess = new AutoResetThreadLocal<>(
            ExportImportThreadLocal.class + "._portletStagingInProcess", false);
    private static final ThreadLocal<Boolean> _portletValidationInProcess = new AutoResetThreadLocal<>(
            ExportImportThreadLocal.class + "._portletValidationInProcess", false);

}
