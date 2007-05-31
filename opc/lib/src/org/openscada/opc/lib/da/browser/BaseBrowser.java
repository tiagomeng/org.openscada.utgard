/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2007 inavare GmbH (http://inavare.com)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.openscada.opc.lib.da.browser;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.EnumSet;

import org.apache.log4j.Logger;
import org.jinterop.dcom.common.JIException;
import org.openscada.opc.dcom.common.impl.EnumString;
import org.openscada.opc.dcom.da.OPCBROWSETYPE;
import org.openscada.opc.dcom.da.impl.OPCBrowseServerAddressSpace;

/**
 * A class implementing base browsing
 * @author Jens Reimann
 *
 */
public class BaseBrowser
{
    private static Logger _log = Logger.getLogger ( BaseBrowser.class );

    protected OPCBrowseServerAddressSpace _browser;
    
    /**
     * The batch size is the number of entries that will be requested with one call
     * from the server. Sometimes too big batch sizes will cause an exception. And
     * smaller batch sizes degrade perfomance. The default is set by {@link EnumString#DEFAULT_BATCH_SIZE}
     * and can be overridden by the java property <q>openscada.dcom.enum-batch-size</q>.
     */
    protected int _batchSize;

    public BaseBrowser ( OPCBrowseServerAddressSpace browser )
    {
        this ( browser, EnumString.DEFAULT_BATCH_SIZE );
    }

    public BaseBrowser ( OPCBrowseServerAddressSpace browser, int batchSize )
    {
        super ();
        _browser = browser;
        _batchSize = batchSize;
    }
    
    /**
     * Set the batch size
     * @param batchSize The new batch size
     */
    public void setBatchSize ( int batchSize )
    {
        _batchSize = batchSize;
    }
    
    /**
     * Get the batch size
     * @return the current batch size
     */
    public int getBatchSize ()
    {
        return _batchSize;
    }

    /**
     * Perform the browse operation.
     * @param type
     * @param filterCriteria
     * @param accessMask
     * @param variantType
     * @return The browse result
     * @throws IllegalArgumentException
     * @throws UnknownHostException
     * @throws JIException
     */
    protected Collection<String> browse ( OPCBROWSETYPE type, String filterCriteria, EnumSet<Access> accessMask, int variantType ) throws IllegalArgumentException, UnknownHostException, JIException
    {
        int accessMaskValue = 0;

        if ( accessMask.contains ( Access.READ ) )
            accessMaskValue |= Access.READ.getCode ();
        if ( accessMask.contains ( Access.WRITE ) )
            accessMaskValue |= Access.WRITE.getCode ();

        _log.debug ( "Browsing with a batch size of " + _batchSize );

        return _browser.browse ( type, filterCriteria, accessMaskValue, variantType ).asCollection ( _batchSize );
    }

}