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

import com.liferay.portal.kernel.util.Validator;

import aQute.bnd.annotation.ProviderType;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.property.FormattedName;
import ezvcard.property.Organization;
import ezvcard.property.StructuredName;

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

    public String getFormattedName() {

        String formattedName = "";

        FormattedName fn = getVCard().getFormattedName();

        if (fn != null) {
            formattedName = fn.getValue();
        }

        return formattedName;

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