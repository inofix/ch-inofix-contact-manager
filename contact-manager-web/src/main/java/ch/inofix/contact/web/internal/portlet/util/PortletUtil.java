package ch.inofix.contact.web.internal.portlet.util;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

import ch.inofix.contact.exception.ImageFileFormatException;
import ch.inofix.contact.exception.KeyFileFormatException;
import ch.inofix.contact.exception.SoundFileFormatException;
//import ezvcard.VCard;
//import ezvcard.VCardVersion;
//import ezvcard.parameter.AddressType;
//import ezvcard.parameter.EmailType;
//import ezvcard.parameter.ExpertiseLevel;
//import ezvcard.parameter.HobbyLevel;
//import ezvcard.parameter.ImageType;
//import ezvcard.parameter.ImppType;
//import ezvcard.parameter.InterestLevel;
//import ezvcard.parameter.KeyType;
//import ezvcard.parameter.SoundType;
//import ezvcard.parameter.TelephoneType;
//import ezvcard.property.Address;
//import ezvcard.property.Anniversary;
//import ezvcard.property.Birthday;
//import ezvcard.property.Birthplace;
//import ezvcard.property.CalendarRequestUri;
//import ezvcard.property.CalendarUri;
//import ezvcard.property.Categories;
//import ezvcard.property.Deathdate;
//import ezvcard.property.Deathplace;
//import ezvcard.property.Email;
//import ezvcard.property.Expertise;
//import ezvcard.property.FreeBusyUrl;
//import ezvcard.property.Gender;
//import ezvcard.property.Hobby;
//import ezvcard.property.Impp;
//import ezvcard.property.Interest;
//import ezvcard.property.Key;
//import ezvcard.property.Kind;
//import ezvcard.property.Language;
//import ezvcard.property.Logo;
//import ezvcard.property.Member;
//import ezvcard.property.Note;
//import ezvcard.property.Organization;
//import ezvcard.property.Photo;
//import ezvcard.property.ProductId;
//import ezvcard.property.Profile;
//import ezvcard.property.Revision;
//import ezvcard.property.Role;
//import ezvcard.property.SortString;
//import ezvcard.property.Sound;
//import ezvcard.property.Source;
//import ezvcard.property.SourceDisplayText;
//import ezvcard.property.StructuredName;
//import ezvcard.property.Telephone;
//import ezvcard.property.Timezone;
//import ezvcard.property.Title;
//import ezvcard.property.Uid;
//import ezvcard.property.Url;
//import ezvcard.util.DataUri;

/**
 * Utility methods used by the ContactManagerPortlet. Based on the model of the
 * ActionUtil of Liferay proper.
 *
 * @author Christian Berndt
 * @created 2015-05-16 15:31
 * @modified 2017-06-30 23:49
 * @version 1.1.9
 *
 */
public class PortletUtil {

    /**
     *
     * @param i
     * @return
     * @since 1.0.4
     */
    public static Integer getPref(int i) {

        // Preference value must be between 1 and 100 inclusive.
        Integer pref = 0;

        if (i < 100) {
            pref = i + 1;
        } else {
            pref = 100;
        }

        return pref;

    }

//    @SuppressWarnings("unchecked")
//    public static VCard getVCard(HttpServletRequest request, VCard vCard, Map<String, File[]> map)
//            throws PortalException {
//
//        // Retrieve the parameters and update the received vCard
//        // with the parameter values.
//        //
//        // Since the vCard might be edited with different clients with
//        // different capabilities we have to update existing cards with
//        // the received parameters.
//        //
//        // VCard attributes are only set (or unset) if a corresponding request
//        // parameter is present, which means, attributes not supported by the
//        // contact-manager portlet will be left untouched.
//
//        Map<String, String[]> parameters = request.getParameterMap();
//
//        if (_log.isDebugEnabled()) {
//
//            Set<String> keySet = parameters.keySet();
//
//            Iterator<String> iterator = keySet.iterator();
//
//            while (iterator.hasNext()) {
//
//                String parameter = iterator.next();
//
//                _log.debug(parameter + " = " + request.getParameter(parameter));
//
//            }
//        }
//
//        // Retrieve and set vCard properties (in alphabetical order)
//
//        if (parameters.containsKey("address.country") || parameters.containsKey("address.label")
//                || parameters.containsKey("address.locality") || parameters.containsKey("address.poBox")
//                || parameters.containsKey("address.postalCode") || parameters.containsKey("address.streetAddress")
//                || parameters.containsKey("address.timezone")) {
//
//            vCard.removeProperties(Address.class);
//
//            String[] addressCountries = request.getParameterValues("address.country");
//            String[] addressLabels = request.getParameterValues("address.label");
//            String[] addressLanguages = request.getParameterValues("address.language");
//            String[] addressLocalities = request.getParameterValues("address.locality");
//            String[] addressPoBoxes = request.getParameterValues("address.poBox");
//            String[] addressPostalCodes = request.getParameterValues("address.postalCode");
//            String[] addressRegions = request.getParameterValues("address.region");
//            String[] addressStreetAddresses = request.getParameterValues("address.streetAddress");
//            String[] addressTimezones = request.getParameterValues("address.timezone");
//            String[] addressTypes = request.getParameterValues("address.type");
//
//            for (int i = 0; i < addressTypes.length; i++) {
//
//                // Do not create an address if no relevant parameter is present
//                if (Validator.isNotNull(addressCountries[i]) || Validator.isNotNull(addressLocalities[i])
//                        || Validator.isNotNull(addressPoBoxes[i]) || Validator.isNotNull(addressPostalCodes[i])
//                        || Validator.isNotNull(addressRegions[i]) || Validator.isNotNull(addressStreetAddresses[i])) {
//
//                    Address address = new Address();
//
//                    Integer pref = getPref(i);
//
//                    address.setPref(pref);
//
//                    if (parameters.containsKey("address.country")) {
//                        address.setCountry(addressCountries[i]);
//                    }
//                    // TODO:
//                    // address.setGeo(latitude, longitude);
//                    if (parameters.containsKey("address.label")) {
//                        address.setLabel(addressLabels[i]);
//                    }
//                    if (parameters.containsKey("address.language")) {
//                        address.setLanguage(addressLanguages[i]);
//                    }
//                    if (parameters.containsKey("address.locality")) {
//                        address.setLocality(addressLocalities[i]);
//                    }
//                    if (parameters.containsKey("address.poBox")) {
//                        address.setPoBox(addressPoBoxes[i]);
//                    }
//                    if (parameters.containsKey("address.postalCode")) {
//                        address.setPostalCode(addressPostalCodes[i]);
//                    }
//                    if (parameters.containsKey("address.region")) {
//                        address.setRegion(addressRegions[i]);
//                    }
//                    if (parameters.containsKey("address.streetAddress")) {
//                        address.setStreetAddress(addressStreetAddresses[i]);
//                    }
//                    if (parameters.containsKey("address.timezone")) {
//                        address.setTimezone(addressTimezones[i]);
//                    }
//
//                    AddressType type = AddressType.find(addressTypes[i]);
//
//                    if (type != null) {
//                        address.getTypes().add(type);
//                    }
//
//                    vCard.addAddress(address);
//                }
//            }
//        }
//
//        // AGENT property is no longer supported in v.4.0
//        // see: https://tools.ietf.org/html/rfc6350#appendix-A.2
//        // vCard.setAgent(agent);
//
//        if (parameters.containsKey("anniversary.day") || parameters.containsKey("anniversary.month")
//                || parameters.containsKey("anniversary.year")) {
//
//            int anniversaryDay = ParamUtil.getInteger(request, "anniversary.day", 1);
//            int anniversaryMonth = ParamUtil.getInteger(request, "anniversary.month", Calendar.JANUARY);
//            int anniversaryYear = ParamUtil.getInteger(request, "anniversary.year", 1970);
//
//            Date anniversaryDate = PortalUtil.getDate(anniversaryMonth, anniversaryDay, anniversaryYear);
//            Anniversary anniversary = new Anniversary(anniversaryDate);
//            vCard.setAnniversary(anniversary);
//        }
//
//        if (parameters.containsKey("birthday.day") || parameters.containsKey("birthday.month")
//                || parameters.containsKey("birthday.year")) {
//
//            int birthdayDay = ParamUtil.getInteger(request, "birthday.day", 1);
//            int birthdayMonth = ParamUtil.getInteger(request, "birthday.month", Calendar.JANUARY);
//            int birthdayYear = ParamUtil.getInteger(request, "birthday.year", 1970);
//            Date birthDate = PortalUtil.getDate(birthdayMonth, birthdayDay, birthdayYear);
//            Birthday birthday = new Birthday(birthDate);
//            vCard.setBirthday(birthday);
//        }
//
//        if (parameters.containsKey("birthplace")) {
//            String birthplaceStr = ParamUtil.getString(request, "birthplace");
//            Birthplace birthplace = new Birthplace(birthplaceStr);
//            vCard.setBirthplace(birthplace);
//        }
//
//        if (parameters.containsKey("calendarRequestUri")) {
//
//            vCard.removeProperties(CalendarRequestUri.class);
//
//            String[] calendarRequestUris = ParamUtil.getParameterValues(request, "calendarRequestUri");
//
//            for (int i = 0; i < calendarRequestUris.length; i++) {
//
//                if (Validator.isNotNull(calendarRequestUris[i])) {
//                    CalendarRequestUri calendarRequestUri = new CalendarRequestUri(calendarRequestUris[i]);
//
//                    Integer pref = getPref(i);
//
//                    calendarRequestUri.setPref(pref);
//
//                    vCard.addCalendarRequestUri(calendarRequestUri);
//                }
//            }
//        }
//
//        if (parameters.containsKey("calendarUri")) {
//
//            vCard.removeProperties(CalendarUri.class);
//
//            String[] calendarUris = ParamUtil.getParameterValues(request, "calendarUri");
//
//            for (int i = 0; i < calendarUris.length; i++) {
//
//                if (Validator.isNotNull(calendarUris[i])) {
//                    CalendarUri calendarUri = new CalendarUri(calendarUris[i]);
//
//                    Integer pref = getPref(i);
//
//                    calendarUri.setPref(pref);
//
//                    vCard.addCalendarUri(calendarUri);
//                }
//            }
//        }
//
//        if (parameters.containsKey("categories.value")) {
//
//            vCard.removeProperties(Categories.class);
//
//            String[] categoriesValues = request.getParameterValues("categories.value");
//            String[] categoriesTypes = request.getParameterValues("categories.type");
//
//            for (int i = 0; i < categoriesValues.length; i++) {
//
//                if (Validator.isNotNull(categoriesValues[i])) {
//
//                    Categories categories = new Categories();
//                    categories.setType(categoriesTypes[i]);
//
//                    String[] values = categoriesValues[i].split(",");
//
//                    for (String value : values) {
//
//                        categories.getValues().add(value.trim());
//
//                    }
//
//                    vCard.addCategories(categories);
//                }
//            }
//        }
//
//        // TODO
//        // vCard.addClientPidMap(clientPidMap);
//
//        // CLASS property is no longer supported in v.4.0
//        // see: https://tools.ietf.org/html/rfc6350#appendix-A.2
//        // vCard.setClassification(classification);
//
//        if (parameters.containsKey("deathdate.day") || parameters.containsKey("deathdate.month")
//                || parameters.containsKey("deathdate.year")) {
//
//            int deathdateDay = ParamUtil.getInteger(request, "deathdate.day", 1);
//            int deathdateMonth = ParamUtil.getInteger(request, "deathdate.month", Calendar.JANUARY);
//            int deathdateYear = ParamUtil.getInteger(request, "deathdate.year", 1970);
//
//            Date deathDate = PortalUtil.getDate(deathdateMonth, deathdateDay, deathdateYear);
//            Deathdate deathdate = new Deathdate(deathDate);
//            vCard.setDeathdate(deathdate);
//        }
//
//        if (parameters.containsKey("deathplace")) {
//            String deathplaceStr = ParamUtil.getString(request, "deathplace");
//            Deathplace deathplace = new Deathplace(deathplaceStr);
//            vCard.setDeathplace(deathplace);
//        }
//
//        if (parameters.containsKey("email.address")) {
//
//            vCard.removeProperties(Email.class);
//
//            String[] emailAddresses = ParamUtil.getParameterValues(request, "email.address");
//            String[] emailTypes = ParamUtil.getParameterValues(request, "email.type");
//
//            for (int i = 0; i < emailAddresses.length; i++) {
//
//                EmailType type = EmailType.find(emailTypes[i]);
//
//                if (Validator.isNotNull(emailAddresses[i])) {
//
//                    Email email = new Email(emailAddresses[i]);
//
//                    Integer pref = getPref(i);
//
//                    email.setPref(pref);
//
//                    if (type != null) {
//                        email.getTypes().add(type);
//                    }
//                    vCard.addEmail(email);
//                }
//            }
//        }
//
//        if (parameters.containsKey("expertise")) {
//
//            vCard.removeProperties(Expertise.class);
//
//            String[] expertises = ParamUtil.getParameterValues(request, "expertise");
//            String[] expertiseLevels = ParamUtil.getParameterValues(request, "expertise.level");
//
//            for (int i = 0; i < expertises.length; i++) {
//
//                if (Validator.isNotNull(expertises[i])) {
//
//                    Expertise expertise = new Expertise(expertises[i]);
//                    ExpertiseLevel level = ExpertiseLevel.find(expertiseLevels[i]);
//                    expertise.setLevel(level);
//
//                    Integer pref = getPref(i);
//
//                    expertise.setPref(pref);
//
//                    vCard.addExpertise(expertise);
//                }
//            }
//        }
//
//        if (parameters.containsKey("formattedName")) {
//            String formattedName = ParamUtil.getString(request, "formattedName");
//            vCard.setFormattedName(formattedName);
//        }
//
//        if (parameters.containsKey("freeBusyUrl")) {
//
//            vCard.removeProperties(FreeBusyUrl.class);
//
//            String[] freeBusyUrls = ParamUtil.getParameterValues(request, "freeBusyUrl");
//
//            for (int i = 0; i < freeBusyUrls.length; i++) {
//
//                if (Validator.isNotNull(freeBusyUrls[i])) {
//
//                    FreeBusyUrl freeBusyUrl = new FreeBusyUrl(freeBusyUrls[i]);
//
//                    Integer pref = getPref(i);
//
//                    freeBusyUrl.setPref(pref);
//
//                    vCard.addFbUrl(freeBusyUrl);
//                }
//            }
//        }
//
//        if (parameters.containsKey("gender")) {
//            String genderStr = ParamUtil.getString(request, "gender");
//            Gender gender = new Gender(genderStr);
//            vCard.setGender(gender);
//        }
//
//        // TODO: add or set GEO?
//        // Geo geo = new Geo(latitude, longitude);
//        // vCard.setGeo(geo);
//
//        if (parameters.containsKey("hobby")) {
//
//            vCard.removeProperties(Hobby.class);
//
//            String[] hobbies = ParamUtil.getParameterValues(request, "hobby");
//            String[] hobbyLevels = ParamUtil.getParameterValues(request, "hobby.level");
//
//            for (int i = 0; i < hobbies.length; i++) {
//
//                if (Validator.isNotNull(hobbies[i])) {
//
//                    Hobby hobby = new Hobby(hobbies[i]);
//                    HobbyLevel level = HobbyLevel.find(hobbyLevels[i]);
//                    hobby.setLevel(level);
//
//                    Integer pref = getPref(i);
//
//                    hobby.setPref(pref);
//
//                    vCard.addHobby(hobby);
//                }
//            }
//        }
//
//        if (parameters.containsKey("impp.uri")) {
//
//            vCard.removeProperties(Impp.class);
//
//            String[] imppProtocols = ParamUtil.getParameterValues(request, "impp.protocol");
//            String[] imppTypes = ParamUtil.getParameterValues(request, "impp.type");
//            String[] imppUris = ParamUtil.getParameterValues(request, "impp.uri");
//
//            for (int i = 0; i < imppUris.length; i++) {
//
//                ImppType type = ImppType.find(imppTypes[i]);
//
//                if (Validator.isNotNull(imppUris[i])) {
//
//                    // TODO: check uri format
//                    String uri = imppProtocols[i] + ":" + imppUris[i];
//                    Impp impp = new Impp(uri);
//                    if (type != null) {
//
//                        impp.getTypes().add(type);
//                    }
//
//                    Integer pref = getPref(i);
//
//                    impp.setPref(pref);
//
//                    vCard.addImpp(impp);
//                }
//            }
//        }
//
//        if (parameters.containsKey("interest")) {
//
//            vCard.removeProperties(Interest.class);
//
//            String[] interests = ParamUtil.getParameterValues(request, "interest");
//            String[] interestLevels = ParamUtil.getParameterValues(request, "interest.level");
//
//            for (int i = 0; i < interests.length; i++) {
//
//                if (Validator.isNotNull(interests[i])) {
//
//                    Interest interest = new Interest(interests[i]);
//                    InterestLevel level = InterestLevel.find(interestLevels[i]);
//                    interest.setLevel(level);
//
//                    Integer pref = getPref(i);
//
//                    interest.setPref(pref);
//
//                    vCard.addInterest(interest);
//
//                }
//            }
//        }
//
//        if (parameters.containsKey("key.file")) {
//
//            vCard.removeProperties(Key.class);
//
//            String[] dataUris = request.getParameterValues("key.data");
//
//            // Retrieve the corresponding file (if any)
//
//            File[] files = map.get("key.file");
//
//            Key key = null;
//
//            for (int i = 0; i < files.length; i++) {
//
//                if (files != null) {
//
//                    File file = files[i];
//
//                    String extension = null;
//
//                    if (file != null) {
//
//                        // Try to create a new key from the request.
//
//                        extension = FileUtil.getExtension(file.getName());
//
//                        try {
//
//                            if ("GPG".equalsIgnoreCase(extension)) {
//
//                                key = new Key(file, KeyType.GPG);
//
//                            } else if ("PGP".equalsIgnoreCase(extension)) {
//
//                                key = new Key(file, KeyType.PGP);
//
//                            } else if ("X509".equalsIgnoreCase(extension)) {
//
//                                key = new Key(file, KeyType.X509);
//
//                            } else {
//
//                                throw new KeyFileFormatException();
//
//                            }
//
//                        } catch (IOException ioe) {
//
//                            throw new PortalException(ioe.getMessage());
//
//                        }
//
//                    } else if (Validator.isNotNull(dataUris[i])) {
//
//                        // Restore an already uploaded key from its data uri.
//
//                        DataUri dataUri = new DataUri(dataUris[i]);
//                        String contentType = dataUri.getContentType();
//                        KeyType type = KeyType.find(contentType, contentType, extension);
//                        key = new Key(dataUri.getData(), type);
//
//                    }
//
//                }
//
//                if (key != null) {
//                    vCard.addKey(key);
//                }
//
//            }
//
//        }
//
//        if (parameters.containsKey("kind")) {
//            String kindStr = ParamUtil.getString(request, "kind");
//            Kind kind = new Kind(kindStr);
//            vCard.setKind(kind);
//        }
//
//        if (parameters.containsKey("languageKeys")) {
//
//            vCard.removeProperties(Language.class);
//
//            String[] keys = ParamUtil.getParameterValues(request, "languageKeys");
//
//            for (String key : keys) {
//                Language language = new Language(key);
//                vCard.addLanguage(language);
//            }
//
//        }
//
//        if (parameters.containsKey("logo.file")) {
//
//            vCard.removeProperties(Logo.class);
//
//            String[] dataUris = request.getParameterValues("logo.data");
//
//            // Retrieve the corresponding file (if any)
//
//            File[] files = map.get("logo.file");
//
//            Logo logo = null;
//
//            for (int i = 0; i < files.length; i++) {
//
//                if (files != null) {
//
//                    File file = files[i];
//
//                    String extension = null;
//
//                    if (file != null) {
//
//                        // Try to create a new logo from the request.
//
//                        extension = FileUtil.getExtension(file.getName());
//
//                        try {
//
//                            if ("GIF".equalsIgnoreCase(extension)) {
//
//                                logo = new Logo(file, ImageType.GIF);
//
//                            } else if ("JPEG".equalsIgnoreCase(extension) || "JPG".equalsIgnoreCase(extension)) {
//
//                                logo = new Logo(file, ImageType.JPEG);
//
//                            } else if ("PNG".equalsIgnoreCase(extension)) {
//
//                                logo = new Logo(file, ImageType.PNG);
//
//                            } else {
//
//                                throw new ImageFileFormatException();
//
//                            }
//
//                        } catch (IOException ioe) {
//
//                            throw new PortalException(ioe.getMessage());
//
//                        }
//
//                    } else if (Validator.isNotNull(dataUris[i])) {
//
//                        // Restore an already uploaded logo from its data uri.
//
//                        DataUri dataUri = new DataUri(dataUris[i]);
//                        String contentType = dataUri.getContentType();
//                        ImageType type = ImageType.find(contentType, contentType, extension);
//                        logo = new Logo(dataUri.getData(), type);
//
//                    }
//
//                }
//
//                if (logo != null) {
//                    vCard.addLogo(logo);
//                }
//
//            }
//        }
//
//        if (parameters.containsKey("member")) {
//
//            vCard.removeProperties(Member.class);
//
//            String[] members = ParamUtil.getParameterValues(request, "member");
//
//            for (int i = 0; i < members.length; i++) {
//
//                if (Validator.isNotNull(members[i])) {
//
//                    Member member = new Member(members[i]);
//
//                    Integer pref = getPref(i);
//
//                    member.setPref(pref);
//
//                    vCard.addMember(member);
//
//                }
//            }
//        }
//
//        if (parameters.containsKey("nickname")) {
//
//            String nickname = ParamUtil.getString(request, "nickname");
//
//            vCard.setNickname(nickname);
//        }
//
//        if (parameters.containsKey("note")) {
//
//            vCard.removeProperties(Note.class);
//
//            String[] notes = ParamUtil.getParameterValues(request, "note");
//
//            for (int i = 0; i < notes.length; i++) {
//
//                Note note = new Note(notes[i]);
//
//                if (Validator.isNotNull(notes[i])) {
//
//                    Integer pref = getPref(i);
//
//                    note.setPref(pref);
//
//                    vCard.addNote(note);
//                }
//            }
//        }
//
//        if (parameters.containsKey("company") || parameters.containsKey("department")
//                || parameters.containsKey("office")) {
//
//            vCard.removeProperties(Organization.class);
//
//            Organization organization = new Organization();
//
//            String company = request.getParameter("company");
//            String department = request.getParameter("department");
//            String office = request.getParameter("office");
//
//            organization.getValues().add(company);
//            organization.getValues().add(department);
//            organization.getValues().add(office);
//
//            _log.info(organization);
//
//            vCard.addOrganization(organization);
//
//        }
//
//        // TODO
//        // vCard.addOrgDirectory(orgDirectory);
//
//        if (parameters.containsKey("phone.number")) {
//
//            String[] phoneNumbers = ParamUtil.getParameterValues(request, "phone.number");
//            String[] phoneTypes = ParamUtil.getParameterValues(request, "phone.type");
//
//            vCard.removeProperties(Telephone.class);
//
//            for (int i = 0; i < phoneNumbers.length; i++) {
//
//                TelephoneType type = TelephoneType.find(phoneTypes[i]);
//
//                if (Validator.isNotNull(phoneNumbers[i])) {
//
//                    Telephone phone = new Telephone(phoneNumbers[i]);
//
//                    if (type != null) {
//                        phone.getTypes().add(type);
//                    }
//
//                    Integer pref = getPref(i);
//
//                    phone.setPref(pref);
//
//                    vCard.addTelephoneNumber(phone);
//                }
//            }
//        }
//
//        if (parameters.containsKey("photo.file")) {
//
//            vCard.removeProperties(Photo.class);
//
//            String[] dataUris = request.getParameterValues("photo.data");
//
//            // Retrieve the corresponding file (if any)
//
//            File[] files = map.get("photo.file");
//
//            Photo photo = null;
//
//            for (int i = 0; i < files.length; i++) {
//
//                if (files != null) {
//
//                    File file = files[i];
//
//                    String extension = null;
//
//                    if (file != null) {
//
//                        // Try to create a new photo from the request.
//
//                        extension = FileUtil.getExtension(file.getName());
//
//                        try {
//
//                            if ("GIF".equalsIgnoreCase(extension)) {
//
//                                photo = new Photo(file, ImageType.GIF);
//
//                            } else if ("JPEG".equalsIgnoreCase(extension) || "JPG".equalsIgnoreCase(extension)) {
//
//                                photo = new Photo(file, ImageType.JPEG);
//
//                            } else if ("PNG".equalsIgnoreCase(extension)) {
//
//                                photo = new Photo(file, ImageType.PNG);
//
//                            } else {
//
//                                throw new ImageFileFormatException();
//
//                            }
//
//                        } catch (IOException ioe) {
//
//                            throw new PortalException(ioe.getMessage());
//
//                        }
//
//                    } else if (Validator.isNotNull(dataUris[i])) {
//
//                        // Restore an already uploaded photo from its data uri.
//
//                        DataUri dataUri = new DataUri(dataUris[i]);
//                        String contentType = dataUri.getContentType();
//                        ImageType type = ImageType.find(contentType, contentType, extension);
//                        photo = new Photo(dataUri.getData(), type);
//
//                    }
//
//                }
//
//                if (photo != null) {
//                    vCard.addPhoto(photo);
//                }
//
//            }
//        }
//
//        if (parameters.containsKey("productId")) {
//            String productIdStr = ParamUtil.getString(request, "productId");
//            ProductId productId = new ProductId(productIdStr);
//            vCard.setProductId(productId);
//        }
//
//        // probably obsolete since 4.0
//        if (parameters.containsKey("profile")) {
//            Profile profile = new Profile();
//            String profileStr = ParamUtil.getString(request, "profile");
//            profile.setValue(profileStr);
//            vCard.setProfile(profile);
//        }
//
//        // TODO
//        // vCard.addRelated(related);
//
//        Revision revision = new Revision(new Date());
//        vCard.setRevision(revision);
//
//        if (parameters.containsKey("role")) {
//
//            vCard.removeProperties(Role.class);
//
//            String[] roles = ParamUtil.getParameterValues(request, "role");
//
//            for (int i = 0; i < roles.length; i++) {
//
//                if (Validator.isNotNull(roles[i])) {
//                    Role role = new Role(roles[i]);
//
//                    Integer pref = getPref(i);
//
//                    role.setPref(pref);
//
//                    vCard.addRole(role);
//                }
//            }
//        }
//
//        if (parameters.containsKey("sortString")) {
//            String sortStringStr = ParamUtil.getString(request, "sortString");
//            SortString sortString = new SortString(sortStringStr);
//            vCard.setSortString(sortString);
//        }
//
//        if (parameters.containsKey("sound.file")) {
//
//            vCard.removeProperties(Sound.class);
//
//            String[] dataUris = request.getParameterValues("sound.data");
//
//            // Retrieve the corresponding file (if any)
//
//            File[] files = map.get("sound.file");
//
//            Sound sound = null;
//
//            for (int i = 0; i < files.length; i++) {
//
//                if (files != null) {
//
//                    File file = files[i];
//
//                    String extension = null;
//
//                    if (file != null) {
//
//                        // Try to create a new sound from the request.
//
//                        extension = FileUtil.getExtension(file.getName());
//
//                        try {
//
//                            if ("AAC".equalsIgnoreCase(extension)) {
//
//                                sound = new Sound(file, SoundType.AAC);
//
//                            } else if ("MIDI".equalsIgnoreCase(extension)) {
//
//                                sound = new Sound(file, SoundType.MIDI);
//
//                            } else if ("MP3".equalsIgnoreCase(extension)) {
//
//                                sound = new Sound(file, SoundType.MP3);
//
//                            } else if ("MPEG".equalsIgnoreCase(extension)) {
//
//                                sound = new Sound(file, SoundType.MPEG);
//
//                            } else if ("OGG".equalsIgnoreCase(extension)) {
//
//                                sound = new Sound(file, SoundType.OGG);
//
//                            } else if ("WAV".equalsIgnoreCase(extension)) {
//
//                                sound = new Sound(file, SoundType.WAV);
//
//                            } else {
//
//                                throw new SoundFileFormatException();
//
//                            }
//
//                        } catch (IOException ioe) {
//
//                            throw new PortalException(ioe.getMessage());
//
//                        }
//
//                    } else if (Validator.isNotNull(dataUris[i])) {
//
//                        // Restore an already uploaded sound from its data uri.
//
//                        DataUri dataUri = new DataUri(dataUris[i]);
//                        String contentType = dataUri.getContentType();
//                        SoundType type = SoundType.find(contentType, contentType, extension);
//                        sound = new Sound(dataUri.getData(), type);
//
//                    }
//
//                }
//
//                if (sound != null) {
//                    vCard.addSound(sound);
//                }
//
//            }
//        }
//
//        if (parameters.containsKey("source.uri")) {
//
//            vCard.removeProperties(Source.class);
//
//            String[] sourceUris = ParamUtil.getParameterValues(request, "source.uri");
//
//            for (int i = 0; i < sourceUris.length; i++) {
//
//                if (Validator.isNotNull(sourceUris[i])) {
//                    Source sourceUri = new Source(sourceUris[i]);
//
//                    Integer pref = getPref(i);
//
//                    sourceUri.setPref(pref);
//
//                    vCard.addSource(sourceUri);
//                }
//            }
//        }
//
//        if (parameters.containsKey("sourceDisplayText")) {
//            String sourceDisplayTextStr = ParamUtil.getString(request, "sourceDisplayText");
//            SourceDisplayText sourceDisplayText = new SourceDisplayText(sourceDisplayTextStr);
//            vCard.setSourceDisplayText(sourceDisplayText);
//        }
//
//        if (parameters.containsKey("structuredName.additional") || parameters.containsKey("structuredName.family")
//                || parameters.containsKey("structuredName.given") || parameters.containsKey("structuredName.prefix")
//                || parameters.containsKey("structuredName.suffix")) {
//
//            StructuredName structuredName = new StructuredName();
//
//            String snAdditional = ParamUtil.getString(request, "structuredName.additional");
//            String snFamily = ParamUtil.getString(request, "structuredName.family");
//            String snGiven = ParamUtil.getString(request, "structuredName.given");
//            String snPrefix = ParamUtil.getString(request, "structuredName.prefix");
//            String snSuffix = ParamUtil.getString(request, "structuredName.suffix");
//
//            structuredName.getAdditionalNames().add(snAdditional);
//            structuredName.setFamily(snFamily);
//            structuredName.setGiven(snGiven);
//            structuredName.getPrefixes().add(snPrefix);
//            structuredName.getSuffixes().add(snSuffix);
//            structuredName.setSortAs(snFamily, snGiven);
//
//            vCard.setStructuredName(structuredName);
//
//            StringBuilder sb = new StringBuilder();
//
//            if (Validator.isNotNull(snPrefix)) {
//                sb.append(snPrefix);
//            }
//
//            if (Validator.isNotNull(snGiven)) {
//                if (Validator.isNotNull(sb.toString())) {
//                    sb.append(StringPool.SPACE);
//                }
//                sb.append(snGiven);
//            }
//
//            if (Validator.isNotNull(snAdditional)) {
//                if (Validator.isNotNull(sb.toString())) {
//                    sb.append(StringPool.SPACE);
//                }
//                sb.append(snAdditional);
//            }
//
//            if (Validator.isNotNull(snFamily)) {
//                if (Validator.isNotNull(sb.toString())) {
//                    sb.append(StringPool.SPACE);
//                }
//                sb.append(snFamily);
//            }
//            if (Validator.isNotNull(snSuffix)) {
//                if (Validator.isNotNull(sb.toString())) {
//                    sb.append(StringPool.COMMA);
//                    sb.append(StringPool.SPACE);
//                }
//                sb.append(snSuffix);
//            }
//
//            vCard.setFormattedName(sb.toString());
//
//        }
//
//        if (parameters.containsKey("timezone")) {
//            // TODO check alternative methods for setting the timezone
//            String timezoneStr = ParamUtil.getString(request, "timezone");
//            Timezone timezone = new Timezone(timezoneStr);
//            vCard.setTimezone(timezone);
//        }
//
//        if (parameters.containsKey("title")) {
//
//            vCard.removeProperties(Title.class);
//
//            String[] titles = ParamUtil.getParameterValues(request, "title");
//
//            for (int i = 0; i < titles.length; i++) {
//
//                if (Validator.isNotNull(titles[i])) {
//                    Title title = new Title(titles[i]);
//
//                    Integer pref = getPref(i);
//
//                    title.setPref(pref);
//
//                    vCard.addTitle(title);
//                }
//            }
//        }
//
//        if (parameters.containsKey("uid")) {
//            String uidStr = ParamUtil.getString(request, "uid");
//            if (Validator.isNotNull(uidStr)) {
//                Uid uid = new Uid(uidStr);
//                vCard.setUid(uid);
//            }
//        }
//
//        if (parameters.containsKey("url.address")) {
//
//            vCard.removeProperties(Url.class);
//
//            String[] urlAddresses = ParamUtil.getParameterValues(request, "url.address");
//            String[] urlTypes = ParamUtil.getParameterValues(request, "url.type");
//
//            for (int i = 0; i < urlAddresses.length; i++) {
//
//                if (Validator.isNotNull(urlAddresses[i])) {
//                    Url url = new Url(urlAddresses[i]);
//                    url.setType(urlTypes[i]);
//
//                    Integer pref = getPref(i);
//
//                    url.setPref(pref);
//
//                    vCard.addUrl(url);
//                }
//            }
//        }
//
//        vCard.setVersion(VCardVersion.V4_0);
//
//        // TODO
//        // vCard.addXml(xml);
//
//        return vCard;
//    }
//
//    /**
//     *
//     * @param vCard
//     * @return
//     * @since 1.0.9
//     */
//    public static String[] getAssetTagNames(VCard vCard) {
//
//        List<Categories> categories = vCard.getCategoriesList();
//
//        List<String> assetTags = new ArrayList<String>();
//
//        for (Categories category : categories) {
//
//            List<String> values = category.getValues();
//            assetTags.addAll(values);
//
//        }
//
//        String[] assetTagNames = new String[0];
//        return assetTags.toArray(assetTagNames);
//    }

    /**
     *
     * @param portletRequest
     * @param vCard
     * @return
     * @throws SystemException
     * @throws PortalException
     * @throws IOException
     * @since 1.0.0
     */
    // public static VCard getVCard(PortletRequest portletRequest, VCard vCard,
    // Map<String, File[]> map)
    // throws PortalException, SystemException {
    //
    // HttpServletRequest request =
    // PortalUtil.getHttpServletRequest(portletRequest);
    //
    // return getVCard(request, vCard, map);
    // }

    /**
     *
     * @param key
     * @return
     * @since 1.0.2
     */
    public static String translate(String key) {
        return translate(key, null);
    }

    /**
     *
     * @param key
     * @param object
     * @return
     * @since 1.0.2
     */
    public static String translate(String key, Object object) {
        return translate(key, new Object[] { object });
    }

    /**
     *
     * @param key
     * @param objects
     * @return
     * @since 1.0.2
     */
    public static String translate(String key, Object[] objects) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("Language");
            return MessageFormat.format(bundle.getString(key), objects);
        } catch (MissingResourceException mre) {
            _log.warn(mre.getMessage());
            return key;
        }
    }

    private static Log _log = LogFactoryUtil.getLog(PortletUtil.class.getName());

}
