/**
 * Copyright (c) 2000-present Inofix Gmbh, Luzern. All rights reserved.
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

package ch.inofix.contact.service.impl;

import aQute.bnd.annotation.ProviderType;
import ch.inofix.contact.service.base.ContactLocalServiceBaseImpl;

/**
 * The implementation of the contact local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link ch.inofix.contact.service.ContactLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM.
 * </p>
 *
 * @author Christian Berndt
 * @see ContactLocalServiceBaseImpl
 * @see ch.inofix.contact.service.ContactLocalServiceUtil
 */
@ProviderType
public class ContactLocalServiceImpl extends ContactLocalServiceBaseImpl {
    /*
     * NOTE FOR DEVELOPERS:
     *
     * Never reference this class directly. Always use {@link
     * ch.inofix.contact.service.ContactLocalServiceUtil} to access the contact
     * local service.
     */
}