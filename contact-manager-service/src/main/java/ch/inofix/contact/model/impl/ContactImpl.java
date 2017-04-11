/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package ch.inofix.contact.model.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.inofix.contact.dto.*;

import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.Validator;

import aQute.bnd.annotation.ProviderType;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.parameter.EmailType;
import ezvcard.parameter.TelephoneType;
import ezvcard.property.Email;
import ezvcard.property.FormattedName;
import ezvcard.property.Organization;
import ezvcard.property.RawProperty;
import ezvcard.property.StructuredName;
import ezvcard.property.Telephone;

/**
 * The extended model implementation for the Contact service. Represents a row
 * in the &quot;Inofix_Contact&quot; database table, with each column mapped to
 * a property of this class.
 *
 * <p>
 * Helper methods and all application logic should be put in this class.
 * Whenever methods are added, rerun ServiceBuilder to copy their definitions
 * into the {@link ch.inofix.contact.model.Contact} interface.
 * </p>
 *
 * @author Christian Berndt
 * @author Stefan Luebbers
 */
@ProviderType
public class ContactImpl extends ContactBaseImpl {
    /*
     * NOTE FOR DEVELOPERS:
     *
     * Never reference this class directly. All methods that expect a contact
     * model instance should use the {@link ch.inofix.contact.model.Contact}
     * interface instead.
     */
    public ContactImpl() {
    }
    
    public String getCompany() {

        String str = "";

        List<Organization> organizations = getVCard().getOrganizations();

        if (organizations.size() > 0) {
            List<String> values = organizations.get(0).getValues();
            if (values.size() > 0) {
                str = values.get(0);
            }
        }

        return str;

    }
    
    
    public EmailDTO getEmail() {

        List<Email> emails = getVCard().getEmails();

        if (emails != null) {

            for (Email email : emails) {
                Integer pref = email.getPref();
                if (pref != null) {
                    if (pref == 1) {
                        return getEmail(email);
                    }
                }
            }
        }

        Email email = getVCard().getProperty(Email.class);

        return getEmail(email);

    }
    
    private EmailDTO getEmail(Email email) {

        EmailDTO emailDTO = new EmailDTO();

        if (email != null) {
            emailDTO.setAddress(email.getValue());

            emailDTO.setAddress(email.getValue());

            // TODO: Add multi-type support
            StringBuilder sb = new StringBuilder();
            Set<EmailType> types = SetUtil.fromList(email.getTypes());
            if (types.size() > 0) {
                for (EmailType type : types) {
                    sb.append(type.getValue());
                }
            } else {
                sb.append("other");
            }

            emailDTO.setType(sb.toString());
        }

        return emailDTO;
    }

    public String getFormattedName() {

        String formattedName = "";

        FormattedName fn = getVCard().getFormattedName();

        if (fn != null) {
            formattedName = fn.getValue();
        }

        return formattedName;

    }
    
    public String getName() {

        String firstLast = getFullName(true);
        String lastFirst = getFullName(false);

        String name = lastFirst;

        if (Validator.isNull(firstLast)) {

            Organization organization = getVCard().getOrganization();

            if (organization != null) {

                List<String> values = organization.getValues();

                Iterator<String> iterator = values.iterator();

                StringBuilder sb = new StringBuilder();

                while (iterator.hasNext()) {

                    sb.append(iterator.next());
                    if (iterator.hasNext()) {
                        sb.append(", ");
                    }

                }

                name = sb.toString();

            }

        }

        return name;

    }
    
    public PhoneDTO getPhone() {

        List<Telephone> phones = getVCard().getTelephoneNumbers();

        if (phones != null) {

            for (Telephone phone : phones) {
                Integer pref = phone.getPref();
                if (pref != null) {
                    if (pref == 1) {
                        return getPhone(phone);
                    }
                }
            }
        }

        Telephone phone = getVCard().getProperty(Telephone.class);

        return getPhone(phone);

    }
    
    private PhoneDTO getPhone(Telephone phone) {

        PhoneDTO phoneDTO = new PhoneDTO();

        if (phone != null) {
            phoneDTO.setNumber(phone.getText());

            StringBuilder sb = new StringBuilder();

            Set<TelephoneType> types = SetUtil.fromList(phone.getTypes());

            // TODO: Add support for multiple telephone types
            // e.g. home-fax, work-mobile, etc.
            if (types.size() > 0) {
                for (TelephoneType type : types) {
                    sb.append(type.getValue());
                }
            } else {
                sb.append("other");
            }

            phoneDTO.setType(sb.toString());
        }

        return phoneDTO;
    }
    
    public String getSalutation() {

        String salutation = "";

        VCard vCard = getVCard();

        RawProperty rawProperty = vCard.getExtendedProperty("x-salutation");

        if (rawProperty != null) {
            salutation = rawProperty.getValue();
        }

        return salutation;

    }

    public VCard getVCard() {

        String str = getCard();
        VCard vCard = null;

        if (Validator.isNotNull(str)) {
            vCard = Ezvcard.parse(str).first();
        } else {
            vCard = new VCard();
        }

        return vCard;

    }

    public String getFullName() {
        return getFullName(false);
    }

    public String getFullName(boolean firstLast) {

        StringBuilder sb = new StringBuilder();

        StructuredName sn = getVCard().getStructuredName();

        if (sn != null) {
            if (firstLast) {
                sb.append(sn.getGiven());
                sb.append(" ");
                sb.append(sn.getFamily());
            } else {
                sb.append(sn.getFamily());
                sb.append(", ");
                sb.append(sn.getGiven());
            }
        }

        String fullName = sb.toString();

        if (Validator.isNull(fullName)) {

            Organization organization = getVCard().getOrganization();

            if (organization != null) {

                List<String> values = organization.getValues();

                Iterator<String> iterator = values.iterator();

                while (iterator.hasNext()) {

                    sb.append(iterator.next());
                    if (iterator.hasNext()) {
                        sb.append(", ");
                    }

                }
            }

        }

        return fullName;

    }

}