/**
 * Copyright (c) 2000-present Inofix GmbH, Luzern. All rights reserved.
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
import ch.inofix.contact.service.base.ContactServiceBaseImpl;

/**
 * The implementation of the contact remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link ch.inofix.contact.service.ContactService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have
 * security checks based on the propagated JAAS credentials because this service
 * can be accessed remotely.
 * </p>
 *
 * @author Christian Berndt
 * @see ContactServiceBaseImpl
 * @see ch.inofix.contact.service.ContactServiceUtil
 */
@ProviderType
public class ContactServiceImpl extends ContactServiceBaseImpl {
    /*
     * NOTE FOR DEVELOPERS:
     *
     * Never reference this class directly. Always use {@link
     * ch.inofix.contact.service.ContactServiceUtil} to access the contact
     * remote service.
     */
}